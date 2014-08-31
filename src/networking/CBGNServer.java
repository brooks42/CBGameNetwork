/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networking;

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
public class CBGNServer implements Runnable, CBGNConnectionListener {

    // a list of connections this Server is maintaining
    private final HashMap<Socket, CBGNConnection> connections;

    private final int port;

    protected CBGNServerListener listener;

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
     * Runs the server, accepting connections and adding them to the list of
     * available connections.
     */
    @Override
    public void run() {

        try {
            if (listener != null) {
                listener.onBegin();
            }

            ServerSocket serverSocket = new ServerSocket(port);
            Socket clientSocket;

            // here we listen for connections and accept them over and over
            while (true) {
                
                // start a new Thread for each client, because server
                clientSocket = serverSocket.accept();
                CBGNConnection conn = new CBGNConnection(this, clientSocket);
                Thread thread = new Thread(conn);
                thread.start();
                
                connections.put(clientSocket, conn);
            }
        } catch (IOException e) {
            System.out.println("LANServer.Exception: " + e.getMessage());
        } finally {
            // close all of the connections
            for (CBGNConnection conn : connections.values()) {
                try {
                    conn.close();
                } catch (IOException e) {
                    // blargh I am ded
                    // TODO: find a better Logger.log(e);
                }
            }
        }
    }

    /**
     * Called when a connection receives a message from a client.
     *
     * @param event
     */
    @Override
    public void onMessage(GameEvent event) {
        try {
            broadcastMessage(event);
        } catch (IOException ex) {
            Logger.getLogger(CBGNServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param event the GameEvent to send
     * @throws IOException if there is an exception handing the write
     */
    protected void broadcastMessage(GameEvent event) throws IOException {
        for (CBGNConnection conn : connections.values()) {
            conn.sendMessage(event.toString());
        }
    }

    /**
     *
     * @param event the GameEvent to send
     * @throws IOException if there is an exception handing the write
     */
    protected void broadcastUDPMessage(GameEvent event) throws IOException {
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
