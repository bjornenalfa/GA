package engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Line2D;

/**
 *
 * @author PastaPojken
 */
public class Shape {

    Vector2D vector; //Anchor in object's position
    double rotation; //relative to object's rotation
    double mass;//mass in g
    double x, y; // X, Y coordinates; Width and Height
    Color myC; //Shape color    

    /**
     * Basic "Shape" constructor
     *
     * @param x - X coordinate
     * @param y - Y coordinate
     * @param v - vector
     * @param r - rotation, in radians
     * @param m - mass, g
     * @param c - color
     */
    public Shape(double x, double y, Vector2D v, double r, double m, Color c) {
        vector = v;
        //vector.readyPoint();
        rotation = r;
        mass = m;
        myC = c;
        this.x = x;
        this.y = y;
    }

    /**
     * Rotates shape
     *
     * @param rad - radians
     */
    public void rotate(double rad) {
        rotation += rad;
    }

    /**
     * Draws shape
     *
     * @param g - Graphics
     */
    public void paint(Graphics g, Object o) {
        g.setColor(myC);
    }

    public void calcPosition(Object o) {
        x = o.position.x + vector.point.x;
        y = o.position.y + vector.point.y;
    }

    public void calcNextPosition(Object o) {
        x = o.nextPosition.x + vector.point.x;
        y = o.nextPosition.y + vector.point.y;
    }
    
    public boolean contains(Point p) {
        return contains(new Point.Double(p.x,p.y));
    }
    
    public boolean contains(Point.Double p) {
        return false;
    }

    //If needed implement different code for each different shape
   /* public boolean contains(Point p) {
        return contains(p.x, p.y);
    }

    public boolean contains(Point.Double p) {
        return contains(p.x, p.y);
    }

    public boolean contains(double px, double py) {
        Line2D linje = new Line2D.Double(px, py, 9999, 9999);
        RectangleShape sh = (RectangleShape) this;
        sh.calcLines();
        //Line2D[] newLines= new Line2D.Double[4];
        int count = 0;
        for (int i = 0; i < sh.lines.length; i++) {
            //newLines[i] = new Line2D.Double(sh.lines[i].origin, sh.lines[i].end);
            if (linje.intersectsLine(sh.lines[i].origin.x, sh.lines[i].origin.y, sh.lines[i].end.x, sh.lines[i].end.y)) {
                count++;
            }
        }
        return count % 2 != 0;
    }*/

}
