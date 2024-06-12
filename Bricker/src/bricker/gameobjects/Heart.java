package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import static bricker.main.BrickerGameManager.ORIGINAL_PADDLE_TAG;

/**
 * Heart.java
 *
 * This class represents a heart (life) object in the game. It extends the GameObject class
 * and handles collisions with the paddle, increasing the player's life count and removing
 * itself from the game.
 */
public class Heart extends GameObject {
       /**
     * Construct a new Heart instance.
     *
     * @param topLeftCorner     Position of the heart, in window coordinates (pixels).
     *                          Note that (0,0) is the top-left corner of the window.
     * @param dimensions        Width and height of the heart in window coordinates.
     * @param renderable        The renderable representing the heart. Can be null, in which case
     *                          the Heart will not be rendered.
     * @param gameObjects       The collection of game objects to remove the heart from.
     * @param livesCounter      The counter for tracking the number of lives.
     * @param minY              The minimum Y-coordinate for the heart to be removed from the game.
     * @param brickerGameManager The game manager instance for handling game object removal.
     * @param paddleTag         The tag of the paddle object for collision detection.
     */

    private final String paddleTag;
    private final float minY;
    private final BrickerGameManager brickerGameManager;


    /**
     * constructor
     */
    public Heart(Vector2 topLeftCorner,
                 Vector2 dimensions,
                 Renderable renderable,
                 BrickerGameManager brickerGameManager,
                 String paddleTag,
                 float minY) {
        super(topLeftCorner, dimensions, renderable);
        this.brickerGameManager = brickerGameManager;
        this.paddleTag = paddleTag;
        this.minY = minY;
//        this.setVelocity(new Vector2(0, 100)); // Set initial downward movement
    }
     /**
     * Handles collision events with other objects. Increases the life count and
     * removes the heart from the game when colliding with the paddle.
     *
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        brickerGameManager.increaseLifeCounter();
        brickerGameManager.removeGameObjectHandler(this);
    }

/**
     * Determines whether a collision with the given GameObject should be executed.
     * Only collides with the paddle.
     *
     * @param other The other GameObject to check for collision.
     * @return True if collision should be executed, false otherwise.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return (other.getTag().equals(ORIGINAL_PADDLE_TAG));// {
//            return super.shouldCollideWith(other);
//        }
//        return false;
    }

    /**
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     *
     *                  used to remove the heart from the window when it gets outside the display
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.getCenter().y() > minY) {
            brickerGameManager.removeGameObjectHandler(this);
        }
    }
}
