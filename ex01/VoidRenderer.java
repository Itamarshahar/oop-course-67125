/**
 * The VoidRenderer class is a implementation of the Renderer interface that
 * does not actually render the board. It is a placeholder implementation
 * that can be used in situations where a renderer is required, but rendering
 * is not necessary.
 *
 * @author itamarshahar2
 * @see Renderer
 */
public class VoidRenderer implements Renderer {
    /**
     * Constructs a new VoidRenderer instance.
     */
//    public VoidRenderer(int size){return;};

    /**
     * Renders the given board. Since this is a void renderer, this method
     * does not actually do anything.
     *
     * @param board the board to be rendered
     */
    @Override
    public void renderBoard(Board board) {
        // Method implementation goes here
        return;
    }
}