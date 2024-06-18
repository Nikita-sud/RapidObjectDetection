package com.nichitabulgaru.dataManagement;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;

public class ViolaJonesReader {

    public static void main(String[] args) throws IOException {
        String positiveImagesDir = "/path/to/positive/images";
        String negativeImagesDir = "/path/to/negative/images";
        List<ImageData> dataset = readViolaJonesData(positiveImagesDir, negativeImagesDir);
        Collections.shuffle(dataset);
        System.out.println(dataset.get(0).imageData);
        System.out.println(dataset.get(0).label);

    }

    public static List<ImageData> readViolaJonesData(String positiveImagesDir, String negativeImagesDir) throws IOException {
        List<ImageData> dataset = new ArrayList<>();
        File posDir = new File(positiveImagesDir);
        File negDir = new File(negativeImagesDir);

        // Чтение позитивных изображений (лиц)
        for (File file : posDir.listFiles()) {
            BufferedImage img = ImageIO.read(file);
            if (img != null) {
                double[][] imageData = convertImageToArray(img);
                double[][] label = {{1}};  // Метка для лица
                dataset.add(new ImageData(imageData, label));
            }
        }

        // Чтение негативных изображений (не лица)
        for (File file : negDir.listFiles()) {
            BufferedImage img = ImageIO.read(file);
            if (img != null) {
                double[][] imageData = convertImageToArray(img);
                double[][] label = {{0}};  // Метка для не лица
                dataset.add(new ImageData(imageData, label));
            }
        }

        return dataset;
    }

    public static double[][] convertImageToArray(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        double[][] array = new double[width * height][1];
        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = img.getRGB(x, y);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;
                double gray = (red + green + blue) / 3.0 / 255.0;
                array[index][0] = gray;
                index++;
            }
        }
        return array;
    }
}

class ImageData {
    public double[][] imageData;
    public double[][] label;

    public ImageData(double[][] imageData, double[][] label) {
        this.imageData = imageData;
        this.label = label;
    }
}
