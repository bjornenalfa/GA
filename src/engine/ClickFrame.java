package engine;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ClickFrame extends JFrame {

    Object object;
    int shapeIndex;
    Main.MyJPanel mainPanel;

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
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setAlwaysOnTop(true);
        setLocation((int) (mainPanel.getLocationOnScreen().x + object.position.x + 70), (int) (mainPanel.getLocationOnScreen().y + object.position.y - (getHeight()+50)));
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
            setLayout(new GridLayout(7, 1, 5, 5));
            addButtonsLabels();
        }

        JLabel positionLabel;
        JLabel velocityLabel;
        JLabel angularVelocityLabel;
        JLabel inertiaLabel;
        JLabel massLabel;

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

            String[] matArray = {"Wood", "Steel", "Rubber", "Concrete", "Ice", "Glass", "Boost", "Slow"};
            final JComboBox comboBox = new JComboBox(matArray);
            comboBox.setSelectedIndex(object.material);
            comboBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    object.material = comboBox.getSelectedIndex();
                }
            });
            add(comboBox);
            
            massLabel = new JLabel("M : " + object.mass, SwingConstants.CENTER);
            add(massLabel);
            inertiaLabel = new JLabel("I : " + object.inertia, SwingConstants.CENTER); 
            add(inertiaLabel);
            positionLabel = new JLabel("P : " + object.position, SwingConstants.CENTER);
            add(positionLabel);
            velocityLabel = new JLabel("V : " + object.velocity, SwingConstants.CENTER);
            add(velocityLabel);
            angularVelocityLabel = new JLabel("AV : " + object.angularVelocity, SwingConstants.CENTER);
            add(angularVelocityLabel);
        }
    }
}
