package networking;

/**
 * The CBGNConnectionListener lets the implementing class listen for connection
 * events, such as received GameEvent objects, being dropped from the server
 * because of a connection loss, or being kicked from the server.
 *
 * @author Chris
 */
interface CBGNConnectionListener {

    /**
     * Called when a Connection receives a GameEvent from the server.
     *
     * @param event the GameEvent
     */
    public void onMessage(GameEvent event);

    /**
     * Called when the connection is closed. The reason string is (supposed to
     * be) a human-readable reason why the connection was closed, such as "you
     * were kicked" or "I quit".
     *
     * @param reason the reason for the connection loss, if appropriate
     */
    //public void onConnectionClosed(String reason);
}
