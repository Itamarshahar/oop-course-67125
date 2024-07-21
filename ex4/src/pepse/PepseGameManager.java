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
import pepse.world.trees.Flora;
//import pepse.world.trees.Tree;
import danogl.util.Vector2;
import java.util.List;
import java.util.Random;

public class PepseGameManager extends GameManager {
    public static final int DAYTIME_CYCLE_DURATION = 30;
    private static final Float MIDNIGHT_OPACITY = 0.5f;
    private static final int DEFAULT_SEED = 42;
    private static final int TREE_TRUNK_LAYER = Layer.DEFAULT + 1;
    private static final int TREE_LEAVES_LAYER = Layer.DEFAULT + 3;
    private static final int WORLD_RENDER_BUFFER = Block.SIZE * 10;
    private static final int AVATAR_LAYER = Layer.DEFAULT + 3;
    private static final int TOP_TERRAIN_LAYER = Layer.DEFAULT;
    private static final Vector2 AVATAR_INIT_POS = Vector2.of(100, 90);

    private WindowController windowController;
    private Terrain terrain;
    private Vector2 initialAvatarLocation = null;

    private int worldLeftBound = 0;
    private int worldRightBound = 0;

    private Camera camera = null;

    private Flora flora;
    private Avatar avatar;


    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    // TODO doc
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowController = windowController;
        int seed = DEFAULT_SEED;
        ///////////////////////////////////////////////////////////////////////
        // Sky Part
        GameObject sky = Sky.create(windowController.getWindowDimensions());
        gameObjects().addGameObject(sky, Layer.BACKGROUND);

        ///////////////////////////////////////////////////////////////////////

        // Terrain Part
        Random random = new Random();
        this.terrain = new Terrain(windowController.getWindowDimensions(), random.nextInt());
//        Terrain terrain = new Terrain(windowController.getWindowDimensions(),
//                0 );
        List<Block> blocks = terrain.createInRange(-5, (int) windowController.getWindowDimensions().x() + 5); //
        // TODO - itamar - verify why (-5) ?
        for (Block block : blocks) {
            gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
        }
        ///////////////////////////////////////////////////////////////////////

        // Light and Dark Part
        float cycleLength = 30;
        GameObject night = Night.create(windowController.getWindowDimensions(), cycleLength);
        gameObjects().addGameObject(night, Layer.BACKGROUND); // TODO
        ///////////////////////////////////////////////////////////////////////

        // Sun Part
        GameObject sun = Sun.create(windowController.getWindowDimensions(), cycleLength);
        gameObjects().addGameObject(sun, Layer.BACKGROUND); // TODO

        ///////////////////////////////////////////////////////////////////////

        // Sun Halo Part
        GameObject sunHalo = SunHalo.create(windowController.getWindowDimensions(), cycleLength, sun);
        sunHalo.addComponent(deltaTime -> sunHalo.setCenter(sun.getCenter()));

        gameObjects().addGameObject(sunHalo, Layer.BACKGROUND); // TODO


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

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    private void trackAvatar() {
        this.camera = new Camera(avatar, Vector2.ZERO, windowController.getWindowDimensions(),
                windowController.getWindowDimensions());
        setCamera(camera);
            initialAvatarLocation = getGroundCenter().subtract(Avatar.AVATAR_DIMENSIONS);

        windowController.getWindowDimensions().mult(0.5f).subtract(initialAvatarLocation);
    }

    private void initWorld() {
        worldLeftBound = -WORLD_RENDER_BUFFER;
        worldRightBound = (int) windowController.getWindowDimensions().x() + WORLD_RENDER_BUFFER;
        createWorldInRange(worldLeftBound, worldRightBound);
    }

    private void createWorldInRange(int leftBound, int rightBound) {
        terrain.createInRange(leftBound, rightBound);
        flora.createInRange(leftBound, rightBound);
        collectHiddenObjects();
    }

    private void collectHiddenObjects() {
        for (GameObject obj : gameObjects()) {
            if (obj.getCenter().x() < worldLeftBound || obj.getCenter().x() > worldRightBound) {
                removeObject(obj);
            }
        }
    }

    private void removeObject(GameObject obj) {
        switch (obj.getTag()) {
            case Terrain.TOP_TAG:
                gameObjects().removeGameObject(obj, TOP_TERRAIN_LAYER);
                break;
            case Terrain.GROUND_TAG:
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
    private void createAvatar(ImageReader imageReader, UserInputListener inputListener,
                              WindowController windowController) {
        Avatar avatar = new Avatar(AVATAR_INIT_POS, inputListener, imageReader);
        avatar.setCenter(Vector2.of(windowController.getWindowDimensions().x() * 0.5f, 10)); // TODO change the 10
        gameObjects().addGameObject(avatar, Layer.DEFAULT);
//        gameObjects().addGameObject(GrapichEnergyCounter.create(avatar), Layer.UI);
        this.avatar = avatar;
    }
     private Vector2 getGroundCenter() {
        float x = windowController.getWindowDimensions().x() / 2;
        float y = terrain.groundHeightAt(x);
        return new Vector2(x, y);
    }
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


