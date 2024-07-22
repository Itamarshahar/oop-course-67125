package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.world.Avatar;

import java.awt.*;
import java.util.function.Consumer;

/**
 * Represents a fruit in the game world, which can respond to jumps and be
 * eaten by the avatar.
 */
public class Fruit extends GameObject implements ResponsiveToJump {
    /**
     * Tag used to identify fruit objects.
     */
    public static final String TAG = "fruit";

    /**
     * Initial color of the fruit.
     */
    private final Color initailFruitColor = new Color(255, 0, 0);

    /**
     * Energy bonus provided when the fruit is eaten.
     */
    private final float BONUS_ENERGY = 10f;

    /**
     * Tag used to identify eaten fruit objects.
     */
    private final String EATEN_FRUIT_TAG = "eaten";

    /**
     * Array of colors representing different stages of the fruit.
     */
    private final Color[] fruitColors;

    /**
     * Array of renderables for the different stages of the fruit.
     */
    private final OvalRenderable[] fruitRanderables;

    /**
     * Callback function to be called when the fruit is eaten.
     */
    private final Consumer<Float> onEaten;

    /**
     * Index to track the current stage of the fruit.
     */
    private int fruitIndex;

    /**
     * Constructs a new Fruit object.
     *
     * @param topLeftCorner The top-left corner of the fruit.
     * @param onEaten       A callback function to be called when the fruit is eaten.
     * @param fruitSize     The size of the fruit.
     * @param fruitColors   An array of colors representing different stages of the fruit.
     */
    public Fruit(Vector2 topLeftCorner, Consumer<Float> onEaten, float fruitSize, Color[] fruitColors) {
        super(topLeftCorner,
                Vector2.of(fruitSize, fruitSize),
                new OvalRenderable(fruitColors[0]));
        this.fruitColors = fruitColors;
        this.fruitRanderables = new OvalRenderable[fruitColors.length];
        for (int i = 0; i < fruitColors.length; i++) {
            this.fruitRanderables[i] = new OvalRenderable(fruitColors[i]);
        }
        this.fruitIndex = 0;
        this.onEaten = onEaten;
        this.setTag(TAG);
    }

    /**
     * Responds to the jump action by changing the fruit's color.
     */
    @Override
    public void onJump() {
        this.fruitIndex++;
        renderer().setRenderable(fruitRanderables[fruitIndex % fruitColors.length]);
    }

    /**
     * Handles the collision event when another game object collides with this fruit.
     *
     * @param other     The other game object involved in the collision.
     * @param collision Details about the collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        if (other.getTag().equals(Avatar.TAG) && this.getTag().equals(TAG)) {
            super.onCollisionEnter(other, collision);
            onEaten.accept(BONUS_ENERGY);
            this.setTag(EATEN_FRUIT_TAG);
            new Transition<Float>(this,
                    this.renderer()::fadeOut,
                    0f,
                    0f,
                    Transition.LINEAR_INTERPOLATOR_FLOAT,
                    PepseGameManager.DAYTIME_CYCLE_DURATION,
                    Transition.TransitionType.TRANSITION_ONCE,
                    this::fadeIn);
        }
    }

    /**
     * Restores the fruit's appearance and tag after being eaten.
     */
    private void fadeIn() {
        renderer().fadeIn(0f);
        setTag(TAG);
    }
}