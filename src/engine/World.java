package engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
    ArrayList<Line> impulses = new ArrayList();

    public World(Vector2D gravity) {
        this.gravity = gravity;
    }

    int followID = 0;
    double followX = 0, followY = 0;
    boolean follow = false;

    public void update(double dt) {
        time += dt;
        if (follow) {
            followX = objects.get(followID).position.x;
            followY = objects.get(followID).position.y;
        }
        for (Object object : objects) {
            object.preUpdate(dt, gravity);
        }
        CollisionChecker.findNewCollisions(objects, planes, dt, gravity, this);
        for (Object object : objects) {
            object.endUpdate();
        }
        if (follow) {
            Main.translateX -= (objects.get(followID).position.x - followX);
            Main.translateY -= (objects.get(followID).position.y - followY);
        }
        System.out.println("update done - delta time:" + dt + " - time: " + time);
    }

    public void paint(Graphics2D g) {
//        g.clearRect(0, 0, 1920, 1080);
        g.setColor(Color.BLACK);
        for (Object object : objects) {
            object.paint(g);
            
        }

        g.setColor(Color.BLACK);
        for (Plane plane : planes) {
            plane.paint(g);
        }
        
        for (Line l : impulses) {
            l.paint(g);
        }
    }

    public void addObject(Object object) {
        objects.add(object);
    }

    public void addPlane(Plane plane) {
        planes.add(plane);
    }

}
