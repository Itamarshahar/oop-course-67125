public class RendererFactory {
    public RendererFactory() {
    } // TODO maybe not public?

    public Renderer buildRenderer(String type, int size) {
        Renderer renderer = null;
        String typeLower = type.toLowerCase();
        switch (typeLower) {
            case "console":
                renderer = new ConsoleRenderer(size);
                break;
            case "none":
                renderer = new VoidRenderer(size);
                break;
        }
        return renderer;
    }
}