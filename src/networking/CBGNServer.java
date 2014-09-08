package networking;

import error.CBGNException;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Chris
 */
public class CBGNServer implements Runnable {

    public final static int DEFAULT_SERVER_PORT = 1776;
    public final static int DEFAULT_SERVER_UDP_PORT = 1777;
    public final static int DEFAULT_SERVER_BROADCAST_PORT = 1778;

    // a list of connections this Server is maintaining
    private final HashMap<Socket, CBGNConnection> connections;

    private final int tcpPort, udpPort, udpBroadcastPort;

    protected CBGNServerListener listener;

    private CBGNServerConnectionAdapter adapter;

    // the UDP connection, in case we need to manage it at some point
    private CBGNConnection udpConn;

    /**
     * Creates a new CBGNServer object with the default ports.
     */
    public CBGNServer() {
        this(DEFAULT_SERVER_PORT, DEFAULT_SERVER_UDP_PORT, DEFAULT_SERVER_BROADCAST_PORT);
    }

    /**
     * Creates a new CBGNServer object listening on the passed port.
     *
     * @param port the port for this Server to listen on
     */
    public CBGNServer(int port) {
        this(port, DEFAULT_SERVER_UDP_PORT, DEFAULT_SERVER_BROADCAST_PORT);
    }

    /**
     * Creates a new CBGNServer object listening on the passed port.
     *
     * @param tcpPort the port for this Server to listen on
     * @param udpPort the port for this Server to listen to UDP on
     */
    public CBGNServer(int tcpPort, int udpPort) {
        this(tcpPort, udpPort, DEFAULT_SERVER_BROADCAST_PORT);
    }

    /**
     * Creates a new CBGNServer object listening on the passed port.
     *
     * @param tcpPort the port for this Server to listen on
     * @param udpPort the port for this Server to listen to UDP on
     * @param udpBroadcastPort the port for this server to send UDP packets to.
     * By default the server will broadcast to (get this)
     * DEFAULT_CLIENT_UDP_PORT.
     */
    public CBGNServer(int tcpPort, int udpPort, int udpBroadcastPort) {
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;
        this.udpBroadcastPort = udpBroadcastPort;
        connections = new HashMap<>();
    }

    /**
     * Sets this server's listener to the passed listener. This will overwrite
     * any currently-set listener.
     *
     * @param listener the CBGNServerListener to send messages to
     */
    public void registerListener(CBGNServerListener listener) {
        this.listener = listener;
    }

    /**
     * Returns the number of registered connections with this server.
     *
     * @return the number of registered connections with this server
     */
    public int numberOfConnections() {
        return connections.size();
    }

    /**
     * Removes the passed connection from the current list of connections. You
     * must manually close this socket. If the socket isn't in the current list
     * of connections, nothing will happen.
     *
     * @param socket the socket to remove
     */
    public void removeConnection(Socket socket) {
        connections.remove(socket);
    }

    /**
     * Runs the server, accepting connections and adding them to the list of
     * available connections.
     */
    @Override
    public void run() {

        try {
            if (listener != null) {
                listener.onBegin();
            }

            adapter = new CBGNServerConnectionAdapter(this);
            ServerSocket serverSocket = new ServerSocket(getTcpPort());
            Socket clientSocket;

            // start the UDP thread here, since we only need one
            DatagramSocket udpSocket = new DatagramSocket(getUdpPort());
            udpConn = new CBGNConnection(adapter, udpSocket);
            Thread thread = new Thread(udpConn);
            thread.start();

            // here we listen for connections and accept them over and over
            while (true) {
                // start a new Thread for each client, because server
                clientSocket = serverSocket.accept();
                CBGNConnection conn = new CBGNConnection(adapter, clientSocket);
                thread = new Thread(conn);
                thread.start();
                listener.onConnection(conn.name);
                connections.put(clientSocket, conn);
            }
        } catch (IOException e) {
            Logger.getLogger(CBGNConnection.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            // close all of the connections
            for (CBGNConnection conn : connections.values()) {
                try {
                    conn.close();
                } catch (IOException e) {
                    Logger.getLogger(CBGNConnection.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }

    /**
     *
     * @param event the GameEvent to send
     * @throws IOException if there is an exception handing the write
     */
    private void broadcastMessage(GameEvent event) throws IOException {
        for (CBGNConnection conn : connections.values()) {
            conn.sendMessage(event.toString());
        }
    }

    /**
     *
     * @param event the GameEvent to send
     * @throws IOException if there is an exception handing the write
     */
    private void broadcastUDPMessage(GameEvent event) throws IOException {
        for (CBGNConnection conn : connections.values()) {
            udpConn.sendUDPMessage(conn.clientSocket.getInetAddress(), this.udpBroadcastPort, event.toString());
        }
    }

    /**
     * Broadcasts the passed data to all of the currently-connected clients. The
     * data should represent a single "event", such as a sent message or a
     * player position.
     *
     * @param data a map of string key/value pairs to send
     * @throws IOException if there is an exception handing the write
     */
    public void broadcastMessage(HashMap<String, String> data) throws IOException {
        broadcastMessage(new GameEvent(data));
    }

    /**
     * Broadcasts the passed data to all of the currently-connected clients over
     * UDP. The data should represent a single "event", such as a sent message
     * or a player position.
     *
     * @param data a map of string key/value pairs to send
     * @throws IOException if there is an exception handing the write
     */
    public void broadcastUDPMessage(HashMap<String, String> data) throws IOException {
        broadcastUDPMessage(new GameEvent(data));
    }

    /**
     * @return the tcpPort
     */
    public int getTcpPort() {
        return tcpPort;
    }

    /**
     * @return the udpPort
     */
    public int getUdpPort() {
        return udpPort;
    }

    /**
     * @return the udpBroadcastPort
     */
    public int getUdpBroadcastPort() {
        return udpBroadcastPort;
    }
}

// internal adapter class to provide a consistent internal API with the 
// CBGNConnectionListener while not having to muck around with exposing 
// its internals to the external API
class CBGNServerConnectionAdapter extends CBGNConnectionListener {

    CBGNServer server;

    public CBGNServerConnectionAdapter(CBGNServer server) {
        this.server = server;
    }

    //
    @Override
    protected void onMessage(CBGNConnection conn, HashMap<String, String> data) {
        try {
            server.listener.onMessage(data);
            server.broadcastMessage(data);
        } catch (IOException e) {
            System.out.println("Server error sending message: " + e.getMessage());
        }
    }

    //
    @Override
    protected void onUDPMessage(CBGNConnection conn, HashMap<String, String> data) {
        try {
            if (data != null) {
                server.listener.onUDPMessage(data);
                server.broadcastUDPMessage(data);
            } else {
                System.out.println("Server received UDP message, but it was bad.");
            }
        } catch (IOException e) {
            System.out.println("Server error sending message: " + e.getMessage());
        }
    }

    //
    @Override
    protected void onConnectionClosed(CBGNConnection conn, String reason) {
        server.listener.onConnectionClosed(conn.clientSocket, new CBGNException(reason));
        server.removeConnection(conn.clientSocket);
    }
}
