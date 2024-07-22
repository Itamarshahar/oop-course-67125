package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.world.Terrain;

import java.awt.*;

/**
 * This class is responsible for creating the sun in the game world.
 * The sun moves in a circular path to simulate the day-night cycle.
 */
public class Sun {


    /**
     * Size factor of the sun relative to the window height.
     */
    public static final float SUN_SIZE_FACTOR = 0.08f;
    private static final float Y_POS_INIT = 1.3f;
    private static final String SUN_TAG = "sun";

    /**
     * Creates a sun object for the game world.
     *
     * @param windowDimensions The dimensions of the game window.
     * @param cycleLength      The length of the day-night cycle in seconds.
     * @return A GameObject representing the sun.
     */
    public static GameObject create(Vector2 windowDimensions,
                                    float cycleLength) {
        Vector2 sunSize = new Vector2(windowDimensions.y() *
                SUN_SIZE_FACTOR,
                windowDimensions.y() * SUN_SIZE_FACTOR);
        Vector2 initialSunCenter = Vector2.of(windowDimensions.x() * 0.5f,
                windowDimensions.y() - (Terrain.RATIO *
                        windowDimensions.y() * Y_POS_INIT));
        Vector2 cycleCenter = Vector2.of(windowDimensions.x() * 0.5f,
                Terrain.RATIO * windowDimensions.y());
        OvalRenderable ovalRenderable = new OvalRenderable(Color.YELLOW);
        GameObject sun = new GameObject(initialSunCenter,
                sunSize, ovalRenderable);

        new Transition<Float>(sun,
                (Float angle) -> sun.setCenter(initialSunCenter.subtract(
    cycleCenter).rotated(angle).add(cycleCenter)),
                0f,
                360f,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength,
                Transition.TransitionType.TRANSITION_LOOP,
                null);

        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(SUN_TAG);
        return sun;
    }
}