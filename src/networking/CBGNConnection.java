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
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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

    // an internal parser we store so we don't have to continuously instantiate one
    private JSONParser parser;

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
        this.listener = listener;
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
                listener.onMessage(this, dataFromJSON(inputLine));
            }
        } catch (IOException e) {
            Logger.getLogger(CBGNConnection.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                close();
            } catch (IOException e) {
                // blargh I am ded
                Logger.getLogger(CBGNConnection.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    /**
     *
     *
     * @param json
     * @return
     */
    private HashMap<String, String> dataFromJSON(String json) {
        HashMap<String, String> data = new HashMap<>();

        JSONParser parser = new JSONParser();
        try {
            data = (HashMap<String, String>) parser.parse(json);
        } catch (ParseException | ClassCastException ex) {
            Logger.getLogger(CBGNConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

        return data;
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
