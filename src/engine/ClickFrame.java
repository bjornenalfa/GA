package engine;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

public class ClickFrame extends JFrame {

    Object object;
    int shapeIndex;
    Main.MyJPanel mainPanel;

    public ClickFrame(Main.MyJPanel mainPanel, Object object, Shape shape) {
        setTitle("");

        MyPanel panel = new MyPanel();
        this.object = object;
        this.mainPanel = mainPanel;
        shapeIndex = object.shapes.indexOf(shape);

        setContentPane(panel);
        getContentPane().setPreferredSize(new Dimension(200, 100));
        pack();
        addWindowListener(windowListener());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setAlwaysOnTop(true);
        setLocation((int) (mainPanel.getLocationOnScreen().x + object.position.x + 70), (int) (mainPanel.getLocationOnScreen().y + object.position.y - 150));
        setVisible(true);

        if (!Main.playing) {
            mainPanel.repaint();
        }
    }

    private WindowAdapter windowListener() {
        return new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                mainPanel.clickFrameList.remove(ClickFrame.this);
                if (!Main.playing) {
                    mainPanel.repaint();
                }
            }
        };
    }

    class MyPanel extends JPanel {

        public MyPanel() {
            setLayout(new GridLayout(2, 3, 5, 5));
            addButtonsLabels();
            addKeyBindings();

            MouseAdapter ma = mouseAdapter();
            addMouseListener(ma);
            addMouseMotionListener(ma);
            addMouseWheelListener(ma);
        }

        JLabel label1;
        JLabel label2;
        JLabel label3;

        @Override
        protected void paintComponent(Graphics g) {

        }

        private void addButtonsLabels() {
            JButton button1 = new JButton("Follow this");
            button1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainPanel.world.followID = mainPanel.world.objects.indexOf(object);
                    mainPanel.world.follow = true;
                    if (!Main.playing) {
                        mainPanel.repaint();
                    }
                }
            });
            add(button1);
            add(new JLabel(""));
            add(new JLabel(""));

            if (mainPanel.dt != 0) {
                label1 = new JLabel("Position : " + object.position, SwingConstants.CENTER);
                add(label1);
                label2 = new JLabel("Velocity : " + object.velocity, SwingConstants.CENTER);
                add(label2);
                label3 = new JLabel("AngularVelocity : " + object.angularVelocity, SwingConstants.CENTER);
                add(label3);
            }
        }

        private void addKeyBindings() {
            getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_0, 0), "keyBinding");
            getActionMap().put("keyBinding", keyBinding());
        }

        private Action keyBinding() {
            return new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                }
            };
        }

        private MouseAdapter mouseAdapter() {
            return new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent me) {
                }
            };
        }
    }
}
