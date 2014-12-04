package engine;

import java.awt.Point;

/**
 *
 * @author PastaPojken
 */
public class Vector2D {

    Point.Double point;
    Double length;
    Double angle;

    public Vector2D(Point.Double point) {
        this.point = point;
        //length = calculateLength(point);
        //angle = calculateAngle(point);
    }

    public Vector2D(int i, int i0) {
        point = new Point.Double(i, i0);
    }

    public Vector2D(double len, double ang) {
        length = len;
        angle = ang;
        //vector = calculateVector(len,ang);
    }

    public Vector2D rotate(double deltaAngle) {
        if (angle == null) {
            angle = calculateAngle(point);
        }
        angle += deltaAngle;
        point = null;
        return this;
    }

    public Point.Double getPoint() {
        if (point == null) {
            point = calculatePoint(length, angle);
        }
        return point;
    }

    public double getAngle() {
        if (angle == null) {
            angle = calculateAngle(point);
        }
        return angle;
    }

    public double getLength() {
        if (length == null) {
            length = calculateLength(point);
        }
        return angle;
    }

    public void readyPoint() {
        if (point == null) {
            point = calculatePoint(length, angle);
        }
    }

    public static double calculateAngle(Point.Double point) {
        return Math.atan2(point.y, point.x);
    }

    public static double calculateLength(Point.Double point) {
        return Math.sqrt(point.x * point.x + point.y * point.y);
    }

    public static Point.Double calculatePoint(double length, double angle) {
        return new Point.Double(Math.cos(angle) * length, Math.sin(angle) * length);
    }
}
