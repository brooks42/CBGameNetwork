/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networking;

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
 *
 * @author Chris
 */
public final class CBGNClient implements Runnable {

    private Socket requestSocket;
    private PrintWriter out;
    private BufferedReader in;
    private DatagramSocket udpSocket;

    // the address and port for this client
    private final InetAddress address;
    private final int tcpPort, udpPort;

    private CBGNConnection conn;

    private CBGNClientConnectionAdapter adapter;

    /**
     * Creates a new CBGNClient with the passed address and port number. When
     * the client is started, it will attempt to use the passed address and port
     * as its socket values.
     *
     * @param address the IP address of the server to connect to.
     * @param tcpPort the port number to connect for TCP
     * @param udpPort the port number to connect for UDP
     */
    public CBGNClient(InetAddress address, int tcpPort, int udpPort) {
        this.address = address;
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;
    }

    /**
     * Starts the TCP and UDP parts of the CBGNClient, opening a socket and
     * communicating as appropriate.
     */
    @Override
    public final void run() {
        try {
            System.out.println("Client started");
            requestSocket = new Socket(address.getHostAddress(), tcpPort);

            adapter = new CBGNClientConnectionAdapter(this);
            conn = new CBGNConnection(adapter, requestSocket);
            conn.run();
        } catch (UnknownHostException e) {
            Logger.getLogger(CBGNClient.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException e) {
            Logger.getLogger(CBGNClient.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                conn.close();
            } catch (IOException e) {
                // everything has gone to hell
                Logger.getLogger(CBGNClient.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    /**
     * Sends the passed GameEvent to the server.
     *
     * @param data the data to be sent in a GameEvent
     * @throws java.io.IOException if there's a problem sending the message
     */
    public void sendMessage(HashMap<String, String> data) throws IOException {
        System.out.println("Sending data: " + new GameEvent(data).toString());
        conn.sendMessage(new GameEvent(data).toString());
    }

    /**
     * Starts the UDP server thread
     */
    public void startUDP() {
        /*System.out.println("Initializing Client UDP");
         // start a new thread to manage the UDP side of the server
         new Thread() {
         @Override
         public void run() {

         try {
         udpSocket = new DatagramSocket(UDP_CLIENT_PORT);
         } catch (SocketException e) {
         System.out.println("LANClient.Exception: " + e.getMessage());
         }
         byte[] receiveData = new byte[1024];
         DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

         while (true) {
         try {
         // start the UDP socket to receive UDP
         udpSocket.receive(receivePacket);
         String message = new String(receivePacket.getData());
         System.out.println("Client received UDP: " + message);
         interpretGameEvent(LANSerializer.deserializeGameEvent(message));
         } catch (IOException ex) {
         java.util.logging.Logger.getLogger(LANClient.class.getName()).log(Level.SEVERE, null, ex);
         }
         }
         }
         }.start();*/
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
        System.out.println("Client got a message");
    }

    @Override
    protected void onConnectionClosed(CBGNConnection conn, String reason) {

    }
}
