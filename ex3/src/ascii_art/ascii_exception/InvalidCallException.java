package ascii_art.ascii_exception;

/**
 * invalid call exception - represents invalid call for function by the user
 */
public class InvalidCallException extends AsciiException{
    /**
     * construct exception with a message
     * @param message message of exception
     */
    public InvalidCallException(String message) {
        super(message);
    }
}
