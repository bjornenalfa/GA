package engine;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author PastaPojken
 */
public class Object {
  Point.Double position;
  Vector2D massCenter; //relative to position
  Point.Double velocity;
  Double rotation;
  Double angularVelocity;
  ArrayList<Shape> shapes; //relative to position
  Double Mass;
  
}
