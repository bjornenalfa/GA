package engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class CircleShape extends Shape {

    double radius; //radius of shape

    public CircleShape(double rad, Vector2D v, double r, double d, Color c) {
        super(v, r, d, c);
        radius = rad;
    }

    @Override
    public void calculateInertia() {
        mass = radius * radius * Math.PI * density;
        inertia = radius * radius * mass;
    }

    @Override
    public void paint(Graphics2D g) {
        super.paint(g);
        vector.readyPoint();
        g.fillOval((int) (x - radius), (int) (y - radius), (int) radius * 2, (int) radius * 2);
        g.drawOval((int) (x - radius), (int) (y - radius), (int) radius * 2, (int) radius * 2);
        g.setColor(Color.BLACK);
        g.drawLine((int) (x), (int) (y), (int) (x + Math.cos(rotation) * radius), (int) (y + Math.sin(rotation) * radius));
    }

    @Override
    public boolean contains(Point.Double p) {
        return p.distanceSq(x, y) < radius * radius;
    }

}
