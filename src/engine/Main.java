package engine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class Main extends JFrame {

    //1 length unit = 1cm
    //1 mass unit = 1g
    //1 force unit = 1g*cm/s/s
    MyJPanel panel;
    boolean keyDownControl = false;
    static double scale = 1, translateX, translateY;
    static boolean playing = false;

    public Main() {
        setTitle("TITLE");

        panel = new MyJPanel();

        //setUndecorated(true);
        //setOpacity((float) 0.9);
        panel.setPreferredSize(new Dimension(800, 600));
        CustomOptionMenu menu = (CustomOptionMenu) panel.optionFrame.getJMenuBar();
        menu.setupFifteen.doClick();
        setContentPane(panel);
        pack();

        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);
        java.awt.Shape shape = new Ellipse2D.Double(0, 0, 800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//        setShape(shape);
    }

    /*private void moveWorld(World w, double x, double y) {
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
     if (!playing) repaint();
     }*/
    private void changeScale(World w, double res) {
        double x = -res * ((panel.getWidth() / 2) / scale - translateX);
        double y = -res * ((panel.getHeight() / 2) / scale - translateY);
        scale -= res * (scale * 0.1);
        translateX -= x * 0.1;
        translateY -= y * 0.1;
        translateX *= 1 / (1 - res * 0.1);
        translateY *= 1 / (1 - res * 0.1);
        if (!playing) {
            repaint();
        }
    }

    public class MyJPanel extends JPanel {

        double dt = 0;
        boolean removing = false;
        boolean adding = false;
        OptionFrame optionFrame;
        World world;
        public ArrayList<ClickFrame> clickFrameList = new ArrayList();

        public MyJPanel() {
            addKeyBindings();
            world = new World(new Vector2D(0, 982));

            optionFrame = new OptionFrame(this);
            MouseAdapter ma = getMouseAdapter();
            addMouseListener(ma);
            addMouseMotionListener(ma);
            addMouseWheelListener(ma);
        }

        private void addKeyBindings() {
            char exit = KeyEvent.VK_ESCAPE;
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(exit), "exit");
            getActionMap().put("exit", exit());
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_CONTROL, InputEvent.CTRL_DOWN_MASK), "ctrl_down");
            getActionMap().put("ctrl_down", ctrl_down());
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released CONTROL"), "ctrl_up");
            getActionMap().put("ctrl_up", ctrl_up());
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_0, InputEvent.CTRL_DOWN_MASK), "ctrl+0");
            getActionMap().put("ctrl+0", ctrl0());
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.getKeyText(KeyEvent.VK_R)), "pos_r");
            getActionMap().put("pos_r", pos_r());
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.getKeyText(KeyEvent.VK_F)), "pos_f");
            getActionMap().put("pos_f", pos_f());
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.getKeyText(KeyEvent.VK_C)), "pos_c");
            getActionMap().put("pos_c", pos_c());
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.getKeyText(KeyEvent.VK_P)), "pauseplay");
            getActionMap().put("pauseplay", pauseplay());
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.getKeyText(KeyEvent.VK_U)), "update");
            getActionMap().put("update", updat());
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.getKeyText(KeyEvent.VK_I)), "reset");
            getActionMap().put("reset", reset());

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

        private Action ctrl0() {
            return new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    scale = 1;
                    //translateX = 0;
                    //translateY = 0;
                    if (!playing) {
                        repaint();
                    }
                }
            };
        }

        private Action pos_r() {
            return new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    translateX = 0;
                    translateY = 0;
                    if (!playing) {
                        repaint();
                    }
                }
            };
        }

        private Action pos_f() {
            return new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    world.follow = !world.follow;
                }
            };
        }

        private Action pos_c() {
            return new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    translateX = -world.objects.get(world.followID).position.x + (panel.getWidth() / 2) / scale;
                    translateY = -world.objects.get(world.followID).position.y + (panel.getHeight() / 2) / scale;
                    if (!playing) {
                        repaint();
                    }
                }
            };
        }

        private Action pauseplay() {
            return new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (optionFrame.panel.paused) {
                        optionFrame.panel.play60.doClick();
                    } else {
                        optionFrame.panel.pause.doClick();
                    }
                }
            };
        }

        private Action updat() {
            return new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    optionFrame.panel.update.doClick();
                }
            };
        }

        private Action reset() {
            return new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    optionFrame.panel.reset.doClick();
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
                boolean pressed = false;

                @Override
                public void mouseReleased(MouseEvent e) {
                    pressed = false;
                    if (removing) {
                        Object stiff = null;
                        for (Object object : world.objects) {
                            for (Shape shape : object.shapes) {
                                if (shape.contains(e.getPoint())) {
                                    stiff = object;
                                    break;
                                }
                            }
                        }
                        if (stiff != null) {
                            world.objects.remove(stiff);
                            if (!playing) {
                                repaint();
                            }
                            removing = false;
                        }
                    } else if (adding) {
                        CustomOptionMenu test = (CustomOptionMenu) optionFrame.getJMenuBar();
                        test.chooseShape((new Point.Double(e.getPoint().x, e.getPoint().y)));
                        if (!playing) {
                            repaint();
                        }
                        adding = false;
                    } else {
                        if (e.getButton() == 3) {
                            for (Object object : world.objects) {
                                for (Shape shape : object.shapes) {
                                    if (shape.contains(e.getPoint())) {
                                        clickFrameList.add(new ClickFrame(MyJPanel.this, object, shape));
                                    }
                                }
                            }
                        }
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    if (keyDownControl) {
                        if (!pressed) {
                            prevX = e.getX();
                            prevY = e.getY();
                            pressed = true;
                        }
                    }
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    if (keyDownControl) {
                        if (!pressed) {
                            prevX = e.getX();
                            prevY = e.getY();
                            pressed = true;
                        }
                        double nowX = e.getX();
                        double nowY = e.getY();
                        translateX -= (prevX - nowX) / scale;
                        translateY -= (prevY - nowY) / scale;
                        if (!playing) {
                            repaint();
                        }
                        prevX = nowX;
                        prevY = nowY;
                    }
                }

                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    changeScale(world, e.getWheelRotation());
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
            g2.clearRect(0, 0, this.getWidth(), this.getHeight());
            g2.scale(scale, scale);
            g2.translate(translateX, translateY);
            //System.out.println("tx:" + translateX + " ty:" + translateY + " s:" + scale);
            world.paint(g2);
            for (ClickFrame clickFrame : clickFrameList) {
                g2.setColor(Color.MAGENTA);
                Object object = clickFrame.object;
                Shape shape = clickFrame.object.shapes.get(clickFrame.shapeIndex);
                if (shape instanceof RectangleShape) {
                    RectangleShape rs = (RectangleShape) shape;
                    g2.drawLine((int) (object.position.x + rs.width / 2), (int) (object.position.y - rs.height / 2), clickFrame.getX() - this.getLocationOnScreen().x, clickFrame.getY() + clickFrame.getHeight() - this.getLocationOnScreen().y);
                } else if (shape instanceof CircleShape) {
                    CircleShape cs = (CircleShape) shape;
                    g2.drawLine((int) (object.position.x + cs.radius), (int) (object.position.y - cs.radius), clickFrame.getX() - this.getLocationOnScreen().x, clickFrame.getY() + clickFrame.getHeight() - this.getLocationOnScreen().y);
                }
            }
            g2.translate(-translateX, -translateY);
        }
    }

    public static void main(String[] args) {
        new Main();
        System.out.println(CollisionChecker.parallel(1, 5, -2, -4, 1, 5, -2, -4));
    }
}
