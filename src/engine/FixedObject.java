package engine;

import java.awt.Point;

/**
 *
 * @author bjodet982
 */
public class FixedObject extends Object {

    FixedObject(Shape shape, Point.Double pos, int mat) {
        super(shape, pos, mat);
        inverseMass = 0d;
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
