package engine;

import java.awt.Point;

/**
 *
 * @author bjodet982
 */
public class Line {

    Point.Double origin;
    Vector2D vector;

    public Line(Point.Double orig, Vector2D vec) {
        origin = orig;
        vector = vec;
    }
    
    public Line(int i, int i0, int i1, int i2) {
        origin = new Point.Double(i,i0);
        vector = new Vector2D(i1-i,i2-i0);
    }
    
    public Point.Double getEnd() {
        return new Point.Double(origin.x+vector.getPoint().x,origin.y+vector.getPoint().y);
    }

    public static double calculateHypotenuse(Point.Double pa, Point.Double pb) {
        return Math.sqrt((pb.x - pa.x) * (pb.x - pa.x) + (pb.y - pa.y) * (pb.y - pa.y));
    }
    
    public static double getAngleBetween(Line a, Line b){
        double cx = Math.abs(a.origin.x-b.origin.x);
        double cy = Math.abs(a.origin.y-b.origin.y);     
        return Math.atan2(cy,cx);
    }

}
