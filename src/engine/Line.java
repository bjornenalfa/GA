package engine;

import java.awt.Graphics2D;
import java.awt.Point;

public class Line {

    Point.Double origin;
    Point.Double end;
    Vector2D vector;
    Vector2D normal;

    public Line(Point.Double orig, Vector2D vec) {
        origin = orig;
        end = new Point.Double(orig.x + vec.getPoint().x, orig.y + vec.getPoint().y);
        vector = vec;
        normal = vector.getCounterClockwiseNormal().normalize();
    }

    public Line(double x1, double y1, double x2, double y2) {
        origin = new Point.Double(x1, y1);
        end = new Point.Double(x2, y2);
        vector = new Vector2D(new Point.Double(x2 - x1, y2 - y1));
        normal = vector.getCounterClockwiseNormal().normalize();
    }

    public Point.Double getEnd() {
        return new Point.Double(origin.x + vector.getPoint().x, origin.y + vector.getPoint().y);
    }

    public static double calculateHypotenuse(Point.Double pa, Point.Double pb) {
        return Math.sqrt((pb.x - pa.x) * (pb.x - pa.x) + (pb.y - pa.y) * (pb.y - pa.y));
    }

    public static double getAngleBetween(double dx, double dy) {
        return Math.atan2(dy, dx);
    }

    public void invertNormal() {
        normal.multiply(-1);
    }

    public void paint(Graphics2D g) {
        double angle = Math.atan2(origin.y - end.y, origin.x - end.x);
        g.drawLine((int) origin.x, (int) origin.y, (int) end.x, (int) end.y);
        g.drawLine((int) end.x, (int) end.y, (int) (end.x + Math.cos(angle + 0.3) * 5), (int) (end.y + Math.sin(angle + 0.2) * 5));
        g.drawLine((int) end.x, (int) end.y, (int) (end.x + Math.cos(angle - 0.3) * 5), (int) (end.y + Math.sin(angle - 0.2) * 5));
    }

}
