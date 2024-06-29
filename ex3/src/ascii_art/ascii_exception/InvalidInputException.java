package ascii_art.ascii_exception;

/**
 * invalid input exception - represents invalid input by the user
 */
public class InvalidInputException extends AsciiException{
    /**
     * construct exception with a message
     * @param message message of exception
     */
    public InvalidInputException(String message) {
        super(message);
    }
}
