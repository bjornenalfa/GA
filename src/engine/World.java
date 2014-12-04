package engine;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 *
 * @author PastaPojken
 */
public class World {

    ArrayList<Object> objects = new ArrayList();
    double time = 0;
    double g;
    ArrayList<Plane> planes = new ArrayList();

    public World(double gravity) {
        g = gravity;
    }

    public void update(double dt) {
        time += dt;
        for (Object obj : objects) {
            obj.update(dt, g);
        }
    }

    public void paint(Graphics graphics) {
        graphics.setColor(Color.BLACK);
        for (Object object : objects) {
            for (Shape shape : object.shapes) {
                shape.paint(graphics);
            }
        }

        graphics.setColor(Color.BLACK);
        for (Plane plane : planes) {
            plane.paint(graphics);
        }
    }

    public void addObject(Object obj) {
        objects.add(obj);
    }

    public void addPlane(Plane plane) {
        planes.add(plane);
    }

}
