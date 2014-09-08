package networking;

import error.CBGNException;
import java.util.HashMap;

/**
 * The CBGNClientListener provides an interface for a game manager object to
 * receive events from the CBGNClient, such as received messages or connection
 * cut-outs.
 *
 * @author Chris
 */
public interface CBGNClientListener {

    /**
     * Called when the client successfully connects.
     */
    public void onClientConnected();

    /**
     * Called by the client when it receives a message from the server.
     *
     * @param data the data received from the server
     */
    public void onClientMessage(HashMap<String, String> data);
    
    /**
     * Called by the client when it receives a UDP message from the server.
     *
     * @param data the data received from the server
     */
    public void onClientUDPMessage(HashMap<String, String> data);

    /**
     * Called when the client's connection is closed for some reason. The reason
     * is detailed in the parameters.
     *
     * @param reason the reason the connection was closed - if the connection
     * was not closed by an exception, this exception's getMessage() will return
     * a human-readable cause (such as "kicked by server").
     */
    public void onClientConnectionClosed(CBGNException reason);
}
