package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * This class is responsible for creating the flora. This class implements the Facade design pattern.
 */
public class Flora {
    /**
     * The tag used for identifying fruits in the game.
     */
    public static final String FRUIT_TAG = Fruit.TAG;
    /**
     * The tag used for identifying leaves in the game.
     */
    public static final String LEAF_TAG = Leaf.TAG;
    /**
     * The tag used for identifying trunks in the game.
     */
    public static final String TRUNK_TAG = Trunk.TAG;
    private static final int RANDOM_BOUND_FLORA = 10;
    private static final float HEIGHT_RATIO = (float) (1.0 / 2);
    private static final int LEAF_BOUND = 120;
    private static final int MAX_LEAVES_PER_TREE = 40;
    private static final int MIN_LEAVES_PER_TREE = 15;
    private static final int MAX_FRUIT_PER_TREE = 4;
    private static final int MIN_FRUIT_PER_TREE = 10;
    private static final int MIN_TRUNK_HEIGHT = 2 * Block.SIZE;
    /**
     * Represent the layers of the leaves.
     */
    public static final int LEAF_LAYER = PepseGameManager.LEAF_LAYER;
    /**
     * Represent the size of the leaves.
     */
    public static int BASE_LEAF_SIZE = 20;
    private static final float FRUIT_SIZE = 0.5f * BASE_LEAF_SIZE;
    private static Function<Float, Float> groundHeightFunction;
    private static Consumer<Float> onEaten;
    private static Consumer<Runnable> addDidJustJumpListener;
    private static final Color[] FRUIT_COLORS = {new Color(255, 0, 0),
            new Color(255, 155, 0), new Color(120, 25, 100), new Color(0, 0, 255)};
    private final GameObjectCollection gameObjects;
    private final Random random = new Random(13);
    private RectangleRenderable trunkBlockRenderable;
    private int maxHeight;
    private int treeHeight = 0;
    private float xCoord;

    /**
     * The constructor of the flora.
     *
     * @param gameObjects              the collection of game objects.
     * @param groundHeightFunction     the ground height function.
     * @param onEaten                  the onEaten consumer, of a Avatar for example (used for eating fruit).
     * @param addDidJustJumpListener   the didJustJump callback, of a Avatar for example. Adds an observer
     *                                 to the Avatar's didJustJump observers.
     * @param trunkLayer               the trunk layer.
     * @param topLayer                 the top layer.
     * @param seed                     the seed for random number generation.
     */
    public Flora(GameObjectCollection gameObjects,
                 Function<Float, Float> groundHeightFunction,
                 Consumer<Float> onEaten,
                 Consumer<Runnable> addDidJustJumpListener,
                 int trunkLayer,
                 int topLayer,
                 int seed) {
        this.gameObjects = gameObjects;
        Flora.groundHeightFunction = groundHeightFunction;
        Flora.onEaten = onEaten;
        Flora.addDidJustJumpListener = addDidJustJumpListener;
        this.trunkBlockRenderable =
                new RectangleRenderable(ColorSupplier.approximateColor(Trunk.BASIC_TRUNK_COLOR));
    }

    /**
     * This method creates the flora in the given range.
     *
     * @param minX the minimal x coordinate.
     * @param maxX the maximal x coordinate.
     */
    public void createInRange(int minX, int maxX) {
        minX -= (minX % Block.SIZE); //round down to the nearest block
        for (int x = minX; x < maxX; x += Block.SIZE) {
            int randomInt = new Random().nextInt(RANDOM_BOUND_FLORA);
            if (randomInt != 0) continue;
            this.xCoord = x;
            maxHeight = (int) (groundHeightFunction.apply(xCoord) * HEIGHT_RATIO);
            createTree(x, groundHeightFunction);
        }
    }

    private void createTree(float groundX, Function<Float, Float> groundHeightFunction) {
        createTrunk(groundHeightFunction, groundX);
        createLeaves();
        createFruit();
    }

    private void createTrunk(Function<Float, Float> groundHeightFunction, float groundX) {
        int groundHeight = (int) Math.floor(groundHeightFunction.apply(xCoord));
        int trunkHeight = random.nextInt(maxHeight - MIN_TRUNK_HEIGHT + 1) + MIN_TRUNK_HEIGHT;
        int yCoord = groundHeight - trunkHeight;

        Trunk trunk = new Trunk(Vector2.of(groundX, yCoord), trunkBlockRenderable);
        treeHeight = yCoord;
        trunk.setDimensions(Vector2.of(Block.SIZE, trunkHeight + Block.SIZE));
        trunk.setTopLeftCorner(Vector2.of(groundX, yCoord));
        addDidJustJumpListener.accept(trunk::onJump);
        gameObjects.addGameObject(trunk, Layer.STATIC_OBJECTS);
    }

    private void createFruit() {
        int fruitAmount = random.nextInt(MAX_FRUIT_PER_TREE) + MIN_FRUIT_PER_TREE;
        for (int i = 0; i < fruitAmount; i++) {
            int x = random.nextInt(LEAF_BOUND) - (LEAF_BOUND / 2);
            int y = random.nextInt(LEAF_BOUND) - (treeHeight / 20);
            Vector2 fruitPosition = Vector2.of(xCoord + x, treeHeight - y);
            Fruit fruit = new Fruit(fruitPosition, onEaten, FRUIT_SIZE, FRUIT_COLORS);
            addDidJustJumpListener.accept(fruit::onJump);
            gameObjects.addGameObject(fruit, Layer.DEFAULT);
        }
    }

    private void createLeaves() {
        int numberOfLeaves = random.nextInt(MAX_LEAVES_PER_TREE) + MIN_LEAVES_PER_TREE;
        for (int i = 0; i < numberOfLeaves; i++) {
            int x = random.nextInt(LEAF_BOUND) - (LEAF_BOUND / 2);
            int y = random.nextInt(LEAF_BOUND) - (treeHeight / 20);
            Vector2 leafPosition = Vector2.of(xCoord + x, treeHeight - y);
            Leaf leaf = new Leaf(leafPosition, Vector2.ONES.mult(Leaf.BASE_LEAF_SIZE),
                    new RectangleRenderable(ColorSupplier.approximateColor(Leaf.BASE_LEAF_COLOR)));
            addDidJustJumpListener.accept(leaf::onJump);
            gameObjects.addGameObject(leaf, LEAF_LAYER);
        }
    }
}
