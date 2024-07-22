package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.world.daynight.Sun;

import java.awt.*;

/**
 * This class is responsible for creating a halo effect around the sun in the game world.
 * The sun halo is a semi-transparent, larger oval that follows the sun to enhance its visual appearance.
 */
public class SunHalo {
    private static final String SUN_HALO_TAG = "sun_halo";
    private static final Color sunHaloColor = new Color(255, 255, 0, 20);
    private static final float HALO_SIZE_FACTOR = Sun.SUN_SIZE_FACTOR * 2f;

    /**
     * Creates a sun halo object for the game world.
     * The halo is positioned and sized relative to the sun.
     *
     * @param windowDimensions The dimensions of the game window.
     * @param cycleLength      The length of the day-night cycle in seconds.
     * @param sun              The sun GameObject around which the halo will be created.
     * @return A GameObject representing the sun halo.
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength, GameObject sun) {
        float sunHaloSize = windowDimensions.y() * HALO_SIZE_FACTOR;
        Vector2 dimensions = Vector2.of(sunHaloSize, sunHaloSize);
        OvalRenderable renderable = new OvalRenderable(sunHaloColor);

        GameObject halo = new GameObject(
                windowDimensions, // Position of the halo, currently set to window dimensions
                dimensions,
                renderable
        );
        halo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        halo.setTag(SUN_HALO_TAG);
        return halo;
    }
}