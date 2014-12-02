package engine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class Main extends JFrame {

    public Main() {
        setTitle("TITLE");

        MyJPanel panel = new MyJPanel();
        panel.setPreferredSize(new Dimension(800, 600));
        panel.shapeList.add(new RectangleShape(100, 100, 100, 100, new Vector2D(), 0, 0, Color.BLUE));
        panel.shapeList.add(new CircleShape(10, 10, 100, new Vector2D(), 0, 0, Color.YELLOW));
        setContentPane(panel);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setVisible(true);
    }

    class MyJPanel extends JPanel {

        ArrayList<Shape> shapeList = new ArrayList<>();

        public MyJPanel(){
            addKeyBindings();
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
            g.setColor(Color.BLACK);
            for (Shape shape : shapeList) {
                shape.paint(g);
            }
        }

    }

    public static void main(String[] args) {
        new Main();
    }
}
