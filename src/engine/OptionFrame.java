package engine;

import engine.Main.MyJPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

/**
 *
 * @author ludsiw279
 */
public class OptionFrame extends JFrame {

    //1 length unit = 1cm
    //1 mass unit = 1g
    //1 force unit = 1g*cm/s/s
    MyOptionPanel panel;

    public OptionFrame(MyJPanel mainPanel) {
        setTitle("OPTIONFRAME");

        panel = new MyOptionPanel(mainPanel);
        setContentPane(panel);
        setJMenuBar(new CustomOptionMenu(panel));
        pack();

        setAlwaysOnTop(true);
        setLocationRelativeTo(null);
        setLocation(getLocation().x + 420 + getSize().width / 2, getLocation().y);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public class MyOptionPanel extends JPanel {

        MyJPanel mainPanel;
        double dt = 0;
        boolean paused = true;
        MyThread thread = new MyThread();
        World backupWorld;
        boolean saved = false;
        private double playbackSpeed = 1;

        public MyOptionPanel(MyJPanel mainPanel) {
            setLayout(new GridLayout(2, 3, 5, 5));
            addKeyBindings();
            this.mainPanel = mainPanel;
            addButtons();
            add(dtLabel);
        }

        JButton update;
        JButton play;
        JButton pause;
        JButton reset;
        JButton save;
        JLabel dtLabel = new JLabel("", SwingConstants.CENTER);

        private void addButtons() {
            update = new JButton("Update");
            update.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (dt == 0) {
                        dt = CustomOptionMenu.doubleInput("Enter dt.");
                    }
                    mainPanel.world.update(dt);
                    updateLabels();
                    mainPanel.repaint();
                }
            });
            add(update);

            play = new JButton("Play");
            play.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (dt == 0) {
                        dt = CustomOptionMenu.doubleInput("Enter dt.");
                    }
                    try {
                        thread.start();
                        System.out.println("started");
                    } catch (Exception ex) {
                    }
                    paused = false;
                }
            });
            add(play);

            pause = new JButton("Pause");
            pause.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    paused = true;
                }
            });
            add(pause);

            reset = new JButton("Reset");
            reset.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (saved) {
                        mainPanel.world = backupWorld;
                        backupWorld = null;
                        dt = 0;
                        dtLabel.setText("");
                        mainPanel.repaint();
                        saved = false;
                    }
                }
            });
            add(reset);

            save = new JButton("Save");
            save.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    makeBackup();
                    saved = true;
                }
            });
            add(save);
        }

        private void makeBackup() {
            backupWorld = new World(mainPanel.world.g);
            for (Object o : mainPanel.world.objects) {
                Object newO = new Object();
                newO.position = new Point.Double(o.position.x, o.position.y);
                for (Shape s : o.shapes) {
                    if (s instanceof RectangleShape) {
                        RectangleShape rs = (RectangleShape) s;
                        newO.addShape(new RectangleShape(rs.width, rs.height, new Vector2D(new Point.Double(s.vector.point.x + 50, s.vector.point.y + 50)), s.rotation, s.mass, s.myC));
                    } else if (s instanceof CircleShape) {
                        CircleShape cs = (CircleShape) s;
                        newO.addShape(new CircleShape(cs.radius, new Vector2D(new Point.Double(s.vector.point.x + 50, s.vector.point.y + 50)), s.rotation, s.mass, s.myC));
                    }
                }
                newO.velocity = o.velocity;
                newO.acceleration = o.acceleration;
                newO.Mass = o.Mass;
                newO.angularVelocity = o.angularVelocity;
                newO.massCenter = o.massCenter;
                newO.rotation = o.rotation;
                backupWorld.objects.add(newO);
            }
            for (Plane p : mainPanel.world.planes) {
                backupWorld.planes.add(new Plane(p.surface));
            }
        }

        private void addKeyBindings() {
            char exit = KeyEvent.VK_ESCAPE;
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(exit), "exit");
            getActionMap().put("exit", exit());
            char updateChar = KeyEvent.VK_U;
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(updateChar), "update");
            getActionMap().put("update", updat());
            char playChar = KeyEvent.VK_P;
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(playChar), "play");
            getActionMap().put("play", play());
            char pauseChar = KeyEvent.VK_SPACE;
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(pauseChar), "pause");
            getActionMap().put("pause", pause());
            char resetChar = KeyEvent.VK_R;
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(resetChar), "reset");
            getActionMap().put("reset", reset());
        }

        private Action exit() {
            return new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            };
        }

        private Action updat() {
            return new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    update.doClick();
                }
            };
        }

        private Action play() {
            return new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    play.doClick();
                }
            };
        }

        private Action pause() {
            return new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pause.doClick();
                }
            };
        }

        private Action reset() {
            return new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    reset.doClick();
                }
            };
        }

        private void updateLabels() {
            dtLabel.setText(("<html><font color=white> " + "dt : " + dt + " </font></html>"));
        }

        @Override
        public void update(Graphics g) {
            paintComponent(g);
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        class MyThread extends Thread {

            @Override
            public void run() {
                while (true) {
                    while (!paused) {
                        mainPanel.world.update(dt);
                        updateLabels();
                        mainPanel.repaint();
                        try {
                            sleep((int)(dt*1000*panel.playbackSpeed));
                        } catch (InterruptedException ex) {
                        }
                    }
                    try {
                        sleep(100); //MUST HAVE OR NO LOOP D:
                    } catch (InterruptedException ex) {
                        Logger.getLogger(OptionFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

    }

}
