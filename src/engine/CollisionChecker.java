package engine;

import java.awt.Point;
import java.awt.geom.Point2D;
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
        System.out.println("vector.x : " + vector.point.x + " - vector.y : " + vector.point.y);
        vector.rotate(-rs.rotation);
        vector.readyPoint();
        System.out.println("vector.x : " + vector.point.x + " - vector.y : " + vector.point.y + " - Width:Height " + rs.width + ":" + rs.height);
        System.out.println(!(vector.point.x < 0 || vector.point.x > rs.width || vector.point.y < 0 || vector.point.y > rs.height));
        return !(vector.point.x < 0 || vector.point.x > rs.width || vector.point.y < 0 || vector.point.y > rs.height);
    }

    public static boolean areLinesAlmostTouching(Line line1, Line line2, double distance, double angleLimit) {
        double firstAngle = line1.vector.getAngle() % Math.PI;
        double secondAngle = line2.vector.getAngle() % Math.PI;
        //System.out.println("distance: " + Math.abs(new Vector2D(new Point.Double(line1.origin.x - line2.origin.x, line1.origin.y - line2.origin.y)).rotate(-line1.vector.getAngle()).getPoint().y));
        //System.out.println("angle differance: "+Math.abs(firstAngle - secondAngle));
        return (Math.abs(firstAngle - secondAngle)) <= angleLimit && Math.abs(new Vector2D(new Point.Double(line1.origin.x - line2.origin.x, line1.origin.y - line2.origin.y)).rotate(-line1.vector.getAngle()).getPoint().y) <= distance;
    }

    public static boolean areLineAndPointAlmostTouching(Line line, Point.Double point, double distance) {
        return new Vector2D(line.origin.x - point.x, line.origin.y - point.y).rotate(line.vector.getAngle()).getPoint().y <= distance;
    }

    public static boolean isNegative(double num) {
        return num == Math.abs(num);
    }

    private static boolean planeAndRectangleIntersect(RectangleShape rectangle, Plane plane) {
        rectangle.calcLines();
        for (Line line : rectangle.lines) {
            if (intersect(line, plane.surface)) {
                return true;
            }
        }
        return false;
    }

    private static Point.Double planeAndRectangleIntersectCorner(RectangleShape rectangle, Plane plane) {
        rectangle.calcLines();
        Point.Double point = null;
        int line1 = 10000;
        int line2 = 10000;
        for (int i = 0; i < 4; i++) {
            if (intersect(rectangle.lines[i], plane.surface)) {
                if (line1 == 10000) {
                    line1 = i;
                } else {
                    line2 = i;
                    break;
                }
            }
        }
        switch ((line1 * 10 + line2)) {
            case 1:
                point = rectangle.lines[0].origin;
            case 3:
                point = rectangle.lines[0].end;
            case 12:
                point = rectangle.lines[1].end;
            case 23:
                point = rectangle.lines[2].end;
        }
        return point;
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

    private static boolean planeAndRectangleTouch(RectangleShape rectangle, Plane plane) {
        for (Line line : rectangle.lines) {
            if (areLinesAlmostTouching(line, plane.surface, 0.1, 0.1)) {
                return true;
            }
        }
        return false;
    }

    private static boolean planeAndCircleTouch(CircleShape circleShape, Plane plane) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static boolean planeAndShapeTouch(Shape shape, Plane plane) {
        boolean collision = false;
        if (shape instanceof RectangleShape) {
            collision = planeAndRectangleTouch((RectangleShape) shape, plane);
        } else if (shape instanceof CircleShape) {
            collision = planeAndCircleTouch((CircleShape) shape, plane);
        }

        return collision;
    }

    public static void findNewCollisions(ArrayList<Object> objects, ArrayList<Plane> planes, double dt, Vector2D g) {
        for (Object object : objects) {
            for (Plane plane : planes) {
                if (planeAndShapeIntersect(object.shapes.get(0), plane)) {
                    System.out.println("COLLISIONS");
                    
                    double imp = ObjectAndPlaneCollisionImpulseLengthCalculator(object, plane);
                    Vector2D vec = new Vector2D(plane.getNormalizedNormal().multiply(imp));
                    object.velocity -= 1/object.getMass()*vec;

//                    Point.Double balancePoint = planeAndRectangleIntersectCorner((RectangleShape) object.shapes.get(0), plane);
//                    if (balancePoint != null) {
//                        Vector2D momentAxis = new Vector2D(object.massCenter.getPoint(), balancePoint);
//                        Vector2D normalComponent = Vector2D.getNormalComponent(Vector2D.multiply(new Vector2D(g), object.getMass()), momentAxis);
//                        object.nextAngularVelocity = normalComponent.getLength() * object.getI() * object.velocity.distance(0, 0);
//                        System.out.println("AV:" + (normalComponent.getLength() * object.getI()));
//                        
//                    }
//                    
//                    
//                    
//                    //System.out.println("NCL: "+ normalComponent.getLength());
//
//                    //System.out.println("it is happening");
//                    /*double k = 0.5;
//                     double change = 0.25;
//                     for (int i = 0; i < 8; i++) {
//                     object.preUpdate(dt * k, g);
//                     if (planeAndShapeIntersect(object.shapes.get(0), plane)) {
//                     k -= change;
//                     } else {
//                     k += change;
//                     }
//                     change /= 2;
//                     }
//                     object.preUpdate(dt * (k + change * 2), g);*/
//                    Vector2D parVel = Vector2D.OrthogonalProjection(new Vector2D(object.velocity), plane.surface.vector);
//                    parVel.readyPoint();
//                    object.nextVelocity = new Point.Double(parVel.point.x, parVel.point.y);
//                    Vector2D accVec = Vector2D.multiply(plane.getNormal().normalize(), g.getLength() * Math.cos(plane.surface.vector.getAngle()));
//                    accVec.readyPoint();
//                    object.acceleration = new Point.Double(accVec.point.x, accVec.point.y);
                }
            }
        }

        for (Object object : objects) {
            for (Plane plane : planes) {
                if (planeAndShapeTouch(object.shapes.get(0), plane)) {
                    System.out.println("touching :>");
                    /*double k = 0.5;
                     double change = 0.25;
                     for (int i = 0; i < 8; i++) {
                     object.preUpdate(dt * k, g);
                     if (planeAndShapeTouch(object.shapes.get(0), plane)) {
                     k -= change;
                     } else {
                     k += change;
                     }
                     change /= 2;
                     }
                     object.preUpdate(dt * (k - change * 2), g);*/
                    //object.velocity = new Point.Double(0, 0);
                }
            }
        }
    }

    public static void ObjectsCollisionImpulseLengthCalculator(Object obj1, Object obj2) {
        Vector2D rv = obj2.velocity.subtract(obj1.velocity);
        double e = Math.floor(obj1.restitution - obj2.restitution);
//        double Impulse = -(1+e)*(Vector2D.scalarProductCoordinates(rv, plane.)

    }

    public static double ObjectAndPlaneCollisionImpulseLengthCalculator(Object obj1, Plane obj2) {
        double velNormal = Vector2D.scalarProductCoordinates(obj1.velocity, obj2.getNormal().normalize());
        if (velNormal < 0) {
            return (-(1.0 + Math.min(obj1.restitution, obj2.restitution)) * velNormal) / (1.0 / obj1.mass);
        } else {
            return 0;
        }
    }
}
