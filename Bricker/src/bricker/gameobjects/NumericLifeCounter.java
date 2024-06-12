package bricker.gameobjects;


import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;
/**
 * NumericLifeCounter.java
 *
 * This class represents a numeric life counter in the game. It displays the
 * number of lives remaining and changes the text color based on the current
 * life count.
 */
public class NumericLifeCounter extends GameObject {
    private static final int GREEN = 3;
    private static final int YELLOW = 2;
    private static final int RED = 1;

    private final Counter livesCounter;
    private final TextRenderable txtRenderer;
    private int currentLives;
    /**
     * Construct a new NumericLifeCounter instance.
     *
     * @param livesCounter   The counter for tracking the number of lives.
     * @param topLeftCorner  Position of the life counter, in window coordinates (pixels).
     * @param dimensions     Width and height of the life counter in window coordinates.
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
     * Update the numeric life counter display based on the current life count.
     * Changes the text color based on the number of lives remaining.
     *
     * @param deltaTime The time elapsed, in seconds, since the last frame.
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
