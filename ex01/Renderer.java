/**
 * The Renderer interface defines the contract for rendering a Board object.
 *
 * Implementations of this interface are responsible for taking
 * a Board instance and rendering it in some form,
 * such as to the console, a graphical user interface,
 * or any other desired output medium.
 *
 * @author itamarshahar2
 * @see RendererFactory
 * @see ConsoleRenderer
 * @see VoidRenderer
 */
public interface Renderer {
    /**
     * Renders the provided Board object.
     *
     * @param board The Board instance to be rendered.
     */
    void renderBoard(Board board);

}

