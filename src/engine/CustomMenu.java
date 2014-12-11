package engine;

import engine.Main.MyJPanel;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.Point;
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
public class CustomMenu extends MenuBar {

    MyJPanel panel;

    public CustomMenu(MyJPanel panel) {
        this.panel = panel;

        Menu add = new Menu("Add");
        add.add(makeAddObject());

        Menu reset = new Menu("Reset");
        reset.add(makeResetObjects());

        add(add);
        add(reset);
    }

    String temp = "Rectangle";
    String shape = null;

    private MenuItem makeAddObject() {
        MenuItem addObject = new MenuItem("Add Object");
        addObject.setShortcut(new MenuShortcut(KeyEvent.VK_O, true));

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

    private MenuItem makeResetObjects() {
        MenuItem resetObjects = new MenuItem("Reset Objects");
        resetObjects.setShortcut(new MenuShortcut(KeyEvent.VK_R, true));

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

    private void rectangleOptions() {
        double x = Double.parseDouble(inputOptionPane("Enter x."));
        double y = Double.parseDouble(inputOptionPane("Enter y."));
        int w = Integer.parseInt(inputOptionPane("Enter width."));
        int h = Integer.parseInt(inputOptionPane("Enter height."));
        double r = Double.parseDouble(inputOptionPane("Enter rotation."));
        double m = Double.parseDouble(inputOptionPane("Enter mass."));
        Color c = stringToColor();
        panel.world.addObject(new Object(new RectangleShape(x, y, w, h, new Vector2D(0, 0), r, m, c), new Point.Double(x, y)));
        panel.repaint();
    }

    private void circleOptions() {
        double x = Double.parseDouble(inputOptionPane("Enter x."));
        double y = Double.parseDouble(inputOptionPane("Enter y."));
        int rad = Integer.parseInt(inputOptionPane("Enter radius."));
        double r = Double.parseDouble(inputOptionPane("Enter rotation."));
        double m = Double.parseDouble(inputOptionPane("Enter mass."));
        Color c = stringToColor();
        panel.world.addObject(new Object(new CircleShape(x, y, rad, new Vector2D(0, 0), r, m, c), new Point.Double(x, y)));
        panel.repaint();
    }

    private Color stringToColor() {
        try {
            Field field = Class.forName("java.awt.Color").getField(inputOptionPane("Enter color.").toUpperCase());
            return (Color) field.get(null);
        } catch (ClassNotFoundException | NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            return stringToColor();
        }
    }

    private String inputOptionPane(String text) {
        return JOptionPane.showInputDialog(text);
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
