package networking;

import error.CBGNException;
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
     * Called by the server when it receives a new GameEvent.
     *
     * @param data the data received by the server, presumably from a client.
     */
    public void onMessage(HashMap<String, String> data);

    /**
     * Called by the server when it stops, either for an error or because of
     * normal termination (in which case the passed exception will be null).
     *
     * @param except The exception that caused this stop, if any
     */
    public void onStopped(CBGNException except);
}
