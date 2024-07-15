package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Night {
    private static final String NIGHT_TAG = "Night";
    private static final Float MIDNIGHT_OPACITY = 0.5f;

    public static GameObject create(Vector2 windowDimensions,
                                    float cycleLength){
        Vector2 topLeftCorner = Vector2.ZERO;
        RectangleRenderable renderable = new RectangleRenderable(new Color(1));
        GameObject night = new GameObject(topLeftCorner, windowDimensions, renderable);
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag(NIGHT_TAG);

        new Transition<Float>(
                night,
                night.renderer()::setOpaqueness,
                0f,
                MIDNIGHT_OPACITY,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                cycleLength / 2,
    Transition.TransitionType.TRANSITION_BACK_AND_FORTH, //TODO maybe TRANSITION_LOOP?
        null);

        return night;
    }
}
