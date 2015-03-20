package engine;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

public class ClickFrame extends JFrame {

    Object object;
    int shapeIndex;
    Main.MyJPanel mainPanel;

    JLabel positionLabel;
    JLabel velocityLabel;
    JLabel angularVelocityLabel;
    JLabel inertiaLabel;
    JLabel massLabel;

    public ClickFrame(Main.MyJPanel mainPane, Object objec, Shape shape) {
        setTitle("");

        object = objec;
        mainPanel = mainPane;
        shapeIndex = object.shapes.indexOf(shape);

        MyPanel panel = new MyPanel();

        setContentPane(panel);
        getContentPane().setPreferredSize(new Dimension(200, 200));
        pack();
        addWindowListener(windowListener());
        addComponentListener(componentListener());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setAlwaysOnTop(true);
        setLocation((int) (mainPanel.getLocationOnScreen().x + object.position.x / Main.scale - Main.translateX + 70 / Main.scale), (int) (mainPanel.getLocationOnScreen().y + object.position.y / Main.scale - Main.translateY - (getHeight() + 50) / Main.scale));
        setVisible(true);

        if (!Main.playing) {
            mainPanel.repaint();
        }
        mainPanel.requestFocus(true);
    }

    private WindowAdapter windowListener() {
        return new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                mainPanel.world.clickFrameList.remove(ClickFrame.this);
                if (!Main.playing) {
                    mainPanel.repaint();
                }
            }
        };
    }

    private ComponentAdapter componentListener() {
        return new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                if (!Main.playing) {
                    mainPanel.repaint();
                }
            }
        };
    }

    public void setLabelTexts() {
        massLabel.setText("M : " + object.mass);
        inertiaLabel.setText("I : " + object.inertia);
        positionLabel.setText("P : " + object.position);
        velocityLabel.setText("V : " + object.velocity);
        angularVelocityLabel.setText("AV : " + object.angularVelocity);
    }

    class MyPanel extends JPanel {

        public MyPanel() {
            setLayout(new GridLayout(7, 1, 5, 5));
            addButtonsLabels();

            char exit = KeyEvent.VK_ESCAPE;
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(exit), "exit");
            getActionMap().put("exit", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ClickFrame.this.dispose();
                }
            });
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

            String[] matArray = {"Wood", "Steel", "Rubber", "Concrete", "Ice", "Glass", "Boost", "Slow", "Bounce"};
            final JComboBox comboBox = new JComboBox(matArray);
            comboBox.setSelectedIndex(object.material);
            comboBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    object.material = comboBox.getSelectedIndex();
                }
            });
            add(comboBox);

            massLabel = new JLabel("", SwingConstants.CENTER);
            add(massLabel);
            inertiaLabel = new JLabel("", SwingConstants.CENTER);
            add(inertiaLabel);
            positionLabel = new JLabel("", SwingConstants.CENTER);
            add(positionLabel);
            velocityLabel = new JLabel("", SwingConstants.CENTER);
            add(velocityLabel);
            angularVelocityLabel = new JLabel("", SwingConstants.CENTER);
            add(angularVelocityLabel);

            setLabelTexts();
        }
    }
}
