package engine;

import java.awt.Graphics;

/**
 *
 * @author PastaPojken
 */
public class Plane {

    Line surface;
    double restitution = 0.5;
    double mass = Double.POSITIVE_INFINITY;
    int ID;
    Vector2D normal;
    int material = Material.Wood;

    public Plane(Line line) {
        surface = line;
        normal = surface.vector.getCounterClockwiseNormal().normalize();
    }

    public Plane(double x1, double y1, double x2, double y2) {
        surface = new Line(x1, y1, x2, y2);
        normal = surface.vector.getCounterClockwiseNormal().normalize();
    }

    public void paint(Graphics g) {
        g.drawLine((int) surface.origin.x, (int) surface.origin.y, (int) surface.getEnd().x, (int) surface.getEnd().y);
    }

    public Vector2D getNormal() {
        return (surface.vector.getCounterClockwiseNormal());
    }

    public Vector2D getNormalizedNormal() {
        return normal;
    }

    public void setRestitution(double res) {
        restitution = res;
    }

    public double getRestitution() {
        return restitution;
    }
}
