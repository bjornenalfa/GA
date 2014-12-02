package engine;

/**
 *
 * @author bjodet982
 */
public class CollisionChecker {

    public static boolean intersect(double px, double py, double qx, double qy, double ex, double ey, double fx, double fy) { // CALCULATE INTERSECTIONS
	/*double cp = (fx-ex)*(py-fy)-(fy-ey)*(px-fx);
         double cq = (fx-ex)*(qy-fy)-(fy-ey)*(qx-fx);
         double ce = (px-qx)*(ey-py)-(py-qy)*(ex-px);
         double cf = (px-qx)*(fy-py)-(py-qy)*(fx-px);
         return (isn(cp) != isn(cq)) && (isn(ce) != isn(cf));*/
        return (isn((fx - ex) * (py - fy) - (fy - ey) * (px - fx)) != isn((fx - ex) * (qy - fy) - (fy - ey) * (qx - fx))) && (isn((px - qx) * (ey - py) - (py - qy) * (ex - px)) != isn((px - qx) * (fy - py) - (py - qy) * (fx - px)));
    }

    public static boolean parallel(double ax, double ay, double bx, double by, double cx, double cy, double dx, double dy) {
        double first_slope = ((by - ay) / (bx - ax));
        double second_slope = ((dy - cy) / (dx - cx));

        if (first_slope == second_slope) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean areTheyAlmostTouching(Line a, Line b, double limit) {
        if (new Vector2D(Line.calculateHypotenuse(a.origin, b.origin), a.vector.getAngle()).getPoint().y <= limit) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isn(double num) {
        return num == Math.abs(num);
    }
}
