package engine;

import engine.Main.MyJPanel;
import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 *
 * @author ludsiw279
 */
public class OptionFrame extends JFrame {

    //1 length unit = 1cm
    //1 mass unit = 1g
    //1 force unit = 1g*cm/s/s
    MyOptionPanel panel;
    MyJPanel mainPanel;

    public OptionFrame(MyJPanel mainPanel) {
        setTitle("OPTIONFRAME");

        this.mainPanel = mainPanel;

        panel = new MyOptionPanel(mainPanel.world);
        setContentPane(panel);
        setMenuBar(new CustomOptionMenu(panel));
        pack();

        setAlwaysOnTop(true);
        setLocationRelativeTo(null);
        setLocation(getLocation().x + 420 + getSize().width / 2, getLocation().y);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public class MyOptionPanel extends JPanel {

        World world;
        double dt = 0;
        boolean paused;
        MyThread thread = new MyThread();
        
        public MyOptionPanel(World world) {
            setLayout(new GridLayout(2, 3, 5, 5));
            addKeyBindings();
            this.world = world;
            addButtons();
            add(new JLabel());
            add(new JLabel());
            add(new JLabel());
        }

        JButton update;
        JButton play;
        JButton pause;

        private void addButtons() {
            update = new JButton("Update");
            update.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (dt == 0) {
                        dt = Double.parseDouble(JOptionPane.showInputDialog(null, "Enter dt.", "TITLE", JOptionPane.QUESTION_MESSAGE));
                    }
                    world.update(dt);
                    mainPanel.repaint();
                }
            });
            add(update);

            play = new JButton("Play");
            play.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (dt == 0) {
                        dt = Double.parseDouble(JOptionPane.showInputDialog(null, "Enter dt.", "TITLE", JOptionPane.QUESTION_MESSAGE));
                        try {
                            thread.start();
                            System.out.println("started");
                        } catch (Exception ex) {
                        }
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
        }

        private void addKeyBindings() {
            char exit = KeyEvent.VK_ESCAPE;
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(exit), "exit");
            getActionMap().put("exit", exit());
            char updateChar = KeyEvent.VK_ESCAPE;
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(updateChar), "update");
            getActionMap().put("update", updat());
            char playChar = KeyEvent.VK_ESCAPE;
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(playChar), "play");
            getActionMap().put("play", play());
            char pauseChar = KeyEvent.VK_ESCAPE;
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(pauseChar), "pause");
            getActionMap().put("pause", pause());
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

        @Override
        public void update(Graphics g) {
            paintComponent(g);
        }

        @Override
        protected void paintComponent(Graphics g) {
        }

        class MyThread extends Thread {

            @Override
            public void run() {
                while (true) {
                    while (!paused) {
                        world.update(dt);
                        mainPanel.repaint();
                        try {
                            sleep(100);
                        } catch (InterruptedException ex) {
                            System.out.println("fak");
                        }
                    }
                }
            }
        }
    
    }
    
}
