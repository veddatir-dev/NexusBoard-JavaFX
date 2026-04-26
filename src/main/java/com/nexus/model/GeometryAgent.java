package com.nexus.model;

import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.List;

public class GeometryAgent {

    public static RecognitionResult recognize(List<Point2D> points) {
        if (points == null || points.size() < 5) return new RecognitionResult("Freehand", 0, 0, 0, 0);

        // Preprocessing: Clean noise
        List<Point2D> cleanedPoints = cleanPoints(points);
        if (cleanedPoints.size() < 5) return new RecognitionResult("Freehand", 0, 0, 0, 0);

        // Original bounds for return
        double minX = cleanedPoints.stream().mapToDouble(Point2D::getX).min().orElse(0);
        double maxX = cleanedPoints.stream().mapToDouble(Point2D::getX).max().orElse(0);
        double minY = cleanedPoints.stream().mapToDouble(Point2D::getY).min().orElse(0);
        double maxY = cleanedPoints.stream().mapToDouble(Point2D::getY).max().orElse(0);
        
        double width = maxX - minX;
        double height = maxY - minY;
        if (width < 10 && height < 10) return new RecognitionResult("Freehand", minX, minY, maxX, maxY);

        // 1. Normalization to 100x100 space
        List<Point2D> normPoints = normalizePoints(cleanedPoints, minX, maxX, minY, maxY);

        Point2D start = normPoints.get(0);
        Point2D end = normPoints.get(normPoints.size() - 1);
        double closureDist = start.distance(end);

        // Check straightness for Line
        double lineScore = calculateLineScore(normPoints, start, end);
        if (lineScore < 0.25 && closureDist > 20) { // More lenient straightness
            return new RecognitionResult("Line", minX, minY, maxX, maxY);
        }

        boolean isClosed = closureDist < 40; // More lenient closure

        // 2. Centroid Analysis
        double cx = 0, cy = 0;
        for (Point2D p : normPoints) {
            cx += p.getX();
            cy += p.getY();
        }
        cx /= normPoints.size();
        cy /= normPoints.size();

        double sumDist = 0;
        List<Double> distances = new ArrayList<>();
        for (Point2D p : normPoints) {
            double dist = p.distance(cx, cy);
            distances.add(dist);
            sumDist += dist;
        }
        double meanDist = sumDist / normPoints.size();
        double variance = 0;
        for (double d : distances) {
            variance += (d - meanDist) * (d - meanDist);
        }
        double stdDev = Math.sqrt(variance / normPoints.size());

        // Low stdDev indicates a Circle or Ellipse (tightened threshold)
        if (stdDev < 12.0 && isClosed) {
            double aspectRatio = width / height;
            if (Math.abs(aspectRatio - 1.0) < 0.4) { // More lenient aspect ratio
                return new RecognitionResult("Circle", minX, minY, maxX, maxY);
            } else {
                return new RecognitionResult("Ellipse", minX, minY, maxX, maxY);
            }
        }

        // 3. Corner Detection (deltaTheta > pi/4)
        int vertices = countVertices(normPoints);

        if (vertices == 3) {
            // Triangle subclass identification
            return mapTriangle(cleanedPoints, minX, minY, maxX, maxY);
        } else if (vertices == 4) {
            // Quadrilateral subclass identification
            return mapQuadrilateral(cleanedPoints, minX, minY, maxX, maxY);
        }

        return new RecognitionResult("Freehand", minX, minY, maxX, maxY);
    }

    private static List<Point2D> cleanPoints(List<Point2D> points) {
        List<Point2D> cleaned = new ArrayList<>();
        for (Point2D p : points) {
            if (cleaned.isEmpty() || cleaned.get(cleaned.size() - 1).distance(p) > 5) {
                cleaned.add(p);
            }
        }
        return cleaned;
    }

    private static List<Point2D> normalizePoints(List<Point2D> points, double minX, double maxX, double minY, double maxY) {
        List<Point2D> norm = new ArrayList<>();
        double w = Math.max(maxX - minX, 1);
        double h = Math.max(maxY - minY, 1);
        for (Point2D p : points) {
            double nx = ((p.getX() - minX) / w) * 100.0;
            double ny = ((p.getY() - minY) / h) * 100.0;
            norm.add(new Point2D(nx, ny));
        }
        return norm;
    }

    private static int countVertices(List<Point2D> normPoints) {
        if (normPoints.size() < 3) return 0;
        int vertices = 0;
        int step = Math.max(1, normPoints.size() / 20); // sample step for smoothness
        for (int i = step; i < normPoints.size() - step; i += step) {
            Point2D prev = normPoints.get(i - step);
            Point2D curr = normPoints.get(i);
            Point2D next = normPoints.get(i + step);
            
            double angle1 = Math.atan2(curr.getY() - prev.getY(), curr.getX() - prev.getX());
            double angle2 = Math.atan2(next.getY() - curr.getY(), next.getX() - curr.getX());
            double diff = Math.abs(angle2 - angle1);
            if (diff > Math.PI) diff = 2 * Math.PI - diff;
            
            if (diff > Math.PI / 6) { // Sharp change > 30 degrees (more lenient than 45)
                vertices++;
            }
        }
        return vertices;
    }

    private static RecognitionResult mapTriangle(List<Point2D> original, double minX, double minY, double maxX, double maxY) {
        double w = maxX - minX;
        double h = maxY - minY;
        
        // Simple heuristic mapping
        // In a real sophisticated agent, we'd find the exact corners of the original shape
        // For this assignment, we map based on basic heuristics of bounding box and symmetry
        double expectedEquiHeight = w * Math.sqrt(3) / 2;
        if (Math.abs(h - expectedEquiHeight) / h < 0.3) { // More lenient
            return new RecognitionResult("EquilateralTriangle", minX, minY, maxX, maxY);
        }
        // Check if one angle is close to 90
        // We'll approximate this by checking if the cluster heavily weights one side
        double cx = 0;
        for (Point2D p : original) cx += p.getX();
        cx /= original.size();
        
        if (Math.abs(cx - minX) < w * 0.3 || Math.abs(cx - maxX) < w * 0.3) { // More lenient
            return new RecognitionResult("RightTriangle", minX, minY, maxX, maxY);
        }

        return new RecognitionResult("IsoscelesTriangle", minX, minY, maxX, maxY);
    }

    private static RecognitionResult mapQuadrilateral(List<Point2D> original, double minX, double minY, double maxX, double maxY) {
        double w = maxX - minX;
        double h = maxY - minY;
        double aspectRatio = w / h;
        
        // Check for slanted shapes (Parallelogram/Rhombus/Trapezoid) vs Orthogonal (Rectangle/Square)
        // We can check how many points align vertically/horizontally
        int verticalAligned = 0, horizontalAligned = 0;
        for(Point2D p : original) {
            if(Math.abs(p.getX() - minX) < w * 0.1 || Math.abs(p.getX() - maxX) < w * 0.1) verticalAligned++;
            if(Math.abs(p.getY() - minY) < h * 0.1 || Math.abs(p.getY() - maxY) < h * 0.1) horizontalAligned++;
        }
        double vertRatio = (double) verticalAligned / original.size();
        double horizRatio = (double) horizontalAligned / original.size();

        if (vertRatio > 0.1 && horizRatio > 0.1) {
            if (Math.abs(aspectRatio - 1.0) < 0.3) {
                return new RecognitionResult("Square", minX, minY, maxX, maxY);
            }
            return new RecognitionResult("Rectangle", minX, minY, maxX, maxY);
        } else if (horizRatio > 0.1 && vertRatio < 0.2) {
            // slanted sides but parallel top/bottom
            if (Math.abs(aspectRatio - 1.0) < 0.3) {
                return new RecognitionResult("Rhombus", minX, minY, maxX, maxY);
            } else if (Math.abs(original.get(0).getX() - original.get(original.size() / 2).getX()) > w * 0.3) {
                 return new RecognitionResult("Trapezoid", minX, minY, maxX, maxY);
            }
            return new RecognitionResult("Parallelogram", minX, minY, maxX, maxY);
        } else {
            // Fallback
            if (Math.abs(aspectRatio - 1.0) < 0.3) return new RecognitionResult("Rhombus", minX, minY, maxX, maxY);
            return new RecognitionResult("Parallelogram", minX, minY, maxX, maxY);
        }
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
}