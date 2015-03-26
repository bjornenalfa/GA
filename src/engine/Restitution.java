package engine;

public class Restitution {
    
    //Wood = 0, Steel = 1, Rubber = 2, Concrete = 3, Ice = 4, Glass = 5
    
    private static final double[] restitution = {
        0.4,0.75,0.85,0.2,0.05,0.5,0.4,0.4,1.0,0.4
    };
    
    public static double get(int mat){
        return restitution[mat];
    }
}