package image;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class SubImage {
    private BufferedImage image;

    public SubImage(BufferedImage image) {
        this.image = image;
    }

    public List<BufferedImage> divideImage(int subImageSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        List<BufferedImage> subImages = new ArrayList<>();

        for (int y = 0; y < height; y += subImageSize) {
            for (int x = 0; x < width; x += subImageSize) {
                int subWidth = Math.min(subImageSize, width - x);
                int subHeight = Math.min(subImageSize, height - y);

                BufferedImage subImage = image.getSubimage(x, y, subWidth, subHeight);
                subImages.add(subImage);
            }
        }
        return subImages;
    }
}
