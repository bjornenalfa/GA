package engine;

import java.awt.Point;
import java.util.ArrayList;

public class CollisionChecker {

    public static final double correctionPercentage = 0.2, slop = 0.01;

    public static boolean intersect(double px, double py, double qx, double qy, double ex, double ey, double fx, double fy) { // CALCULATE INTERSECTIONS
        return (isNegative((fx - ex) * (py - fy) - (fy - ey) * (px - fx)) != isNegative((fx - ex) * (qy - fy) - (fy - ey) * (qx - fx))) && (isNegative((px - qx) * (ey - py) - (py - qy) * (ex - px)) != isNegative((px - qx) * (fy - py) - (py - qy) * (fx - px)));
    }

    public static boolean intersect(Line line1, Line line2) { // CALCULATE INTERSECTIONS
        line1.vector.readyPoint();
        line2.vector.readyPoint();
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
        vector.rotate(-shape.rotation);
        vector.readyPoint();
        vector.add(new Vector2D(new Point.Double(shape.width / 2.0, shape.height / 2.0)));
        return !(vector.point.x < 0 || vector.point.x > shape.width || vector.point.y < 0 || vector.point.y > shape.height);
    }

    public static boolean areLinesAlmostTouching(Line line1, Line line2, double distance, double angleLimit) {
        double firstAngle = line1.vector.getAngle() % Math.PI;
        double secondAngle = line2.vector.getAngle() % Math.PI;
        return (Math.abs(firstAngle - secondAngle)) <= angleLimit && Math.abs(new Vector2D(new Point.Double(line1.origin.x - line2.origin.x, line1.origin.y - line2.origin.y)).rotate(-line1.vector.getAngle()).getPoint().y) <= distance;
    }

    public static boolean areLineAndPointAlmostTouching(Line line, Point.Double point, double distance) {
        return new Vector2D(line.origin.x - point.x, line.origin.y - point.y).rotate(line.vector.getAngle()).getPoint().y <= distance;
    }

    public static boolean isNegative(double num) {
        return num == Math.abs(num);
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

    public static void findNewCollisions(ArrayList<Object> objects, double dt, Vector2D g, World world) {
        int iterator = -1;
        for (Object object : objects) {
            if (!(object instanceof FixedObject)) {
                iterator++;
                if (iterator < 10) {
                    doObjectObject(objects, world, dt, g);
                } else {
                    break;
                }
            }
        }
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
                        int pointIterator = 0;
                        Line line;
                        if (mostRectangle == 1) {
                            line = intersectingLinesList2.get(0);
                            for (Point.Double point : mostAmountOfPointsInTheOtherRectangleList) {
                                pointIterator++;
                                double depth = Vector2D.scalarProductCoordinates(new Vector2D(line.origin, point), line.normal);
                                if (pointIterator == 2) {
                                    if (Math.abs(depth - deepness) < 0.001d) {
                                        deepestPoint = new Point.Double((point.x + deepestPoint.x) / 2d, (point.y + deepestPoint.y) / 2d);
                                        Vector2D vec = new Vector2D(firstObject.nextPosition, deepestPoint);
                                        Point.Double betterPoint = new Vector2D(line.normal).multiply(Vector2D.scalarProductCoordinates(vec, line.normal)).point;
                                        deepestPoint.x = betterPoint.x + firstObject.nextPosition.x;
                                        deepestPoint.y = betterPoint.y + firstObject.nextPosition.y;
                                        world.points.add(deepestPoint);
                                    } else if (depth <= deepness) {
                                        deepness = depth;
                                        deepestPoint = point;
                                    }
                                } else if (depth <= deepness) {
                                    deepness = depth;
                                    deepestPoint = point;
                                }
                            }
                        } else {
                            line = intersectingLinesList1.get(0);
                            for (Point.Double point : mostAmountOfPointsInTheOtherRectangleList) {
                                pointIterator++;
                                double depth = Vector2D.scalarProductCoordinates(new Vector2D(line.origin, point), line.normal);
                                if (pointIterator == 2) {
                                    if (Math.abs(depth - deepness) < 0.001d) {
                                        deepestPoint = new Point.Double((point.x + deepestPoint.x) / 2d, (point.y + deepestPoint.y) / 2d);
                                        Vector2D vec = new Vector2D(secondObject.nextPosition, deepestPoint);
                                        Point.Double betterPoint = new Vector2D(line.normal).multiply(Vector2D.scalarProductCoordinates(vec, line.normal)).point;
                                        deepestPoint.x = betterPoint.x + secondObject.nextPosition.x;
                                        deepestPoint.y = betterPoint.y + secondObject.nextPosition.y;
                                        world.points.add(deepestPoint);
                                    } else if (depth <= deepness) {
                                        deepness = depth;
                                        deepestPoint = point;
                                    }
                                } else if (depth <= deepness) {
                                    deepness = depth;
                                    deepestPoint = point;
                                }
                            }
                        }
                        if (deepestPoint == null) {
                            //NOTHING :(
                        } else {
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
                            Vector2D vec = new Vector2D(secondObject.nextPosition, firstPointsInRectangle2List.get(0));
                            double cx = Vector2D.scalarProductCoordinates(vec, new Vector2D(rectangle2.lines[0].vector).normalize());
                            double cy = Vector2D.scalarProductCoordinates(vec, new Vector2D(rectangle2.lines[1].vector).normalize());
                            double bx = cx;
                            double by = cy;
                            if (Math.abs(cx) > Math.abs(cy)) {
                                bx = (cx / Math.abs(cx)) * (rectangle2.width / 2.0);
                            } else {
                                by = (cy / Math.abs(cy)) * (rectangle2.height / 2.0);
                            }
                            double depth = Math.sqrt((bx - cx) * (bx - cx) + (by - cy) * (by - cy));
                            solveCollision(firstObject, secondObject, firstPointsInRectangle2List.get(0), new Vector2D(new Point.Double(cx, cy), new Point.Double(bx, by)).rotate(rectangle2.rotation + Math.PI), depth, dt, g, world);
                            rectangle1.calcNextPosition();
                            rectangle2.calcNextPosition();
                        } else {
                            Vector2D vec = new Vector2D(firstObject.nextPosition, secondPointsInRectangle1List.get(0));
                            double cx = Vector2D.scalarProductCoordinates(vec, new Vector2D(rectangle1.lines[0].vector).normalize());
                            double cy = Vector2D.scalarProductCoordinates(vec, new Vector2D(rectangle1.lines[1].vector).normalize());
                            double bx = cx;
                            double by = cy;
                            if (Math.abs(cx) > Math.abs(cy)) {
                                bx = (cx / Math.abs(cx)) * (rectangle1.width / 2.0);
                            } else {
                                by = (cy / Math.abs(cy)) * (rectangle1.height / 2.0);
                            }
                            double depth = Math.sqrt((bx - cx) * (bx - cx) + (by - cy) * (by - cy));
                            solveCollision(secondObject, firstObject, secondPointsInRectangle1List.get(0), new Vector2D(new Point.Double(cx, cy), new Point.Double(bx, by)).rotate(rectangle1.rotation + Math.PI), depth, dt, g, world);
                            rectangle1.calcNextPosition();
                            rectangle2.calcNextPosition();
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

        if (contactVelocity <= 0) {
            double firstObjectCrossNormal = Vector2D.crossProduct(firstObjectCenterToCollisionPoint, normal);
            double secondObjectCrossNormal = Vector2D.crossProduct(secondObjectCenterToCollisionPoint, normal);
            double massInverseSum = firstObject.inverseMass + secondObject.inverseMass + firstObjectCrossNormal * firstObjectCrossNormal * firstObject.inverseInertia + secondObjectCrossNormal * secondObjectCrossNormal * secondObject.inverseInertia;
            double impulseLength = -(1.0 + restitution) * contactVelocity;
            impulseLength /= massInverseSum;
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

            world.impulses.add(new Line(collisionPoint, new Vector2D(frictionImpulse).multiply(massInverseSum)));

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

}
