package engine;

import java.awt.Graphics;

/**
 *
 * @author PastaPojken
 */
public class Plane {

    Line surface;
    double friction;
    int ID;

    public Plane(Line line) {
        surface = line;
    }

    public Plane(double i, double i0, double i1, double i2) {
        surface = new Line(i, i0, i1, i2);
    }

    public void paint(Graphics g) {
        g.drawLine((int) surface.origin.x, (int) surface.origin.y, (int) surface.getEnd().x, (int) surface.getEnd().y);
    }
}
