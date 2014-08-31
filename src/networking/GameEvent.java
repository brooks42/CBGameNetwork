package networking;

import java.util.HashMap;

/**
 * The GameEvent class contains a map of events and a connection the event has
 * occurred for. This class is used to send events back and forth between the
 * server and the client, to be unpacked on the appropriate end.
 *
 * @author Chris
 */
final class GameEvent {

    // the connection this GameEvent was sent by.
    private final CBGNConnection connection;

    // the data for this GameEvent, if any
    private final HashMap<String, String> data;

    /**
     * Creates a new GameEvent with the passed data.
     *
     * @param data the data for this GameEvent.
     */
    public GameEvent(HashMap<String, String> data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot create a GameEvent wrapping a null data object.");
        }
        this.connection = null;
        this.data = data;
    }

    /**
     * Creates a new GameEvent with the passed connection and the passed data.
     *
     * @param conn the CBGNConnection this GameEvent originated from.
     * @param data the data for this GameEvent.
     */
    protected GameEvent(CBGNConnection conn, HashMap<String, String> data) {
        if (conn == null) {
            throw new IllegalArgumentException("Connection cannot be null when instantiating a GameEvent.");
        }
        if (data == null) {
            throw new IllegalArgumentException("Cannot create a GameEvent wrapping a null data object.");
        }
        this.connection = conn;
        this.data = data;
    }

    /**
     * Returns the CBGNConnection object that initiated this GameEvent. This
     * value is null if the server sent this GameEvent.
     *
     * @return the Connection object that started this GameEvent.
     */
    protected CBGNConnection getConnection() {
        return this.connection;
    }

    /**
     * Gets the data object this GameEvent is wrapping. The data is an arbitrary
     * String, String HashMap.
     *
     * @return the data object for this GameEvent.
     */
    public HashMap<String, String> getData() {
        return this.data;
    }

    /**
     * Appends the passed key, value pair to this GameEvent.
     *
     * @param key
     * @param value
     */
    public void append(String key, String value) {
        this.data.put(key, value);
    }

    /**
     * Returns the String version of this GameEvent, serialized into JSON and
     * with a unique descriptor for its connection object.
     *
     * @return a String version of this GameEvent
     */
    @Override
    public String toString() {
        // TODO: this should serialize this event using JSON
        return "lolnotimplemented";
    }
}
