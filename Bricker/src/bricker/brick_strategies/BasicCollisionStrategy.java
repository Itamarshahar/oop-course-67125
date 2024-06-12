package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.util.Counter;
import bricker.brick_strategies.CollisionStrategy;

/**
 * This class implements the basic collision strategy for the game.
 * When a collision occurs between two game objects, it removes one of the objects
 * (presumably the brick) from the game and decrements the brick counter.
 */
public class BasicCollisionStrategy implements CollisionStrategy {
    /**
     * The bricksCounter is a Counter object that keeps track of the
     * number of bricks remaining in the game.
     * It is used to determine when the game should end or proceed to
     * the next level.
     */
    protected Counter bricksCounter;

    /**
     * The objectCollection is a BrickerGameManager instance that manages
     * the game objects, such as bricks,
     * balls, and paddles. It is responsible for creating, updating, and
     * removing game objects, as well as
     * handling collisions and other game logic.
     */
    protected BrickerGameManager objectCollection;

    /**
     * Constructor to initialize the BasicCollisionStrategy.
     *
     * @param objectCollection The BrickerGameManager instance that manages
     *                         the game objects.
     */
    public BasicCollisionStrategy(BrickerGameManager objectCollection) {
        this.objectCollection = objectCollection;
        this.bricksCounter = objectCollection.getBrickCounter();
    }

    /**
     * Handles the collision between two game objects.
     * If one of the objects is removed from the game, it decrements the
     * brick counter.
     *
     * @param obj1 The first game object involved in the collision.
     * @param obj2 The second game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject obj1, GameObject obj2) {
        if (objectCollection.removeGameObjectHandler(obj1)) {
            bricksCounter.decrement();
        }
    }
}
