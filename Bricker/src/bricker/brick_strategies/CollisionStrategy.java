package bricker.brick_strategies;

import danogl.GameObject;

/**
 * This interface defines the contract for collision strategies in the game.
 */
public interface CollisionStrategy {
    /**
     * Handles the collision between two game objects.
     *
     * @param obj1 The first game object involved in the collision.
     * @param obj2 The second game object involved in the collision.
     */
    public void onCollision(GameObject obj1, GameObject obj2);
}