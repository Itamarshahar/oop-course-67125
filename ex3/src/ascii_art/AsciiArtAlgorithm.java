package ascii_art;

import image.Image;
import image.PaddedImage;
import image.SubImage;
import image_char_matching.SubImgCharMatcher;

import java.io.IOException;
import java.util.List;

/**
 * The AsciiArtAlgorithm class converts an image to ASCII art using a specified character set and resolution.
 * It divides the image into smaller sub-images, calculates their brightness, and matches each sub-image to a character
 * based on its brightness to create the final ASCII art representation.
 */
public class AsciiArtAlgorithm {
    private final Image image;
    private final char[] charset;
    private final int resolution;

    /**
     * Constructs an AsciiArtAlgorithm with the given image, character set, and resolution.
     *
     * @param image      the image to convert to ASCII art
     * @param charset    the set of characters to use for the ASCII art
     * @param resolution the resolution of the resulting ASCII art
     * @throws IOException if an error occurs during image processing
     */
    public AsciiArtAlgorithm(Image image, char[] charset, int resolution) throws IOException {
        this.image = image;
        this.charset = charset;
        this.resolution = resolution;
    }

    /**
     * Runs the ASCII art conversion algorithm.
     *
     * @return a 2D character array representing the ASCII art
     */
    public char[][] run() {
        Image paddedImage = new PaddedImage(image).padToPowerOfTwo();
        int subImageSize = paddedImage.getWidth() / resolution;

        SubImgCharMatcher matcher = new SubImgCharMatcher(charset);
        char[][] asciiArt = new char[resolution][resolution];

        SubImage subImage = new SubImage(paddedImage);
        List<Image> subImages = subImage.divideImage(subImageSize);

        for (int col = 0; col < resolution; col++) {
            for (int row = 0; row < resolution; row++) {
                int index = row + col * asciiArt[0].length;
                double brightness = image.calculateImageBrightness(subImages.get(index));
                asciiArt[row][col] = matcher.getCharByImageBrightness(brightness);
            }
        }
        return asciiArt;
    }

}
