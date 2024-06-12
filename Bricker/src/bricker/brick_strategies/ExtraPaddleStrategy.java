
package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
/**
 * The ExtraPaddleStrategy class implements the CollisionStrategy interface and is responsible
 * for adding an extra paddle to the game when a collision occurs between two game objects.
 */
public class ExtraPaddleStrategy implements CollisionStrategy {
    private final CollisionStrategy wrappedCollision;
    private final BrickerGameManager brickerGameManager;

    /**
     * Constructs a new ExtraPaddleStrategy instance.
     *
     * @param wrappedCollision   The CollisionStrategy to be wrapped by
     *                           this strategy.
     * @param brickerGameManager The BrickerGameManager instance used to
     *                           add an extra paddle.
     */
    ExtraPaddleStrategy(CollisionStrategy wrappedCollision,
                        BrickerGameManager brickerGameManager) {
        this.wrappedCollision = wrappedCollision;
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * Handles the collision between two game objects.
     *
     * @param obj1 The first game object involved in the collision.
     * @param obj2 The second game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject obj1, GameObject obj2) {
        // Call the wrapped collision strategy's onCollision method
        wrappedCollision.onCollision(obj1, obj2);
        // Add an extra paddle to the game
        brickerGameManager.addExtraPaddle();
    }
}