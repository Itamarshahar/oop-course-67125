package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a block in the game world.
 * A block is a game object with a fixed size and specific physical properties.
 */
public class Block extends GameObject {
    /**
     * The size of the block in pixels.
     */
    public static final int SIZE = 30;

    /**
     * Constructs a new Block.
     *
     * @param topLeftCorner The position of the top-left corner of the block.
     * @param renderable    The renderable representing the block's appearance.
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }
}