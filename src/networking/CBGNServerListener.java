package networking;

import error.CBGNException;
import java.net.Socket;
import java.util.HashMap;

/**
 * The CBGNServerListener interface allows an implementing class to listen for
 * server events and respond to them.
 *
 * @author Chris
 */
public interface CBGNServerListener {

    /**
     * Called when the server starts successfully.
     */
    public void onBegin();

    /**
     * Called when the server receives a new connection.
     *
     * @param name the name of the new connection. Unless this is overridden,
     * this will typically be the socket's toString()
     */
    public void onConnection(String name);

    /**
     * Called by the server when it receives a new GameEvent.
     *
     * @param data the data received by the server, presumably from a client.
     */
    public void onMessage(HashMap<String, String> data);

    /**
     * Called when the server's connection to the socket is closed for some
     * reason. The reason is detailed in the parameters.
     *
     * @param socket the socket that was closed
     * @param reason the reason the socket was closed
     */
    public void onConnectionClosed(Socket socket, CBGNException reason);

    /**
     * Called by the server when it stops, either for an error or because of
     * normal termination (in which case the passed exception will be null).
     *
     * @param except The exception that caused this stop, if any
     */
    public void onStopped(CBGNException except);
}
