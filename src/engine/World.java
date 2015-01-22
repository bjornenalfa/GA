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
    Vector2D gravity;
    ArrayList<Plane> planes = new ArrayList();

    public World(Vector2D gravity) {
        this.gravity = gravity;
    }

    public void update(double dt) {
        time += dt;
        for (Object object : objects) {
            object.preUpdate(dt, gravity);
        }
        CollisionChecker.findNewCollisions(objects, planes, dt, gravity);
        for (Object object : objects) {
            object.endUpdate();
        }
        System.out.println("update done - delta time:" + dt + " - time: " + time);
    }

    public void paint(Graphics g) {
//        g.clearRect(0, 0, 1920, 1080);
        g.setColor(Color.BLACK);
        for (Object object : objects) {
            for (Shape shape : object.shapes) {
                shape.paint(g);
            }
        }

        g.setColor(Color.BLACK);
        for (Plane plane : planes) {
            plane.paint(g);
        }
    }

    public void addObject(Object object) {
        objects.add(object);
    }

    public void addPlane(Plane plane) {
        planes.add(plane);
    }

}
