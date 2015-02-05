package engine;

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
    Point.Double velocity = new Point.Double(0, 0);
    Point.Double nextVelocity;
    Point.Double acceleration = new Point.Double(0, 0);
    double rotation;
    double angularVelocity;
    double angularAcceleration;
    double nextRotation;
    double nextAngularVelocity;
    double restitution;
    ArrayList<Shape> shapes = new ArrayList(1); //relative to position
    Double mass;
    int ID;
    ArrayList<Object> collisions = new ArrayList();
    ArrayList<Plane> touching = new ArrayList();
    ArrayList<Force> forces = new ArrayList();

    public Object() {

    }

    public Object(Shape shape, Point.Double pos) {
        shapes.add(shape);
        position = pos;
        calcMassCenter();
        shape.setParent(this);
    }

    public void calcMassCenter() {
        massCenter = new Vector2D(0, 0);
        for (Shape shape : shapes) {
            //todo, discuss next time
            //massCenter.
        }
    }

    public void preUpdate(double dt, Vector2D g) {
        nextPosition = move(position, velocity, new Point.Double(acceleration.x + g.point.x, acceleration.y + g.point.y), dt);
        nextVelocity = new Point.Double(velocity.x + g.point.x * dt, velocity.y + g.point.y * dt);
        nextRotation = rotation + angularVelocity * dt;
        nextAngularVelocity = angularVelocity + angularAcceleration * dt * dt / 2;
        System.out.println("p:{" + nextPosition.x + ":" + nextPosition.y + "} v:{" + nextVelocity.x + ":" + nextVelocity.y + "}");
        System.out.println("av:"+angularVelocity+";r:"+rotation+";aa:"+angularAcceleration);

        for (Shape shape : shapes) {
            shape.calcNextPosition();
        }
    }

    public void endUpdate() {
        position = nextPosition;
        velocity = nextVelocity;
        rotate(nextRotation-rotation);
        //rotation = nextRotation;
        angularVelocity = nextAngularVelocity;
    }

    public Point.Double interpolate(double k, double dt) {
        //return new Point.Double(position.x + (nextPosition.x - position.x) * k, position.y + (nextPosition.y - position.y) * k);
        return new Point.Double(position.x + velocity.x * dt + (acceleration.x * dt * dt) / 2, position.y + velocity.y * dt + (acceleration.y * dt * dt) / 2);
    }

    static public Point.Double move(Point.Double pos, Point.Double vel, Point.Double acc, double dt) {
        return new Point.Double(pos.x + vel.x * dt + (acc.x * dt * dt) / 2, pos.y + vel.y * dt + (acc.y * dt * dt) / 2);
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
    public void setRestitution(double res){
        restitution=res;
    }
    
    public double getRestitution(){
        return restitution;
    }
    
}
