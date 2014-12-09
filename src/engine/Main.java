package engine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;

public class Main extends JFrame {

    //1 length unit = 1cm
    //1 mass unit = 1g
    //1 force unit = 1g*cm/s/s
    MyJPanel panel;

    public Main() {
        setTitle("TITLE");

        panel = new MyJPanel();

        setUndecorated(true);
        setOpacity((float) 0.9);

        panel.setPreferredSize(new Dimension(800, 600));
        panel.world.addObject(new Object().addShapeReturn(new RectangleShape(100, 100, 100, 100, new Vector2D(new Point.Double(100, 100)), 0, 0, Color.BLUE)));
        panel.world.addObject(new Object().addShapeReturn(new RectangleShape(100, 100, 100, 100, new Vector2D(new Point.Double(100, 100)), 0, 0, Color.BLUE)));
        panel.world.addPlane(new Plane(0, 500, 800, 500));
        setContentPane(panel);
//        setMenuBar(makeMenuBar());
        pack();

        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);
        java.awt.Shape shape = new Ellipse2D.Double(0, 0, 800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setShape(shape);
    }

    private MenuBar makeMenuBar() {
        Menu menu = new Menu("Add");
        menu.add(makeAddObject());

        MenuBar menuBar = new MenuBar();
        menuBar.add(menu);
        return menuBar;
    }

    Shape temp = new RectangleShape(1, 1, 1, 1, null, 1, 1, Color.GREEN);
    Shape shape = null;
    
    private MenuItem makeAddObject() {
        MenuItem addObject = new MenuItem("Add Object");
        addObject.setShortcut(new MenuShortcut(KeyEvent.VK_O, true));

        addObject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int chooseShape = JOptionPane.showConfirmDialog(null, radioPanel(), "", JOptionPane.OK_CANCEL_OPTION);
                if (chooseShape == JOptionPane.CANCEL_OPTION || chooseShape == JOptionPane.CLOSED_OPTION) {
                    return;
                } else if (chooseShape == JOptionPane.OK_OPTION) {
                     shape = temp;
                }
                
                panel.world.addObject(new Object());
            }
        });

        return addObject;
    }

    private JPanel radioPanel() {
        final JPanel radioPanel = new JPanel(new GridLayout(2, 1));
        ButtonGroup buttonGroup = new ButtonGroup();
        
        JRadioButton rectangle = rectangleChoice();
        JRadioButton circle = circleChoice();

        buttonGroup.add(rectangle);
        radioPanel.add(rectangle);
        buttonGroup.add(circle);
        radioPanel.add(circle);
        return radioPanel;
    }

    private JRadioButton rectangleChoice() {
        JRadioButton rectangle = new JRadioButton("Rectangle");
        rectangle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                temp = new RectangleShape(1, 1, 1, 1, null, 1, 1, Color.GREEN);
            }
        });
        rectangle.setSelected(true);
        return rectangle;
    }
    
    private JRadioButton circleChoice() {
        JRadioButton circle = new JRadioButton("Circle");
        circle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                temp = new CircleShape(1, 1, 1, null, 1, 1, Color.GREEN);
            }
        });
        return circle;
    }

    class MyJPanel extends JPanel {

        World world;

        public MyJPanel() {
            addKeyBindings();
            world = new World(9.82);
            JButton button = new JButton("Update");
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    world.update(0.01);
                }
            });
            add(button);
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
            world.paint(g);

        }

    }

    public static void main(String[] args) {
        new Main();
        System.out.println(CollisionChecker.parallel(1, 5, -2, -4, 1, 5, -2, -4));
    }
}
