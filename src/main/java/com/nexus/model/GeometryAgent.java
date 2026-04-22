package com.nexus.model;

import javafx.geometry.Point2D;
import java.util.List;

public class GeometryAgent {

    public static RecognitionResult recognize(List<Point2D> points) {
        if (points == null || points.size() < 5) return new RecognitionResult("Freehand", 0, 0, 0, 0);

        // Preprocessing: Remove duplicate points and smooth
        List<Point2D> cleanedPoints = cleanPoints(points);
        if (cleanedPoints.size() < 5) return new RecognitionResult("Freehand", 0, 0, 0, 0);

        // Basic bounds
        double minX = cleanedPoints.stream().mapToDouble(p -> p.getX()).min().orElse(0);
        double maxX = cleanedPoints.stream().mapToDouble(p -> p.getX()).max().orElse(0);
        double minY = cleanedPoints.stream().mapToDouble(p -> p.getY()).min().orElse(0);
        double maxY = cleanedPoints.stream().mapToDouble(p -> p.getY()).max().orElse(0);

        double width = maxX - minX;
        double height = maxY - minY;
        if (width < 10 || height < 10) return new RecognitionResult("Freehand", minX, minY, maxX, maxY);

        Point2D start = cleanedPoints.get(0);
        Point2D end = cleanedPoints.get(cleanedPoints.size() - 1);
        double closureDist = start.distance(end);

        // 1. Line Detection: Check straightness
        double lineScore = calculateLineScore(cleanedPoints, start, end);
        if (lineScore < 0.05 && closureDist > Math.max(width, height) * 0.3) {
            return new RecognitionResult("Line", minX, minY, maxX, maxY);
        }

        // Must be closed for shapes
        if (closureDist > Math.max(width, height) * 0.2) return new RecognitionResult("Freehand", minX, minY, maxX, maxY);

        // 2. Circle/Ellipse Detection
        CircleFit circleFit = fitCircle(cleanedPoints);
        if (circleFit.error < 0.15) {
            double aspectRatio = width / height;
            if (Math.abs(aspectRatio - 1.0) < 0.2) {
                return new RecognitionResult("Circle", minX, minY, maxX, maxY);
            } else {
                return new RecognitionResult("Ellipse", minX, minY, maxX, maxY);
            }
        }

        // 3. Rectangle/Square Detection
        double rectScore = calculateRectangleScore(cleanedPoints, minX, maxX, minY, maxY);
        if (rectScore > 0.8) {
            double aspectRatio = width / height;
            if (Math.abs(aspectRatio - 1.0) < 0.15) {
                return new RecognitionResult("Square", minX, minY, maxX, maxY);
            } else {
                return new RecognitionResult("Rectangle", minX, minY, maxX, maxY);
            }
        }

        // 4. Triangle Detection
        double triangleScore = calculateTriangleScore(cleanedPoints);
        if (triangleScore > 0.7) {
            return new RecognitionResult("Triangle", minX, minY, maxX, maxY);
        }

        return new RecognitionResult("Freehand", minX, minY, maxX, maxY);
    }

    private static List<Point2D> cleanPoints(List<Point2D> points) {
        List<Point2D> cleaned = new java.util.ArrayList<>();
        for (Point2D p : points) {
            if (cleaned.isEmpty() || cleaned.get(cleaned.size() - 1).distance(p) > 2) {
                cleaned.add(p);
            }
        }
        return cleaned;
    }

    private static double calculateLineScore(List<Point2D> points, Point2D start, Point2D end) {
        if (points.size() < 3) return 0;
        double lineLength = start.distance(end);
        if (lineLength == 0) return 1;
        double maxDev = 0;
        for (Point2D p : points) {
            double dev = distanceToLine(p, start, end) / lineLength;
            maxDev = Math.max(maxDev, dev);
        }
        return maxDev;
    }

    private static double distanceToLine(Point2D p, Point2D a, Point2D b) {
        double dx = b.getX() - a.getX();
        double dy = b.getY() - a.getY();
        double length = Math.sqrt(dx * dx + dy * dy);
        if (length == 0) return p.distance(a);
        double t = ((p.getX() - a.getX()) * dx + (p.getY() - a.getY()) * dy) / (length * length);
        t = Math.max(0, Math.min(1, t));
        double projX = a.getX() + t * dx;
        double projY = a.getY() + t * dy;
        return p.distance(new Point2D(projX, projY));
    }

    private static CircleFit fitCircle(List<Point2D> points) {
        // Simple circle fit using geometric median approximation
        double cx = 0, cy = 0;
        for (Point2D p : points) {
            cx += p.getX();
            cy += p.getY();
        }
        cx /= points.size();
        cy /= points.size();

        double radius = 0;
        for (Point2D p : points) {
            radius += Math.sqrt((p.getX() - cx) * (p.getX() - cx) + (p.getY() - cy) * (p.getY() - cy));
        }
        radius /= points.size();

        double error = 0;
        for (Point2D p : points) {
            double dist = Math.sqrt((p.getX() - cx) * (p.getX() - cx) + (p.getY() - cy) * (p.getY() - cy));
            error += Math.abs(dist - radius);
        }
        error /= (points.size() * radius);

        return new CircleFit(cx, cy, radius, error);
    }

    private static double calculateRectangleScore(List<Point2D> points, double minX, double maxX, double minY, double maxY) {
        int cornerPoints = 0;
        double margin = 0.15;
        for (Point2D p : points) {
            boolean inCorner = (p.getX() < minX + (maxX - minX) * margin && p.getY() < minY + (maxY - minY) * margin) ||
                               (p.getX() > maxX - (maxX - minX) * margin && p.getY() < minY + (maxY - minY) * margin) ||
                               (p.getX() < minX + (maxX - minX) * margin && p.getY() > maxY - (maxY - minY) * margin) ||
                               (p.getX() > maxX - (maxX - minX) * margin && p.getY() > maxY - (maxY - minY) * margin);
            if (inCorner) cornerPoints++;
        }
        return (double) cornerPoints / 4.0; // Expect 4 corners
    }

    private static double calculateTriangleScore(List<Point2D> points) {
        // Simple triangle detection: check for 3 main directions
        List<Double> angles = new java.util.ArrayList<>();
        for (int i = 1; i < points.size() - 1; i++) {
            Point2D prev = points.get(i - 1);
            Point2D curr = points.get(i);
            Point2D next = points.get(i + 1);
            double angle1 = Math.atan2(curr.getY() - prev.getY(), curr.getX() - prev.getX());
            double angle2 = Math.atan2(next.getY() - curr.getY(), next.getX() - curr.getX());
            double diff = Math.abs(angle2 - angle1);
            if (diff > Math.PI) diff = 2 * Math.PI - diff;
            angles.add(diff);
        }
        // Count significant direction changes (> 30 degrees)
        int changes = 0;
        for (double a : angles) {
            if (a > Math.PI / 6) changes++;
        }
        return changes > 2 ? 0.8 : 0.0;
    }

    private static class CircleFit {
        @SuppressWarnings("unused")
        double cx, cy, radius, error;
        CircleFit(double cx, double cy, double radius, double error) {
            this.cx = cx;
            this.cy = cy;
            this.radius = radius;
            this.error = error;
        }
    }
}