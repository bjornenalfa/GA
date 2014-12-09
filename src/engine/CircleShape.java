package engine;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author bjodet982
 */
public class CircleShape extends Shape {

    int radius; //radius of shape

    public CircleShape(int x, int y, int rad, Vector2D v, double r, double m, Color c) {
        super(x, y, v, r, m, c);
        radius = rad;
    }

    @Override
    public void paint(Graphics g, Object o) {
        super.paint(g, o);
        g.fillOval(x, y, radius, radius);
        g.drawOval(x, y, radius, radius);
    }

}
