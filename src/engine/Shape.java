package engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class Shape {

    Vector2D vector;
    double deltaRotation; //relative to object's rotation
    double rotation; //actual rotation
    double mass;
    double density;
    double x, y;
    double inertia;
    Color color;
    Object parent;

    public Shape(Vector2D vec, double dRotation, double dens, Color col) {
        vector = vec;
        deltaRotation = dRotation;
        rotation = dRotation;
        density = dens;
        color = col;
    }

    public void rotate(double rad) {
        rotation += rad;
        vector.rotate(rad);
    }

    public void paint(Graphics2D g) {
        g.setColor(color);
    }

    public void calcPosition() {
        vector.readyPoint();
        rotation = parent.rotation + deltaRotation;
        x = parent.position.x + vector.point.x;
        y = parent.position.y + vector.point.y;
    }

    public void calcNextPosition() {
        vector.readyPoint();
        rotation = parent.nextRotation + deltaRotation;
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
