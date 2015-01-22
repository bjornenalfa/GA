package engine;

import java.awt.Point;
import java.awt.geom.Point2D;

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

    public Vector2D(int x, int y) {
        point = new Point.Double(x, y);
    }

    public Vector2D(double len, double ang) {
        length = len;
        angle = ang;
        //vector = calculateVector(len,ang);
    }

    public Vector2D(Vector2D base) {
        length = base.length;
        angle = base.angle;
        point = (Point2D.Double) base.point.clone();
    }

    public Vector2D rotate(double deltaAngle) {
        if (angle == null) {
            angle = calculateAngle(point);
        }
        angle += deltaAngle;
        readyLength();
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
        return length;
    }

    public void readyPoint() {
        if (point == null) {
            point = calculatePoint(length, angle);
        }
    }

    public void readyLength() {
        if (length == null) {
            calculateLength();
        }
    }

    public void add(Vector2D secondVector) {
        readyPoint();
        point.x += secondVector.getPoint().x;
        point.y += secondVector.getPoint().y;
    }

    public void subtract(Vector2D secondVector) {
        readyPoint();
        point.x -= secondVector.getPoint().x;
        point.y -= secondVector.getPoint().y;
    }

    public void multiply(double num) {
        if (length != null) {
            length *= num;
        }
        if (point != null) {
            point.x *= num;
            point.y *= num;
        }
    }

    public void calculateLength() {
        length = calculateLength(point);
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

    public static Vector2D add(Vector2D firstVector, Vector2D secondVector) {
        firstVector.point.x += secondVector.getPoint().x;
        firstVector.point.y += secondVector.getPoint().y;
        return firstVector;
    }

    public static Vector2D subtract(Vector2D firstVector, Vector2D secondVector) {
        firstVector.point.x -= secondVector.getPoint().x;
        firstVector.point.y -= secondVector.getPoint().y;
        return firstVector;
    }

    public static Vector2D multiply(double num, Vector2D vector) {
        if (vector.length != null) {
            vector.length *= num;
        }
        if (vector.point != null) {
            vector.point.x *= num;
            vector.point.y *= num;
        }
        return vector;
    }

    /**
     * do this if we needed it :)
     *
     * @param vector1
     * @param vector2
     * @return
     */
    public static double scalarProductAngleLength(Vector2D vector1, Vector2D vector2) {
        return (0.0); //do this if we needed it :)
    }

    /**
     * Needs point!!
     *
     * @param vector1
     * @param vector2
     * @return
     */
    public static double scalarProductCoordinates(Vector2D vector1, Vector2D vector2) {
        return (vector1.point.x * vector2.point.x + vector1.point.y * vector2.point.y);
    }

    public static Vector2D OrthogonalProjection(Vector2D vector, Vector2D base) {
        return multiply((scalarProductCoordinates(vector, base) / (Math.pow(base.getLength(), 2))), new Vector2D(base));
    }

    public static Vector2D getNormalComponent(Vector2D vector, Vector2D base) {
        return subtract(new Vector2D(vector), OrthogonalProjection(vector, base));
    }
}
