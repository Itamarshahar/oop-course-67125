package ascii_art.ascii_exception;

/**
 * invalid command exception - represents invalid command by the user
 */
public class InvalidCommandException extends AsciiException{
    /**
     * construct exception with a message
     * @param message message of exception
     */
    public InvalidCommandException(String message) {
        super(message);
    }
}
