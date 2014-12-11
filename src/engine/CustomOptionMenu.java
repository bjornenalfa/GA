package engine;

import engine.OptionFrame.MyOptionPanel;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 *
 * @author Letfik
 */
public class CustomOptionMenu extends MenuBar {

    MyOptionPanel panel;

    public CustomOptionMenu(MyOptionPanel panel) {
        this.panel = panel;

        Menu add = new Menu("Add");
        add.add(makeAddObject());
        add.add(makeAddPlane());

        Menu remove = new Menu("Remove");
        remove.add(makeResetObjects());
        remove.add(makeResetPlanes());
        remove.add(makeRemoveObject());

        add(add);
        add(remove);
    }

    String temp = "Rectangle";
    String shape = null;

    private MenuItem makeAddObject() {
        MenuItem addObject = new MenuItem("Add Object");
        addObject.setShortcut(new MenuShortcut(KeyEvent.VK_O, false));

        addObject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int chooseShape = JOptionPane.showConfirmDialog(null, shapeRadioPanel(), "Choose shape.", JOptionPane.OK_CANCEL_OPTION);
                if (chooseShape == JOptionPane.CANCEL_OPTION || chooseShape == JOptionPane.CLOSED_OPTION) {
                    return;
                } else if (chooseShape == JOptionPane.OK_OPTION) {
                    shape = temp;
                }
                switch (shape) {
                    case "Rectangle":
                        rectangleOptions();
                        break;
                    case "Circle":
                        circleOptions();
                        break;
                }
            }
        });
        return addObject;
    }

    private MenuItem makeAddPlane() {
        MenuItem addPlane = new MenuItem("Add Plane");
        addPlane.setShortcut(new MenuShortcut(KeyEvent.VK_P, false));

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

    private MenuItem makeResetObjects() {
        MenuItem resetObjects = new MenuItem("Reset Objects");
        resetObjects.setShortcut(new MenuShortcut(KeyEvent.VK_O, true));

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

    private MenuItem makeResetPlanes() {
        MenuItem resetPlanes = new MenuItem("Reset Planes");
        resetPlanes.setShortcut(new MenuShortcut(KeyEvent.VK_P, true));

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

    private MenuItem makeRemoveObject() {
        MenuItem removeObject = new MenuItem("Remove Object");
        removeObject.setShortcut(new MenuShortcut(KeyEvent.VK_R, true));
        removeObject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });

        return removeObject;
    }

    private void rectangleOptions() {
        double x = doubleInput("Enter x.");
        double y = doubleInput("Enter y.");
        int w = intInput("Enter width.");
        int h = intInput("Enter height.");
        double r = doubleInput("Enter rotation.");
        double m = doubleInput("Enter mass.");
        Color c = stringToColor("Enter color.");
        panel.world.addObject(new Object(new RectangleShape(x, y, w, h, new Vector2D(0, 0), r, m, c), new Point.Double(x, y)));
        panel.repaint();
    }

    private void circleOptions() {
        double x = doubleInput("Enter x.");
        double y = doubleInput("Enter y.");
        int rad = intInput("Enter radius.");
        double r = doubleInput("Enter rotation.");
        double m = doubleInput("Enter mass.");
        Color c = stringToColor("Enter color");
        panel.world.addObject(new Object(new CircleShape(x, y, rad, new Vector2D(0, 0), r, m, c), new Point.Double(x, y)));
        panel.repaint();
    }

    private int intInput(String text) {
        try {
            return Integer.parseInt(inputOptionPane(text));
        } catch (NumberFormatException e) {
            return intInput(text + " (int)");
        }
    }

    private double doubleInput(String text) {
        try {
            return Double.parseDouble(inputOptionPane(text));
        } catch (NumberFormatException e) {
            return doubleInput(text + " (double)");
        }
    }

    private Color stringToColor(String text) {
        try {
            Field field = Class.forName("java.awt.Color").getField(inputOptionPane(text).toUpperCase());
            return (Color) field.get(null);
        } catch (ClassNotFoundException | NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            return stringToColor(text + " (jawa.awt.Color)");
        }
    }

    private String inputOptionPane(String text) {
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

}
