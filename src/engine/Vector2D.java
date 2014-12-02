package engine;

import java.awt.Point;

/**
 *
 * @author PastaPojken
 */
public class Vector2D {

    Point.Double vector;
    double length;
    double angle;

    public Vector2D(Point.Double point) {
        vector = point;
        length = calculateLength(point);
        angle = calculateAngle(point);
    }

    public Vector2D(double len, double ang) {
        length = len;
        angle = ang;
        vector = calculateVector(len,ang);
    }

    public static double calculateAngle(Point.Double point) {
        return Math.atan2(point.y, point.x);
    }

    public static double calculateLength(Point.Double point) {
        return Math.sqrt(point.x * point.x + point.y * point.y);
    }

    public static Point.Double calculateVector(double length, double angle) {
        return new Point.Double(Math.cos(angle)*length,Math.sin(angle)*length);
    }
}
