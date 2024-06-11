package Bricker.src.bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
/**
 * Paddle.java
 *
 * This class represents the paddle in the game. It handles user input for
 * moving the paddle left and right within the game window boundaries.
 */
public class Paddle extends GameObject {
    private static final float MOVEMENT_SPEED = 300;
    private final UserInputListener inputListener;
    private final int wallWidth;
    private final Vector2 windowDimension;



    /**
     * Construct a new Paddle instance.
     *
     * @param topLeftCorner  Position of the paddle, in window coordinates (pixels).
     * @param dimensions     Width and height of the paddle in window coordinates.
     * @param renderable     The renderable representing the paddle's appearance.
     * @param inputListener  The listener for user input to control the paddle.
     * @param wallWidth      The width of the wall surrounding the game area.
     * @param windowDimension The dimensions of the game window.
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions,
                  Renderable renderable, UserInputListener inputListener, int wallWidth, Vector2 windowDimension) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.wallWidth = wallWidth;
        this.windowDimension = windowDimension;
    }
    /**
     * Update the paddle's position based on user input and the game window boundaries.
     *
     * @param deltaTime The time elapsed, in seconds, since the last frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movmentDir = Vector2.ZERO;
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT)&&
                getTopLeftCorner().x() - wallWidth > 0){
            movmentDir = movmentDir.add(Vector2.LEFT.mult(MOVEMENT_SPEED));
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT) &&
                getTopLeftCorner().x() + getDimensions().x() + wallWidth < windowDimension.x()){
            movmentDir = movmentDir.add(Vector2.RIGHT.mult(MOVEMENT_SPEED));
        }

        setVelocity(movmentDir);
    }


}
