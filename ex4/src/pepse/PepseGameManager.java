package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import pepse.world.daynight.Sun;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;
//import pepse.world.trees.Tree;
import danogl.util.Vector2;

import java.util.Random;
/**
 * The {@code PepseGameManager} class manages the overall game environment, including
 * initialization and updating of game objects such as terrain, sky, sun, and flora.
 * It also handles avatar creation and tracks the avatar's movement within the game world.
 * <p>
 * This class extends {@link danogl.GameManager} and is responsible for setting up the
 * game environment, managing day-night cycles, and dynamically creating and removing
 * game objects based on the camera's position.
 */

public class PepseGameManager extends GameManager {
    /**
     * Represent the duration of the daytime cycle
     */
    public static final int DAYTIME_CYCLE_DURATION = 30;
    private static final Float MIDNIGHT_OPACITY = 0.5f;
    private static final int DEFAULT_SEED = 42;
    private static final int TREE_TRUNK_LAYER = Layer.DEFAULT + 1;
    private static final int TREE_LEAVES_LAYER = Layer.DEFAULT + 3;
    private static final int WORLD_RENDER_BUFFER = Block.SIZE * 10;
    private static final int AVATAR_LAYER = Layer.DEFAULT + 3;
    private static final int TOP_TERRAIN_LAYER = Layer.DEFAULT;
    private static final Vector2 AVATAR_INIT_POS = Vector2.of(100, 90);

    /**
     * Represent the layer of leaves.
     */
    public static int LEAF_LAYER = Layer.BACKGROUND+1;
    private static final int SUN_LAYER = Layer.BACKGROUND;
    private WindowController windowController;
    private Terrain terrain;
    private Vector2 initialAvatarLocation = null;

    private int worldLeftBound = 0;
    private int worldRightBound = 0;

    private Camera camera = null;

    private Flora flora;
    private Avatar avatar;

    /**
     * Main method to start the game.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    /**
     * Initializes the game, setting up the game environment, including sky, terrain,
     * day-night cycle, sun, and flora. It also creates the avatar and initializes the world.
     *
     * @param imageReader An instance of ImageReader to load images.
     * @param soundReader An instance of SoundReader to load sounds.
     * @param inputListener An instance of UserInputListener to handle user input.
     * @param windowController An instance of WindowController to control the game window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader,
                inputListener, windowController);
        this.windowController = windowController;
        int seed = DEFAULT_SEED;
        ///////////////////////////////////////////////////////////////////////
        // Sky Part
        GameObject sky = Sky.create(windowController.getWindowDimensions());
        gameObjects().addGameObject(sky, Layer.BACKGROUND);

        ///////////////////////////////////////////////////////////////////////

        // Terrain Part
        Random random = new Random();
        this.terrain = new Terrain(windowController.getWindowDimensions(),
                random.nextInt(), gameObjects());
//        Terrain terrain = new Terrain(windowController.getWindowDimensions(),
//                0 );
//       terrain.createInRange(-5, (int) windowController.getWindowDimensions().x() + 5); //
        // TODO - itamar - verify why (-5) ?

        ///////////////////////////////////////////////////////////////////////

        // Light and Dark Part
        GameObject night = Night.create(windowController.getWindowDimensions(),
                DAYTIME_CYCLE_DURATION);
        gameObjects().addGameObject(night, Layer.BACKGROUND); // TODO
        ///////////////////////////////////////////////////////////////////////

        // Sun Part
        GameObject sun = Sun.create(windowController.getWindowDimensions(),
                DAYTIME_CYCLE_DURATION);
        gameObjects().addGameObject(sun, SUN_LAYER); // TODO

        ///////////////////////////////////////////////////////////////////////

        // Sun Halo Part
        GameObject sunHalo = SunHalo.create(windowController.getWindowDimensions(),
                DAYTIME_CYCLE_DURATION, sun);
        sunHalo.addComponent(deltaTime -> sunHalo.setCenter(sun.getCenter()));

        gameObjects().addGameObject(sunHalo, SUN_LAYER); // TODO


        ///////////////////////////////////////////////////////////////////////
        // Truck
//        Trunk trunk = new Trunk(windowController.getWindowDimensions()); //
        // TODO fix

//         this.avatar = new Avatar(AVATAR_INIT_POS,
//                 inputListener,
//                    imageReader);
        createAvatar(imageReader, inputListener,windowController);
        trackAvatar();


//
        this.flora = new Flora(gameObjects(),
                terrain::groundHeightAt,
                this.avatar::changeEnergyBy,
                this.avatar::addDidJustJumpObserver,
                TREE_TRUNK_LAYER,
                TREE_LEAVES_LAYER,
                seed);

        initWorld();
        addCollisionListeners();

    }

    /**
     * Updates the game state, including creating new world sections as the camera moves.
     *
     * @param deltaTime The time elapsed since the last update.
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
            createWorldInRange(worldLeftBound - WORLD_RENDER_BUFFER,
                    worldLeftBound);
            worldLeftBound -= WORLD_RENDER_BUFFER;
            worldRightBound -= WORLD_RENDER_BUFFER;
        }

    }

    /**
     * Tracks the avatar by setting the camera to follow it.
     */
    private void trackAvatar() {
        this.camera = new Camera(avatar,
                Vector2.ZERO, windowController.getWindowDimensions(),
                windowController.getWindowDimensions());
        setCamera(camera);
        initialAvatarLocation = getGroundCenter().subtract(
                Avatar.DEFAULT_DIMENSIONS);

        windowController.getWindowDimensions().mult(0.5f).subtract(
                initialAvatarLocation);
    }

    /**
     * Initializes the world bounds and creates the initial world section.
     */
    private void initWorld() {
        worldLeftBound = -WORLD_RENDER_BUFFER;
        worldRightBound = (int) windowController.getWindowDimensions().x() +
                WORLD_RENDER_BUFFER;
        createWorldInRange(worldLeftBound, worldRightBound);
    }

    /**
     * Creates a section of the world between the specified bounds.
     *
     * @param leftBound The left boundary of the world section.
     * @param rightBound The right boundary of the world section.
     */
    private void createWorldInRange(int leftBound, int rightBound) {
        terrain.createInRange(leftBound, rightBound);
        flora.createInRange(leftBound, rightBound);
        collectHiddenObjects();
    }

    /**
     * Removes game objects that are outside the current world bounds.
     */
    private void collectHiddenObjects() {
        for (GameObject obj : gameObjects()) {
            if (obj.getCenter().x() < worldLeftBound ||
                    obj.getCenter().x() > worldRightBound) {
                removeObjectsHandler(obj);

            }
        }
    }

    /**
     * Handles the removal of game objects based on their tags.
     *
     * @param obj The game object to be removed.
     */
    private void removeObjectsHandler(GameObject obj) {
        switch (obj.getTag()) {
            case Terrain.GROUND_TAG:
                gameObjects().removeGameObject(obj,
                        Terrain.LOWER_TERRAIN_LAYER);
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
     * Creates and initializes the avatar.
     *
     * @param imageReader An instance of ImageReader to load avatar images.
     * @param inputListener An instance of UserInputListener to handle user input.
     * @param windowController An instance of WindowController to control the game window.
     */
    private void createAvatar(ImageReader imageReader, UserInputListener inputListener,
                              WindowController windowController) {
        Avatar avatar = new Avatar(AVATAR_INIT_POS, inputListener, imageReader);
        avatar.setCenter(Vector2.of(windowController.getWindowDimensions().x() * 0.5f, 10));
        // TODO change the 10
        gameObjects().addGameObject(avatar, Layer.DEFAULT);
        Avatar.displayEnergy(gameObjects());
//        gameObjects().addGameObject(GrapichEnergyCounter.create(avatar), Layer.UI);
        this.avatar = avatar;
    }

    /**
     * Calculates the center position of the ground at the midpoint of the window width.
     *
     * @return The center position of the ground as a Vector2.
     */
    private Vector2 getGroundCenter() {
        float x = windowController.getWindowDimensions().x() / 2;
        float y = terrain.groundHeightAt(x);
        return new Vector2(x, y);
    }

    /**
     * Configures the collision layers for different game objects.
     */
    private void addCollisionListeners() {
        gameObjects().layers().shouldLayersCollide(
                AVATAR_LAYER,
                TREE_LEAVES_LAYER,
                false);
        gameObjects().layers().shouldLayersCollide(
                AVATAR_LAYER,
                TOP_TERRAIN_LAYER,
                true);
        gameObjects().layers().shouldLayersCollide(
                AVATAR_LAYER,
                TREE_TRUNK_LAYER,
                true);
        gameObjects().layers().shouldLayersCollide(
                Terrain.LOWER_TERRAIN_LAYER,
                TREE_LEAVES_LAYER,
                false);

        gameObjects().layers().shouldLayersCollide(
                TOP_TERRAIN_LAYER,
                TREE_LEAVES_LAYER,
                true);
    }
}
