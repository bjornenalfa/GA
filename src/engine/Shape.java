package engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class Shape {

    Vector2D vector; //Anchor in object's position
    double dRotate; //relative to object's rotation
    double rotation; //actual rotation
    double mass;//mass in g
    double density;//density in g/cm^2
    double x, y; // X, Y coordinates; Width and Height
    double inertia; //Tröghetsmomentet
    Color myC; //Shape color    
    Object parent;

    /**
     * Basic "Shape" constructor
     *
     * @param x - X coordinate
     * @param y - Y coordinate
     * @param v - vector
     * @param r - rotation, in radians
     * @param d - density, g/cm2
     * @param c - color
     */
    public Shape(Vector2D v, double r, double d, Color c) {
        vector = v;
        dRotate = r;
        rotation = r;
        density = d;
        myC = c;
        this.x = x;
        this.y = y;
    }

    public void rotate(double rad) {
        rotation += rad;
        vector.rotate(rad);
    }

    public void paint(Graphics2D g) {
        g.setColor(myC);
    }

    public void calcPosition() {
        vector.readyPoint();
        rotation = parent.rotation + dRotate;
        x = parent.position.x + vector.point.x;
        y = parent.position.y + vector.point.y;
    }

    public void calcNextPosition() {
        vector.readyPoint();
        rotation = parent.nextRotation + dRotate;
        x = parent.nextPosition.x + vector.point.x;
        y = parent.nextPosition.y + vector.point.y;
    }

    public boolean contains(Point p) {
        return contains(new Point.Double(p.x, p.y));
    }

    public boolean contains(Point.Double p) {
        return false;
    }

    public void setParent(Object object) {
        parent = object;
        calcPosition();
    }

    public void calculateInertia() {
        mass = 1;
        inertia = 100;
    }
}
