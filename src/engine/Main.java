package engine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
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
    boolean keyDownControl = false;
    double scale = 1, mouseX = 0, mouseY = 0;

    public Main() {
        setTitle("TITLE");

        panel = new MyJPanel();

        //setUndecorated(true);
        //setOpacity((float) 0.9);
        panel.setPreferredSize(new Dimension(800, 600));
        panel.world.objects.add(new Object(new RectangleShape(100, 100, new Vector2D(new Point.Double(0, 0)), 0, 0, Color.BLUE), new Point.Double(100, 100)));
        //panel.world.objects.get(0).shapes.get(0).setParent(panel.world.objects.get(0));
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

    private void moveWorld(World w, double x, double y) {
        for (Object o : w.objects) {
            o.position.x -= x;
            o.position.y -= y;
        }
        for (Plane o : w.planes) {
            o.surface.origin.x -= x;
            o.surface.end.x -= x;
            o.surface.origin.y -= y;
            o.surface.end.y -= y;
        }
        repaint();
    }

    private void resizeWorld(World w, double res) {
        scale -= (res / 10);
        if (scale < 0.1) {
            scale = 0.1;
        }
        repaint();
    }

    public class MyJPanel extends JPanel {

        World world;
        double dt = 0;
        boolean removing = false;
        boolean adding = false;
        OptionFrame optionFrame;

        public MyJPanel() {
            addKeyBindings();
            world = new World(10);

            optionFrame = new OptionFrame(this);
            addMouseListener(getMouseAdapter());
            addMouseMotionListener(getMouseAdapter());
            addMouseWheelListener(getMouseAdapter());
        }

        private void addKeyBindings() {
            char exit = KeyEvent.VK_ESCAPE;
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(exit), "exit");
            getActionMap().put("exit", exit());
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ctrl CONTROL"), "ctrl_down");
            getActionMap().put("ctrl_down", ctrl_down());
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released CONTROL"), "ctrl_up");
            getActionMap().put("ctrl_up", ctrl_up());
        }

        private Action ctrl_down() {
            return new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    keyDownControl = true;
                }

            };
        }

        private Action ctrl_up() {
            return new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    keyDownControl = false;
                }
            };
        }

        private Action exit() {
            return new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            };
        }

        private MouseAdapter getMouseAdapter() {
            return new MouseAdapter() {
                double prevX = 0, prevY = 0;

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (removing) {
                        Object stiff = null;
                        for (Object object : world.objects) {
                            int w, h;
                            for (Shape shape : object.shapes) {
                                if (shape instanceof RectangleShape) {
                                    RectangleShape rs = (RectangleShape) shape;
                                    w = rs.width;
                                    h = rs.height;
                                } else if (shape instanceof CircleShape) {
                                    CircleShape cs = (CircleShape) shape;
                                    w = cs.radius;
                                    h = cs.radius;
                                }
                                if (shape.contains(e.getPoint())) {
                                    stiff = object;
                                    break;
                                }
                            }
                        }
                        if (stiff != null) {
                            world.objects.remove(stiff);
                            repaint();
                            removing = false;
                        }
                    }
                    if (adding) {
                        CustomOptionMenu test = (CustomOptionMenu) optionFrame.getMenuBar();
                        test.chooseShape((new Point.Double(e.getPoint().x, e.getPoint().y)));
                        repaint();
                        adding = false;
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    prevX=e.getX();
                    prevY=e.getY();
                }
                
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (keyDownControl) {
                        mouseX=prevX-e.getX();
                        mouseY=prevY-e.getY();
                        repaint();
                        prevX=e.getX();
                        prevY=e.getY();
                    }
                }

                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    resizeWorld(world, e.getWheelRotation());
                }
            };
        }

        @Override
        public void update(Graphics g) {
            paintComponent(g);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.translate(mouseX, mouseY);
            g2.clearRect(0, 0, this.getWidth(), this.getHeight());
            g2.scale(scale, scale);
            world.paint(g2);
            g2.translate(-mouseX, -mouseY);
        }
    }

    public static void main(String[] args) {
        new Main();
        System.out.println(CollisionChecker.parallel(1, 5, -2, -4, 1, 5, -2, -4));
    }
}
