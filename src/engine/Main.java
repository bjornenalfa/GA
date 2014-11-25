package engine;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JFrame {

    public Main() {
        setTitle("TITLE");

        MyJPanel panel = new MyJPanel();
        panel.setPreferredSize(new Dimension(800, 600));
        panel.shapeList.add(new RectangleShape(100, 100, 100, 100, new Vector2D(), 0, 0));
        panel.shapeList.add(new CircleShape(10, 10, 100, new Vector2D(), 0, 0));
        setContentPane(panel);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setVisible(true);
    }

    class MyJPanel extends JPanel {

        ArrayList<Shape> shapeList = new ArrayList<>();

        @Override
        public void update(Graphics g) {
            paintComponent(g);
        }

        @Override
        protected void paintComponent(Graphics g) {
            for (Shape shape : shapeList) {
                shape.paint(g);
            }
        }

    }

    public static void main(String[] args) {
        Main main = new Main();
    }
}
