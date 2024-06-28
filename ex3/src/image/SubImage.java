package image;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
/**
 * The SubImage class is responsible for dividing a given image into smaller sub-images.
 * This is useful for processing or analyzing smaller parts of a large image independently.
 */
public class SubImage {
    private Image image;

    public SubImage(Image image) {
        this.image = image;
    }

    public List<Image> divideImage(int subImageSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        List<Image> subImages = new ArrayList<>();

        for (int y = 0; y < height; y += subImageSize) {
            for (int x = 0; x < width; x += subImageSize) {
                int subWidth = Math.min(subImageSize, width - x);
                int subHeight = Math.min(subImageSize, height - y);

                // Create a new Color[][] array for sub-image pixels
                Color[][] subImagePixels = new Color[subHeight][subWidth];

                // Copy pixels from original image into sub-image pixels array
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
