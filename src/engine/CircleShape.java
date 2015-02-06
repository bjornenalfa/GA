package engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 *
 * @author bjodet982
 */
public class CircleShape extends Shape {

    double radius; //radius of shape

    public CircleShape(double rad, Vector2D v, double r, double m, Color c) {
        super(v, r, m, c);
        radius = rad;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        vector.readyPoint();
        g.fillOval((int) (parent.position.x + vector.point.x-this.radius), (int) (parent.position.y + vector.point.y-this.radius), (int) radius*2, (int) radius*2);
        g.drawOval((int) (parent.position.x + vector.point.x-this.radius), (int) (parent.position.y + vector.point.y-this.radius), (int) radius*2, (int) radius*2);
    }

    @Override
    public boolean contains(Point.Double p) {
        return p.distanceSq(x, y) < radius * radius;
    }

}
