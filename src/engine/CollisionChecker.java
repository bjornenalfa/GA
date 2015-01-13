package engine;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author bjodet982
 */
public class CollisionChecker {

    public static boolean intersect(double px, double py, double qx, double qy, double ex, double ey, double fx, double fy) { // CALCULATE INTERSECTIONS
	/*double cp = (fx-ex)*(py-fy)-(fy-ey)*(px-fx);
         double cq = (fx-ex)*(qy-fy)-(fy-ey)*(qx-fx);
         double ce = (px-qx)*(ey-py)-(py-qy)*(ex-px);
         double cf = (px-qx)*(fy-py)-(py-qy)*(fx-px);
         return (isn(cp) != isn(cq)) && (isn(ce) != isn(cf));*/
        return (isNegative((fx - ex) * (py - fy) - (fy - ey) * (px - fx)) != isNegative((fx - ex) * (qy - fy) - (fy - ey) * (qx - fx))) && (isNegative((px - qx) * (ey - py) - (py - qy) * (ex - px)) != isNegative((px - qx) * (fy - py) - (py - qy) * (fx - px)));
    }

    public static boolean intersect(Line line1, Line line2) { // CALCULATE INTERSECTIONS
        line1.vector.readyPoint();
        line2.vector.readyPoint();
        /*double cp = (line2.vector.point.x)*(line1.origin.y-line2.end.y)-(line2.vector.point.y)*(line1.origin.x-line2.end.x);
         double cq = (line2.vector.point.x)*(line1.end.y-line2.end.y)-(line2.vector.point.y)*(line1.end.x-line2.end.x);
         double ce = (-line1.vector.point.x)*(line2.origin.y-line1.origin.y)-(-line1.vector.point.y)*(line2.origin.x-line1.origin.x);
         double cf = (-line1.vector.point.x)*(line2.end.y-line1.origin.y)-(-line1.vector.point.y)*(line2.end.x-line1.origin.x);
        
         double cp = (line2.vector.point.x)*(line1.origin.y-line2.end.y)-(line2.vector.point.y)*(line1.origin.x-line2.end.x);
         double cq = (line2.vector.point.x)*(line1.end.y-line2.end.y)-(line2.vector.point.y)*(line1.end.x-line2.end.x);
         double ce = (line1.vector.point.y)*(line2.origin.x-line1.origin.x)-(line1.vector.point.x)*(line2.origin.y-line1.origin.y);
         double cf = (line1.vector.point.y)*(line2.end.x-line1.origin.x)-(line1.vector.point.x)*(line2.end.y-line1.origin.y);
         return (isn(cp) != isn(cq)) && (isn(ce) != isn(cf));*/
        return (isNegative((line2.vector.point.x) * (line1.origin.y - line2.end.y) - (line2.vector.point.y) * (line1.origin.x - line2.end.x)) != isNegative((line2.vector.point.x) * (line1.end.y - line2.end.y) - (line2.vector.point.y) * (line1.end.x - line2.end.x))) && (isNegative((line1.vector.point.y) * (line2.origin.x - line1.origin.x) - (line1.vector.point.x) * (line2.origin.y - line1.origin.y)) != isNegative((line1.vector.point.y) * (line2.end.x - line1.origin.x) - (line1.vector.point.x) * (line2.end.y - line1.origin.y)));
    }

    public static boolean parallel(double ax, double ay, double bx, double by, double cx, double cy, double dx, double dy) {
        double firstSlope = ((by - ay) / (bx - ax));
        double secondSlope = ((dy - cy) / (dx - cx));

        return firstSlope == secondSlope;
    }

    public static boolean parallel(Line a, Line b) {
        double firstSlope = (a.vector.getPoint().y) / (a.vector.getPoint().x);
        double secondSlope = (b.vector.getPoint().y) / (b.vector.getPoint().x);

        return firstSlope == secondSlope;
    }

    public static boolean pointInRectangleShape(RectangleShape rs, Point.Double p) {
        Vector2D vector = new Vector2D(new Point.Double(p.x - rs.x, p.y - rs.y));
        vector.calculateLength();
        System.out.println("vector.x : " + vector.point.x + " - vector.y : " + vector.point.y);
        vector.rotate(-rs.rotation);
        vector.readyPoint();
        System.out.println("vector.x : " + vector.point.x + " - vector.y : " + vector.point.y + " - Width:Height " + rs.width + ":" + rs.height);
        System.out.println(!(vector.point.x < 0 || vector.point.x > rs.width || vector.point.y < 0 || vector.point.y > rs.height));
        return !(vector.point.x < 0 || vector.point.x > rs.width || vector.point.y < 0 || vector.point.y > rs.height);
    }

    public static boolean areTheyAlmostTouching(Line a, Line b, double limit, double slopeLimit) {
        double firstSlope = a.vector.getAngle() % Math.PI;
        double secondSlope = b.vector.getAngle() % Math.PI;
        return (Math.abs(firstSlope - secondSlope)) <= slopeLimit && new Vector2D(a.origin.x - b.origin.x, a.origin.y - b.origin.y).rotate(a.vector.getAngle()).getPoint().y <= limit;
    }

    public static boolean isNegative(double num) {
        return num == Math.abs(num);
    }

    private static boolean planeAndRectangleIntersect(RectangleShape rec, Plane plane) {
        rec.calcLines();
        for (Line line : rec.lines) {
            if (intersect(line, plane.surface)) {
                return true;
            }
        }
        return false;
    }

    private static boolean planeAndCircleIntersect(CircleShape shape, Plane plane) {
        System.out.println("NOT IMPLEMENTED - CircleShape collision!");
        return false;
    }

    public static boolean planeAndShapeIntersect(Shape shape, Plane plane) {
        boolean collision = false;
        if (shape instanceof RectangleShape) {
            collision = planeAndRectangleIntersect((RectangleShape) shape, plane);
        } else if (shape instanceof CircleShape) {
            collision = planeAndCircleIntersect((CircleShape) shape, plane);
        }

        return collision;
    }

    public static void findNewCollisions(ArrayList<Object> objects, ArrayList<Plane> planes, double dt, double g) {
        for (Object object : objects) {
            for (Plane plane : planes) {
                if (planeAndShapeIntersect(object.shapes.get(0), plane)) {
                    System.out.println("it is happening");
                    double k = 0.5;
                    double change = 0.25;
                    for (int i = 0; i < 8; i++) {
                        object.preUpdate(dt * k, g);
                        if (planeAndShapeIntersect(object.shapes.get(0), plane)) {
                            k -= change;
                        } else {
                            k += change;
                        }
                        change /= 2;
                    }
                    object.preUpdate(dt * (k - change * 2), g);
                    object.velocity = new Point.Double(0, 0);
                }
            }
        }
    }
}
