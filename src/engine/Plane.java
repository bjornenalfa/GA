package engine;

import java.awt.Graphics;

/**
 *
 * @author PastaPojken
 */
public class Plane {

    Line surface;
    double friction;
    double restitution=0.5;
    double mass=Double.POSITIVE_INFINITY;
    int ID;

    public Plane(Line line) {
        surface = line;
    }

    public Plane(double x1, double y1, double x2, double y2) {
        surface = new Line(x1, y1, x2, y2);
    }

    public void paint(Graphics g) {
        g.drawLine((int) surface.origin.x, (int) surface.origin.y, (int) surface.getEnd().x, (int) surface.getEnd().y);
    }

    public Vector2D getNormal() {
        return (surface.vector.getNormal());
    }
    
    public Vector2D getNormalizedNormal() {
        return (surface.vector.getNormal().normalize());
    }

    public void setRestitution(double res) {
        restitution = res;
    }

    public double getRestitution() {
        return restitution;
    }
}
