package engine;

import java.awt.Graphics;

/**
 *
 * @author PastaPojken
 */
public class Shape {

    Vector2D vector; //Anchor in object's position
    double rotation; //relative to object's rotation
    double mass;//mass in Kg
    int x, y; // X, Y coordinates; Width and Height

    /**
     * Basic "Shape" constructor
     *
     * @param v - vector
     * @param r - rotation, in radians
     * @param m - mass, Kg
     */
    public Shape(Vector2D v, double r, double m) {
        vector = v;
        rotation = r;
        mass = m;
    }

    public Shape(int x, int y, Vector2D v, double r, double m) {
        vector = v;
        rotation = r;
        mass = m;
        this.x = x;
        this.y = y;
    }

    /**
     * Rotates shape
     * @param rad - radians
     */
    public void rotate(double rad) {
        rotation+=rad;
    }

    public void paint(Graphics g) {

    }

}
