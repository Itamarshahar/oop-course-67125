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
 * This class is responsible for the leaf.

 */
public class Leaf extends GameObject implements ResponsiveToJump{
    /**
     * The base leaf color.
     */
    public static final Color BASE_LEAF_COLOR = new Color(50, 200, 30);
    public static final String TAG = "leaf";
    private static final float ROTATE_ANGLE_TIME = 3f;
    private static final Float ROTATE_JUMP_ANGLE = 90f;
    private static final float LEAF_WIND_ANGLE = 45f;
    private RectangleRenderable renderable = new RectangleRenderable(BASE_LEAF_COLOR);
    /**
     * The base leaf size.
     */
    public static int BASE_LEAF_SIZE = 20;

    /**
     * This method creates the leaf.
     * @param topLeftCorner the top left corner of the leaf.
     * @param dimensions the dimensions of the leaf.
     * @param renderable the renderable of the leaf.
     * @param didJustJump the action to be performed when the avatar jumps.
     */
    public Leaf(Vector2 topLeftCorner,
                Vector2 dimensions,
                Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        Random random = new Random();
        int randomInt = random.nextInt(PepseGameManager.DAYTIME_CYCLE_DURATION);
        ScheduledTask angleTask = new ScheduledTask(this,
                randomInt, true, this::angleTransition);
        ScheduledTask widthTask = new ScheduledTask(this,
                randomInt, true, this::widthTransition);
        setTag(TAG);
    }

    private Transition<Float> angleTransition() {
        float curAngle = this.renderer().getRenderableAngle();
        return new Transition<Float>(this, this.renderer()::setRenderableAngle,
                curAngle, curAngle + LEAF_WIND_ANGLE, Transition.LINEAR_INTERPOLATOR_FLOAT,
                PepseGameManager.DAYTIME_CYCLE_DURATION,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
    }

    private Transition<Vector2> widthTransition() {
        return new Transition<Vector2>(this, this::setDimensions,
                Vector2.ONES.mult(BASE_LEAF_SIZE),
                new Vector2(BASE_LEAF_SIZE * 0.5f, BASE_LEAF_SIZE * 1.5f),
                Transition.LINEAR_INTERPOLATOR_VECTOR, PepseGameManager.DAYTIME_CYCLE_DURATION,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
    }

    @Override
    /**
     * This method is called when a collision occurs.
     */
    public boolean shouldCollideWith(GameObject other) {
        return false;
    }

    private void rotateJump(){
        Transition<Float> transition = new Transition<Float>(this,
                this.renderer()::setRenderableAngle,
                this.renderer().getRenderableAngle(),
                this.renderer().getRenderableAngle() + ROTATE_JUMP_ANGLE,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                ROTATE_ANGLE_TIME,
                Transition.TransitionType.TRANSITION_ONCE,
                null);
    }


    @Override
    public void onJump() {
        Transition<Float> transition = new Transition<Float>(this,
                this.renderer()::setRenderableAngle,
                this.renderer().getRenderableAngle(),
                this.renderer().getRenderableAngle() + ROTATE_JUMP_ANGLE,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                ROTATE_ANGLE_TIME,
                Transition.TransitionType.TRANSITION_ONCE,
                null);
    }
}
