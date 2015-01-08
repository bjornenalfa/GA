package engine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        this.addKeyListener(keyL());

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

    private void moveWorld(World w) {
        for (int i = 0; i < w.objects.size(); i++) {
            w.objects.get(i).position.setLocation(0, 0);
        }

    }

    private KeyListener keyL() {
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_CONTROL) {
                    keyDownControl = true;
                }/*else if (ke.getKeyCode() == KeyEvent.VK_KP_UP) {
                    keyDownControl = true;
                } else if (ke.getKeyCode() == KeyEvent.VK_KP_DOWN) {
                    keyDownControl = true;
                } else if (ke.getKeyCode() == KeyEvent.VK_KP_LEFT) {
                    keyDownControl = true;
                } else if (ke.getKeyCode() == KeyEvent.VK_KP_RIGHT) {
                    keyDownControl = true;
                }*/
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_CONTROL) {
                    keyDownControl = false;
                } /*else if (ke.getKeyCode() == KeyEvent.VK_KP_UP) {
                    keyDownControl = false;
                } else if (ke.getKeyCode() == KeyEvent.VK_KP_DOWN) {
                    keyDownControl = false;
                } else if (ke.getKeyCode() == KeyEvent.VK_KP_LEFT) {
                    keyDownControl = false;
                } else if (ke.getKeyCode() == KeyEvent.VK_KP_RIGHT) {
                }*/
            }
        };
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
            MouseAdapter ma = getMouseAdapter();
            addMouseListener(ma);
            addMouseMotionListener(ma);
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

        private MouseAdapter getMouseAdapter() {
            return new MouseAdapter() {
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
                public void mouseDragged(MouseEvent me) {
                    if(keyDownControl){
                        moveWorld(world);
                    }
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
