package Bricker.src.bricker.gameobjects;

import Bricker.src.bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import static Bricker.src.bricker.main.BrickerGameManager.ORIGINAL_PADDLE_TAG;

public class Heart extends GameObject {
    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     * Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     * the GameObject will not be rendered.
     * @param gameObjects all the objects on the window
     * @param livesCounter counts the number of the users lives
     * @param used to get the dimensoin of the window, for removing the heart from the gameObjects when
     * it is outside the display
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
        this.setVelocity(new Vector2(0, 100)); // Set initial downward movement
    }

    /**
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     *                  <p>
     *                  this function removes the heart from the board if it hits the paddle and
     *                  make increment of the users lives
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        brickerGameManager.increaseLifeCounter();
        brickerGameManager.removeGameObjectHandler(this);


    }

    /**
     * @param other The other GameObject- that we could collide with.
     * @return boolean - true if collision will execute, false if not
     * <p>
     * checks if collision should be executed
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
     *                  <p>
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
