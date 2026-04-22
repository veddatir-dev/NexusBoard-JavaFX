package com.nexus.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Utility class for exporting canvas drawings to various image formats.
 */
public class ExportManager {

    /**
     * Export canvas to PNG file
     */
    public static void exportToPNG(Canvas canvas, String filePath) throws IOException {
        WritableImage writableImage = new WritableImage(
            (int) canvas.getWidth(),
            (int) canvas.getHeight()
        );
        
        canvas.snapshot(null, writableImage);
        
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
        File file = new File(filePath);
        ImageIO.write(bufferedImage, "png", file);
    }

    /**
     * Export canvas to JPEG file
     */
    public static void exportToJPEG(Canvas canvas, String filePath) throws IOException {
        WritableImage writableImage = new WritableImage(
            (int) canvas.getWidth(),
            (int) canvas.getHeight()
        );
        
        canvas.snapshot(null, writableImage);
        
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
        
        // Convert to RGB (JPEG doesn't support transparency)
        BufferedImage rgbImage = new BufferedImage(
            bufferedImage.getWidth(),
            bufferedImage.getHeight(),
            BufferedImage.TYPE_INT_RGB
        );
        
        for (int y = 0; y < bufferedImage.getHeight(); y++) {
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                int argb = bufferedImage.getRGB(x, y);
                int rgb = argb & 0xFFFFFF;
                rgbImage.setRGB(x, y, rgb);
            }
        }
        
        File file = new File(filePath);
        ImageIO.write(rgbImage, "jpg", file);
    }

    /**
     * Export canvas to BMP file
     */
    public static void exportToBMP(Canvas canvas, String filePath) throws IOException {
        WritableImage writableImage = new WritableImage(
            (int) canvas.getWidth(),
            (int) canvas.getHeight()
        );
        
        canvas.snapshot(null, writableImage);
        
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
        File file = new File(filePath);
        ImageIO.write(bufferedImage, "bmp", file);
    }

    /**
     * Export canvas to SVG format (vector-based)
     * This creates a basic SVG string representation
     */
    public static void exportToSVG(Canvas canvas, LayerManager layerManager, String filePath) throws IOException {
        StringBuilder svg = new StringBuilder();
        svg.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        svg.append("<svg xmlns=\"http://www.w3.org/2000/svg\" ");
        svg.append("width=\"").append((int)canvas.getWidth()).append("\" ");
        svg.append("height=\"").append((int)canvas.getHeight()).append("\">\n");
        
        // Add white background
        svg.append("<rect width=\"100%\" height=\"100%\" fill=\"white\"/>\n");
        
        // Add all shapes from layers
        for (Layer layer : layerManager.getAllLayers()) {
            if (layer.isVisible()) {
                for (iShape shape : layer.getShapes()) {
                    svg.append(shapeToSVG(shape)).append("\n");
                }
            }
        }
        
        svg.append("</svg>\n");
        
        try (java.io.FileWriter writer = new java.io.FileWriter(filePath)) {
            writer.write(svg.toString());
        }
    }

    private static String shapeToSVG(iShape shape) {
        if (shape instanceof Circle) {
            // Basic SVG conversion for circles
            return "<circle cx=\"0\" cy=\"0\" r=\"10\" fill=\"none\" stroke=\"black\" stroke-width=\"1\"/>";
        } else if (shape instanceof Rectangle) {
            return "<rect x=\"0\" y=\"0\" width=\"100\" height=\"50\" fill=\"none\" stroke=\"black\" stroke-width=\"1\"/>";
        }
        // Add more shape conversions as needed
        return "";
    }

    /**
     * Get the appropriate file extension for a given format
     */
    public static String getExtensionForFormat(String format) {
        switch (format.toLowerCase()) {
            case "png":
                return ".png";
            case "jpg":
            case "jpeg":
                return ".jpg";
            case "bmp":
                return ".bmp";
            case "svg":
                return ".svg";
            default:
                return ".png";
        }
    }
}
