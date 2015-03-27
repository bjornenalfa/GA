package engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class RectangleShape extends Shape {

    int width, height;

    double middleToOrigindr;

    Vector2D middleToOrigin;

    Line[] lines = new Line[4];
    int[] polygonx = new int[4];
    int[] polygony = new int[4];

    public RectangleShape(int w, int h, Vector2D v, double r, double d, Color c) {
        super(v, r, d, c);
        middleToOrigin = new Vector2D(-w / 2, -h / 2);
        middleToOrigindr = middleToOrigin.getAngle();
        middleToOrigin.rotate(r);
        width = w;
        height = h;
        calculateInertia();
    }

    @Override
    public void calculateInertia() {
        mass = density * height * width;
        inertia = (width * width + height * height) * mass / 3.0;
    }

    public void calcLines() {
        middleToOrigin.readyPoint();
        Line top = new Line(new Point.Double(middleToOrigin.point.x + x, middleToOrigin.point.y + y), new Vector2D(width, rotation));
        lines[0] = top;
        Line left = new Line(new Point.Double(middleToOrigin.point.x + x, middleToOrigin.point.y + y), new Vector2D(height, rotation + Math.PI / 2));
        lines[1] = left;
        lines[2] = new Line(left.end, top.vector);
        lines[3] = new Line(top.end, left.vector);

        //FIX NORMALS
        lines[1].invertNormal();
        lines[2].invertNormal();

        polygonx[0] = (int) (top.origin.x);
        polygony[0] = (int) (top.origin.y);
        polygonx[1] = (int) (top.end.x);
        polygony[1] = (int) (top.end.y);
        polygonx[2] = (int) (lines[2].end.x);
        polygony[2] = (int) (lines[2].end.y);
        polygonx[3] = (int) (left.end.x);
        polygony[3] = (int) (left.end.y);
    }

    @Override
    public void calcNextPosition() {
        vector.readyPoint();
        rotation = parent.nextRotation + deltaRotation;
        middleToOrigin.rotate(rotation - (middleToOrigin.angle - middleToOrigindr));
        x = parent.nextPosition.x + vector.point.x;
        y = parent.nextPosition.y + vector.point.y;
        calcLines();
    }

    @Override
    public void rotate(double angle) {
        middleToOrigin.rotate(angle);
        rotation = rotation + angle;
        vector.rotate(angle);
    }

    @Override
    public void paint(Graphics2D g) {
        super.paint(g);
        g.drawPolygon(polygonx, polygony, 4);
        g.fillPolygon(polygonx, polygony, 4);
        if (!(parent instanceof FixedObject)) {
            g.setColor(Color.ORANGE);
            middleToOrigin.readyPoint();
            g.drawLine((int) x, (int) y, (int) (x + middleToOrigin.point.x), (int) (y + middleToOrigin.point.y));
        }
    }

    @Override
    public boolean contains(Point.Double p) {
        return CollisionChecker.pointInRectangleShape(this, p);
    }

    @Override
    public void setParent(Object object) {
        super.setParent(object);
        calcLines();
    }

}
