package engine;

import engine.Main.MyJPanel;
import engine.OptionFrame.MyOptionPanel;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;

/**
 *
 * @author Letfik
 */
public class CustomOptionMenu extends JMenuBar {

    static final ScriptEngineManager manager = new ScriptEngineManager();
    static final ScriptEngine engine = manager.getEngineByName("js");
    MyOptionPanel optionPanel;
    MyJPanel panel;

    JMenuItem setupOne = makeSetupOne();
    JMenuItem setupTwo = makeSetupTwo();
    JMenuItem setupThree = makeSetupThree();
    JMenuItem setupFour = makeSetupFour();
    JMenuItem setupFive = makeSetupFive();
    JMenuItem setupSix = makeSetupSix();
    JMenuItem setupSeven = makeSetupSeven();
    JMenuItem setupEight = makeSetupEight();
    JMenuItem setupNine = makeSetupNine();

    public CustomOptionMenu(MyOptionPanel panel) {
        this.optionPanel = panel;
        this.panel = panel.mainPanel;

        JMenu add = new JMenu("Add");
        add.add(makeAddObjectAt());
        add.add(makeAddPlane());
        add.add(makeAddObject());

        JMenu remove = new JMenu("Remove");
        remove.add(makeRemoveObject());
        remove.add(makeResetPlanes());
        remove.add(makeResetObjects());

        JMenu setup = new JMenu("Setup");
        setup.add(setupOne);
        setup.add(setupTwo);
        setup.add(setupThree);
        setup.add(setupFour);
        setup.add(setupFive);
        setup.add(setupSix);
        setup.add(setupSeven);
        setup.add(setupEight);
        setup.add(setupNine);

        JMenu playback = playbackMenu();
        playback.setText("Playback Speed");

        add(add);
        add(remove);
        add(setup);
        add(playback);
    }

    String temp = "Rectangle";
    String shape = null;

    private JMenuItem makeAddObject() {
        JMenuItem addObject = new JMenuItem("Add Object");
        addObject.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.SHIFT_DOWN_MASK));
        addObject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseShape(null);
            }
        });
        return addObject;
    }

    private JMenuItem makeAddPlane() {
        JMenuItem addPlane = new JMenuItem("Add Plane");
        addPlane.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.SHIFT_DOWN_MASK));
        addPlane.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double x1 = Double.parseDouble(inputOptionPane("Enter x1."));
                double y1 = Double.parseDouble(inputOptionPane("Enter y1."));
                double x2 = Double.parseDouble(inputOptionPane("Enter x2."));
                double y2 = Double.parseDouble(inputOptionPane("Enter y2."));
                panel.world.addPlane(new Plane(x1, y1, x2, y2));
                panel.repaint();
            }
        });
        return addPlane;
    }

    private JMenuItem makeAddObjectAt() {
        JMenuItem addObjectAt = new JMenuItem("Add Object at Click");
        addObjectAt.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.SHIFT_DOWN_MASK));
        addObjectAt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.adding = true;
            }
        });
        return addObjectAt;
    }

    private JMenuItem makeResetObjects() {
        JMenuItem resetObjects = new JMenuItem("Reset Objects");
        resetObjects.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
        resetObjects.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirmReset = JOptionPane.showConfirmDialog(null, "Are you sure you want to reset?\n This action can't be undone.", "RESET", JOptionPane.YES_NO_OPTION);
                if (confirmReset == JOptionPane.YES_OPTION) {
                    panel.world.objects = new ArrayList();
                    panel.repaint();
                }
            }
        });
        return resetObjects;
    }

    private JMenuItem makeResetPlanes() {
        JMenuItem resetPlanes = new JMenuItem("Reset Planes");
        resetPlanes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK));
        resetPlanes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirmReset = JOptionPane.showConfirmDialog(null, "Are you sure you want to reset?\n This action can't be undone.", "RESET", JOptionPane.YES_NO_OPTION);
                if (confirmReset == JOptionPane.YES_OPTION) {
                    panel.world.planes = new ArrayList();
                    panel.repaint();
                }
            }
        });
        return resetPlanes;
    }

    private JMenuItem makeRemoveObject() {
        JMenuItem removeObject = new JMenuItem("Remove Object");
        removeObject.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        removeObject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.removing = true;
            }
        });
        return removeObject;
    }

    public void chooseShape(Point.Double p) {
        JRadioButton rectangle = new JRadioButton();
        JRadioButton circle = new JRadioButton();
        JPanel radiopanel = shapeRadioPanel(rectangle, circle);
        rectangle.setSelected(true);
        circle.setSelected(false);
        int chooseShape = JOptionPane.showConfirmDialog(null, radiopanel, "Choose shape.", JOptionPane.OK_CANCEL_OPTION);
        if (chooseShape == JOptionPane.CANCEL_OPTION || chooseShape == JOptionPane.CLOSED_OPTION) {
            return;
        } else if (chooseShape == JOptionPane.OK_OPTION) {
            shape = temp;
        }
        switch (shape) {
            case "Rectangle":
                if (p == null) {
                    rectangleOptions();
                } else {
                    rectangleOptions(p);
                }
                break;
            case "Circle":
                if (p == null) {
                    circleOptions();
                } else {
                    circleOptions(p);
                }
                break;
        }
    }

    private void rectangleOptions() {
        double x = doubleInput("Enter x.");
        double y = doubleInput("Enter y.");
        rectangleOptions(new Point.Double(x, y));
    }

    private void rectangleOptions(Point.Double p) {
        int w = intInput("Enter width.");
        int h = intInput("Enter height.");
        double r = doubleInput("Enter rotation.");
        double m = doubleInput("Enter mass.");
        Color c = stringToColorInput("Enter color.");
        panel.world.addObject(new Object(new RectangleShape(w, h, new Vector2D(0, 0), r, m, c), new Point.Double(p.x - w / 2, p.y - h / 2)));
        panel.repaint();
    }

    private void circleOptions() {
        double x = doubleInput("Enter x.");
        double y = doubleInput("Enter y.");
        circleOptions(new Point.Double(x, y));
    }

    private void circleOptions(Point.Double p) {
        int rad = intInput("Enter radius.");
        double r = doubleInput("Enter rotation.");
        double m = doubleInput("Enter mass.");
        Color c = stringToColorInput("Enter color");
        panel.world.addObject(new Object(new CircleShape(rad, new Vector2D(0, 0), r, m, c), new Point.Double(p.x, p.y)));
        panel.repaint();
    }

    public static int intInput(String text) {
        try {
            return Integer.parseInt(inputOptionPane(text));
        } catch (NumberFormatException e) {
            if (text.contains("(int)")) {
                return intInput(text);
            } else {
                return intInput(text + " (int)");
            }
        }
    }

    public static double doubleInput(String text) {
        try {
            return stringParser(text);
        } catch (NumberFormatException e) {
            if (text.contains("(double)")) {
                return doubleInput(text);
            } else {
                return doubleInput(text + " (double)");
            }
        }
    }

    public static Color stringToColorInput(String text) {
        try {
            Field field = Class.forName("java.awt.Color").getField(inputOptionPane(text).toUpperCase());
            return (Color) field.get(null);
        } catch (ClassNotFoundException | NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            if (text.contains("(jawa.awt.Color)")) {
                return stringToColorInput(text);
            } else {
                return stringToColorInput(text + " (jawa.awt.Color)");
            }
        }
    }

    private static String inputOptionPane(String text) {
        return JOptionPane.showInputDialog(null, text, "TITLE", JOptionPane.QUESTION_MESSAGE);
    }

    public static Double stringParser(String text) {
        String s = inputOptionPane(text);
        try {
            if (!s.contains("/") && !s.contains(".")) {
                s += ".0";
            }
            java.lang.Object result = engine.eval((s));
            return (double) result;
        } catch (ScriptException ex) {
            Logger.getLogger(OptionFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex){
        }
        return 0.0;
    }

    private JPanel shapeRadioPanel(JRadioButton rectangle, JRadioButton circle) {
        final JPanel radioPanel = new JPanel(new GridLayout(2, 1));
        ButtonGroup buttonGroup = new ButtonGroup();

        rectangle = rectangleChoice();
        circle = circleChoice();

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
                temp = "Rectangle";
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
                temp = "Circle";
            }
        });
        return circle;
    }

    private JMenu playbackMenu() {
        JMenu menu = new JMenu();
        ButtonGroup bg = new ButtonGroup();

        JRadioButton hhs = new JRadioButton("1/4 speed");
        hhs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                optionPanel.playbackSpeed = 4;
            }
        });
        bg.add(hhs);
        menu.add(hhs);

        JRadioButton hs = new JRadioButton("1/2 speed");
        hs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                optionPanel.playbackSpeed = 2;
            }
        });
        bg.add(hs);
        menu.add(hs);

        JRadioButton ns = new JRadioButton("1 speed");
        ns.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                optionPanel.playbackSpeed = 1;
            }
        });
        ns.setSelected(true);
        bg.add(ns);
        menu.add(ns);

        JRadioButton ds = new JRadioButton("2 speed");
        ds.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                optionPanel.playbackSpeed = 0.5;
            }
        });
        bg.add(ds);
        menu.add(ds);

        JRadioButton dds = new JRadioButton("4 speed");
        dds.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                optionPanel.playbackSpeed = 0.25;
            }
        });
        bg.add(dds);
        menu.add(dds);

        return menu;
    }

    private JMenuItem makeSetupOne() {
        JMenuItem setup = new JMenuItem("Setup One");
        setup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.world = new World(new Vector2D(0, 982));
                panel.world.objects.add(new Object(new RectangleShape(100, 100, new Vector2D(new Point.Double(0, 0)), 0, 0.5, Color.BLUE), new Point.Double(400, 100)));
                panel.world.addPlane(new Plane(0, 500, 800, 500));
                panel.optionFrame.panel.save.doClick();
                panel.repaint();
            }
        });
        return setup;
    }

    private JMenuItem makeSetupTwo() {
        JMenuItem setup = new JMenuItem("Setup Two");
        setup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.world = new World(new Vector2D(0, 982));
                panel.world.objects.add(new Object(new RectangleShape(100, 100, new Vector2D(new Point.Double(0, 0)), 0, 0.5, Color.BLUE), new Point.Double(400, 100)));
                panel.world.addPlane(new Plane(0, 0, 8000, 6000));
                panel.optionFrame.panel.save.doClick();
                panel.repaint();
            }
        });
        return setup;
    }

    private JMenuItem makeSetupThree() {
        JMenuItem setup = new JMenuItem("Setup Three");
        setup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.world = new World(new Vector2D(0, 982));
                panel.world.addPlane(new Plane(0, 0, 8000, 6000));
                panel.world.objects.add(new Object(new RectangleShape(100, 100, new Vector2D(new Point.Double(0, 0)), panel.world.planes.get(0).surface.vector.getAngle(), 0.5, Color.BLUE), new Point.Double(400, 100)));
                panel.optionFrame.panel.save.doClick();
                panel.repaint();
            }
        });
        return setup;
    }

    private JMenuItem makeSetupFour() {
        JMenuItem setup = new JMenuItem("Setup Four");
        setup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.world = new World(new Vector2D(0, 982));
                panel.world.objects.add(new Object(new CircleShape(50, new Vector2D(new Point.Double(0, 0)), 0, 0.5, Color.RED), new Point.Double(400, 100)));
                panel.world.addPlane(new Plane(0, 0, 8000, 6000));
                panel.optionFrame.panel.save.doClick();
                panel.repaint();
            }
        });
        return setup;
    }

    private JMenuItem makeSetupFive() {
        JMenuItem setup = new JMenuItem("Setup Five");
        setup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.world = new World(new Vector2D(0, 982));
                panel.world.objects.add(new Object(new RectangleShape(100, 100, new Vector2D(new Point.Double(0, 0)), 0, 0.5, Color.BLUE), new Point.Double(350, 300)));
                panel.world.objects.add(new Object(new CircleShape(50, new Vector2D(new Point.Double(0, 0)), 0, 0.5, Color.RED), new Point.Double(400, 100)));
                panel.world.addPlane(new Plane(0, 500, 800, 500));
                panel.optionFrame.panel.save.doClick();
                panel.repaint();
            }
        });
        return setup;
    }

    private JMenuItem makeSetupSix() {
        JMenuItem setup = new JMenuItem("Setup Six");
        setup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.world = new World(new Vector2D(0, 982));
                panel.world.objects.add(new Object(new RectangleShape(100, 100, new Vector2D(new Point.Double(0, 0)), 0, 0.5, Color.BLUE), new Point.Double(100, 100)));
                panel.world.objects.get(0).velocity = new Vector2D(100, 0);
                panel.world.addPlane(new Plane(0, 500, 800, 500));
                panel.optionFrame.panel.save.doClick();
                panel.repaint();
            }
        });
        return setup;
    }

    private JMenuItem makeSetupSeven() {
        JMenuItem setup = new JMenuItem("Setup Seven");
        setup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.world = new World(new Vector2D(0, 982));
                panel.world.objects.add(new Object(new RectangleShape(100, 100, new Vector2D(new Point.Double(0, 0)), 0, 0.5, Color.BLUE), new Point.Double(155, 140)));
                panel.world.objects.get(0).velocity = new Vector2D(0, 5000);
                int x = 100, y = 100;
                double angle = Math.toRadians(90);
                double length = 102;
                int segments = 360;
                double dAngle = Math.toRadians(-1);
                for (int i = 0; i <= segments; i++) {
                    panel.world.addPlane(new Plane((int) (x - Math.cos(angle) * 10 + .5), (int) (y - Math.sin(angle) * 10 + .5), (int) (x + Math.cos(angle) * (length + 10) + .5), (int) (y + Math.sin(angle) * (length + 10) + .5)));
                    x = (int) (x + Math.cos(angle) * length + .5);
                    y = (int) (y + Math.sin(angle) * length + .5);
                    angle += dAngle;
                }
                panel.optionFrame.panel.save.doClick();
                panel.repaint();
            }
        });
        return setup;
    }
    
    private JMenuItem makeSetupEight() {
        JMenuItem setup = new JMenuItem("Setup Eight");
        setup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.world = new World(new Vector2D(0, 982));
                panel.world.objects.add(new Object(new RectangleShape(100, 100, new Vector2D(new Point.Double(0, 0)), 0, 0.5, Color.BLUE), new Point.Double(200, 100)));
                panel.world.addPlane(new Plane(0, 150, 400, 550));
                panel.world.addPlane(new Plane(400, 550, 8000, 550));
                panel.optionFrame.panel.save.doClick();
                panel.repaint();
            }
        });
        return setup;
    }
    
    private JMenuItem makeSetupNine() {
        JMenuItem setup = new JMenuItem("Setup Nine");
        setup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.world = new World(new Vector2D(0, 982));
                panel.world.objects.add(new Object(new RectangleShape(100, 100, new Vector2D(new Point.Double(0, 0)), 0, 0.5, Color.BLUE), new Point.Double(100, 100)));
                panel.world.objects.get(0).velocity = new Vector2D(700, -100);
                panel.world.addPlane(new Plane(-1000, 500, 700, 500));
                panel.world.addPlane(new Plane(700, 500, 700, 0));
                panel.optionFrame.panel.save.doClick();
                panel.repaint();
            }
        });
        return setup;
    }
}
