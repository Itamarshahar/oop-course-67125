package Bricker.src.bricker.gameobjects;
import Bricker.src.bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Collision;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import danogl.util.Counter;
/**
 * ExtraPaddle.java
 *
 * This class represents an extra paddle in the game. It extends the Paddle class
 * and handles collisions with other objects, removing itself from the game after
 * a certain number of collisions.
 */
public class ExtraPaddle extends Paddle{
    private final BrickerGameManager gameObjectCollection;
    private final Counter collisionCounter;
    private int paddleLives = 2;
     /**
     * Construct a new ExtraPaddle instance.
     *
     * @param topLeftCorner     Position of the paddle, in window coordinates (pixels).
     *                          Note that (0,0) is the top-left corner of the window.
     * @param dimensions        Width and height of the paddle in window coordinates.
     * @param renderable        The renderable representing the paddle. Can be null, in which case
     *                          the ExtraPaddle will not be rendered.
     * @param inputListener     The user input listener for controlling the paddle.
     * @param wallWidth         The width of the wall surrounding the game area.
     * @param windowDimension   The dimensions of the game window.
     * @param gameObjectCollection The collection of game objects to remove the paddle from.
     */
    public ExtraPaddle(Vector2 topLeftCorner, Vector2 dimensions,
                       Renderable renderable, UserInputListener inputListener, int wallWidth, Vector2 windowDimension
                        , BrickerGameManager gameObjectCollection){
        super(topLeftCorner,dimensions,renderable,inputListener,wallWidth,windowDimension);
        this.collisionCounter = new Counter();
        this.gameObjectCollection = gameObjectCollection;


    }

    /**
     * Handles collision events with other objects. Increments a collision counter
     * and removes the paddle from the game after a certain number of collisions.
     *
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other instanceof Ball){
            collisionCounter.increment();
        }
        if(collisionCounter.value() == paddleLives){
            gameObjectCollection.removeGameObjectHandler(this);
        }

    }


}
