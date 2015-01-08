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
        for (Object object : objects) {
            object.preUpdate(dt, g);
        }
        CollisionChecker.findNewCollisions(objects, planes, dt, g);
        for (Object object : objects) {
            object.endUpdate();
        }
        System.out.println("update done - delta time:" + dt + " - time: " + time);
    }

    public void paint(Graphics graphics) {
        graphics.clearRect(0, 0, 800, 600);
        graphics.setColor(Color.BLACK);
        for (Object object : objects) {
            for (Shape shape : object.shapes) {
                shape.paint(graphics, object);
            }
        }

        graphics.setColor(Color.BLACK);
        for (Plane plane : planes) {
            plane.paint(graphics);
        }
    }

    public void addObject(Object object) {
        objects.add(object);
    }

    public void addPlane(Plane plane) {
        planes.add(plane);
    }

}
