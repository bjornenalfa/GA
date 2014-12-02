package engine;

/**
 *
 * @author bjodet982
 */
public class CollisionChecker {
    
    public static boolean intersect(double px,double py,double qx,double qy,double ex,double ey,double fx,double fy) { // CALCULATE INTERSECTIONS
	/*double cp = (fx-ex)*(py-fy)-(fy-ey)*(px-fx);
	double cq = (fx-ex)*(qy-fy)-(fy-ey)*(qx-fx);
	double ce = (px-qx)*(ey-py)-(py-qy)*(ex-px);
	double cf = (px-qx)*(fy-py)-(py-qy)*(fx-px);
	return (isn(cp) != isn(cq)) && (isn(ce) != isn(cf));*/
        return (isn((fx-ex)*(py-fy)-(fy-ey)*(px-fx)) != isn((fx-ex)*(qy-fy)-(fy-ey)*(qx-fx))) && (isn((px-qx)*(ey-py)-(py-qy)*(ex-px)) != isn((px-qx)*(fy-py)-(py-qy)*(fx-px)));
    }

    public static boolean isn(double num) {
        return num == Math.abs(num);
    }
}
