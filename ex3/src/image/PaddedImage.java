package image;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class PaddedImage {
    private Image image;

    public PaddedImage(Image image) {
        this.image = image;
    }

    public Image padToPowerOfTwo() {
        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();
        int newWidth = getNextPowerOfTwo(originalWidth);
        int newHeight = getNextPowerOfTwo(originalHeight);

        // Create a new Color[][] array for padded pixels
        Color[][] paddedPixels = new Color[newHeight][newWidth];

        // Copy original pixels into paddedPixels, fill with white where necessary
        for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < newWidth; x++) {
                if (x < originalWidth && y < originalHeight) {
                    paddedPixels[y][x] = image.getPixel(x, y);
                } else {
                    paddedPixels[y][x] = Color.WHITE; // White padding
                }
            }
        }

        // Create and return a new Image object with paddedPixels
        return new Image(paddedPixels, newWidth, newHeight);
    }

    /**
     * Calculates the next power of two greater than or equal to n.
     *
     * @param n the number to calculate the next power of two
     * @return the next power of two
     */
    private static int getNextPowerOfTwo(int n) {
        return (int) Math.pow(2, Math.ceil(Math.log(n) / Math.log(2)));
    }
}
