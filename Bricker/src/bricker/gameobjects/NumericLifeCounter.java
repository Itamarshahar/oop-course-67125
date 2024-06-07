package Bricker.src.bricker.gameobjects;


import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;

public class NumericLifeCounter extends GameObject {
    private static final int GREEN = 3;
    private static final int YELLOW = 2;
    private static final int RED = 1;

    private final Counter livesCounter;
    private final TextRenderable txtRenderer;
    private int currentLives;
    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.

     */
     public NumericLifeCounter(Counter livesCounter,
                              Vector2 topLeftCorner, Vector2 dimensions) {
        super(topLeftCorner, dimensions, null);
        txtRenderer = new TextRenderable(Integer.toString(livesCounter.value()));
        txtRenderer.setColor(Color.green);
        this.renderer().setRenderable(txtRenderer);
        this.livesCounter = livesCounter;
        this.currentLives = livesCounter.value();


    }

    /**
     * Update the numeric lives according to current lives count.
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (currentLives != livesCounter.value()){
            txtRenderer.setString(Integer.toString(livesCounter.value()));
            if (livesCounter.value() >= GREEN){
                txtRenderer.setColor(Color.green);
            }
            else if(livesCounter.value() == YELLOW){
                txtRenderer.setColor(Color.yellow);
            }
            else if(livesCounter.value() == RED){
                txtRenderer.setColor(Color.red);
            }
            currentLives = livesCounter.value();
        }
    }

}
