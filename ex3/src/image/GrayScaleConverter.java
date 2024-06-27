package image;

import java.awt.*;

public class GrayScaleConverter {
    private static final Double redFactor =  0.2126;
    private static final Double greenFactor =  0.7152;
    private static final Double blueFactor =   0.0722;
    public static double calculateBrightness(Image image) {
        double totalBrightness = 0.0;
        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = image.getPixel(y, x);
                double grayPixel = (color.getRed() * redFactor) + (color.getGreen() *greenFactor) + (color.getBlue() * blueFactor);
                totalBrightness += grayPixel / 255.0;
            }
        }
        return totalBrightness / (width * height);
    }
}
