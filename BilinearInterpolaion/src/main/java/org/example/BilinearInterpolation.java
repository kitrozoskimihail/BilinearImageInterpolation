package org.example;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class BilinearInterpolation {

    public static void main(String[] args) throws IOException {

        String path = "src/cat.jpg";
        BufferedImage image = ImageIO.read(new File(path));

        int width = image.getWidth();
        int height = image.getHeight();
        int scaleFactor = 3;

        int newWidth = width * scaleFactor;
        int newHeight = height * scaleFactor;

        BufferedImage newImage = new BufferedImage(newWidth, newHeight, image.getType());

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int rgb = image.getRGB(j, i);
                newImage.setRGB(j * scaleFactor, i * scaleFactor, rgb);
            }
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width - 1; j++) {
                int leftRgb = newImage.getRGB(j * scaleFactor, i * scaleFactor);
                int rightRgb = newImage.getRGB((j + 1) * scaleFactor, i * scaleFactor);
                for (int k = 1; k < scaleFactor; k++) {
                    int interpolatedRgb = interpolateColor(leftRgb, rightRgb, k, scaleFactor);
                    newImage.setRGB(j * scaleFactor + k, i * scaleFactor, interpolatedRgb);
                }
            }
        }

        for (int j = 0; j < newWidth; j++) {
            for (int i = 0; i < height - 1; i++) {
                int topRgb = newImage.getRGB(j, i * scaleFactor);
                int bottomRgb = newImage.getRGB(j, (i + 1) * scaleFactor);
                for (int k = 1; k < scaleFactor; k++) {
                    int interpolatedRgb = interpolateColor(topRgb, bottomRgb, k, scaleFactor);
                    newImage.setRGB(j, i * scaleFactor + k, interpolatedRgb);
                }
            }
        }

        String outputImagePath = "src/new.jpg";
        ImageIO.write(newImage, "jpg", new File(outputImagePath));
    }

    private static int interpolateColor(int color1, int color2, int k, int scaleFactor) {
        int red1 = (color1 >> 16) & 0xFF;
        int green1 = (color1 >> 8) & 0xFF;
        int blue1 = color1 & 0xFF;

        int red2 = (color2 >> 16) & 0xFF;
        int green2 = (color2 >> 8) & 0xFF;
        int blue2 = color2 & 0xFF;

        int red = (int) Math.ceil(((1 - (float) k / scaleFactor) * red1) + ((float) k / scaleFactor * red2));
        int green = (int) Math.ceil(((1 - (float) k / scaleFactor) * green1) + ((float) k / scaleFactor * green2));
        int blue = (int) Math.ceil(((1 - (float) k / scaleFactor) * blue1) + ((float) k / scaleFactor * blue2));

        return (red << 16) | (green << 8) | blue;
    }
}
