package image;

import java.awt.image.BufferedImage;

public class PaddedImage {
    private BufferedImage image;
    private final int white = 0xFFFFFF;
    public PaddedImage(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage padToPowerOfTwo() {
        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();
        int newWidth = getNextPowerOfTwo(originalWidth);
        int newHeight = getNextPowerOfTwo(originalHeight);

        BufferedImage paddedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < newWidth; x++) {
                if (x < originalWidth && y < originalHeight) {
                    paddedImage.setRGB(x, y, image.getRGB(x, y));
                } else {
                    paddedImage.setRGB(x, y, white); // white padding
                }
            }
        }
        return paddedImage;
    }

    private int getNextPowerOfTwo(int n) {
        return (int) Math.pow(2, Math.ceil(Math.log(n) / Math.log(2)));
    }
}
