package engine;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author bjodet982
 */
public class CircleShape extends Shape {

    int radius; //radius of shape

    public CircleShape(double x, double y, int rad, Vector2D v, double r, double m, Color c) {
        super(x, y, v, r, m, c);
        radius = rad;
    }

    @Override
    public void paint(Graphics g, Object o) {
        super.paint(g, o);
        g.fillOval((int) x, (int) y, radius, radius);
        g.drawOval((int) x, (int) y, radius, radius);
    }

}
