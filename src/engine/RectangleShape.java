package engine;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Rectangular shape, has width and height;
 *
 *
 * @author PastaPojken
 */
public class RectangleShape extends Shape {

    int w, h; // Width and Height

    public RectangleShape(int x, int y, int w, int h, Vector2D v, double r, double m, Color c) {
        super(x, y, v, r, m, c);
        this.w = w;
        this.h = h;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.fillRect(x, y, w, h);
        g.drawRect(x, y, w, h);
    }

}
