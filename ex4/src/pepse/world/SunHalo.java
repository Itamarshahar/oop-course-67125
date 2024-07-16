package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class SunHalo {
    private static final String SUN_HALO_TAG = "sun_halo";
    private static final Color sunHaloColor = new Color(255, 255, 0, 20);

  private static final float HALO_SIZE_FACTOR = Sun.SUN_SIZE_FACTOR * 2f;
    public static GameObject create(Vector2 windowDimensions,
                                    float cycleLength,
                                    GameObject sun)
    {
        float sunHaloSize = windowDimensions.y() * HALO_SIZE_FACTOR;
        Vector2 dimensions = Vector2.of(sunHaloSize, sunHaloSize);
        OvalRenderable randerable = new OvalRenderable(sunHaloColor);

        GameObject halo = new GameObject(
//                windowDimensions.mult(0.5f), // TODO
                windowDimensions,
                dimensions,
                randerable
        );
        halo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        halo.setTag(SUN_HALO_TAG);
        return halo;
    }
}
