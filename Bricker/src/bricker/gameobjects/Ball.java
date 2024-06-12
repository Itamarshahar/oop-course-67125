package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Ball.java
 *
 * This class represents a ball in the game. It extends the GameObject class
 * and handles collisions with other objects, updating its velocity and playing
 * a sound effect.
 */
public class Ball extends GameObject {

    private int collisionCounter;
    private final Sound collisionSound;

    /**
     * Construct a new Ball instance.
     *
     * @param topLeftCorner  Position of the ball, in window coordinates
     *                       (pixels).
     *                       Note that (0,0) is the top-left corner of
     *                       the window.
     * @param dimensions     Width and height of the ball in window
     *                       coordinates.
     * @param renderable     The renderable representing the ball.
     *                       Can be null, in which case
     *                       the Ball will not be rendered.
     * @param collisionSound The sound to be played on collision.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions,
                Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
        this.collisionCounter = 0;
    }

    /**
     * Gets the current collision counter for the ball.
     *
     * @return The current collision counter.
     */
    public int getCollisionCounter() {
        return collisionCounter;
    }

    /**
     * Handles collision events with other objects. Increments the collision
     * counter,
     * flips the velocity based on the collision normal, and plays the
     * collision sound.
     *
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        collisionCounter++;
        super.onCollisionEnter(other, collision);
        Vector2 newVel = getVelocity().flipped(collision.getNormal());
        collisionSound.play();
        setVelocity(newVel);

    }
}
