package Bricker.src.bricker.brick_strategies;

import Bricker.src.bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * This class implements a collision strategy that adds an extra life
 * to the player when a collision occurs between two game objects.
 */
public class ExtraLifeStrategy implements CollisionStrategy {
    private final CollisionStrategy wrappedCollision;
    private final BrickerGameManager brickerGameManager;

    /**
     * Constructor to initialize the ExtraLifeStrategy.
     *
     * @param wrappedCollision   The CollisionStrategy instance to be wrapped
     *                          by this strategy.
     * @param brickerGameManager The BrickerGameManager instance that manages
     *                          the game.
     */
    public ExtraLifeStrategy(CollisionStrategy wrappedCollision,
                             BrickerGameManager brickerGameManager) {
        this.wrappedCollision = wrappedCollision;
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * Handles the collision between two game objects.
     * It calls the wrapped collision strategy and then adds an extra life
     * to the player.
     *
     * @param obj1 The first game object involved in the collision.
     * @param obj2 The second game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject obj1, GameObject obj2) {
        wrappedCollision.onCollision(obj1, obj2);
        brickerGameManager.addHeart(obj1.getCenter());
    }
}
