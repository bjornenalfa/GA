package engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 *
 * @author PastaPojken
 */
public class Force {

    Vector2D vector;
    Point.Double attackPoint;

    public Force(Point.Double attackPoint, Vector2D vector) {
        this.attackPoint = attackPoint;
        this.vector = vector;
    }

    public void paint(Graphics g) {
        g.setColor(Color.RED);
        double scale = 1;
        g.drawLine((int) attackPoint.x, (int) attackPoint.y, (int) (vector.getPoint().x * scale), (int) (vector.getPoint().y * scale));
    }

    public double getAcc(double m) {
        return vector.getLength() / m;
    }

    public double getForce() {
        return vector.getLength();
    }
}
