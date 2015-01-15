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
        setup.add(makeSetupOne());
        setup.add(makeSetupTwo());

        JMenu playback = playbackMenu();

        add(add);
        add(remove);
        add(setup);
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
        int chooseShape = JOptionPane.showConfirmDialog(null, shapeRadioPanel(), "Choose shape.", JOptionPane.OK_CANCEL_OPTION);
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
    
    public static Double stringParser(String s) {
        try {
            java.lang.Object result = engine.eval((inputOptionPane(s)));
            if (((Double) result).intValue() == ((Double) result)) {
                return ((Double) result);
            } else {
                long multi = 10 * 10 * 10 * 10 * 10 * 10;
                multi *= 10 * 10 * 10 * 10 * 10;
                return (double) ((long) ((((Double) result)) * multi)) / multi;
            }
        } catch (ScriptException ex) {
            Logger.getLogger(OptionFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0.0;
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
            return Double.parseDouble(inputOptionPane(text));
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

    private JPanel shapeRadioPanel() {
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
        bg.add(hhs);
        menu.add(hhs);
        JRadioButton hs = new JRadioButton("1/2 speed");
        bg.add(hs);
        menu.add(hs);
        JRadioButton ns = new JRadioButton("1 speed");
        bg.add(ns);
        menu.add(ns);
        JRadioButton ds = new JRadioButton("2 speed");
        bg.add(ds);
        menu.add(ds);
        JRadioButton dds = new JRadioButton("4 speed");
        bg.add(dds);
        menu.add(ds);

        return menu;
    }

    private JMenuItem makeSetupOne() {
        JMenuItem setupOne = new JMenuItem("Setup One");
        setupOne.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.world = new World(10);
                panel.world.objects.add(new Object(new RectangleShape(100, 100, new Vector2D(new Point.Double(0, 0)), 0, 0, Color.BLUE), new Point.Double(400, 100)));
                panel.world.addPlane(new Plane(0, 500, 800, 500));
                panel.repaint();
            }
        });
        return setupOne;
    }

    private JMenuItem makeSetupTwo() {
        JMenuItem setupTwo = new JMenuItem("Setup Two");
        setupTwo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.world = new World(10);
                panel.world.objects.add(new Object(new RectangleShape(100, 100, new Vector2D(new Point.Double(0, 0)), 0, 0, Color.BLUE), new Point.Double(400, 100)));
                panel.world.addPlane(new Plane(0, 0, 800, 600));
                panel.repaint();
            }
        });
        return setupTwo;
    }

    private JMenuItem makeSetupThree() {
        JMenuItem setupThree = new JMenuItem("Setup Three");
        setupThree.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        return setupThree;
    }

}
