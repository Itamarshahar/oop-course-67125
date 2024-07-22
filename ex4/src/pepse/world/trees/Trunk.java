package pepse.world.trees;

import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;

/**
 * Represents the trunk of a tree in the game world.
 * The trunk can respond to a jump action by changing its color.
 */
public class Trunk extends Block implements ResponsiveToJump {

    /**
     * Basic color of the trunk.
     */
    public static final Color BASIC_TRUNK_COLOR = new Color(100, 50, 20);

    /**
     * Tag used to identify trunk objects.
     */
    public static final String TAG = "trunk";

    /**
     * Constructs a new Trunk instance.
     *
     * @param topLeftCorner The position of the top left corner of the trunk.
     * @param renderable    The renderable representing the trunk's appearance.
     */
    public Trunk(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, renderable);
        this.setTag(TAG);
    }

    /**
     * Updates the state of the trunk.
     *
     * @param deltaTime The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    /**
     * Responds to a jump action by changing the trunk's color.
     */
    @Override
    public void onJump() {
        // On jump of the avatar we will change the trunk color
        Color newTrunkColor = ColorSupplier.approximateColor(
                Trunk.BASIC_TRUNK_COLOR);
        RectangleRenderable newTrunkRenderer = new
                RectangleRenderable(newTrunkColor);
        renderer().setRenderable(newTrunkRenderer);
    }
}