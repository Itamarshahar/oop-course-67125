/**
 * The RendererFactory class is responsible for creating instances of
 * different types of Renderer objects based on the provided type and size
 * parameters.
 *
 * This factory class simplifies the process of obtaining the appropriate
 * Renderer implementation for a given use case
 *
 * @author itamarshahar2
 * @see Renderer
 * @see ConsoleRenderer
 * @see VoidRenderer
 */
public class RendererFactory {
    public RendererFactory() {
    }
    /**
     * Builds a Renderer instance based on the specified type and size parameters.
     *
     * @param type The type of Renderer to create, such as "console" or "none".
     * @param size The size parameter to be passed to the Renderer constructor.
     * @return A Renderer instance of the appropriate type, or null if the type
     * is not recognized.
     */
    public Renderer buildRenderer(String type, int size) {
        Renderer renderer = null;
        String typeLower = type.toLowerCase();
        switch (typeLower) {
            case "console":
                renderer = new ConsoleRenderer(size);
                break;
            case "none":
                renderer = new VoidRenderer();
                break;
        }
        return renderer;
    }
}