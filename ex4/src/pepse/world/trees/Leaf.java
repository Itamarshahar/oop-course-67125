package pepse.world.trees;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;

import java.awt.*;
import java.util.Random;

/**
 * Represents a leaf in the game world, which can respond to jumps
 * and exhibit wind-like movement.
 */
public class Leaf extends GameObject implements ResponsiveToJump {

    /**
     * Base color of the leaf.
     */
    public static final Color BASE_LEAF_COLOR = new Color(50, 200, 30);

    /**
     * Tag used to identify leaf objects.
     */
    public static final String TAG = "leaf";

    /**
     * Time taken to rotate the leaf in response to a jump.
     */
    private static final float ROTATE_ANGLE_TIME = 3f;

    /**
     * Angle to rotate the leaf in response to a jump.
     */
    private static final Float ROTATE_JUMP_ANGLE = 90f;

    /**
     * Angle to which the leaf rotates to simulate wind.
     */
    private static final float LEAF_WIND_ANGLE = 45f;

    /**
     * Base size of the leaf.
     */
    public static int BASE_LEAF_SIZE = 20;

    /**
     * Renderable object for the leaf.
     */
    private final RectangleRenderable renderable = new RectangleRenderable(BASE_LEAF_COLOR);

    /**
     * Constructs a new Leaf object.
     *
     * @param topLeftCorner The top-left corner of the leaf.
     * @param dimensions    The dimensions of the leaf.
     * @param renderable    The renderable object representing the leaf's appearance.
     */
    public Leaf(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        Random random = new Random();
        int randomInt = random.nextInt(PepseGameManager.DAYTIME_CYCLE_DURATION);
        ScheduledTask angleTask = new ScheduledTask(this,
                randomInt,
                true,
                this::angleTransition);
        ScheduledTask widthTask = new ScheduledTask(this,
                randomInt,
                true,
                this::widthTransition);
        setTag(TAG);
    }

    /**
     * Creates a transition for the leaf's angle to simulate wind movement.
     *
     * @return A Transition object for the leaf's angle.
     */
    private Transition<Float> angleTransition() {
        float curAngle = this.renderer().getRenderableAngle();
        return new Transition<Float>(this,
                this.renderer()::setRenderableAngle,
                curAngle,
                curAngle + LEAF_WIND_ANGLE,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                PepseGameManager.DAYTIME_CYCLE_DURATION,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
    }

    /**
     * Creates a transition for the leaf's width to simulate natural leaf movement.
     *
     * @return A Transition object for the leaf's dimensions.
     */
    private Transition<Vector2> widthTransition() {
        return new Transition<Vector2>(this,
                this::setDimensions,
                Vector2.ONES.mult(BASE_LEAF_SIZE),
                new Vector2(BASE_LEAF_SIZE * 0.5f, BASE_LEAF_SIZE * 1.5f),
                Transition.LINEAR_INTERPOLATOR_VECTOR,
                PepseGameManager.DAYTIME_CYCLE_DURATION,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
    }

    /**
     * Determines if the leaf should collide with another game object.
     *
     * @param other The other game object.
     * @return False, as leaves do not collide with other objects.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return false;
    }

    /**
     * Creates a transition to rotate the leaf in response to a jump.
     */
    private void rotateJump() {
        new Transition<Float>(this,
                this.renderer()::setRenderableAngle,
                this.renderer().getRenderableAngle(),
                this.renderer().getRenderableAngle() + ROTATE_JUMP_ANGLE,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                ROTATE_ANGLE_TIME,
                Transition.TransitionType.TRANSITION_ONCE,
                null);
    }

    /**
     * Responds to the jump action by rotating the leaf.
     */
    @Override
    public void onJump() {
        new Transition<Float>(this,
                this.renderer()::setRenderableAngle,
                this.renderer().getRenderableAngle(),
                this.renderer().getRenderableAngle() + ROTATE_JUMP_ANGLE,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                ROTATE_ANGLE_TIME,
                Transition.TransitionType.TRANSITION_ONCE,
                null);
    }
}