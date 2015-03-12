package engine;

import java.awt.Color;
import java.awt.Point;

/**
 *
 * @author bjodet982
 */
public class FixedObject extends Object {


    FixedObject(Shape shape, Point.Double pos, int mat) {
        super(shape, pos, mat);
        inverseInertia = 0d;
    }

    FixedObject(int x1, int y1, int x2, int y2, int mat) {
        super(new RectangleShape(Math.abs(x2 - x1), Math.abs(y2 - y1), new Vector2D(0, 0), 0, Double.POSITIVE_INFINITY, Color.DARK_GRAY), new Point.Double((x1 + x2) / 2, (y1 + y2) / 2), mat);
        inverseInertia = 0d;
    }
    
    FixedObject(int x1, int y1, int x2, int y2, int height, int mat) {
        super(new RectangleShape((int)Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1)), height, new Vector2D(0, 0), Math.atan2(y2-y1,x2-x1), Double.POSITIVE_INFINITY, Color.DARK_GRAY), new Point.Double((x1 + x2) / 2, (y1 + y2) / 2), mat);
        inverseInertia = 0d;
    }

    @Override
    public void preUpdate(double dt, Vector2D g) {
        nextPosition = position;
        nextVelocity = new Vector2D(0, 0);
        nextAngularVelocity = 0;
        nextRotation = rotation;

        for (Shape shape : shapes) {
            shape.calcNextPosition();
        }
    }

    @Override
    public void endUpdate() {
    }

    @Override
    public void applyImpulse(Vector2D impulse, Vector2D contactVector) {
    }
}
