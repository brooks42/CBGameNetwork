/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networking;

import error.CBGNException;
import java.io.IOException;
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

    // a list of connections this Server is maintaining
    private final HashMap<Socket, CBGNConnection> connections;

    private final int port;

    protected CBGNServerListener listener;

    private CBGNServerConnectionAdapter adapter;

    /**
     * Creates a new CBGNServer object listening on the passed port.
     *
     * @param port the port for this Server to listen on
     */
    public CBGNServer(int port) {
        this.port = port;
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
            ServerSocket serverSocket = new ServerSocket(port);
            Socket clientSocket;

            // here we listen for connections and accept them over and over
            while (true) {
                // start a new Thread for each client, because server
                clientSocket = serverSocket.accept();
                CBGNConnection conn = new CBGNConnection(adapter, clientSocket);
                Thread thread = new Thread(conn);
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
            conn.sendUDPMessage(event.toString());
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
    protected void onConnectionClosed(CBGNConnection conn, String reason) {
        server.listener.onConnectionClosed(conn.clientSocket, new CBGNException(reason));
        server.removeConnection(conn.clientSocket);
    }
}
