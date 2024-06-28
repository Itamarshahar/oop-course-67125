package image;

import java.awt.Color;

/**
 * Represents an image that can be padded to have dimensions that are powers of two.
 */
public class PaddedImage {

    private final Image image;

    /**
     * Constructs a new PaddedImage object.
     *
     * @param image The original image to pad.
     */
    public PaddedImage(Image image) {
        this.image = image;
    }

    /**
     * Finds the closest power of two greater than or equal to the specified number.
     *
     * @param num The number to find the closest power of two for.
     * @return The closest power of two greater than or equal to num.
     */
    private static int findClosestPowerOfTwo(int num) {
        return (int) Math.pow(2, Math.ceil(Math.log(num) / Math.log(2)));
    }

    /**
     * Pads the image to have dimensions that are powers of two.
     * The padding is done with white color.
     *
     * @return A new Image object with dimensions padded to the next power of two.
     */
    public Image padToPowerOfTwo() {
        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();
        int newWidth = findClosestPowerOfTwo(originalWidth);
        int newHeight = findClosestPowerOfTwo(originalHeight);

        // Calculate padding amounts
        int padLeft = (newWidth - originalWidth) / 2;
        int padTop = (newHeight - originalHeight) / 2;

        // Create a new Color[][] array for padded pixels
        Color[][] paddedPixels = new Color[newHeight][newWidth];

        // Copy original pixels into paddedPixels, fill with white where necessary
        for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < newWidth; x++) {
                if (y >= padTop && y < padTop + originalHeight && x >= padLeft && x < padLeft + originalWidth) {
                    paddedPixels[y][x] = image.getPixel(y - padTop, x - padLeft);
                } else {
                    paddedPixels[y][x] = Color.WHITE; // White padding
                }
            }
        }

        // Create and return a new Image object with paddedPixels
        return new Image(paddedPixels, newWidth, newHeight);
    }
}