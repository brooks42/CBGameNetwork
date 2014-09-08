package networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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
    protected DatagramSocket udpSocket;
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
        if (socket == null) {
            throw new IllegalArgumentException("Cannot create a CBGNConnection with a null TCP socket.");
        }
        this.listener = listener;
        this.clientSocket = socket;
        this.name = clientSocket.toString();

        parser = new JSONParser();
    }

    /**
     * Creates a Connection around the passed Socket.
     *
     * @param server
     * @param socket
     */
    public CBGNConnection(CBGNConnectionListener listener, DatagramSocket socket) {
        System.out.println("Creating CBGNConnection UDP");
        if (listener == null) {
            throw new IllegalArgumentException("Cannot create a CBGNConnection with a null listener.");
        }
        if (socket == null) {
            throw new IllegalArgumentException("Cannot create a CBGNConnection with a null UDP socket.");
        }
        this.listener = listener;
        this.udpSocket = socket;
        this.name = udpSocket.toString();

        parser = new JSONParser();
    }

    /**
     *
     */
    @Override
    public void run() {
        try {
            if (clientSocket != null) {
                // do a TCP connection
                out = new PrintStream(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    listener.onMessage(this, dataFromJSON(inputLine));
                }
            }
            if (udpSocket != null) {
                // do a UDP connection instead
                DatagramPacket p = new DatagramPacket(new byte[512], 0, 512);

                do {
                    try {
                        udpSocket.receive(p);
                        listener.onUDPMessage(this, dataFromJSON(new String(p.getData())));
                    } catch (NullPointerException e) {
                        // this means the connection was killed, we should handle this through the listener.
                        // either way this socket is done, so we're done.
                        // listener.onConnectionClosed();
                        System.out.println("Connection is dead :( :" + e.getMessage());
                        break;
                    }
                } while (true);
            }
        } catch (IOException e) {
            Logger.getLogger(CBGNConnection.class.getName()).log(Level.SEVERE, null, e);
            try {
                System.out.println("Server closed");
                close();
            } catch (IOException ex) {
                // blargh I am ded
                Logger.getLogger(CBGNConnection.class.getName()).log(Level.SEVERE, null, ex);
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
        json = json.trim();

        if (json.equals("")) {
            return new HashMap<>();
        }

        try {
            return (HashMap<String, String>) parser.parse(json);
        } catch (ParseException | ClassCastException ex) {
            Logger.getLogger(CBGNConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
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
        out.println(message);
        out.flush();
    }

    /**
     * Sends a UDP message to the passed address using this Connection's
     * underlying UDP socket.
     *
     * @param message
     * @throws IOException
     */
    public void sendUDPMessage(InetAddress addr, int port, String message) throws IOException {
        if (udpSocket == null) {
            throw new IOException("Trying to send UDP over a TCP connection. Check that you're sending UDP from a UDP connection.");
        }
        System.out.println("Sending to address " + addr.toString() + ":" + port);
        udpSocket.send(new DatagramPacket(message.getBytes(), 0, message.getBytes().length, addr, port));
    }

    /**
     * Closes this Connection, closing its socket and doing any required
     * cleanup.
     *
     * @throws IOException if closing the socket fails
     */
    public void close() throws IOException {
        listener.onConnectionClosed(this, "Quit");
        if (clientSocket != null) {
            clientSocket.close();
        }
        if (udpSocket != null) {
            udpSocket.close();
        }
    }
}
