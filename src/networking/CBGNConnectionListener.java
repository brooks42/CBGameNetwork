package networking;

import java.util.HashMap;

/**
 * The CBGNConnectionListener lets the extending class listen for connection
 * events, such as received GameEvent objects, being dropped from the server
 * because of a connection loss, or being kicked from the server.
 *
 * @author Chris
 */
abstract class CBGNConnectionListener {

    /**
     * Called when a Connection receives a GameEvent from the server.
     *
     * @param event the GameEvent
     */
    protected abstract void onMessage(CBGNConnection conn, HashMap<String, String> data);

    /**
     * Called when the connection is closed. The reason string is (supposed to
     * be) a human-readable reason why the connection was closed, such as "you
     * were kicked" or "I quit".
     *
     * @param reason the reason for the connection loss, if appropriate
     */
    protected abstract void onConnectionClosed(CBGNConnection conn, String reason);
}
