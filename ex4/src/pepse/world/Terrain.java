package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.*;

/**
 * This class is responsible for generating the terrain in the game world.
 * It uses noise generation to create a realistic and varied ground height.
 */
public class Terrain {
    public static final String GROUND_TAG = "ground";
    public static final String TOP_TAG = "topGround";
    public static final float RATIO = (float) (2.0 / 3.0);
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final int TOP_TERRAIN_LAYER = Layer.DEFAULT;
    private static final int TERRAIN_DEPTH = 20;
    public static int LOWER_TERRAIN_LAYER = Layer.FOREGROUND - 1;    // maybe irrelevant
    private final float groundHeightAtX0;
    private final NoiseGenerator noiseGenerator;
    private final Vector2 windowDimensions;
    private final GameObjectCollection gameObjects;

    /**
     * Constructs a new Terrain object.
     *
     * @param windowDimensions The dimensions of the game window.
     * @param seed             The seed for the noise generator to create varied terrain.
     */
    public Terrain(Vector2 windowDimensions, int seed, GameObjectCollection gameObjects) {
        this.groundHeightAtX0 = windowDimensions.y() * RATIO;
        this.windowDimensions = windowDimensions;
        this.noiseGenerator = new NoiseGenerator(seed, (int) groundHeightAtX0);
        this.gameObjects = gameObjects;
    }

    /**
     * Returns the height of the ground at a given x-coordinate.
     *
     * @param x The x-coordinate.
     * @return The height of the ground at the given x-coordinate.
     */
    public float groundHeightAt(float x) {
        float noise = (float) noiseGenerator.noise(x, Block.SIZE * 7);
        return groundHeightAtX0 + noise;
    }

    /**
     * Creates blocks in the specified range.
     *
     * @param minX The minimum x-coordinate of the range.
     * @param maxX The maximum x-coordinate of the range.
     * @return A list of blocks representing the terrain in the specified range.
     */
    public void createInRange(int minX, int maxX) {
//        List<Block> blockList = new ArrayList<>();
        minX -= (minX % Block.SIZE);
        for (int x = minX; x < maxX; x += Block.SIZE) {
            double topY = Math.floor(groundHeightAt(x) / Block.SIZE) * Block.SIZE;
            for (float y = windowDimensions.y(); y > topY; y -= Block.SIZE) {
                Block block = new Block(Vector2.of(x, y), new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR)));
                block.setTag(GROUND_TAG);
                gameObjects.addGameObject(block, Layer.STATIC_OBJECTS);
            }
        }
    }
}