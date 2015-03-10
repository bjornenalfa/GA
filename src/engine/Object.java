package engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author PastaPojken
 */
public class Object {

    Point.Double position = new Point.Double(0, 0);
    Point.Double nextPosition;
    Vector2D massCenter; //relative to position
    Vector2D velocity = new Vector2D(0, 0);
    Vector2D nextVelocity;
    Vector2D acceleration = new Vector2D(0, 0);
    double rotation = 0;
    double angularVelocity = 0;
    double torque = 0;
    double nextRotation = 0;
    double nextAngularVelocity = 0;
    double nextTorque = 0;
    double restitution = 0.5;
    double inertia = 1000;
    ArrayList<Shape> shapes = new ArrayList(1); //relative to position
    Double mass = 0.5;
    Double inverseMass;
    int ID;
    //ArrayList<Object> collisions = new ArrayList();
    //ArrayList<Plane> touching = new ArrayList();
    //ArrayList<Force> forces = new ArrayList();
    int material = Material.Wood;

    public Object(Shape shape, Point.Double pos) {
        shapes.add(shape);
        position = pos;
        calcMassCenter();
        shape.setParent(this);
    }

    public Object(Shape shape, Point.Double pos, int mat) {
        shapes.add(shape);
        position = pos;
        calcMassCenter();
        shape.setParent(this);
        material = mat;
        restitution = Restitution.get(mat);
    }

    public void calcMassCenter() {
        massCenter = new Vector2D(0, 0);
        mass = 0d;
        for (Shape shape : shapes) {
            mass += shape.mass;
            //todo, discuss next time
            //massCenter.
        }
        inverseMass = 1 / mass;
    }

    public void preUpdate(double dt, Vector2D g) {
        nextPosition = move(position, velocity, new Vector2D(new Point.Double(acceleration.point.x + g.point.x, acceleration.point.y + g.point.y)), dt);
        nextVelocity = new Vector2D(new Point.Double(velocity.point.x + g.point.x * dt, velocity.point.y + g.point.y * dt));
        nextAngularVelocity = angularVelocity + (torque / inertia) * dt;
        nextRotation = rotation + angularVelocity * dt;
        System.out.println("p:{" + nextPosition.x + ":" + nextPosition.y + "} v:{" + nextVelocity.point.x + ":" + nextVelocity.point.y + "}");
        System.out.println("av:" + angularVelocity + ";r:" + rotation + ";tq:" + torque);

        for (Shape shape : shapes) {
            shape.calcNextPosition();
        }
    }

    public void endUpdate() {
        position = nextPosition;
        velocity = nextVelocity;
        //rotate(nextRotation - rotation);
        rotation = nextRotation;
        angularVelocity = nextAngularVelocity;
    }

    public Point.Double interpolate(double k, double dt) {
        //return new Point.Double(position.x + (nextPosition.x - position.x) * k, position.y + (nextPosition.y - position.y) * k);
        return new Point.Double(position.x + velocity.point.x * dt + (acceleration.point.x * dt * dt) / 2, position.y + velocity.point.y * dt + (acceleration.point.y * dt * dt) / 2);
    }

    static public Point.Double move(Point.Double pos, Vector2D vel, Vector2D acc, double dt) {
        return new Point.Double(pos.x + vel.point.x * dt + (acc.point.x * dt * dt) / 2, pos.y + vel.point.y * dt + (acc.point.y * dt * dt) / 2);
        //return new Point.Double(pos.x + vel.x * dt, pos.y + vel.y * dt);
    }

    public Object addShapeReturn(Shape shape) {
        this.shapes.add(shape);
        return this;
    }

    public void addShape(Shape shape) {
        shapes.add(shape);
        shape.setParent(this);
    }

    public void rotate(double angle) {
        rotation += angle;
        for (Shape shape : shapes) {
            shape.rotate(angle);
        }
    }

    public double getMass() {
        double mass = 0;
        for (Shape shape : shapes) {
            mass += shape.mass;
        }
        return mass;
    }

    public double getI() {
        return shapes.get(0).I;
    }

    public void setRestitution(double res) {
        restitution = res;
    }

    public double getRestitution() {
        return restitution;
    }

    public void paint(Graphics2D g) {
        for (Shape shape : shapes) {
            shape.paint(g);
        }
        velocity.paint(g, position.x, position.y, 1, Color.red);
    }

    public void applyImpulse(Vector2D impulse, Vector2D contactVector) {
        nextVelocity.add(new Vector2D(impulse).multiply(1 / mass));
        nextAngularVelocity += Vector2D.crossProduct(contactVector, impulse) / inertia;
    }

}
