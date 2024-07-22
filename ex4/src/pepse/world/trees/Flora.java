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
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * This class is responsible for creating the flora. This class implements the Facade design pattern.
 */
public class Flora {
    public static final String FRUIT_TAG = Fruit.TAG;
    public static final String LEAF_TAG = Leaf.TAG;
    public static final String TRUNK_TAG = Trunk.TAG;
    private static final int RANDOM_BOUND_FLORA = 10;
//    private static final int BOUND = 20;
    private static final float HIGHT_RATIO = (float) (1.0 / 2);
    private static final int LEAF_BOUND = 120;
    private static final int MAX_LEAVE_PER_TREE = 40;
    private static final int MIN_LEAVS_PER_TREE = 15;
    private static final int MAX_FRUIT_PER_TREE = 4;
    private static final int MIN_FRUIT_PER_TREE = 10;
    private static final int MIN_TRUNK_HIGHT = 2 * Block.SIZE;
//    private static final int MIN_TREE_HEIGHT = 6; // In blocks (not pixels)
//    private static final int MAX_TREE_HEIGHT = 12; // In blocks (not pixels)
//    private static final int TREE_SPACING = 4; // In blocks (not pixels)
    public static final int LEAF_LAYER = PepseGameManager.LEAF_LAYER;
    public static int BASE_LEAF_SIZE = 20;
    private static final float FRUIT_SIZE = 0.5f * BASE_LEAF_SIZE;
    private static Function<Float, Float> groundHeightFunction;
    private static Consumer<Float> onEaten;
//    private static BooleanSupplier didJustJump;
    private static Consumer<Runnable> addDidJustJumpListener;
    private static final Color[] FRUIT_COLORS = {new Color(255, 0, 0), new Color(255, 155, 0), new Color(120, 25, 100), new Color(0, 0, 255)};
    private final GameObjectCollection gameObjects;
//    private final int seed;
//    private final int trunkLayer;
//    private final int topLayer;
    private final Random random = new Random(13);
    private RectangleRenderable trunkBlockRenderable;
//    private Trunk trunk;
//    private final ArrayList<Leaf> leaves = new ArrayList<>();
//    private final ArrayList<Fruit> fruits = new ArrayList<>();
    private int maxHight;
    //    private Random random = new Random();
//    private RectangleRenderable trunkBlockRenderable;
    private int treeHight = 0;
//    private Trunk trunk;
//    private ArrayList<Leaf> leaves = new ArrayList<>();
//    private ArrayList<Fruit> fruits = new ArrayList<>();


//    public static int BASE_LEAF_SIZE = 20;
    private float xCoord;
    //    private Consumer<Float> onEaten;


    /**
     * The constructor of the flora.
     *
     * @param groundHeightFunction   the ground height function.
     * @param onEaten                the onEaten consumer, of a Avatar for example (used for eating fruit).
     * @param addDidJustJumpListener the didJustJump callback, of a Avatar for example. Adds an observer
     *                               to the Avatar's didJustJump observers.
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
//        this.trunkLayer = trunkLayer;
//        this.topLayer = topLayer;
//        this.seed = seed;
        this.trunkBlockRenderable =
                new RectangleRenderable(ColorSupplier.approximateColor(Trunk.BASIC_TRUNK_COLOR));
    }

    /**
     * This method creates the flora in the given range.
     *
     * @param minX the minimal x coordinate.
     * @param maxX the maximal x coordinate.
     * @return the flora in the given range.
     */
    public void createInRange(int minX, int maxX) {
        minX -= (minX % Block.SIZE); //round down to the nearest block
        for (int x = minX; x < maxX; x += Block.SIZE) {
            int randomInt = new Random().nextInt(RANDOM_BOUND_FLORA);
            if (randomInt != 0) continue;
            this.xCoord = x;
            maxHight = (int) (groundHeightFunction.apply(xCoord) * HIGHT_RATIO);

//            Tree tree = new Tree(groundHeightFunction,
//                                x,
//                                onEaten,
//                                addDidJustJumpListener,
//                                seed);
//            float groundHeight = groundHeightFunction.apply((float) x) - Block.SIZE;
            // -1 block to account for ground
            createTree(x,
//                    groundHeight,
//                            onEaten,
//                                addDidJustJumpListener,
                    groundHeightFunction);
        }
    }


    void createTree(float groundX,
//                    float groundHeight,
//                    Consumer<Float> onEaten,
//                    Consumer<Runnable> addDidJustJumpListener,
                    Function<Float, Float> groundHeightFunction) {
        createTrunk(groundHeightFunction, groundX);
        createLeaves();
        createFruit();
    }

    private void createTrunk(Function<Float, Float> groundHeightFunction,
                             float groundX
//                             Consumer<Runnable> addDidJustJumpListener
    ) {
//        TreeTrunk.create(gameObjects, groundHeight, groundX, treeHeight, trunkLayer);
//        TreeTop.create(gameObjects, groundHeight, groundX, treeHeight, topLayer, seed);
        int groundHight = (int) Math.floor(groundHeightFunction.apply(xCoord));
        int trunkHight = random.nextInt(maxHight - MIN_TRUNK_HIGHT + 1) + MIN_TRUNK_HIGHT;
        int yCoord = groundHight - trunkHight;

        Trunk trunk = new Trunk(Vector2.of(groundX, yCoord), trunkBlockRenderable);
        treeHight = yCoord;
        trunk.setDimensions(Vector2.of(Block.SIZE, trunkHight + Block.SIZE));
        trunk.setTopLeftCorner(Vector2.of(groundX, yCoord));
//        this.trunk = trunk;
        addDidJustJumpListener.accept(trunk::onJump);
        gameObjects.addGameObject(trunk, Layer.STATIC_OBJECTS);

    }

    private void createFruit() {
        int fruitAmount = random.nextInt(MAX_FRUIT_PER_TREE) + MIN_FRUIT_PER_TREE;
        for (int i = 0; i < fruitAmount; i++) {
            int x = random.nextInt(LEAF_BOUND) - (LEAF_BOUND / 2);
            int y = random.nextInt(LEAF_BOUND) - (treeHight / 20);
            Vector2 fruitPosition = Vector2.of(xCoord + x, treeHight - y);
            Fruit fruit = new Fruit(fruitPosition, onEaten, FRUIT_SIZE, FRUIT_COLORS);
//            fruits.add(fruit);
//            fruit.setTag();
            addDidJustJumpListener.accept(fruit::onJump);
            gameObjects.addGameObject(fruit, Layer.DEFAULT);
        }
    }


    private void createLeaves() {
        int numberOfLeaves = random.nextInt(MAX_LEAVE_PER_TREE) + MIN_LEAVS_PER_TREE;
        for (int i = 0; i < numberOfLeaves; i++) {
            int x = random.nextInt(LEAF_BOUND) - (LEAF_BOUND / 2);
            int y = random.nextInt(LEAF_BOUND) - (treeHight / 20);
            Vector2 leafPosition = Vector2.of(xCoord + x, treeHight - y);
            Leaf leaf = new Leaf(leafPosition, Vector2.ONES.mult(Leaf.BASE_LEAF_SIZE), new RectangleRenderable(ColorSupplier.approximateColor(Leaf.BASE_LEAF_COLOR)));
//            leaves.add(leaf);
            addDidJustJumpListener.accept(leaf::onJump);
            gameObjects.addGameObject(leaf, LEAF_LAYER);
        }
    }

}
