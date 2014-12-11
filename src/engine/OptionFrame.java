package engine;

import engine.Main.MyJPanel;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;
import javax.swing.JFrame;
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
    MyJPanel myPanel;

    public OptionFrame(MyJPanel myPanel) {
        setTitle("OPTIONFRAME");

        this.myPanel = myPanel;

        panel = new MyOptionPanel(myPanel.world);
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
        }

        private void addButtons() {
            JButton update = new JButton("Update");
            update.setMnemonic(KeyEvent.VK_U);
            update.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (dt == 0) {
                        dt = Double.parseDouble(JOptionPane.showInputDialog(null, "Enter dt.", "TITLE", JOptionPane.QUESTION_MESSAGE));
                    }
                    world.update(dt);
                    myPanel.repaint();
                }
            });
            add(update);

            JButton play = new JButton("Play");
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

            JButton pause = new JButton("Pause");
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
        }

        private Action exit() {
            return new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
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
                        myPanel.repaint();
                        try {
                            sleep(200);
                        } catch (InterruptedException ex) {
                            System.out.println("fak");
                        }
                    }
                }
            }
        }

    }

}
