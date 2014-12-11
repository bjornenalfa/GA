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

    public Plane(double x1, double y1, double x2, double y2) {
        surface = new Line(x1, y1, x2, y2);
    }

    public void paint(Graphics g) {
        g.drawLine((int) surface.origin.x, (int) surface.origin.y, (int) surface.getEnd().x, (int) surface.getEnd().y);
    }
}
