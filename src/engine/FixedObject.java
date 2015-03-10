package engine;

import java.awt.Color;
import java.awt.Point;

/**
 *
 * @author bjodet982
 */
public class FixedObject extends Object {

    static final double inverseMass = 0d;
    
    FixedObject(Shape shape, Point.Double pos, int mat) {
        super(shape, pos, mat);
    }

    FixedObject(int x1, int y1, int x2, int y2, int mat) {
        super(new RectangleShape(Math.abs(x2-x1),Math.abs(y2-y1), new Vector2D(0,0), 0, 0, Color.DARK_GRAY), new Point.Double((x1+x2)/2, (y1+y2)/2), mat);
    }
    
    @Override
    public void preUpdate(double dt, Vector2D g) {
        nextPosition = position;
        nextVelocity = new Vector2D(0,0);
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
