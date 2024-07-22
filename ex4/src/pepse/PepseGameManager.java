package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;
import danogl.util.Vector2;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

/**
 * Main class for the PEPS-E game.
 */
public class PepseGameManager extends GameManager {
    /**
     * Daytime cycle length.
     */
    public static final int DAYTIME_CYCLE_DURATION = 30;
    private static final int DEFAULT_SEED = 42;
    private static final int TREE_TRUNK_LAYER = Layer.DEFAULT + 1;
    /**
     * Represent the layers of the leaves
     */
    public static final int TREE_LEAVES_LAYER = Layer.BACKGROUND + 1;
    private static final int WORLD_RENDER_BUFFER = Block.SIZE * 10;
    private static final int AVATAR_LAYER = Layer.DEFAULT + 3;
    private static final int TOP_TERRAIN_LAYER = Layer.DEFAULT;
    private static final Vector2 AVATAR_INIT_POS = Vector2.of(100, 90);
    private static final int SUN_LAYER = Layer.BACKGROUND;
    // Define the map with collision settings as a class variable
    private static final Map<Map.Entry<Integer, Integer>, Boolean> COLLISION_SETTINGS = new HashMap<>();

    static {
        COLLISION_SETTINGS.put(Map.entry(AVATAR_LAYER, TREE_LEAVES_LAYER), false);
        COLLISION_SETTINGS.put(Map.entry(AVATAR_LAYER, TOP_TERRAIN_LAYER), true);
        COLLISION_SETTINGS.put(Map.entry(AVATAR_LAYER, TREE_TRUNK_LAYER), true);
        COLLISION_SETTINGS.put(Map.entry(Terrain.LOWER_TERRAIN_LAYER, TREE_LEAVES_LAYER), false);
        COLLISION_SETTINGS.put(Map.entry(TOP_TERRAIN_LAYER, TREE_LEAVES_LAYER), true);
    }


    private WindowController windowController;
    private Terrain terrain;
    private Vector2 initialAvatarLocation = null;
    private int worldLeftBound = 0;
    private int worldRightBound = 0;
    private Camera camera = null;
    private Flora flora;
    private Avatar avatar;

    /**
     * Main method to run the game.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    /**
     * Initializes the game.
     *
     * @param imageReader    Image reader for loading images
     * @param soundReader    Sound reader for loading sounds
     * @param inputListener  User input listener
     * @param windowController Window controller for managing the game window
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowController = windowController;
        int seed = DEFAULT_SEED;

        // Sky Part
        GameObject sky = Sky.create(windowController.getWindowDimensions());
        gameObjects().addGameObject(sky, Layer.BACKGROUND);

        // Terrain Part
        Random random = new Random();
        this.terrain = new Terrain(windowController.getWindowDimensions(),
                random.nextInt(), gameObjects());

        // Light and Dark Part
        GameObject night = Night.create(windowController.getWindowDimensions(),
                DAYTIME_CYCLE_DURATION);
        gameObjects().addGameObject(night, Layer.BACKGROUND);

        // Sun Part
        GameObject sun = Sun.create(windowController.getWindowDimensions(),
                DAYTIME_CYCLE_DURATION);
        gameObjects().addGameObject(sun, SUN_LAYER);

        // Sun Halo Part
        GameObject sunHalo = SunHalo.create(windowController.getWindowDimensions(),
                DAYTIME_CYCLE_DURATION, sun);
        sunHalo.addComponent(deltaTime -> sunHalo.setCenter(sun.getCenter()));
        gameObjects().addGameObject(sunHalo, SUN_LAYER);

        // Avatar Part
        createAvatar(imageReader, inputListener, windowController);
        trackAvatar();

        this.flora = new Flora(gameObjects(), terrain::groundHeightAt, this.avatar::changeEnergyBy,
                this.avatar::addDidJustJumpObserver, TREE_TRUNK_LAYER, TREE_LEAVES_LAYER, seed);

        initWorld();
        applyCollisionSettings();
    }

    /**
     * Updates the game state.
     *
     * @param deltaTime Time passed since the last frame
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        Vector2 windowDimensions = windowController.getWindowDimensions();
        float rightScreenX = camera.screenToWorldCoords(windowDimensions).x();
        float leftScreenX = camera.screenToWorldCoords(windowDimensions).x() - windowDimensions.x();

        if (rightScreenX >= worldRightBound) {
            createWorldInRange(worldRightBound, worldRightBound + WORLD_RENDER_BUFFER);
            worldRightBound += WORLD_RENDER_BUFFER;
            worldLeftBound += WORLD_RENDER_BUFFER;
        }

        if (leftScreenX <= worldLeftBound) {
            createWorldInRange(worldLeftBound - WORLD_RENDER_BUFFER, worldLeftBound);
            worldLeftBound -= WORLD_RENDER_BUFFER;
            worldRightBound -= WORLD_RENDER_BUFFER;
        }
    }

    /**
     * Tracks the avatar with the camera.
     */
    private void trackAvatar() {
        this.camera = new Camera(avatar, Vector2.ZERO, windowController.getWindowDimensions(),
                windowController.getWindowDimensions());
        setCamera(camera);
        initialAvatarLocation = getGroundCenter().subtract(Avatar.DEFAULT_DIMENSIONS);
        windowController.getWindowDimensions().mult(0.5f).subtract(initialAvatarLocation);
    }

    /**
     * Initializes the game world.
     */
    private void initWorld() {
        worldLeftBound = -WORLD_RENDER_BUFFER;
        worldRightBound = (int) windowController.getWindowDimensions().x() + WORLD_RENDER_BUFFER;
        createWorldInRange(worldLeftBound, worldRightBound);
    }

    /**
     * Creates the game world in the specified range.
     *
     * @param leftBound  The left boundary of the range
     * @param rightBound The right boundary of the range
     */
    private void createWorldInRange(int leftBound, int rightBound) {
        terrain.createInRange(leftBound, rightBound);
        flora.createInRange(leftBound, rightBound);
        removeOutOfBoundObjects();
    }

    /**
     * Checks if the object is outside the world bounds.
     *
     * @param obj The game object to check
     * @return true if the object is outside the bounds, false otherwise
     */
    private boolean isObjectOutOfBounds(GameObject obj) {
        return obj.getCenter().x() < worldLeftBound ||
                obj.getCenter().x() > worldRightBound;
    }

    /**
     * Collects and removes objects that are outside the world bounds.
     */
    private void removeOutOfBoundObjects() {
        for (GameObject obj : gameObjects()) {
            if (isObjectOutOfBounds(obj)) {
                removeObjectsHandler(obj);
            }
        }
    }

    /**
     * Removes objects from the game based on their tags.
     *
     * @param obj The object to be removed
     */
    private void removeObjectsHandler(GameObject obj) {
        switch (obj.getTag()) {
            case Terrain.TAG:
                gameObjects().removeGameObject(obj, Terrain.LOWER_TERRAIN_LAYER);
            case Flora.LEAF_TAG:
                gameObjects().removeGameObject(obj, TREE_LEAVES_LAYER);
                break;
            case Flora.TRUNK_TAG:
                gameObjects().removeGameObject(obj, TREE_TRUNK_LAYER);
                break;
            case Flora.FRUIT_TAG:
                gameObjects().removeGameObject(obj, TREE_TRUNK_LAYER);
                break;
        }
    }

    /**
     * Creates the avatar.
     *
     * @param imageReader      Image reader for loading images
     * @param inputListener    User input listener
     * @param windowController Window controller for managing the game window
     */
    private void createAvatar(ImageReader imageReader, UserInputListener
            inputListener, WindowController windowController) {
        Avatar avatar = new Avatar(AVATAR_INIT_POS, inputListener, imageReader);
        avatar.setCenter(Vector2.of(windowController.getWindowDimensions().x() * 0.5f, 10));
        gameObjects().addGameObject(avatar, Layer.DEFAULT);
        Avatar.displayEnergy(gameObjects());
        this.avatar = avatar;
    }

    /**
     * Gets the ground center position.
     *
     * @return The ground center position as a Vector2
     */
    private Vector2 getGroundCenter() {
        float x = windowController.getWindowDimensions().x() / 2;
        float y = terrain.groundHeightAt(x);
        return new Vector2(x, y);
    }
    private void applyCollisionSettings() {
        // Apply the collision settings
        for (Entry<Map.Entry<Integer, Integer>, Boolean> entry : COLLISION_SETTINGS.entrySet()) {
            Map.Entry<Integer, Integer> layers = entry.getKey();
            int layer1 = layers.getKey();
            int layer2 = layers.getValue();
            boolean shouldCollide = entry.getValue();
            gameObjects().layers().shouldLayersCollide(layer1, layer2, shouldCollide);
        }
    }
}
