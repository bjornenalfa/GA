package engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 * Rectangular shape, has width and height;
 *
 *
 * @author PastaPojken
 */
public class RectangleShape extends Shape {

    int width, height; // Width and Height

    //ArrayList<Line> lines = new ArrayList(4);
    Line[] lines = new Line[4];

    public RectangleShape(double x, double y, int w, int h, Vector2D v, double r, double m, Color c) {
        super(x, y, v, r, m, c);
        width = w;
        height = h;
    }
    
    public void calcLines() {
        Line top = new Line(new Point.Double(x, y), new Vector2D(width, rotation));
        lines[0] = top;
        Line left = new Line(new Point.Double(x, y), new Vector2D(height, rotation + Math.PI / 2));
        lines[1] = left;
        lines[2] = new Line(left.end, top.vector);
        lines[3] = new Line(top.end, left.vector);
    }

    @Override
    public void paint(Graphics g, Object o) {
        super.paint(g, o);
        calcLines();
        
        g.fillRect((int) (o.position.x + vector.point.x), (int) (o.position.y + vector.point.y), width, height);
        g.drawRect((int) (o.position.x + vector.point.x), (int) (o.position.y + vector.point.y), width, height);
        
        g.setColor(Color.RED);
        for (Line line : lines ) {
            g.drawLine((int)(line.origin.x), (int)(line.origin.y), (int)(line.end.x), (int)(line.end.y));
        }
    }
    
    @Override
    public boolean contains(Point.Double p) {
        return CollisionChecker.pointInRectangleShape(this, p);
    }

}
