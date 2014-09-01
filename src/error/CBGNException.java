package error;

/**
 * The CBGNException class contains (hopefully) helpful information about any
 * networking problems that occur in the CBGameNetworking library.
 *
 * @author Chris
 */
public class CBGNException extends Exception {

    public CBGNException(String reason) {
        super(reason);
    }
}
