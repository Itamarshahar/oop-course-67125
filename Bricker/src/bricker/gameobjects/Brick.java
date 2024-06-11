package Bricker.src.bricker.gameobjects;

import Bricker.src.bricker.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
/**
 * Brick.java
 *
 * This class represents a brick in the game. It extends the GameObject class
 * and handles collisions with other objects using a specified
 * CollisionStrategy.
 */
public class Brick extends GameObject {

    private final CollisionStrategy collisionStrategy;

    /**
     * Construct a new Brick instance.
     *
     * @param topLeftCorner     Position of the brick, in window coordinates
     *                          (pixels).
     *
     *                          Note that (0,0) is the top-left corner of the
     *                          window.
     * @param dimensions        Width and height of the brick in window
     *                          coordinates.
     * @param renderable        The renderable representing the brick. Can
     *                          be null, in which case
     *                          the Brick will not be rendered.
     * @param collisionStrategy The strategy to handle collisions with this
     *                         brick.
     */
    public Brick(Vector2 topLeftCorner, Vector2 dimensions,
                 Renderable renderable,
                 CollisionStrategy collisionStrategy) {

        super(topLeftCorner, dimensions, renderable);
        this.collisionStrategy = collisionStrategy;
    }

    /**
     * Handles collision events with other objects by invoking the associated
     * CollisionStrategy.
     *
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.collisionStrategy.onCollision(this,other);
    }
}
