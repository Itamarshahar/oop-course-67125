package image;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
/**
 * The SubImage class is responsible for dividing a given image into smaller sub-images.
 * This is useful for processing or analyzing smaller parts of a large image independently.
 */
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides functionality to divide an image into smaller sub-images.
 */
public class SubImage {
    private Image image;

    /**
     * Constructs a SubImage object with the specified image.
     *
     * @param image The image to be divided into sub-images.
     */
    public SubImage(Image image) {
        this.image = image;
    }

    /**
     * Divides the image into smaller sub-images of the specified size.
     *
     * @param resolution The size of each sub-image.
     * @return A list of sub-images.
     */
    public List<Image> divideImage(int resolution) {
        int width = image.getWidth();
        int height = image.getHeight();
        List<Image> subImages = new ArrayList<>();

        for (int y = 0; y < height; y += resolution) {
            for (int x = 0; x < width; x += resolution) {
                int subWidth = Math.min(resolution, width - x);
                int subHeight = Math.min(resolution, height - y);

                // Create a new Color[][] array for sub-image pixels
                Color[][] subImagePixels = new Color[subHeight][subWidth];

                // Copy pixels from the original image into the sub-image pixels array
                for (int subY = 0; subY < subHeight; subY++) {
                    for (int subX = 0; subX < subWidth; subX++) {
                        subImagePixels[subY][subX] = image.getPixel(x + subX, y + subY);
                    }
                }

                // Create a new Image object for the sub-image and add it to the list
                Image subImage = new Image(subImagePixels, subWidth, subHeight);
                subImages.add(subImage);
            }
        }

        return subImages;
    }
}