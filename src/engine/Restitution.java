package engine;

public class Restitution {

    //Wood = 0, Steel = 1, Rubber = 2, Concrete = 3, Ice = 4, Glass = 5
    private static final double[] restitution = {
        0.4, 0.75, 0.85, 0.2, 0.05, 0.5, 0.4, 0.4, 0.7, 0.4, 1.5
    };
    
    private static final double[] secondaryRestitution = {
        0.0, 0.0, 0.1, 0.0, 0.0, 0.0, 0.0, 0.0, 0.3, 0.0, 0.5
    };

    public static double get(int mat) {
        return restitution[mat];
    }
    
    public static double get2(int mat) {
        return secondaryRestitution[mat];
    }

    static double get(Object firstObject, Object secondObject) {
        return ((get(firstObject.material)+get2(secondObject.material))*firstObject.inverseMass+(get(secondObject.material)+get2(firstObject.material))*secondObject.inverseMass)/(firstObject.inverseMass+secondObject.inverseMass);
    }
}
