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

    public static double calculateHypotenuse(Point.Double pa, Point.Double pb) {
        return Math.sqrt((pb.x - pa.x) * (pb.x - pa.x) + (pb.y - pa.y) * (pb.y - pa.y));
    }
    
    public static double getAngleBetween(Line a, Line b){
        double cx = Math.abs(a.origin.x-b.origin.x);
        double cy = Math.abs(a.origin.y-b.origin.y);     
        return Math.atan(cy/cx);
    }
    
    
}
