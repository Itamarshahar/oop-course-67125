////package image;
////
////import image.Image;
////
////import java.awt.*;
////
////public class PaddedImage {
////    private Image image;
////
////    public PaddedImage(Image image) {
////        this.image = image;
////    }
////
////    public Image padToPowerOfTwo() {
////        int originalWidth = image.getWidth();
////        int originalHeight = image.getHeight();
////        int newWidth = getNextPowerOfTwo(originalWidth);
////        int newHeight = getNextPowerOfTwo(originalHeight);
////
////        // Create a new Color[][] array for padded pixels
////        Color[][] paddedPixels = new Color[newHeight][newWidth];
////
////        // Copy original pixels into paddedPixels, fill with white where necessary
////        for (int y = 0; y < newHeight; y++) {
////            for (int x = 0; x < newWidth; x++) {
////                if (x < originalWidth && y < originalHeight) {
////                    paddedPixels[y][x] = image.getPixel(y, x);
////                } else {
////                    paddedPixels[y][x] = Color.WHITE; // White padding
////                }
////            }
////        }
////
////        // Create and return a new Image object with paddedPixels
////        return new Image(paddedPixels, newWidth, newHeight);
////    }
////
////    /**
////     * Calculates the next power of two greater than or equal to n.
////     *
////     * @param n the number to calculate the next power of two
////     * @return the next power of two
////     */
////    private static int getNextPowerOfTwo(int n) {
////        return (int) Math.pow(2, Math.ceil(Math.log(n) / Math.log(2)));
////    }
////}
//
////////////////////////////////////////////////////////////////////////////////
//package image;
//
////import ascii_art.Shell;
////
////import java.awt.*;
//
//import java.awt.*;
//
///**
// * Represents an image that has been padded to have dimensions that are powers of two.
// */
//public class PaddedImage {
//    private final int newWidth;
//    private final int newHeight;
//    private final Image origImage;
//
//
//    /**
//     * Constructs a new `MakePowerOfTow` object.
//     * @param originalImage The original image to pad.
//     * @throws IllegalArgumentException If the original image is null.
//     */
//    public PaddedImage(Image originalImage) throws IllegalArgumentException{
//        if(originalImage == null){
////            throw new IllegalArgumentException(Shell.DID_NOT_EXECUTE_DUE_TO_PROBLEM_WITH_IMAGE_FILE);
//        }
//        this.origImage = originalImage;
//        this.newWidth = findClosestPowerOfTwo(originalImage.getWidth());
//        this.newHeight = findClosestPowerOfTwo(originalImage.getHeight());
//    }
//
//    private int findClosestPowerOfTwo(int num) {
//        if (num > 0 && (num & (num - 1)) == 0) {
//            return num;
//        }
//        return findClosestPowerOfTwo(num+1);
//    }
//
//    /**
//     * Creates a new image with the same content as the original image,
//     * but with dimensions that are powers of two. Pad new pixels white.
//     * @return The new image.
//     */
//    public Image createNewImage() {
//        Color[][] pixelArray = new Color[newHeight][newWidth];
//        int rashiowHaight = newHeight - origImage.getHeight();
//        int rashiowWidth = newWidth - origImage.getWidth();
//        paddHight(pixelArray, rashiowHaight / 2);
//        paddWidth(pixelArray, rashiowHaight/2, rashiowWidth);
//        paddAtOreginFoto(pixelArray, rashiowHaight/2, rashiowWidth/2);
//        return new Image(pixelArray, newWidth, newHeight);
//    }
//
//    private void paddHight(Color[][] pixelArray, int rashioHight) {
//        for (int row = 0; row < (rashioHight); row++) {
//            for (int col = 0; col < newWidth; col++) {
//                pixelArray[row][col] = Color.white;
//                pixelArray[newHeight - (row + 1)][col] = Color.white;
//            }
//        }
//    }
//
//    private void paddWidth(Color[][] pixelArray, int rashiowHaight, int rashiowWidth) {
//        for (int row = rashiowHaight; row < (newHeight - rashiowHaight); row++) {
//            for (int col = 0; col < (rashiowWidth / 2); col++) {
//                pixelArray[row][col] = Color.white;
//                pixelArray[row][newWidth - (col + 1)] = Color.white;
//            }
//        }
//    }
//
//    private void paddAtOreginFoto(Color[][] pixelArray, int heightRatio, int widthRatio) {
//        for (int row = 0; row < origImage.getHeight(); row++) {
//            for (int col = 0; col < origImage.getWidth(); col++) {
//                pixelArray[row + heightRatio][col + widthRatio] = origImage.getPixel(row, col);
//            }
//        }
//    }
//
//}
//
//
//
//
//
//
package image;

import java.awt.*;

public class PaddedImage {
    private final Image image;

    public PaddedImage(Image image) {
        this.image = image;
    }

    /**
     * Finds the closest power of two greater than or equal to num.
     *
     * @param num the number to find the closest power of two
     * @return the closest power of two
     */
    private static int findClosestPowerOfTwo(int num) {
        return (int) Math.pow(2, Math.ceil(Math.log(num) / Math.log(2)));

    }

    public Image padToPowerOfTwo() {
        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();
        int newWidth = findClosestPowerOfTwo(originalWidth);
        int newHeight = findClosestPowerOfTwo(originalHeight);

        // Calculate padding amounts
        int padLeft = (newWidth - originalWidth) / 2;
        int padRight = newWidth - originalWidth - padLeft; // TODO remove?
        int padTop = (newHeight - originalHeight) / 2;
        int padBottom = newHeight - originalHeight - padTop; // TODO remove?

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
