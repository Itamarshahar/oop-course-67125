package Bricker.src.bricker.gameobjects;

import  Bricker.src.bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import danogl.collisions.Layer;

public class Puck extends Ball {
    private final float minHeight;
    private final BrickerGameManager brickerGameManager;

    /**
     * Constructs a new `Puck` object, inheriting from the `Ball` class.
     *
     * @param topLeftCorner Position of the puck in window coordinates (pixels).
     * @param dimensions Width and height of the puck in window coordinates.
     * @param renderable The renderable representing the puck's appearance.
     * @param collisionSound The sound played when the puck collides with other objects.
     * @param minHeight The minimum Y-coordinate the puck can reach before being removed.
     * @param brickerGameManager Reference to the BrickerGameManager instance for game logic interaction.
     */
    public Puck(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound,
                float minHeight, BrickerGameManager brickerGameManager) {
        super(topLeftCorner, dimensions, renderable, collisionSound);
        this.minHeight = minHeight;
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * Handles collisions with other objects, calling the parent class's method first.
     * No specific behavior is added in this overridden method.
     *
     * @param other The other object involved in the collision.
     * @param collision Collision object containing details about the collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
    }

    /**
     * Retrieves the puck's collision counter from the parent class.
     *
     * @return The number of collisions the puck has been involved in.
     */
    @Override
    public int getCollisionCounter() {
        return super.getCollisionCounter();
    }

    /**
     * Updates the puck's position and checks if it goes below the `minHeight`.
     * If it does, it removes the puck from the game using the `brickerGameManager`.
     *
     * @param deltaTime Time elapsed since last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.getCenter().y() > minHeight) {
            brickerGameManager.removeGameObjectHandler(this); // TODO ,
            // Layer.DEFAULT);
            // Remove puck when below minHeight
        }
    }
}
