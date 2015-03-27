package engine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
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
    static double scale = 1, translateX, translateY;
    static boolean playing = false;

    public Main() {
        setTitle("Fysiksimulator 2.0");

        panel = new MyJPanel();

        //setUndecorated(true);
        //setOpacity((float) 0.9);
        panel.setPreferredSize(new Dimension(800, 600));
        CustomOptionMenu menu = (CustomOptionMenu) panel.optionFrame.getJMenuBar();
        menu.setupTwelve.doClick();
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
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK), "pos_r");
            getActionMap().put("pos_r", pos_r());
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.getKeyText(KeyEvent.VK_F)), "pos_f");
            getActionMap().put("pos_f", pos_f());
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.getKeyText(KeyEvent.VK_C)), "pos_c");
            getActionMap().put("pos_c", pos_c());
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.getKeyText(KeyEvent.VK_P)), "pauseplay");
            getActionMap().put("pauseplay", pauseplay());
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.getKeyText(KeyEvent.VK_U)), "update");
            getActionMap().put("update", updat());
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.getKeyText(KeyEvent.VK_R)), "reset");
            getActionMap().put("reset", reset());
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.getKeyText(KeyEvent.VK_T)), "pos_t");
            getActionMap().put("pos_t", pos_t());
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.getKeyText(KeyEvent.VK_D)), "pos_d");
            getActionMap().put("pos_d", pos_d());

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

        private Action pos_t() {
            return new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int extra = 500;
                    int frequency = 1;
                    for (int x = -extra; x < panel.getWidth() + extra; x += frequency) {
                        for (int y = -extra; y < panel.getHeight() + extra; y += frequency) {
                            for (Object object : world.objects) {
                                for (Shape shape : object.shapes) {
                                    if (shape.contains(new Point.Double(x, y))) {
                                        world.points.add(new Point.Double(x, y));
                                    }
                                }
                            }
                        }
                    }
                    repaint();
                }
            };
        }

        private Action pos_d() {
            return new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for (Object object : world.objects) {
                        for (Shape shape : object.shapes) {
                            if (shape instanceof RectangleShape) {
                                RectangleShape rshape = (RectangleShape) shape;
                                rshape.calcNextPosition();
                            }
                        }
                    }
                    repaint();
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
                            double x = (e.getX() / scale - translateX);
                            double y = (e.getY() / scale - translateY);
                            repaint();
                            for (Object object : world.objects) {
                                for (Shape shape : object.shapes) {
                                    if (shape.contains(new Point.Double(x, y))) {
                                        world.clickFrameList.add(new ClickFrame(MyJPanel.this, object, shape));
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
            world.paint(g2);
            int count = 0;
            for (ClickFrame clickFrame : world.clickFrameList) {
                count++;
                g2.setColor(Color.getHSBColor((float) ((count * Math.E) % 1), 1f, 0.8f));
                //g2.setColor(Color.MAGENTA);
                Object object = clickFrame.object;
                Shape shape = clickFrame.object.shapes.get(clickFrame.shapeIndex);
                double x1 = (clickFrame.getX() - this.getLocationOnScreen().x) / scale - translateX;
                double y1 = (clickFrame.getY() - this.getLocationOnScreen().y) / scale - translateY;
                double x2 = object.position.x;
                double y2 = object.position.y;
                Vector2D v = new Vector2D(new Point.Double(x1, y1), new Point.Double(x2, y2));
                double kx = Math.min(1, Math.max(v.point.x / clickFrame.getWidth(), 0));
                double ky = Math.min(1, Math.max(v.point.y / clickFrame.getHeight(), 0));
                x1 += (clickFrame.getWidth() * kx) / scale;
                y1 += (clickFrame.getHeight() * ky) / scale;
                v = new Vector2D(new Point.Double(x1, y1), new Point.Double(x2, y2));
                Vector2D diagonal;
                if (Math.abs(v.point.x) < Math.abs(v.point.y)) {
                    double dx = v.point.x / Math.abs(v.point.x);
                    double dy = v.point.y / Math.abs(v.point.y);
                    diagonal = new Vector2D(new Point.Double(Math.abs(v.point.x) * dx, Math.abs(v.point.x) * dy));
                } else {
                    double dx = v.point.x / Math.abs(v.point.x);
                    double dy = v.point.y / Math.abs(v.point.y);
                    diagonal = new Vector2D(new Point.Double(Math.abs(v.point.y) * dx, Math.abs(v.point.y) * dy));
                }
                //Vector2D straight = v.subtract(diagonal);
                if (shape instanceof RectangleShape) {
                    RectangleShape rs = (RectangleShape) shape;
                    if (v.point.x == 0 || v.point.y == 0) {
                        g2.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
                    } else {
                        g2.drawLine((int) x1, (int) y1, (int) (x1 + diagonal.point.x), (int) (y1 + diagonal.point.y));
                        g2.drawLine((int) (x1 + diagonal.point.x), (int) (y1 + diagonal.point.y), (int) (x2), (int) (y2));
                    }
                } else if (shape instanceof CircleShape) {
                    CircleShape cs = (CircleShape) shape;
                    if (v.point.x == 0 || v.point.y == 0) {
                        g2.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
                    } else {
                        g2.drawLine((int) x1, (int) y1, (int) (x1 + diagonal.point.x), (int) (y1 + diagonal.point.y));
                        g2.drawLine((int) (x1 + diagonal.point.x), (int) (y1 + diagonal.point.y), (int) (x2), (int) (y2));
                    }
                }
                /*double x2 = (clickFrame.getX() - this.getLocationOnScreen().x + clickFrame.getWidth()/2.0) / scale - translateX;
                 double y2 = (clickFrame.getY() - this.getLocationOnScreen().y + clickFrame.getHeight()/2.0) / scale - translateY;
                 double x1 = object.position.x;
                 double y1 = object.position.y;
                 Vector2D v = new Vector2D(new Point.Double(x1,y1),new Point.Double(x2,y2));
                 double kx;
                 double ky;
                 if (v.point.x>0) {
                 kx = -0.5;
                 } else {
                 kx = 0.5;
                 }
                 if (v.point.y>0) {
                 ky = -0.5;
                 } else {
                 ky = 0.5;
                 }
                 x2+=(clickFrame.getWidth()*kx)/scale;
                 y2+=(clickFrame.getHeight()*ky)/scale;
                 v = new Vector2D(new Point.Double(x1,y1),new Point.Double(x2,y2));
                 Vector2D diagonal;
                 if (Math.abs(v.point.x)<Math.abs(v.point.y)) {
                 double dx = v.point.x/Math.abs(v.point.x);
                 double dy = v.point.y/Math.abs(v.point.y);
                 diagonal = new Vector2D(new Point.Double(Math.abs(v.point.x)*dx,Math.abs(v.point.x)*dy));
                 } else {
                 double dx = v.point.x/Math.abs(v.point.x);
                 double dy = v.point.y/Math.abs(v.point.y);
                 diagonal = new Vector2D(new Point.Double(Math.abs(v.point.y)*dx,Math.abs(v.point.y)*dy));
                 }
                 //Vector2D straight = v.subtract(diagonal);
                 if (shape instanceof RectangleShape) {
                 RectangleShape rs = (RectangleShape) shape;
                 if (v.point.x == 0 || v.point.y == 0) {
                 g2.drawLine((int)x1,(int)y1,(int)x2,(int)y2);
                 } else {
                 g2.drawLine((int) x1, (int) y1, (int) (x1+diagonal.point.x), (int) (y1+diagonal.point.y));
                 g2.drawLine((int) (x1+diagonal.point.x), (int) (y1+diagonal.point.y), (int) (x2), (int) (y2));
                 }
                 } else if (shape instanceof CircleShape) {
                 CircleShape cs = (CircleShape) shape;
                 if (v.point.x == 0 || v.point.y == 0) {
                 g2.drawLine((int)x1,(int)y1,(int)x2,(int)y2);
                 } else {
                 g2.drawLine((int) x1, (int) y1, (int) (x1+diagonal.point.x), (int) (y1+diagonal.point.y));
                 g2.drawLine((int) (x1+diagonal.point.x), (int) (y1+diagonal.point.y), (int) (x2), (int) (y2));
                 }
                 }*/
            }
            g2.translate(-translateX, -translateY);
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}
