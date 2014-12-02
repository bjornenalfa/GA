package engine;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author PastaPojken
 */
public class Object {

    Point.Double position;
    Point.Double nextPosition;
    Vector2D massCenter; //relative to position
    Point.Double velocity;
    Double rotation;
    Double angularVelocity;
    ArrayList<Shape> shapes = new ArrayList(); //relative to position
    Double Mass;

    public Object() {

    }

    public void update(double dt) {
        nextPosition = move(position, velocity, dt);
    }

    public Point.Double interpolate(double k) {
        return new Point.Double(position.x + (nextPosition.x - position.x) * k, position.y + (nextPosition.y - position.y) * k);
    }

    static public Point.Double move(Point.Double pos, Point.Double vel, double dt) {
        return new Point.Double(pos.x + vel.x * dt, pos.y + vel.y * dt);
    }

    public Object addShapeReturn(Shape shape) {
        this.shapes.add(shape);
        return this;
    }

    public void addShape(Shape shape) {
        shapes.add(shape);
    }

}
