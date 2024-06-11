package Bricker.src.bricker.brick_strategies;

import Bricker.src.bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * This class implements a collision strategy that creates two new pucks
 * when a collision occurs between two game objects.
 */
public class PuckStrategy implements CollisionStrategy {
    private final CollisionStrategy baseStrategy;
    private final BrickerGameManager objectCollection;

    /**
     * Constructor to initialize the PuckStrategy.
     *
     * @param baseStrategy     The base CollisionStrategy instance to be wrapped by this strategy.
     * @param objectCollection The BrickerGameManager instance that manages the game objects.
     */
    public PuckStrategy(CollisionStrategy baseStrategy, BrickerGameManager objectCollection) {
        this.objectCollection = objectCollection;
        this.baseStrategy = baseStrategy;
    }

    /**
     * Handles the collision between two game objects.
     * It calls the base collision strategy and then creates two new pucks at the collision position.
     *
     * @param obj1 The first game object involved in the collision.
     * @param obj2 The second game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject obj1, GameObject obj2) {
        baseStrategy.onCollision(obj1, obj2);
        objectCollection.createPuck(obj1.getCenter());
        objectCollection.createPuck(obj1.getCenter());
    }
}