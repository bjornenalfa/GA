package engine;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 *
 * @author PastaPojken
 */
public class World {
    ArrayList<Object> objects;
    double time = 0;
    double g;

    public World(double gravity) {
        g = gravity;
    }

    public void update() {
        
    }

    public void paint(Graphics paint) {
        paint.setColor(Color.BLACK);
        for (Object obj : objects) {
            for (Shape shape : obj.shapes) {
                shape.paint(paint);
            }
        }
    }

    public void addObj(Object obj) {
        objects.add(obj);
    }

}
