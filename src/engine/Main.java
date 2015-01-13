package engine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
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

    }

    private void resizeWorld(World w, double res) {
        for (Object o : w.objects) {
            for (Shape s : o.shapes) {
                if (s instanceof RectangleShape) {
                    RectangleShape rs = (RectangleShape) s;
                    rs.height -= res;
                    rs.width -= res;
                } else if (s instanceof CircleShape) {
                    CircleShape rs = (CircleShape) s;
                    rs.radius -= res;
                }
            }
        }
        for (Plane o : w.planes) {
            if (o.surface.vector.getAngle() % 360 == 0) {
                o.surface.vector.getPoint().x -= res;
            } else {
                o.surface.vector.getPoint().y -= res;
            }
        }

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
                double oldY, oldX;

                @Override
                public void mousePressed(MouseEvent e) {
                    oldY = e.getY();
                    oldX = e.getX();
                }

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
                public void mouseDragged(MouseEvent e) {
                    double y = e.getY();
                    double x = e.getX();
                    if (keyDownControl) {
                        moveWorld(world, oldX - x, oldY - y);
                    }
                    oldY = y;
                    oldX = x;
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
            world.paint(g);
        }

    }

    public static void main(String[] args) {
        new Main();
        System.out.println(CollisionChecker.parallel(1, 5, -2, -4, 1, 5, -2, -4));
    }
}
