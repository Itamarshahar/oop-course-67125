package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.*;

import static pepse.PepseGameManager.LOWER_TERRAIN_LAYER;

/**
 * The {@code Terrain} class is responsible for generating and managing the terrain in the game world.
 * It uses noise generation to create realistic and varied ground heights, and manages the terrain's
 * appearance and position in the game environment.
 */
public class Terrain {

    /**
     * Tag used to identify ground objects in the game world.
     */
    public static final String TAG = "ground";

    /**
     * Ratio of ground height relative to the window dimensions.
     */
    public static final float RATIO = (float) (2.0 / 3.0);

    /**
     * Base color used for rendering ground blocks.
     */
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);


    /**
     * The initial ground height at x-coordinate 0.
     */
    private final float groundHeightAtX0;

    /**
     * Noise generator used to create varied terrain heights.
     */
    private final NoiseGenerator noiseGenerator;

    /**
     * Dimensions of the game window.
     */
    private final Vector2 windowDimensions;

    /**
     * Collection of game objects managed by the game.
     */
    private final GameObjectCollection gameObjects;

    /**
     * Constructs a new {@code Terrain} object with the specified parameters.
     *
     * @param windowDimensions The dimensions of the game window.
     * @param seed             The seed for the noise generator, used to create varied terrain.
     * @param gameObjects      The collection of game objects to which terrain blocks will be added.
     */
    public Terrain(Vector2 windowDimensions, int seed, GameObjectCollection gameObjects) {
        this.groundHeightAtX0 = windowDimensions.y() * RATIO;
        this.windowDimensions = windowDimensions;
        this.noiseGenerator = new NoiseGenerator(seed, (int) groundHeightAtX0);
        this.gameObjects = gameObjects;
    }

    /**
     * Returns the height of the ground at a specified x-coordinate.
     *
     * @param x The x-coordinate at which to get the ground height.
     * @return The height of the ground at the specified x-coordinate.
     */
    public float groundHeightAt(float x) {
        float noise = (float) noiseGenerator.noise(x, Block.SIZE * 7);
        return groundHeightAtX0 + noise;
    }

    /**
     * Creates and adds terrain blocks to the game world in the specified x-coordinate range.
     *
     * @param minX The minimum x-coordinate of the range (inclusive).
     * @param maxX The maximum x-coordinate of the range (exclusive).
     */
    public void createInRange(int minX, int maxX) {
        minX -= (minX % Block.SIZE);
        for (int x = minX; x < maxX; x += Block.SIZE) {
            double topY = Math.floor(groundHeightAt(x) / Block.SIZE) * Block.SIZE;
            for (float y = windowDimensions.y() + 150; y > topY; y -= Block.SIZE) {
                Block block = new Block(Vector2.of(x, y),
                        new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR)));
                block.setTag(TAG);
                gameObjects.addGameObject(block, LOWER_TERRAIN_LAYER);
            }
        }
    }
}
