package ascii_art.ascii_exception;

/**
 * father class that holds all the exception related to Ascii art program
 */
public class AsciiException extends Exception{
    /**
     * construct exception with a message
     * @param message message of exception
     */
    public AsciiException(String message) {
        super(message);
    }
}
