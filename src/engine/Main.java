package engine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class Main extends JFrame {

    //1 length unit = 1cm
    //1 mass unit = 1g
    //1 force unit = 1g*cm/s/s
    MyJPanel panel;

    public Main() {
        setTitle("TITLE");

        panel = new MyJPanel();

        //setUndecorated(true);
        //setOpacity((float) 0.9);
        panel.setPreferredSize(new Dimension(800, 600));
        panel.world.objects.add(new Object(new RectangleShape(100, 100, 100, 100, new Vector2D(new Point.Double(0, 0)), 0, 0, Color.BLUE), new Point.Double(100, 100)));
        //panel.world.addObject(new Object().addShapeReturn(new RectangleShape(100, 100, 100, 100, new Vector2D(new Point.Double(100, 100)), 0, 0, Color.BLUE)));
        panel.world.addPlane(new Plane(0, 500, 800, 500));
        setContentPane(panel);
        pack();

        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);
        java.awt.Shape shape = new Ellipse2D.Double(0, 0, 800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//        setShape(shape);
    }

    public class MyJPanel extends JPanel {

        World world;
        double dt = 0;

        public MyJPanel() {
            addKeyBindings();
            world = new World(10);

            OptionFrame optionFrame = new OptionFrame(this);
        }

        private void addKeyBindings() {
            char exit = KeyEvent.VK_ESCAPE;
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(exit), "exit");
            getActionMap().put("exit", exit());
        }

        private Action exit() {
            return new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            };
        }

        @Override
        public void update(Graphics g) {
            paintComponent(g);
        }

        @Override
        protected void paintComponent(Graphics g) {
            world.paint(g);
        }

    }

    public static void main(String[] args) {
        new Main();
        System.out.println(CollisionChecker.parallel(1, 5, -2, -4, 1, 5, -2, -4));
    }
}
