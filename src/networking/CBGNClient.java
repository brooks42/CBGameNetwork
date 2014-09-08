package networking;

import error.CBGNException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The CBGNClient class represents a client to the CBGNServer. Typically a single
 * game instance will create a single client instance, connect to a server through
 * the client's API and then clean up when it's finished.
 *
 * @author Chris
 */
public final class CBGNClient implements Runnable {

    public final static int DEFAULT_CLIENT_TCP_PORT = 1776;
    public final static int DEFAULT_CLIENT_UDP_PORT = 1778;

    private Socket tcpSocket;
    private PrintWriter out;
    private BufferedReader in;
    private DatagramSocket udpSocket;

    // the address and port for this client's connection to the server
    private final InetAddress address;
    private final int tcpPort, udpPort, serverUDPPort;

    private CBGNConnection tcpConn, udpConn;

    private CBGNClientConnectionAdapter adapter;

    // a listener this client will provide callbacks to when appropriate.
    protected CBGNClientListener listener;

    /**
     * Creates a new CBGNClient with the passed address and port number. When
     * the client is started, it will attempt to use the passed address and port
     * as its socket values.
     *
     * @param listener a listener to perform callbacks on
     * @param address the IP address of the server to connect to.
     * @param tcpPort the port number to connect for TCP
     * @param udpPort the port number to connect (on this machine) for UDP
     * @param serverUDPPort the port number for sending UDP messages to the
     * server (in other words, the server's UDP port)
     */
    public CBGNClient(CBGNClientListener listener, InetAddress address, int tcpPort, int udpPort, int serverUDPPort) {
        if (listener == null || address == null) {
            throw new IllegalArgumentException("Listener and address for a client cannot be null.");
        }
        this.listener = listener;
        this.address = address;
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;
        this.serverUDPPort = serverUDPPort;
    }

    /**
     * Sets this client's listener to the passed one.
     *
     * @param listener a new CBGNClientListener for callbacks
     */
    public void setListener(CBGNClientListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Listener cannot be null.");
        }
        this.listener = listener;
    }

    /**
     * Starts the TCP and UDP parts of the CBGNClient, opening a socket and
     * communicating as appropriate.
     */
    @Override
    public final void run() {
        try {
            tcpSocket = new Socket(address.getHostAddress(), getTcpPort());
            udpSocket = new DatagramSocket(getUdpPort(), address);

            adapter = new CBGNClientConnectionAdapter(this);
            tcpConn = new CBGNConnection(adapter, tcpSocket);
            udpConn = new CBGNConnection(adapter, udpSocket);
            new Thread(tcpConn).start();
            new Thread(udpConn).start();
        } catch (UnknownHostException e) {
            Logger.getLogger(CBGNClient.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException | IllegalArgumentException e) {
            Logger.getLogger(CBGNClient.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Sends the passed HashMap of data to the server.
     *
     * @param data the data to be sent to the server
     * @throws java.io.IOException if there's a problem sending the message
     */
    public void sendMessage(HashMap<String, String> data) throws IOException {
        tcpConn.sendMessage(new GameEvent(data).toString());
    }

    /**
     * Sends the passed HashMap of data to the server over UDP.
     *
     * @param data the data to be sent to the server
     * @throws java.io.IOException if there's a problem sending the message
     */
    public void sendUDPMessage(HashMap<String, String> data) throws IOException {
        udpConn.sendUDPMessage(address, getServerUDPPort(), new GameEvent(data).toString());
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
     * @return the serverUDPPort
     */
    public int getServerUDPPort() {
        return serverUDPPort;
    }
}

// internal adapter class to provide a consistent internal API with the 
// CBGNConnectionListener while not having to muck around with exposing 
// its internals to the external API
class CBGNClientConnectionAdapter extends CBGNConnectionListener {

    CBGNClient client;

    public CBGNClientConnectionAdapter(CBGNClient client) {
        this.client = client;
    }

    @Override
    protected void onMessage(CBGNConnection conn, HashMap<String, String> data) {
        client.listener.onClientMessage(data);
    }

    @Override
    protected void onUDPMessage(CBGNConnection conn, HashMap<String, String> data) {
        client.listener.onClientUDPMessage(data);
    }

    @Override
    protected void onConnectionClosed(CBGNConnection conn, String reason) {
        client.listener.onClientConnectionClosed(new CBGNException(reason));
    }
}
