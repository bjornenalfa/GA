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
        double firstSlope = ((by - ay) / (bx - ax));
        double secondSlope = ((dy - cy) / (dx - cx));

        return firstSlope == secondSlope;
    }

    public static boolean parallel(Line a, Line b) {
        double firstSlope = (a.vector.getPoint().y) / (a.vector.getPoint().x);
        double secondSlope = (b.vector.getPoint().y) / (b.vector.getPoint().x);

        return firstSlope == secondSlope;
    }

    public static boolean areTheyAlmostTouching(Line a, Line b, double limit, double slopeLimit) {
        double firstSlope = a.vector.getAngle() % Math.PI;
        double secondSlope =  b.vector.getAngle() % Math.PI;
        return (Math.abs(firstSlope-secondSlope))<=slopeLimit && new Vector2D(a.origin.x - b.origin.x, a.origin.y - b.origin.y).rotate(a.vector.getAngle()).getPoint().y <= limit;
    }

    public static boolean isn(double num) {
        return num == Math.abs(num);
    }
}
