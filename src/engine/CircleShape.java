package engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 *
 * @author bjodet982
 */
public class CircleShape extends Shape {

    int radius; //radius of shape

    public CircleShape(int rad, Vector2D v, double r, double m, Color c) {
        super(v, r, m, c);
        radius = rad;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.fillOval((int) (parent.position.x + vector.point.x), (int) (parent.position.y + vector.point.y), radius, radius);
        g.drawOval((int) (parent.position.x + vector.point.x), (int) (parent.position.y + vector.point.y), radius, radius);
    }

    @Override
    public boolean contains(Point.Double p) {
        return p.distanceSq(x, y) < radius * radius;
    }

}
