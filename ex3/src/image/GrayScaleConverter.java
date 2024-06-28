package image;

import java.awt.Color;

/**
 * This class provides methods to convert an image to grayscale and calculate its brightness.
 */
public class GrayScaleConverter {

    private static final double RED_FACTOR = 0.2126;
    private static final double GREEN_FACTOR = 0.7152;
    private static final double BLUE_FACTOR = 0.0722;

    /**
     * Calculates the average brightness of an image.
     * The brightness is calculated based on the luminance of each pixel.
     *
     * @param image The image for which to calculate the brightness.
     * @return The average brightness of the image.
     */
    public static double calculateGrayScaleBrightness(Image image) {
        double totalBrightness = 0.0;
        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = image.getPixel(y, x);
                double grayPixel = (color.getRed() * RED_FACTOR) + (color.getGreen() * GREEN_FACTOR) + (color.getBlue() * BLUE_FACTOR);
                totalBrightness += grayPixel / 255.0;
            }
        }
        return totalBrightness / (width * height);
    }
}