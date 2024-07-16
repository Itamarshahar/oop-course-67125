package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Sun {
    private static final float Y_POS_INIT = 1.4f; // TODO Why???

    private static final String SUN_TAG = "sun";

    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        Vector2 initialSunCenter = Vector2.of(windowDimensions.x() * 0.5f, windowDimensions.y() - (Terrain.RATIO * windowDimensions.y() * Y_POS_INIT));
        Vector2 cycleCenter = Vector2.of(windowDimensions.x() * 0.5f ,
                Terrain.RATIO * windowDimensions.y());
        OvalRenderable ovalRenderable = new OvalRenderable(Color.YELLOW);
        Vector2 sunSize = new Vector2(100, 100);
        GameObject sun = new GameObject(initialSunCenter, sunSize, ovalRenderable);
        new Transition<Float>(sun,
                (Float angle) -> sun.setCenter(initialSunCenter.subtract(cycleCenter).rotated(angle).add(cycleCenter)),
                0f,
                360f,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength ,
                Transition.TransitionType.TRANSITION_LOOP,
        null);
                sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(SUN_TAG);
        return sun;
    }
}
