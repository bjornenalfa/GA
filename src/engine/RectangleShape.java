package engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

/**
 * Rectangular shape, has width and height;
 *
 *
 * @author PastaPojken
 */
public class RectangleShape extends Shape {

    int w, h; // Width and Height

    //ArrayList<Line> lines = new ArrayList(4);
    Line[] lines = new Line[4];

    public RectangleShape(int x, int y, int w, int h, Vector2D v, double r, double m, Color c) {
        super(x, y, v, r, m, c);
        this.w = w;
        this.h = h;
    }

    public void calcLines() {
        Line top = new Line(new Point.Double(x, y), new Vector2D(w, rotation));
        lines[0] = top;
        Line left = new Line(new Point.Double(x, y), new Vector2D(h, rotation + Math.PI / 2));
        lines[1] = left;
        lines[2] = new Line(left.vector.getPoint(), top.vector);
        lines[3] = new Line(top.vector.getPoint(), left.vector);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.fillRect(x, y, w, h);
        g.drawRect(x, y, w, h);
    }

}
