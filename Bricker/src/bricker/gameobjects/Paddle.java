package Bricker.src.bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

public class Paddle extends GameObject {
    private static final float MOVEMENT_SPEED = 300;
    private final UserInputListener inputListener;
    private final int wallWidth;
    private final Vector2 windowDimension;


    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions,
                  Renderable renderable, UserInputListener inputListener, int wallWidth, Vector2 windowDimension) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.wallWidth = wallWidth;
        this.windowDimension = windowDimension;
    }

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
