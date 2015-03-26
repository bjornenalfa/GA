package engine;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author bjodet982
 */
public class CollisionChecker {

    public static final double correctionPercentage = 0.2, slop = 0.01;

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

    public static boolean pointInRectangleShape(RectangleShape shape, Point.Double point) {
        point.x += .5;
        point.y += .5;
        Vector2D vector = new Vector2D(new Point.Double(point.x - shape.x, point.y - shape.y));
//        System.out.println("vector.x : " + vector.point.x + " - vector.y : " + vector.point.y);
        vector.rotate(-shape.rotation);
        vector.readyPoint();
        vector.add(new Vector2D(new Point.Double(shape.width / 2.0, shape.height / 2.0)));
        //vector.add(new Vector2D(new Point.Double(0.5,0.5)));
//        System.out.println("vector.x : " + vector.point.x + " - vector.y : " + vector.point.y + " - Width:Height " + shape.width + ":" + shape.height);
        //System.out.println(!(vector.point.x < 0 || vector.point.x > shape.width || vector.point.y < 0 || vector.point.y > shape.height));
        return !(vector.point.x < 0 || vector.point.x > shape.width || vector.point.y < 0 || vector.point.y > shape.height);
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

    static ArrayList<Line> intersectingLinesList1 = new ArrayList();
    static ArrayList<Line> intersectingLinesList2 = new ArrayList();

    private static boolean rectangleAndRectangleIntersect(RectangleShape rec1, RectangleShape rec2) {
        rec1.calcLines();
        rec2.calcLines();
        intersectingLinesList1.clear();
        intersectingLinesList2.clear();
        for (Line line1 : rec1.lines) {
            for (Line line2 : rec2.lines) {
                if (intersect(line1, line2)) {
                    intersectingLinesList1.add(line1);
                    intersectingLinesList2.add(line2);
                }
            }
        }
        return !intersectingLinesList2.isEmpty() || !intersectingLinesList1.isEmpty();
    }

    private static boolean circleAndCirleIntersect(CircleShape cir1, CircleShape cir2) {
        return (cir2.x - cir1.x) * (cir2.x - cir1.x) + (cir2.y - cir1.y) * (cir2.y - cir1.y) < (cir1.radius + cir2.radius) * (cir1.radius + cir2.radius);
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
        System.out.println("LINJER:" + (line1 * 10 + line2));
        switch ((line1 * 10 + line2)) {
            case 1:
                point = rectangle.lines[0].origin; // VÄNSTER  UPP
                break;
            case 3:
                point = rectangle.lines[0].end; // HÖGER UPP
                break;
            case 12:
                point = rectangle.lines[1].end; // VÄNSTER NER
                break;
            case 23:
                point = rectangle.lines[2].end; // HÖGER NER
                break;
        }
        return point;
    }

    private static boolean planeAndCircleIntersect(CircleShape shape, Plane plane) {
        Vector2D planeToCenter = new Vector2D(plane.surface.origin, new Point.Double(shape.x, shape.y));
        double distanceNormal = Math.abs(Vector2D.scalarProductCoordinates(planeToCenter, plane.normal));
        double distanceTangent = Vector2D.scalarProductCoordinates(planeToCenter, new Vector2D(plane.surface.vector).normalize());
        if (distanceNormal < shape.radius) {
            if (distanceTangent > plane.surface.vector.getLength()) {
                if (distanceTangent < plane.surface.vector.getLength() + shape.radius) {
                    return Point.Double.distanceSq(plane.surface.end.x, plane.surface.end.y, shape.x, shape.y) < shape.radius * shape.radius;
                } else {
                    return false;
                }
            } else if (distanceTangent < 0) {
                if (distanceTangent > -shape.radius) {
                    return Point.Double.distanceSq(plane.surface.origin.x, plane.surface.origin.y, shape.x, shape.y) < shape.radius * shape.radius;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    private static boolean planeAndShapeIntersect(Shape shape, Plane plane) {
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
        return planeAndCircleIntersect(circleShape, plane);
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

    public static Point.Double planeCornerInRectangle(RectangleShape shape, Plane plane) {
        if (pointInRectangleShape(shape, plane.surface.origin)) {
            return plane.surface.origin;
        } else if (pointInRectangleShape(shape, plane.surface.end)) {
            return plane.surface.end;
        } else {
            return null;
        }
    }

    public static void findNewCollisions(ArrayList<Object> objects, ArrayList<Plane> planes, double dt, Vector2D g, World world) {
        for (int i = 0; i < objects.size(); i++) {
            Object object = objects.get(i);
            for (Plane plane : planes) {
                if (planeAndShapeIntersect(object.shapes.get(0), plane)) {
                    System.out.println("COLLISIONS");

                    //NORMALS
                    boolean pointFromPlane = false;

                    Point.Double collisionPoint = null;
                    if (object.shapes.get(0) instanceof RectangleShape) {
                        collisionPoint = planeAndRectangleIntersectCorner((RectangleShape) object.shapes.get(0), plane);
                    } else if (object.shapes.get(0) instanceof CircleShape) {
                        CircleShape shape = (CircleShape) object.shapes.get(0);
                        Vector2D planeToCenter = new Vector2D(plane.surface.origin, new Point.Double(shape.x, shape.y));
                        double distanceNormal = Math.abs(Vector2D.scalarProductCoordinates(planeToCenter, plane.normal));
                        double distanceTangent = Vector2D.scalarProductCoordinates(planeToCenter, new Vector2D(plane.surface.vector).normalize());
                        if (distanceNormal < shape.radius) {
                            if (distanceTangent > plane.surface.vector.getLength()) {
                                if (distanceTangent < plane.surface.vector.getLength() + shape.radius) {
                                    collisionPoint = plane.surface.end; //return Point.Double.distanceSq(plane.surface.end.x, plane.surface.end.y, shape.x, shape.y) < shape.radius * shape.radius;
                                }
                            } else if (distanceTangent < 0) {
                                if (distanceTangent > -shape.radius) {
                                    collisionPoint = plane.surface.origin;
                                }
                            } else {
                                collisionPoint = Vector2D.OrthogonalProjection(planeToCenter, plane.surface.vector).getPoint();
                                collisionPoint.x += plane.surface.origin.x;
                                collisionPoint.y += plane.surface.origin.y;
                            }
                        }
                    }
                    if (collisionPoint == null) {
                        collisionPoint = planeCornerInRectangle((RectangleShape) object.shapes.get(0), plane);
                        pointFromPlane = true;
                    }
                    if (collisionPoint == null) {
                        System.out.println("NO CORNER");
                        double ImpLength = ObjectAndPlaneCollisionImpulseLengthCalculator(object, plane);
                        Vector2D impulse = new Vector2D(new Vector2D(plane.getNormalizedNormal()).multiply(ImpLength));
                        object.nextVelocity = object.velocity.add(impulse.multiply(-1 / object.getMass()));
                        //VISUAL
                        Vector2D impOrt = Vector2D.OrthogonalProjection(new Vector2D(plane.surface.origin, object.nextPosition), plane.surface.vector);
                        Point.Double p = new Point.Double(plane.surface.origin.x + impOrt.point.x, plane.surface.origin.y + impOrt.point.y);
                        world.impulses.add(new Line(p, impulse));
                        System.out.println("Impulse big thingy stuff " + ImpLength);

                        //FRICTION
                        ObjectAndPlaneCollisionFrictionCalculator(object, plane, ImpLength, world);
                    } else {
                        System.out.println("YES CORNER");
                        Vector2D CoMtoCollisionPoint = new Vector2D(object.nextPosition, collisionPoint);
                        Vector2D normal = null;
                        if (object.shapes.get(0) instanceof CircleShape || pointFromPlane) {
                            normal = new Vector2D(collisionPoint, object.nextPosition);
                            world.normals.add(new Line(collisionPoint, normal));
                            //Vector2D normal = new Vector2D(1,-1);
                            normal.normalize();
                        } else if (object.shapes.get(0) instanceof RectangleShape) {
                            /*Shape shape = object.shapes.get(0);
                             Vector2D planeToCenter = new Vector2D(plane.surface.origin, new Point.Double(shape.x, shape.y));
                             Point.Double planePoint = Vector2D.OrthogonalProjection(planeToCenter, plane.surface.vector).getPoint();
                             planePoint.x+=plane.surface.origin.x;
                             planePoint.y+=plane.surface.end.y;*/
                            normal = plane.normal;
                            world.normals.add(new Line(collisionPoint, normal));
                        }

                        double ImpLength = ObjectAndPlaneCollisionImpulseLengthCalculator(object, plane, normal, CoMtoCollisionPoint);
                        Vector2D impulse = new Vector2D(new Vector2D(normal).multiply(ImpLength));
                        object.applyImpulse(new Vector2D(impulse).multiply(-1), CoMtoCollisionPoint);
                        //object.nextVelocity = object.nextVelocity.add(impulse.multiply(-1 / object.getMass()));
                        //VISUAL
                        Vector2D impOrt = Vector2D.OrthogonalProjection(new Vector2D(plane.surface.origin, object.nextPosition), plane.surface.vector);
                        Point.Double p = new Point.Double(plane.surface.origin.x + impOrt.point.x, plane.surface.origin.y + impOrt.point.y);
                        world.impulses.add(new Line(p, new Vector2D(impulse).multiply(-1)));
                        System.out.println("Impulse big thingy stuff " + ImpLength);

                        //FRICTION
                        Vector2D impulse2 = ObjectAndPlaneCollisionFrictionCalculator2(object, plane, ImpLength, world, CoMtoCollisionPoint);

                        //object.applyImpulse(new Vector2D(impulse2).multiply(-1), CoMtoCollisionPoint);
                        //world.impulses.add(new Line(p, new Vector2D(impulse2).multiply(-1)));
                        //object.nextAngularVelocity = object.nextAngularVelocity + Vector2D.crossProduct(CoMtoCollisionPoint, impulse) / object.inertia;
                        //object.nextAngularVelocity = object.nextAngularVelocity + Vector2D.crossProduct(CoMtoCollisionPoint, impulse2) / object.inertia;
                        //object.nextAngularVelocity = object.nextAngularVelocity + Vector2D.crossProduct(CoMtoCollisionPoint, impulse2.add(impulse)) / object.inertia;
                        System.out.println("NEXT ANGULAR VELOCITY IS " + object.nextAngularVelocity);
                    }

                    if (object.shapes.get(0) instanceof RectangleShape) {
                        RectanglePlanePositionCorrection(object, plane);
                    } else if (object.shapes.get(0) instanceof CircleShape) {
                        CirclePlanePositionCorrection(object, plane);
                    }
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
            for (int j = i + 1; j < objects.size(); j++) {
                objectToObjectSolver(objects, i, j, world, dt, g);
            }
        }
        doObjectObject(objects, world, dt, g);
        doObjectObject(objects, world, dt, g);
        doObjectObject(objects, world, dt, g);
    }

    public static void doObjectObject(ArrayList<Object> objects, World world, double dt, Vector2D g) {
        for (int i = 0; i < objects.size(); i++) {
            for (int j = i + 1; j < objects.size(); j++) {
                objectToObjectSolver(objects, i, j, world, dt, g);
            }
        }
    }

    public static void objectToObjectSolver(ArrayList<Object> objects, int i, int j, World world, double dt, Vector2D g) {
        if (objects.get(i).inverseMass + objects.get(j).inverseMass == 0) {
            return;
        }
        if (CollideObjects(objects.get(i), objects.get(j)) != 0) {
            Object firstObject = objects.get(i);
            Object secondObject = objects.get(j);

            CircleShape circle1 = null, circle2 = null;
            RectangleShape rectangle1 = null, rectangle2 = null;
            Vector2D v;
            Point.Double collisionPoint;
            double x, y;
            Vector2D normal = null;

            switch (CollideObjects(firstObject, secondObject)) {
                case 1:
                    rectangle1 = (RectangleShape) firstObject.shapes.get(0);
                    rectangle2 = (RectangleShape) secondObject.shapes.get(0);

                    ArrayList<Point.Double> firstPointsInRectangle2List = new ArrayList();
                    if (rectangle2.contains(rectangle1.lines[0].origin)) {
                        firstPointsInRectangle2List.add(rectangle1.lines[0].origin);
                    }
                    if (rectangle2.contains(rectangle1.lines[0].end)) {
                        firstPointsInRectangle2List.add(rectangle1.lines[0].end);
                    }
                    if (rectangle2.contains(rectangle1.lines[1].end)) {
                        firstPointsInRectangle2List.add(rectangle1.lines[1].end);
                    }
                    if (rectangle2.contains(rectangle1.lines[2].end)) {
                        firstPointsInRectangle2List.add(rectangle1.lines[2].end);
                    }

                    ArrayList<Point.Double> secondPointsInRectangle1List = new ArrayList();
                    if (rectangle1.contains(rectangle2.lines[0].origin)) {
                        secondPointsInRectangle1List.add(rectangle2.lines[0].origin);
                    }
                    if (rectangle1.contains(rectangle2.lines[0].end)) {
                        secondPointsInRectangle1List.add(rectangle2.lines[0].end);
                    }
                    if (rectangle1.contains(rectangle2.lines[1].end)) {
                        secondPointsInRectangle1List.add(rectangle2.lines[1].end);
                    }
                    if (rectangle1.contains(rectangle2.lines[2].end)) {
                        secondPointsInRectangle1List.add(rectangle2.lines[2].end);
                    }

                    ArrayList<Point.Double> leastAmountOfPointsInTheOtherRectangleList;
                    ArrayList<Point.Double> mostAmountOfPointsInTheOtherRectangleList;

                    int mostRectangle = 1;

                    if (Math.min(firstPointsInRectangle2List.size(), secondPointsInRectangle1List.size()) == 0) {
                        if (firstPointsInRectangle2List.size() > secondPointsInRectangle1List.size()) {
                            mostAmountOfPointsInTheOtherRectangleList = firstPointsInRectangle2List;
                        } else {
                            mostAmountOfPointsInTheOtherRectangleList = secondPointsInRectangle1List;
                            mostRectangle = 2;
                        }
                        Point.Double deepestPoint = null;
                        double deepness = 0;
                        Line line;
                        if (mostRectangle == 1) {
                            line = intersectingLinesList2.get(0);
                            for (Point.Double point : mostAmountOfPointsInTheOtherRectangleList) {
                                double depth = Vector2D.scalarProductCoordinates(new Vector2D(line.origin, point), line.normal);
                                if (depth <= deepness) {
                                    deepness = depth;
                                    deepestPoint = point;
                                }
                            }
                        } else {
                            line = intersectingLinesList1.get(0);
                            for (Point.Double point : mostAmountOfPointsInTheOtherRectangleList) {
                                double depth = Vector2D.scalarProductCoordinates(new Vector2D(line.origin, point), line.normal);
                                if (depth <= deepness) {
                                    deepness = depth;
                                    deepestPoint = point;
                                }
                            }
                        }
                        if (deepestPoint == null) {
                            System.out.println("PANIC!! NO POINT");
                        } else {
                            System.out.println("RECTANGLE IS " + mostRectangle);
                            if (mostRectangle == 1) {
                                solveCollision(secondObject, firstObject, deepestPoint, line.normal, -deepness, dt, g, world);
                                rectangle1.calcNextPosition();
                                rectangle2.calcNextPosition();
                            } else {
                                solveCollision(firstObject, secondObject, deepestPoint, line.normal, -deepness, dt, g, world);
                                rectangle1.calcNextPosition();
                                rectangle2.calcNextPosition();
                            }
                        }
                    } else if ((firstPointsInRectangle2List.size() == 1) && (secondPointsInRectangle1List.size() == 1)) {
                        if (firstObject.mass > secondObject.mass) {
                            normal = new Vector2D(firstPointsInRectangle2List.get(0), secondObject.nextPosition);
                            Vector2D vec = new Vector2D(secondObject.nextPosition,firstPointsInRectangle2List.get(0));
                            double cx = Vector2D.scalarProductCoordinates(vec,new Vector2D(rectangle2.lines[0].vector).normalize());
                            double cy = Vector2D.scalarProductCoordinates(vec,new Vector2D(rectangle2.lines[1].vector).normalize());
                            double bx = cx;
                            double by = cy;
                            if (Math.abs(cx)>Math.abs(cy)) {
                                bx = (cx/Math.abs(cx))*(rectangle2.width/2.0);
                            } else {
                                by = (cy/Math.abs(cy))*(rectangle2.height/2.0); 
                            }
                            double depth = Math.sqrt((bx-cx)*(bx-cx)+(by-cy)*(by-cy));
                            solveCollision(firstObject, secondObject, firstPointsInRectangle2List.get(0), new Vector2D(new Point.Double(cx,cy), new Point.Double(bx,by)).rotate(rectangle2.rotation+Math.PI), depth, dt, g, world);
                            rectangle1.calcNextPosition();
                            rectangle2.calcNextPosition();
                        } else {
                            normal = new Vector2D(secondPointsInRectangle1List.get(0), firstObject.nextPosition);
                            Vector2D vec = new Vector2D(secondObject.nextPosition,secondPointsInRectangle1List.get(0));
                            double cx = Vector2D.scalarProductCoordinates(vec,new Vector2D(rectangle1.lines[0].vector).normalize());
                            double cy = Vector2D.scalarProductCoordinates(vec,new Vector2D(rectangle1.lines[1].vector).normalize());
                            double bx = cx;
                            double by = cy;
                            if (Math.abs(cx)>Math.abs(cy)) {
                                bx = (cx/Math.abs(cx))*(rectangle1.width/2.0);
                            } else {
                                by = (cy/Math.abs(cy))*(rectangle1.height/2.0); 
                            }
                            double depth = Math.sqrt((bx-cx)*(bx-cx)+(by-cy)*(by-cy));
                            solveCollision(secondObject, firstObject, secondPointsInRectangle1List.get(0), new Vector2D(new Point.Double(cx,cy), new Point.Double(bx,by)).rotate(rectangle1.rotation+Math.PI), depth, dt, g, world);
                            rectangle1.calcNextPosition();
                            rectangle2.calcNextPosition();
                        }

                    } else if (Math.min(firstPointsInRectangle2List.size(), secondPointsInRectangle1List.size()) != 0) {
                        if (firstPointsInRectangle2List.size() > secondPointsInRectangle1List.size()) {
                            leastAmountOfPointsInTheOtherRectangleList = secondPointsInRectangle1List;
                            mostAmountOfPointsInTheOtherRectangleList = firstPointsInRectangle2List;
                        } else {
                            leastAmountOfPointsInTheOtherRectangleList = firstPointsInRectangle2List;
                            mostAmountOfPointsInTheOtherRectangleList = secondPointsInRectangle1List;
                        }

                    }
                    break;
                case 2:
                    circle1 = (CircleShape) firstObject.shapes.get(0);
                    circle2 = (CircleShape) secondObject.shapes.get(0);
                    v = new Vector2D(new Point.Double(firstObject.nextPosition.x, firstObject.nextPosition.y), new Point.Double(secondObject.nextPosition.x, secondObject.nextPosition.y));
                    double distance = v.getLength();
                    double radius = circle2.radius + circle1.radius;
                    if (distance >= radius) {
                        break;
                    } else {

                        collisionPoint = new Point.Double((firstObject.nextPosition.x * circle2.radius + secondObject.nextPosition.x * circle1.radius) / (circle1.radius + circle2.radius), (firstObject.nextPosition.y * circle2.radius + secondObject.nextPosition.y * circle1.radius) / (circle1.radius + circle2.radius));
                        //collisionPoint=new Point.Double((collisionVector.point.x+inverseCollisionVector.point.x)/2d, (collisionVector.point.y+inverseCollisionVector.point.y)/2d);
                        Point.Double point1 = v.normalize().multiply(circle1.radius).point;
                        point1.x += firstObject.nextPosition.x;
                        point1.y += firstObject.nextPosition.y;
                        double penetrationDepth = new Vector2D(point1, collisionPoint).getLength();

                        solveCollision(firstObject, secondObject, collisionPoint, new Vector2D(new Point.Double(firstObject.nextPosition.x, firstObject.nextPosition.y), new Point.Double(secondObject.nextPosition.x, secondObject.nextPosition.y)), penetrationDepth, dt, g, world);
                    }

                    break;
                case 3:
                    Object temp = firstObject;
                    firstObject = secondObject;
                    secondObject = temp;
                case 4:
                    circle1 = (CircleShape) secondObject.shapes.get(0);
                    rectangle1 = (RectangleShape) firstObject.shapes.get(0);
                    v = new Vector2D(new Point.Double(rectangle1.x, rectangle1.y), new Point.Double(circle1.x, circle1.y));
                    x = Vector2D.scalarProductCoordinates(v, rectangle1.lines[0].vector.normalize());
                    x = Math.min(rectangle1.width / 2.0, Math.max(x, -rectangle1.width / 2.0));
                    y = Vector2D.scalarProductCoordinates(v, rectangle1.lines[1].vector.normalize());
                    y = Math.min(rectangle1.height / 2.0, Math.max(y, -rectangle1.height / 2.0));
                    collisionPoint = new Point.Double(x, y);
                    Vector2D collisionRotate = new Vector2D(collisionPoint);
                    collisionRotate.rotate(firstObject.shapes.get(0).rotation);
                    collisionPoint = collisionRotate.getPoint();
                    collisionPoint.x += firstObject.nextPosition.x;
                    collisionPoint.y += firstObject.nextPosition.y;
                    normal = new Vector2D(collisionPoint, secondObject.nextPosition);

                    solveCollision(firstObject, secondObject, collisionPoint, normal, (circle1.radius - normal.getLength()), dt, g, world);
                    rectangle1.calcNextPosition();
                    break;
                case 0:
                default:
                    break;
            }
        }
    }

    public static void solveCollision(Object firstObject, Object secondObject, Point.Double collisionPoint, Vector2D normal, double penetrationDepth, double dt, Vector2D g, World world) {
        double restitution = Math.min(Restitution.get(firstObject.material), Restitution.get(secondObject.material));
        double staticFriction = Friction.getStatic(firstObject.material, secondObject.material);
        double dynamicFriction = Friction.getDynamic(firstObject.material, secondObject.material);
        Vector2D firstObjectCenterToCollisionPoint = new Vector2D(firstObject.nextPosition, collisionPoint);
        Vector2D secondObjectCenterToCollisionPoint = new Vector2D(secondObject.nextPosition, collisionPoint);
        Vector2D relativeVelocity = new Vector2D(secondObject.nextVelocity).add(Vector2D.crossProduct(secondObject.nextAngularVelocity, secondObjectCenterToCollisionPoint)).subtract(firstObject.nextVelocity).subtract(Vector2D.crossProduct(firstObject.nextAngularVelocity, firstObjectCenterToCollisionPoint));
        System.out.println("RELATIVE VELOCITY A:" + Math.toDegrees(relativeVelocity.getAngle()) + " L:" + relativeVelocity.getLength());

        if (relativeVelocity.getLength() < (new Vector2D(g).multiply(dt)).getLength() + 0.0001f) {
            if (firstObject.inverseMass == 0) {
                restitution = 0;
            } else {
                restitution = 0;
            }
        }
        world.normals.add(new Line(collisionPoint, normal));
        normal.normalize();
        double contactVelocity = Vector2D.scalarProductCoordinates(relativeVelocity, normal);
        System.out.println("CONTACT VELOCITY: " + contactVelocity);

        if (contactVelocity <= 0) {
            double firstObjectCrossNormal = Vector2D.crossProduct(firstObjectCenterToCollisionPoint, normal);
            double secondObjectCrossNormal = Vector2D.crossProduct(secondObjectCenterToCollisionPoint, normal);
            double massInverseSum = firstObject.inverseMass + secondObject.inverseMass + firstObjectCrossNormal * firstObjectCrossNormal * firstObject.inverseInertia + secondObjectCrossNormal * secondObjectCrossNormal * secondObject.inverseInertia;
            System.out.println("MASSINVERSESUM:" + massInverseSum);
            System.out.println("RESTITUTION:" + restitution);
            double impulseLength = -(1.0 + restitution) * contactVelocity;
            System.out.println("IMPULSELENGTH:" + impulseLength);
            impulseLength /= massInverseSum;
            System.out.println("IMPULSELENGTH:" + impulseLength);
            Vector2D impulse = new Vector2D(normal).multiply(impulseLength);
            world.impulses.add(new Line(collisionPoint, new Vector2D(normal).multiply(impulseLength * (secondObject.inverseMass + firstObject.inverseMass))));
            firstObject.applyImpulse(new Vector2D(impulse).multiply(-1), firstObjectCenterToCollisionPoint);
            secondObject.applyImpulse(impulse, secondObjectCenterToCollisionPoint);

            Vector2D tangent = new Vector2D(relativeVelocity).subtract(new Vector2D(normal).multiply(Vector2D.scalarProductCoordinates(relativeVelocity, normal)));
            tangent.normalize();
            double jjTangent = -Vector2D.scalarProductCoordinates(relativeVelocity, tangent);
            jjTangent /= massInverseSum;

            Vector2D frictionImpulse;
            if (Math.abs(jjTangent) < (impulseLength * staticFriction)) {
                frictionImpulse = new Vector2D(tangent).multiply(jjTangent);
            } else {
                frictionImpulse = new Vector2D(tangent).multiply(-impulseLength * dynamicFriction);
            }

            world.impulses.add(new Line(collisionPoint, frictionImpulse));

            firstObject.applyImpulse(new Vector2D(frictionImpulse).multiply(-1), firstObjectCenterToCollisionPoint);
            secondObject.applyImpulse(frictionImpulse, secondObjectCenterToCollisionPoint);

            if ((firstObject.inverseMass + secondObject.inverseMass) > 0) {
                Vector2D correction = new Vector2D(normal).multiply(correctionPercentage * (Math.max(penetrationDepth - slop, 0d) / (firstObject.inverseMass + secondObject.inverseMass)));
                Point.Double temporaryPoint = new Vector2D(correction).multiply(firstObject.inverseMass).getPoint();
                firstObject.nextPosition.x -= temporaryPoint.x;
                firstObject.nextPosition.y -= temporaryPoint.y;
                temporaryPoint = new Vector2D(correction).multiply(secondObject.inverseMass).getPoint();
                secondObject.nextPosition.x += temporaryPoint.x;
                secondObject.nextPosition.y += temporaryPoint.y;
            }
        }
    }

    public static int CollideObjects(Object object1, Object object2) {
        if (object1.shapes.get(0) instanceof RectangleShape && object2.shapes.get(0) instanceof RectangleShape) {
            if (rectangleAndRectangleIntersect((RectangleShape) object1.shapes.get(0), (RectangleShape) object2.shapes.get(0))) {
                return 1;
            }
        } else if (object1.shapes.get(0) instanceof CircleShape && object2.shapes.get(0) instanceof CircleShape) {
            if (circleAndCirleIntersect((CircleShape) object1.shapes.get(0), (CircleShape) object2.shapes.get(0))) {
                return 2;
            }
        } else if (object1.shapes.get(0) instanceof RectangleShape && object2.shapes.get(0) instanceof CircleShape) {
            if (rectangleAndCircleIntersect((RectangleShape) object1.shapes.get(0), (CircleShape) object2.shapes.get(0))) {
                return 4;
            }
        } else {
            if (rectangleAndCircleIntersect((RectangleShape) object2.shapes.get(0), (CircleShape) object1.shapes.get(0))) {
                return 3;
            }
        }
        return 0;
    }

    public static boolean rectangleAndCircleIntersect(RectangleShape rectangle, CircleShape circle) {
        Vector2D v = new Vector2D(new Point.Double(rectangle.x, rectangle.y), new Point.Double(circle.x, circle.y));
        double x = Vector2D.scalarProductCoordinates(v, rectangle.lines[0].vector.normalize());
        double cx = x;
        x = Math.min(rectangle.width / 2.0, Math.max(x, -rectangle.width / 2.0));
        double y = Vector2D.scalarProductCoordinates(v, rectangle.lines[1].vector.normalize());
        double cy = y;
        y = Math.min(rectangle.height / 2.0, Math.max(y, -rectangle.height / 2.0));
        if (Point.distanceSq(x, y, cx, cy) <= circle.radius * circle.radius) {
            return true;
        }
        return false;
    }

    public static void ObjectsCollisionImpulseLengthCalculator(Object obj1, Object obj2) {
//        Vector2D relativeVelocityAlongNormal = new Vector2D(obj2.velocity).subtract(obj1.velocity).multiply(obj2.normal);
        double e = Math.floor(obj1.restitution - obj2.restitution);
//        double Impulse = -(1+e)*(Vector2D.scalarProductCoordinates(rv, plane.)

    }

    public static double ObjectAndPlaneCollisionImpulseLengthCalculator(Object object, Plane plane, Vector2D normal, Vector2D CoMtoCP) {
        double relativeVelocityAlongNormal = Vector2D.scalarProductCoordinates(new Vector2D(object.nextVelocity).subtract(Vector2D.crossProduct(object.nextAngularVelocity, CoMtoCP)), normal);
        if (relativeVelocityAlongNormal > 0) {
            return 0;
        } else {
            return ((1.0 + Math.min(object.restitution, plane.restitution)) * relativeVelocityAlongNormal) / ((1.0 / object.mass) + (Vector2D.crossProduct(CoMtoCP, normal) * Vector2D.crossProduct(CoMtoCP, normal)) / object.inertia);
        }
    }

    public static double ObjectAndPlaneCollisionImpulseLengthCalculator(Object object, Plane plane) {
        //object.velocity.readyPoint();
        //plane.surface.vector.readyPoint();
        double relativeVelocityAlongNormal = Vector2D.scalarProductCoordinates(object.nextVelocity, plane.getNormalizedNormal());
        //relativeVelocityAlongNormal /= 100; // DET ÄR I CM INTE I METER FFS!
        System.out.println("relVelNorm " + relativeVelocityAlongNormal);
        if (relativeVelocityAlongNormal > 0) {
            return 0;
        } else {
            return ((1.0 + Math.min(object.restitution, plane.restitution)) * relativeVelocityAlongNormal) / (1.0 / object.mass);
        }
    }

    public static void ObjectAndPlaneCollisionFrictionCalculator(Object object, Plane plane, double collisionMagnitude, World world) {
        Vector2D tangent = new Vector2D(object.nextVelocity).subtract(new Vector2D(plane.normal).multiply(Vector2D.scalarProductCoordinates(object.nextVelocity, plane.normal)));
        tangent.normalize();
        double jt = -Vector2D.scalarProductCoordinates(object.nextVelocity, tangent);

        System.out.println("TANGENT " + tangent.show());

        double mu = Friction.getStatic(object.material, plane.material);

        System.out.println("JT:" + jt);

        Vector2D frictionImpulse;
        if (Math.abs(jt * object.mass) < Math.abs(collisionMagnitude * mu)) {
            System.out.println("FIRST");
            frictionImpulse = tangent.multiply(jt * Friction.getDynamic(object.material, plane.material));
        } else {
            System.out.println("SECOND");
            frictionImpulse = tangent.multiply(Friction.getDynamic(object.material, plane.material) * collisionMagnitude);
        }
        System.out.println("FRICTION IMPULSE " + frictionImpulse.show());
        frictionImpulse.multiply(1.0 / object.mass);
        //VISUAL IMPULSE
        Vector2D impOrt = Vector2D.OrthogonalProjection(new Vector2D(plane.surface.origin, object.nextPosition), plane.surface.vector);
        Point.Double p = new Point.Double(plane.surface.origin.x + impOrt.point.x, plane.surface.origin.y + impOrt.point.y);
        world.impulses.add(new Line(p, frictionImpulse));
        object.nextVelocity.add(frictionImpulse);
    }

    public static Vector2D ObjectAndPlaneCollisionFrictionCalculator2(Object object, Plane plane, double collisionMagnitude, World world, Vector2D CoMtoCP) {
        Vector2D tangent = new Vector2D(object.nextVelocity).subtract(Vector2D.crossProduct(object.nextAngularVelocity, CoMtoCP)).subtract(new Vector2D(plane.normal).multiply(Vector2D.scalarProductCoordinates(object.nextVelocity, plane.normal)));
        tangent.normalize();
        double jt = -Vector2D.scalarProductCoordinates(object.nextVelocity, tangent);

        jt /= ((1.0 / object.mass) + (Vector2D.crossProduct(CoMtoCP, tangent) * Vector2D.crossProduct(CoMtoCP, tangent)) / object.inertia);

        double mu = Friction.getStatic(object.material, plane.material);

        Vector2D frictionImpulse;
        if (Math.abs(jt) < Math.abs(collisionMagnitude * mu)) {
            frictionImpulse = tangent.multiply(jt * Friction.getDynamic(object.material, plane.material));
        } else {
            frictionImpulse = tangent.multiply(Friction.getDynamic(object.material, plane.material) * collisionMagnitude);
        }
        frictionImpulse.multiply(1.0 / object.mass);
        //VISUAL IMPULSE
        Vector2D impOrt = Vector2D.OrthogonalProjection(new Vector2D(plane.surface.origin, object.nextPosition), plane.surface.vector);
        Point.Double p = new Point.Double(plane.surface.origin.x + impOrt.point.x, plane.surface.origin.y + impOrt.point.y);
        world.impulses.add(new Line(p, frictionImpulse));
        //ACTUAL
        //object.nextVelocity.add(frictionImpulse);
        object.applyImpulse(frictionImpulse, CoMtoCP);
        return frictionImpulse;
    }

    public static double RectanglePlanePenetrationDepth(Object object, Plane plane) {
        RectangleShape rectangle = (RectangleShape) object.shapes.get(0);
        /*double PenetrationDepth = 0.0;
         PenetrationDepth = Math.min(PenetrationDepth, Vector2D.scalarProductCoordinates(new Vector2D(plane.surface.origin, rectangle.lines[0].origin), plane.normal)); // VÄNSTER  UPP
         PenetrationDepth = Math.min(PenetrationDepth, Vector2D.scalarProductCoordinates(new Vector2D(plane.surface.origin, rectangle.lines[0].end), plane.normal)); // HÖGER  UPP
         PenetrationDepth = Math.min(PenetrationDepth, Vector2D.scalarProductCoordinates(new Vector2D(plane.surface.origin, rectangle.lines[1].end), plane.normal)); // VÄNSTER  NER
         PenetrationDepth = Math.min(PenetrationDepth, Vector2D.scalarProductCoordinates(new Vector2D(plane.surface.origin, rectangle.lines[2].end), plane.normal)); // HÖGER NER*/
        System.out.println("PENETRATION DEPTH: " + Math.min(Math.min(Math.min(Math.min(0.0, Vector2D.scalarProductCoordinates(new Vector2D(plane.surface.origin, rectangle.lines[2].end), plane.normal)), Vector2D.scalarProductCoordinates(new Vector2D(plane.surface.origin, rectangle.lines[1].end), plane.normal)), Vector2D.scalarProductCoordinates(new Vector2D(plane.surface.origin, rectangle.lines[0].end), plane.normal)), Vector2D.scalarProductCoordinates(new Vector2D(plane.surface.origin, rectangle.lines[0].origin), plane.normal)));
        return Math.min(Math.min(Math.min(Math.min(0.0, Vector2D.scalarProductCoordinates(new Vector2D(plane.surface.origin, rectangle.lines[2].end), plane.normal)), Vector2D.scalarProductCoordinates(new Vector2D(plane.surface.origin, rectangle.lines[1].end), plane.normal)), Vector2D.scalarProductCoordinates(new Vector2D(plane.surface.origin, rectangle.lines[0].end), plane.normal)), Vector2D.scalarProductCoordinates(new Vector2D(plane.surface.origin, rectangle.lines[0].origin), plane.normal));
    }

    public static double CirclePlanePenetrationDepth(Object object, Plane plane) {
        CircleShape circle = (CircleShape) object.shapes.get(0);

        return circle.radius - DistanceBetweenPointAndPlane(new Point.Double(circle.x, circle.y), plane);
    }

    public static void CirclePlanePositionCorrection(Object object, Plane plane) {
        Vector2D correction = new Vector2D(plane.normal).multiply(Math.max(-CirclePlanePenetrationDepth(object, plane) - slop, 0.0f) * object.mass * correctionPercentage);
        correction.multiply(1.0 / object.mass);
        object.nextPosition.x -= correction.point.x;
        object.nextPosition.y -= correction.point.y;
    }

    public static void RectanglePlanePositionCorrection(Object object, Plane plane) {
        Vector2D correction = new Vector2D(plane.normal).multiply(Math.max(-RectanglePlanePenetrationDepth(object, plane) - slop, 0.0f) * object.mass * correctionPercentage);
        correction.multiply(1.0 / object.mass);
        object.nextPosition.x += correction.point.x;
        object.nextPosition.y += correction.point.y;

    }

    public static double DistanceBetweenPointAndPlane(Point.Double point, Plane plane) {
        return Vector2D.scalarProductCoordinates(new Vector2D(plane.surface.origin, point), plane.normal);
    }

}
