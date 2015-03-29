package engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public class World {

    ArrayList<Object> objects = new ArrayList();
    double time = 0;
    Vector2D gravity;
    ArrayList<Line> impulses = new ArrayList();
    ArrayList<Line> pImpulses = new ArrayList();
    ArrayList<Line> normals = new ArrayList();
    ArrayList<Line> pNormals = new ArrayList();
    ArrayList<Point.Double> points = new ArrayList();
    ArrayList<Point.Double> pPoints = new ArrayList();
    public ArrayList<ClickFrame> clickFrameList = new ArrayList();

    boolean showImpulses = true;
    boolean showNormals = true;
    boolean showCollisionPoints = true;

    public World(Vector2D gravity) {
        this.gravity = gravity;
    }

    int followID = 0;
    double followX = 0, followY = 0;
    boolean follow = true;

    public void update(double dt) {
        time += dt;
        if (follow) {
            followX = objects.get(followID).position.x;
            followY = objects.get(followID).position.y;
        }
        for (Object object : objects) {
            object.preUpdate(dt, gravity);
        }
        CollisionChecker.findNewCollisions(objects, dt, gravity, this);
        for (Object object : objects) {
            object.endUpdate();
        }
        for (ClickFrame clickFrame : clickFrameList) {
            clickFrame.setLabelTexts();
        }
        if (follow) {
            Main.translateX -= (objects.get(followID).position.x - followX);
            Main.translateY -= (objects.get(followID).position.y - followY);
        }
    }

    public void paint(Graphics2D g) {
        g.setColor(Color.BLACK);
        for (Object object : objects) {
            object.paint(g);
        }

        while (impulses.size() >= 500) {
            impulses.remove(0);
        }
        
        if (showImpulses) {
            g.setColor(new Color(0.3f, 0.3f, 1f, 1f));
            pImpulses = (ArrayList<Line>) impulses.clone();
            for (Line l : pImpulses) {
                l.paint(g);
            }
        }

        while (normals.size() >= 100) {
            normals.remove(0);
        }

        if (showNormals) {
            g.setColor(new Color(0.3f, 1f, 0.3f, 1f));
            pNormals = (ArrayList<Line>) normals.clone();
            for (Line l : pNormals) {
                l.paint(g);
            }
        }
        
        if (showCollisionPoints) {
            g.setColor(new Color(1f, 0.7f, 0.1f, 1f));
            pPoints = (ArrayList<Point.Double>) points.clone();
            for (Point.Double p : pPoints) {
                g.drawRect((int) (p.x), (int) (p.y), 0, 0);
            }
        }
    }

    public void addObject(Object object) {
        objects.add(object);
    }

}
