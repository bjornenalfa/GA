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

    Vector2D middleToOrigin;

    //ArrayList<Line> lines = new ArrayList(4);
    Line[] lines = new Line[4];
    int[] polygonx = new int[4];
    int[] polygony = new int[4];

    public RectangleShape(int w, int h, Vector2D v, double r, double m, Color c) {
        super(v, r, m, c);
        middleToOrigin = new Vector2D(-w / 2, -h / 2);
        middleToOrigin.rotate(r);
        vector.add(middleToOrigin);
        width = w;
        height = h;
        calculateI();
    }
    
    /**
     * Räknar ut tröghetsmomentet
     */
    @Override
    public void calculateI() {
        I = (1.0/12.0) * mass * 0.001 * (width*width*0.0001 + height*height*0.0001);
    }

    public void calcLines() { //Calculated from the top left corner :>
        Line top = new Line(new Point.Double(x, y), new Vector2D(width, rotation));
        lines[0] = top;
        Line left = new Line(new Point.Double(x, y), new Vector2D(height, rotation + Math.PI / 2));
        lines[1] = left;
        lines[2] = new Line(left.end, top.vector);
        lines[3] = new Line(top.end, left.vector);

        polygonx[0] = (int) top.origin.x;
        polygony[0] = (int) top.origin.y;
        polygonx[1] = (int) top.end.x;
        polygony[1] = (int) top.end.y;
        polygonx[2] = (int) lines[2].end.x;
        polygony[2] = (int) lines[2].end.y;
        polygonx[3] = (int) left.end.x;
        polygony[3] = (int) left.end.y;
    }

    @Override
    public void calcNextPosition() {
        vector.readyPoint();
        x = parent.nextPosition.x + vector.point.x;
        y = parent.nextPosition.y + vector.point.y;
        calcLines();
    }

    @Override
    public void rotate(double angle) {
        middleToOrigin.rotate(angle);
        rotation += angle;
        vector.rotate(angle);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        calcLines();

        //g.fillRect((int) (o.position.x + vector.point.x), (int) (o.position.y + vector.point.y), width, height);
        //g.drawRect((int) (o.position.x + vector.point.x), (int) (o.position.y + vector.point.y), width, height);
        //g.setColor(Color.RED);
        /*for (Line line : lines ) {
         g.drawLine((int)(line.origin.x), (int)(line.origin.y), (int)(line.end.x), (int)(line.end.y));
         }*/
        g.drawPolygon(polygonx, polygony, 4);
        g.fillPolygon(polygonx, polygony, 4);
    }

    @Override
    public boolean contains(Point.Double p) {
        return CollisionChecker.pointInRectangleShape(this, p);
    }

}
