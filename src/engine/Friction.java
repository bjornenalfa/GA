package engine;

public class Friction {
    
    //Wood = 0, Steel = 1, Rubber = 2, Concrete = 3, Ice = 4, Glass = 5
    
    private static final double[][] staticFriction = {
        {.375,.4,1.3,.62,.05,0}, 
        {.4,.74,1.2,0,0,.6},
        {1.3,1.2,1.5,1,0.8,1.2},
        {.62,0,1,0,0,0},
        {.05,0,.8,0,.1,0},
        {0,.6,1.2,0,0,.94}
    };
    private static final double[][] dynamicFriction = {
        {.2,.25,0,.42,.03,0}, 
        {.25,.57,0,0,0,.25},
        {0,0,0,.8,0,0},
        {.42,0,.8,0,0,0},
        {.03,0,0,0,.03,0},
        {0,.25,0,0,0,.4}
    };
    
    public static double getStatic(int mat1, int mat2){
        return staticFriction[mat1][mat2];
    }
    
    public static double getDynamic(int mat1, int mat2){
        return dynamicFriction[mat1][mat2];
    }
    
}