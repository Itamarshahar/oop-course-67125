package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * This class implements a collision strategy that starts a camera effect
 * when a collision occurs between two game objects.
 */
public class CameraStrategy implements CollisionStrategy {
    private final CollisionStrategy wrappedCollision;
    private final BrickerGameManager brickerGameManager;

    /**
     * Constructor to initialize the CameraStrategy.
     *
     * @param wrappedCollision   The CollisionStrategy instance to be wrapped by this strategy.
     * @param brickerGameManager The BrickerGameManager instance that manages the game.
     */
    public CameraStrategy(CollisionStrategy wrappedCollision, BrickerGameManager brickerGameManager) {
        this.wrappedCollision = wrappedCollision;
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * Handles the collision between two game objects.
     * It calls the wrapped collision strategy and then starts the camera effect.
     *
     * @param obj1 The first game object involved in the collision.
     * @param obj2 The second game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject obj1, GameObject obj2) {
        wrappedCollision.onCollision(obj1, obj2);
        brickerGameManager.startCamera(obj2.getTag());
    }
}
