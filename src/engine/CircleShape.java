package engine;

import java.awt.Graphics;

/**
 *
 * @author bjodet982
 */
public class CircleShape extends Shape {

    int radius; //radius of shape

    public CircleShape(Vector2D v, double r, double m) {
        super(v, r, m);
    }

    public CircleShape(int x, int y, int rad, Vector2D v, double r, double m) {
        super(x, y, v, r, m);
        radius = rad;
    }

    public void paint(Graphics g) {

    }

}
