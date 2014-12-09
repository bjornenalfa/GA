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

    public Plane(int i, int i0, int i1, int i2) {
        surface = new Line(i, i0, i1, i2);
    }

    public void paint(Graphics g) {
        g.drawLine((int) surface.origin.x, (int) surface.origin.y, (int) surface.getEnd().x, (int) surface.getEnd().y);
    }
}
