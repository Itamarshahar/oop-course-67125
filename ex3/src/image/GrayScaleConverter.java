package image;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class GrayScaleConverter {
    public static double calculateBrightness(BufferedImage image) {
        double totalBrightness = 0.0;
        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(image.getRGB(x, y));
                double grayPixel = color.getRed() * 0.2126 + color.getGreen() * 0.7152 + color.getBlue() * 0.0722;
                totalBrightness += grayPixel / 255;
            }
        }
        return totalBrightness / (width * height);
    }
}
