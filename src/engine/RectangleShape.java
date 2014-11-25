package engine;

import java.awt.Graphics;

/**
 * Rectangular shape, has width and height;
 *
 *
 * @author PastaPojken
 */
public class RectangleShape extends Shape {

    int x, y, w, h; // X, Y coordinates; Width and Height

    public RectangleShape(Vector2D v, double r, double m) {
        super(v, r, m);
    }

    public RectangleShape(int x, int y, int w, int h, Vector2D v, double r, double m) {
        super(x, y, v, r, m);
        this.w = w;
        this.h = h;
    }

    public void paint(Graphics g) {

    }

}
