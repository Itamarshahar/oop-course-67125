package ascii_art;

import image.Image;
import image.PaddedImage;
import image.SubImage;
import image.GrayScaleConverter;
import image_char_matching.SubImgCharMatcher;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

public class AsciiArtAlgorithm {
    private final Image image;
    private final char[] charset;
    private final int resolution;

    public AsciiArtAlgorithm(Image image, char[] charset, int resolution) throws IOException {
        this.image = image;
        this.charset = charset;
        this.resolution = resolution;
    }

    public char[][] run() {
        Image paddedImage = new PaddedImage(image).padToPowerOfTwo();
        int subImageSize = paddedImage.getWidth() / resolution;

        SubImgCharMatcher matcher = new SubImgCharMatcher(charset);
        char[][] asciiArt = new char[resolution][resolution];

        SubImage subImageProcessor = new SubImage(paddedImage);
        List<Image> subImages = subImageProcessor.divideImage(subImageSize);

        for (int row = 0; row < resolution; row++) {
            for (int col = 0; col < resolution; col++) {
                Image subImage = subImages.get(row * resolution + col); // Get sub-image from the list
                int index = col + row * asciiArt[0].length;
                double brightness =
                        GrayScaleConverter.calculateBrightness(subImages.get(index));
                asciiArt[row][col] = matcher.getCharByImageBrightness(brightness);
            }
        }

        return asciiArt;
    }

}
