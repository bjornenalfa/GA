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
    JMenuItem setupTen = makeSetupTen();
    JMenuItem setupEleven = makeSetupEleven();
    JMenuItem setupTwelve = makeSetupTwelve();
    JMenuItem setupThirteen = makeSetupThirteen();
    JMenuItem setupFourteen = makeSetupFourteen();
    JMenuItem setupFifteen = makeSetupFifteen();
    JMenuItem setupSixteen = makeSetupSixteen();

    public CustomOptionMenu(MyOptionPanel panel) {
        this.optionPanel = panel;
        this.panel = panel.mainPanel;

        JMenu add = new JMenu("Add");
        add.add(makeAddObjectAt());
        add.add(makeAddObject());

        JMenu remove = new JMenu("Remove");
        remove.add(makeRemoveObject());
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
        setup.add(setupTen);
        setup.add(setupEleven);
        setup.add(setupTwelve);
        setup.add(setupThirteen);
        setup.add(setupFourteen);
        setup.add(setupFifteen);
        setup.add(setupSixteen);

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
                    if (!Main.playing) {
                        panel.repaint();
                    }
                }
            }
        });
        return resetObjects;
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
        panel.world.addObject(new Object(new RectangleShape(w, h, new Vector2D(0, 0), r, m, c), new Point.Double(p.x, p.y)));
        if (!Main.playing) {
            panel.repaint();
        }
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
        if (!Main.playing) {
            panel.repaint();
        }
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
        } catch (NullPointerException ex) {
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
        
        JRadioButton hhhhs = new JRadioButton("1/16 speed");
        hhhhs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                optionPanel.playbackSpeed = 16;
            }
        });
        bg.add(hhhhs);
        menu.add(hhhhs);
        
        JRadioButton hhhs = new JRadioButton("1/8 speed");
        hhhs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                optionPanel.playbackSpeed = 8;
            }
        });
        bg.add(hhhs);
        menu.add(hhhs);

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
        
        JRadioButton ddds = new JRadioButton("8 speed");
        ddds.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                optionPanel.playbackSpeed = 0.125;
            }
        });
        bg.add(ddds);
        menu.add(ddds);
        
        JRadioButton dddds = new JRadioButton("16 speed");
        dddds.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                optionPanel.playbackSpeed = 1/16d;
            }
        });
        bg.add(dddds);
        menu.add(dddds);

        return menu;
    }

    private void afterSetup() {
        panel.optionFrame.panel.save.doClick();
        for (int i = 0; i < panel.world.clickFrameList.size(); i++) {
            ClickFrame clickFrame = panel.world.clickFrameList.get(i);
            panel.world.clickFrameList.remove(clickFrame);
            clickFrame.dispose();
        }
        if (!Main.playing) {
            panel.repaint();
        }
    }

    private JMenuItem makeSetupOne() {
        JMenuItem setup = new JMenuItem("Setup One");
        setup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.world = new World(new Vector2D(0, 982));
                panel.world.follow = false;
                panel.world.objects.add(new Object(new RectangleShape(100, 100, new Vector2D(new Point.Double(0, 0)), 0, 0.5, Color.BLUE), new Point.Double(400, 100), Material.Wood));
                panel.world.objects.add(new FixedObject(0, 500, 800, 500, 30, Material.Concrete));

                afterSetup();
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
                panel.world.objects.add(new Object(new RectangleShape(100, 100, new Vector2D(new Point.Double(0, 0)), 0, 0.5, Color.BLUE), new Point.Double(400, 100), Material.Wood));
                panel.world.objects.add(new FixedObject(0, 0, 8000, 6000, 30, Material.Concrete));

                afterSetup();
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
                panel.world.objects.add(new FixedObject(0, 0, 8000, 6000, 30, Material.Concrete));
                panel.world.objects.add(new Object(new RectangleShape(100, 100, new Vector2D(new Point.Double(0, 0)), Math.atan2(3, 4), 0.00005, Color.BLUE), new Point.Double(400, 100), Material.Wood));
                panel.world.followID = 1;
                afterSetup();
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
                panel.world.objects.add(new Object(new CircleShape(50, new Vector2D(new Point.Double(0, 0)), 0, 0.5, Color.RED), new Point.Double(400, 100), Material.Wood));
                panel.world.objects.add(new FixedObject(0, 0, 8000, 6000, 30, Material.Concrete));

                afterSetup();
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
                panel.world.follow = false;
                panel.world.objects.add(new Object(new RectangleShape(100, 100, new Vector2D(new Point.Double(0, 0)), 0, 0.0005, Color.BLUE), new Point.Double(350, 300), Material.Wood));
                panel.world.objects.add(new Object(new CircleShape(50, new Vector2D(new Point.Double(0, 0)), 0, 0.0005, Color.RED), new Point.Double(400, 100), Material.Wood));
                panel.world.objects.add(new FixedObject(0, 500, 800, 500, 20, Material.Concrete));
//                panel.world.objects.add(new FixedObject(0, 500, 800, 500, 10, Material.Concrete));

                afterSetup();
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
                panel.world.objects.add(new Object(new RectangleShape(100, 100, new Vector2D(new Point.Double(0, 0)), 0, 0.5, Color.BLUE), new Point.Double(100, 100), Material.Wood));
                panel.world.objects.get(0).velocity = new Vector2D(100, 0);
                panel.world.objects.add(new FixedObject(0, 500, 800, 500, 30, Material.Concrete));
                panel.world.follow = false;

                afterSetup();
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
                panel.world.objects.add(new Object(new RectangleShape(100, 100, new Vector2D(new Point.Double(0, 0)), 0, 0.5, Color.BLUE), new Point.Double(155, 140), Material.Wood));
                panel.world.objects.get(0).velocity = new Vector2D(0, 5000);
                int x = 90, y = 100;
                double angle = Math.toRadians(90);
                double length = 200;
                int segments = 360;
                double extra = 250;
                double dAngle = Math.toRadians(-1);
                for (int i = 0; i <= segments; i++) {
                    panel.world.objects.add(new FixedObject((int) (x - Math.cos(angle) * extra + .5), (int) (y - Math.sin(angle) * extra + .5), (int) (x + Math.cos(angle) * (length + extra) + .5), (int) (y + Math.sin(angle) * (length + extra) + .5), 20, Material.Boost));
                    x = (int) (x + Math.cos(angle) * length + .5);
                    y = (int) (y + Math.sin(angle) * length + .5);
                    angle += dAngle;
                }

                afterSetup();
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
                panel.world.objects.add(new Object(new RectangleShape(100, 100, new Vector2D(new Point.Double(0, 0)), 0, 0.5, Color.BLUE), new Point.Double(200, 100), Material.Wood));
                panel.world.objects.add(new FixedObject(0, 150, 400, 550, 30, Material.Concrete));
                panel.world.objects.add(new FixedObject(400, 550, 8000, 550, 30, Material.Concrete));
                panel.world.follow = false;

                afterSetup();
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
                panel.world.follow = false;
                panel.world.objects.add(new Object(new RectangleShape(100, 100, new Vector2D(new Point.Double(0, 0)), 0, 0.5, Color.BLUE), new Point.Double(100, 100), Material.Boost));
                panel.world.objects.get(0).velocity = new Vector2D(700, -100);
                panel.world.objects.add(new FixedObject(-5000, 100, 700, 500, 30, Material.Concrete));
                panel.world.objects.add(new FixedObject(700, 500, 700, 0, 30, Material.Concrete));

                afterSetup();
            }
        });
        return setup;
    }

    private JMenuItem makeSetupTen() {
        JMenuItem setup = new JMenuItem("Setup Ten");
        setup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.world = new World(new Vector2D(0, 982));
                panel.world.objects.add(new Object(new CircleShape(50, new Vector2D(new Point.Double(0, 0)), 0, 0.5, Color.RED), new Point.Double(155, 140), Material.Wood));
                panel.world.objects.get(0).velocity = new Vector2D(0, 0000);
                int x = 50, y = 100;
                double angle = Math.toRadians(90);
                double length = 102;
                int segments = 360;
                double extra = 5000;
                double dAngle = Math.toRadians(-1);
                for (int i = 0; i <= segments; i++) {
                    panel.world.objects.add(new FixedObject((int) (x - Math.cos(angle) * extra + .5), (int) (y - Math.sin(angle) * extra + .5), (int) (x + Math.cos(angle) * (length + extra) + .5), (int) (y + Math.sin(angle) * (length + extra) + .5), 20, Material.Boost));
                    x = (int) (x + Math.cos(angle) * length + .5);
                    y = (int) (y + Math.sin(angle) * length + .5);
                    angle += dAngle;
                }

                afterSetup();
            }
        });
        return setup;
    }

    private JMenuItem makeSetupEleven() {
        JMenuItem setup = new JMenuItem("Setup Eleven");
        setup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.world = new World(new Vector2D(0, 1));
                panel.world.follow = false;
                panel.world.objects.add(new Object(new RectangleShape(100, 100, new Vector2D(new Point.Double(0, 0)), 0, 0.5, Color.BLUE), new Point.Double(400, 100), Material.Wood));
                panel.world.objects.get(0).angularVelocity = 1;
                panel.world.objects.add(new FixedObject(0, 500, 800, 500, 30, Material.Concrete));

                afterSetup();
            }
        });
        return setup;
    }

    private JMenuItem makeSetupTwelve() {
        JMenuItem setup = new JMenuItem("Setup Twelve");
        setup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.world = new World(new Vector2D(0, 982));
                panel.world.follow = false;
                panel.world.objects.add(new Object(new RectangleShape(100, 100, new Vector2D(new Point.Double(0, 0)), -0, 0.0005, Color.BLUE), new Point.Double(400, 100), Material.Wood));
                panel.world.objects.add(new FixedObject(0, 550, 800, 550, 20, Material.Concrete));
                panel.world.objects.add(new FixedObject(0, 350, 390, 350, 20, Material.Concrete));
                panel.world.objects.add(new FixedObject(new CircleShape(50, new Vector2D(new Point.Double(0, 0)), 0, Double.POSITIVE_INFINITY, Color.DARK_GRAY), new Point.Double(350, 250), Material.Concrete));
                panel.world.objects.add(new FixedObject(new CircleShape(5000, new Vector2D(new Point.Double(0, 0)), 0, Double.POSITIVE_INFINITY, Color.DARK_GRAY), new Point.Double(800, 5540), Material.Boost));
                panel.world.objects.add(new FixedObject(new CircleShape(5000, new Vector2D(new Point.Double(0, 0)), 0, Double.POSITIVE_INFINITY, Color.DARK_GRAY), new Point.Double(3800, 5540), Material.Glide));
                panel.world.objects.add(new FixedObject(8000, 3540, 9000, 3540, 50, Material.SuperBounce));
                panel.world.objects.add(new FixedObject(new CircleShape(5000, new Vector2D(new Point.Double(0, 0)), 0, Double.POSITIVE_INFINITY, Color.DARK_GRAY), new Point.Double(13500, 5540), Material.Boost));

                afterSetup();
            }
        });
        return setup;
    }

    private JMenuItem makeSetupThirteen() {
        JMenuItem setup = new JMenuItem("Setup Thirteen");
        setup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.world = new World(new Vector2D(0, 982));
                panel.world.follow = false;
                panel.world.objects.add(new Object(new CircleShape(50, new Vector2D(new Point.Double(0, 0)), 0, 0.5, Color.RED), new Point.Double(400, 100), Material.Wood));
                panel.world.objects.add(new FixedObject(0, 350, 399, 350, 30, Material.Concrete));

                afterSetup();
            }
        });
        return setup;
    }

    private JMenuItem makeSetupFourteen() {
        JMenuItem setup = new JMenuItem("Setup Fourteen");
        setup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.world = new World(new Vector2D(0, 982));
                panel.world.follow = false;
                panel.world.objects.add(new Object(new CircleShape(50, new Vector2D(new Point.Double(0, 0)), 0, 0.5, Color.RED), new Point.Double(400, 100), Material.Wood));
                panel.world.objects.add(new FixedObject(400, 600, 400, 350, 1, Material.Concrete));

                afterSetup();
            }
        });
        return setup;
    }

    private JMenuItem makeSetupFifteen() {
        JMenuItem setup = new JMenuItem("Setup Fifteen");
        setup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.world = new World(new Vector2D(0, 982));
                panel.world.follow = false;
                panel.world.objects.add(new Object(new RectangleShape(100, 100, new Vector2D(new Point.Double(0, 0)), 0, 0.0005, Color.BLUE), new Point.Double(350, 300), Material.Wood));

                panel.world.objects.add(new Object(new CircleShape(50, new Vector2D(new Point.Double(0, 0)), 0, 0.0005, Color.RED), new Point.Double(290, 100), Material.Wood));
                panel.world.objects.get(0).angularVelocity = -9.8;
                panel.world.objects.add(new Object(new CircleShape(50, new Vector2D(new Point.Double(0, 0)), 0, 0.0005, Color.RED), new Point.Double(400, 100), Material.Wood));
                panel.world.objects.get(1).angularVelocity = 10;
                panel.world.objects.add(new Object(new CircleShape(20, new Vector2D(new Point.Double(0, 0)), 0, 0.0005, Color.RED), new Point.Double(350, 50), Material.Wood));
                panel.world.objects.get(2).angularVelocity = -3;
                panel.world.objects.add(new Object(new CircleShape(20, new Vector2D(new Point.Double(0, 0)), 0, 0.0005, Color.RED), new Point.Double(250, -50), Material.Wood));
                panel.world.objects.get(3).angularVelocity = -5;
                panel.world.objects.add(new Object(new CircleShape(20, new Vector2D(new Point.Double(0, 0)), 0, 0.0005, Color.RED), new Point.Double(150, 50), Material.Wood));
                panel.world.objects.get(4).angularVelocity = 7;
                panel.world.objects.add(new Object(new CircleShape(20, new Vector2D(new Point.Double(0, 0)), 0, 0.0005, Color.RED), new Point.Double(50, 50), Material.Wood));
                panel.world.objects.get(5).angularVelocity = -9;
                panel.world.objects.add(new FixedObject(-800, 400, 800, 600, 100, Material.Wood));
                panel.world.objects.add(new FixedObject(800, 600, 900, 0, 100, Material.Wood));
                afterSetup();
            }
        });
        return setup;
    }
    
    private JMenuItem makeSetupSixteen() {
        JMenuItem setup = new JMenuItem("Setup Sixteen");
        setup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.world = new World(new Vector2D(0, 982));
                panel.world.follow = false;
                panel.world.objects.add(new Object(new RectangleShape(100, 100, new Vector2D(new Point.Double(0, 0)), 0, 0.0005, Color.BLUE), new Point.Double(100, 100), Material.Wood));
                panel.world.objects.get(0).velocity = new Vector2D(200,0);
//                panel.world.objects.add(new FixedObject(new RectangleShape(50,50,Vector2D.zero,0,Double.POSITIVE_INFINITY,Color.DARK_GRAY),new Point.Double(100,180), Material.Glide));
//                panel.world.objects.add(new FixedObject(new RectangleShape(50,50,Vector2D.zero,0,Double.POSITIVE_INFINITY,Color.DARK_GRAY),new Point.Double(200,180), Material.Glide));
//                panel.world.objects.add(new FixedObject(new RectangleShape(50,50,Vector2D.zero,0,Double.POSITIVE_INFINITY,Color.DARK_GRAY),new Point.Double(300,180), Material.Glide));
//                panel.world.objects.add(new FixedObject(new RectangleShape(50,50,Vector2D.zero,0,Double.POSITIVE_INFINITY,Color.DARK_GRAY),new Point.Double(400,180), Material.Glide));
//                panel.world.objects.add(new FixedObject(new RectangleShape(50,50,Vector2D.zero,0,Double.POSITIVE_INFINITY,Color.DARK_GRAY),new Point.Double(500,180), Material.Glide));
//                panel.world.objects.add(new FixedObject(new RectangleShape(50,50,Vector2D.zero,0,Double.POSITIVE_INFINITY,Color.DARK_GRAY),new Point.Double(600,180), Material.Glide));
//                panel.world.objects.add(new FixedObject(new RectangleShape(50,50,Vector2D.zero,0,Double.POSITIVE_INFINITY,Color.DARK_GRAY),new Point.Double(700,180), Material.Glide));
                
//                panel.world.objects.add(new FixedObject(new CircleShape(25,Vector2D.zero,0,Double.POSITIVE_INFINITY,Color.DARK_GRAY),new Point.Double(100,180), Material.Boost));
//                panel.world.objects.add(new FixedObject(new CircleShape(25,Vector2D.zero,0,Double.POSITIVE_INFINITY,Color.DARK_GRAY),new Point.Double(200,180), Material.Boost));
//                panel.world.objects.add(new FixedObject(new CircleShape(25,Vector2D.zero,0,Double.POSITIVE_INFINITY,Color.DARK_GRAY),new Point.Double(300,180), Material.Boost));
//                panel.world.objects.add(new FixedObject(new CircleShape(25,Vector2D.zero,0,Double.POSITIVE_INFINITY,Color.DARK_GRAY),new Point.Double(400,180), Material.Boost));
//                panel.world.objects.add(new FixedObject(new CircleShape(25,Vector2D.zero,0,Double.POSITIVE_INFINITY,Color.DARK_GRAY),new Point.Double(500,180), Material.Boost));
//                panel.world.objects.add(new FixedObject(new CircleShape(25,Vector2D.zero,0,Double.POSITIVE_INFINITY,Color.DARK_GRAY),new Point.Double(600,180), Material.Boost));
//                panel.world.objects.add(new FixedObject(new CircleShape(25,Vector2D.zero,0,Double.POSITIVE_INFINITY,Color.DARK_GRAY),new Point.Double(700,180), Material.Boost));
//                panel.world.objects.add(new FixedObject(new CircleShape(25,Vector2D.zero,0,Double.POSITIVE_INFINITY,Color.DARK_GRAY),new Point.Double(800,180), Material.Boost));
//                panel.world.objects.add(new FixedObject(new CircleShape(25,Vector2D.zero,0,Double.POSITIVE_INFINITY,Color.DARK_GRAY),new Point.Double(900,180), Material.Boost));
//                panel.world.objects.add(new FixedObject(new CircleShape(25,Vector2D.zero,0,Double.POSITIVE_INFINITY,Color.DARK_GRAY),new Point.Double(1000,180), Material.Boost));
//                panel.world.objects.add(new FixedObject(new CircleShape(25,Vector2D.zero,0,Double.POSITIVE_INFINITY,Color.DARK_GRAY),new Point.Double(1100,180), Material.Boost));
                for (int i = 0;i<20;i++) {
                    panel.world.objects.add(new FixedObject(new CircleShape(25,Vector2D.zero,0,Double.POSITIVE_INFINITY,Color.DARK_GRAY),new Point.Double(100+i*100,180+i*10), Material.Ice));
                }
                
                afterSetup();
            }
        });
        return setup;
    }
}
