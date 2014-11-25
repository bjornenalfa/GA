package engine;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JFrame {
    
    public Main(){
        setTitle("TITLE");
        
        MyJPanel panel = new MyJPanel();
        panel.setPreferredSize(new Dimension(800,600));
        setContentPane(panel);
        
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }
    
    
    class MyJPanel extends JPanel {
        
        @Override
        public void update(Graphics g){
            paintComponent(g);
        }
        
        @Override
        protected void paintComponent(Graphics g){
//            for (Shape shape : shapeList) {
//                shape.paint(g);
//            }
        }
        
    }
    
    public static void main(String[] args) {
        new Main();
    }
}