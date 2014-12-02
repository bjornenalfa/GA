package engine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class Main extends JFrame {
    //1 length unit = 1cm
    //1 mass unit = 1g
    //1 force unit = 1g*cm/s/s
    public Main() {
        setTitle("TITLE");

        MyJPanel panel = new MyJPanel();
        panel.setPreferredSize(new Dimension(800, 600));
        panel.w.addObj(new Object().addShapeReturn(new RectangleShape(100, 100, 100, 100, new Vector2D(new Point.Double(100, 100)), 0, 0, Color.BLUE)));
        panel.w.addObj(new Object().addShapeReturn(new RectangleShape(100, 100, 100, 100, new Vector2D(new Point.Double(100, 100)), 0, 0, Color.BLUE)));
        setContentPane(panel);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setVisible(true);
    }

    class MyJPanel extends JPanel {

        World w;

        public MyJPanel() {
            addKeyBindings();
            w = new World(9.82);
        }

        private void addKeyBindings() {
            char exit = KeyEvent.VK_ESCAPE;
            getInputMap().put(KeyStroke.getKeyStroke(exit), "exit");
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
            w.paint(g);

        }

    }

    public static void main(String[] args) {
        new Main();
        System.out.println(CollisionChecker.parallel(1, 5, -2, -4, 1, 5, -2, -4));
    }
}
