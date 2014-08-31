/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 *
 * @author Chris
 */
class CBGNConnection implements Runnable {

    // the socket for this Connection
    protected Socket clientSocket;
    private PrintStream out;
    private BufferedReader in;
    // the name of this Connection. For now it's just the toString() of its socket
    public String name;

    // a listener for this connection, typically the thing (server, client) that 
    // created this connection in the first place.
    private CBGNConnectionListener listener;

    /**
     * Creates a Connection around the passed Socket.
     *
     * @param server
     * @param socket
     */
    public CBGNConnection(CBGNConnectionListener listener, Socket socket) {
        if (listener == null) {
            throw new IllegalArgumentException("Cannot create a CBGNConnection with a null listener.");
        }
        this.clientSocket = socket;
        this.name = clientSocket.toString();
    }

    /**
     *
     */
    @Override
    public void run() {
        try {
            out = new PrintStream(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                System.out.println("Connection " + name + " got " + inputLine);
                // TODO: turn the input line into a map with JSON
            }
        } catch (IOException e) {
            // TODO: find a logger Logger.log(e);
        } finally {
            try {
                close();
            } catch (IOException e) {
                // blargh I am ded
                // TODO: find a logger Logger.log(e);
            }
        }
    }

    /**
     * Prints the passed message to this Connection's output stream
     *
     * @param message
     * @throws IOException if there is an exception handing the write
     */
    public void sendMessage(String message) throws IOException {
        if (out == null) {
            throw new IOException("Connection " + this.name + " could not send message to null OutputStream.");
        }
        System.out.println(name + " sending " + message);
        out.println(message);
        out.flush();
    }

    /**
     * Sends a UDP message using this Connection's underlying UDP thread.
     *
     * @param message
     * @throws IOException
     */
    public void sendUDPMessage(String message) throws IOException {
        // TODO: send UDP
    }

    /**
     * Closes this Connection, closing its socket and doing any required
     * cleanup.
     *
     * @throws IOException if closing the socket fails
     */
    public void close() throws IOException {
        //listener.onConnectionClosed("Quit");
        clientSocket.close();
    }
}
