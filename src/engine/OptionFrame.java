package engine;

import engine.Main.MyJPanel;
import static engine.Main.playing;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class OptionFrame extends JFrame {

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
        double playbackSpeed = 1;

        public MyOptionPanel(MyJPanel mainPanel) {
            setLayout(new GridLayout(3, 3, 5, 5));
            this.mainPanel = mainPanel;
            addButtons();
        }

        JButton update;
        JButton play;
        JButton play60;
        JButton pause;
        JButton reset;
        JButton save;
        JButton setDt;
        JButton clearArrows;
        JLabel dtLabel = new JLabel("", SwingConstants.CENTER);

        private void addButtons() {
            play = new JButton("Play");
            play.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (dt == 0) {
                        dt = CustomOptionMenu.doubleInput("Enter dt.");
                    }
                    if (dt != 0) {
                        try {
                            thread.start();
                        } catch (Exception ex) {
                        }
                        paused = false;
                    }
                    mainPanel.requestFocus();
                }
            });
            add(play);

            play60 = new JButton("Play at 60FPS");
            play60.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dt = 1.0 / 60.0;
                    play.doClick();
                }
            });
            add(play60);

            pause = new JButton("Pause");
            pause.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    paused = true;
                    mainPanel.requestFocus();
                }
            });
            add(pause);

            update = new JButton("Update");
            update.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (dt == 0) {
                        dt = CustomOptionMenu.doubleInput("Enter dt.");
                    }
                    mainPanel.world.update(dt);
                    updateLabels();
                    if (!playing) {
                        mainPanel.repaint();
                    }
                    mainPanel.requestFocus();
                }
            });
            add(update);

            save = new JButton("Save");
            save.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    makeBackup();
                    saved = true;
                    mainPanel.requestFocus();
                }
            });
            add(save);

            reset = new JButton("Reset");
            reset.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (saved) {
                        for (int i = 0; i < mainPanel.world.clickFrameList.size(); i++) {
                            ClickFrame clickFrame = mainPanel.world.clickFrameList.get(i);
                            mainPanel.world.clickFrameList.remove(clickFrame);
                            clickFrame.dispose();
                        }
                        boolean temp1 = mainPanel.world.follow;
                        int temp2 = mainPanel.world.followID;
                        mainPanel.world = copyWorld(backupWorld);
                        mainPanel.world.follow = temp1;
                        mainPanel.world.followID = temp2;
                        if (!playing) {
                            mainPanel.repaint();
                        }
                        mainPanel.requestFocus();
                    }
                }
            });
            add(reset);

            add(dtLabel);

            clearArrows = new JButton("Clear arrows");
            clearArrows.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainPanel.world.impulses.clear();
                    mainPanel.world.normals.clear();
                    if (!playing) {
                        mainPanel.repaint();
                    }
                    mainPanel.requestFocus();
                }
            });
            add(clearArrows);

            setDt = new JButton("Set dt");
            setDt.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dt = CustomOptionMenu.doubleInput("Enter dt.");
                }
            });
            add(setDt);
        }

        private void makeBackup() {
            backupWorld = copyWorld(mainPanel.world);
        }

        private World copyWorld(World world) {
            World newWorld = new World(world.gravity);
            newWorld.time = world.time;
            for (Object o : world.objects) {
                Object newO = null;
                for (Shape s : o.shapes) {
                    s.vector.readyPoint();
                    if (s instanceof RectangleShape) {
                        RectangleShape rs = (RectangleShape) s;
                        if (o instanceof FixedObject) {
                            newO = new FixedObject(new RectangleShape(rs.width, rs.height, new Vector2D(new Point.Double(s.vector.point.x, s.vector.point.y)), s.deltaRotation, s.density, s.color), new Point.Double(o.position.x, o.position.y), o.material);
                        } else {
                            newO = new Object(new RectangleShape(rs.width, rs.height, new Vector2D(new Point.Double(s.vector.point.x, s.vector.point.y)), s.deltaRotation, s.density, s.color), new Point.Double(o.position.x, o.position.y), o.material);
                        }
                    } else if (s instanceof CircleShape) {
                        CircleShape cs = (CircleShape) s;
                        if (o instanceof FixedObject) {
                            newO = new FixedObject(new CircleShape(cs.radius, new Vector2D(new Point.Double(s.vector.point.x, s.vector.point.y)), s.deltaRotation, s.density, s.color), new Point.Double(o.position.x, o.position.y), o.material);
                        } else {
                            newO = new Object(new CircleShape(cs.radius, new Vector2D(new Point.Double(s.vector.point.x, s.vector.point.y)), s.deltaRotation, s.density, s.color), new Point.Double(o.position.x, o.position.y), o.material);
                        }
                    }
                }
                newO.velocity = o.velocity;
                newO.acceleration = o.acceleration;
                newO.angularVelocity = o.angularVelocity;
                newO.rotation = o.rotation;
                newWorld.objects.add(newO);
            }
            return newWorld;
        }

        private void updateLabels() {
            String dtString = Double.toString(dt);
            dtLabel.setText(("<html><font color=white> " + "dt : " + dtString + " </font></html>"));
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
                save.doClick();
                double timer = 0;
                while (true) {
                    Main.playing = true;
                    while (!paused) {
                        mainPanel.world.update(dt);
                        updateLabels();
                        mainPanel.repaint();
                        try {
                            if (dt * 1000 * panel.playbackSpeed < 1) {
                                timer += dt * 1000 * panel.playbackSpeed;
                                if (timer > 1) {
                                    timer = timer - 1;
                                    sleep(1);
                                }
                            } else {
                                sleep((int) (dt * 1000 * panel.playbackSpeed));
                            }
                        } catch (InterruptedException ex) {
                        }
                    }
                    Main.playing = false;
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
