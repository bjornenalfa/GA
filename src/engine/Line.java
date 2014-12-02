package engine;

import java.awt.Point;

/**
 *
 * @author bjodet982
 */
public class Line {
    Point.Double origin;
    Vector2D vector;
    
    public Line(Point.Double orig,Vector2D vec) {
        origin = orig;
        vector = vec;
    }
}
