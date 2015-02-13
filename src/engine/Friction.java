package engine;

public class Friction {
    
    
    //Wood = 0, Steel = 1, Rubber = 2, Concrete = 3, Ice = 4, Glass = 5
    
    static double[][] staticFriction = {
        {0.375,0,0,0,0,0}, 
        {0,0,0,0,0,0},
        {0,0,0,0,0,0},
        {0,0,0,0,0,0},
        {0,0,0,0,0,0},
        {0,0,0,0,0,0}
    };
    static double[][] dynamicFriction = {
        {0.2,0,0,0,0,0}, 
        {0,0,0,0,0,0},
        {0,0,0,0,0,0},
        {0,0,0,0,0,0},
        {0,0,0,0,0,0},
        {0,0,0,0,0,0}
    };
    
    public static double getStatic(int mat1, int mat2){
        return staticFriction[mat1][mat2];
    }
    
    public static double getDynamic(int mat1, int mat2){
        return dynamicFriction[mat1][mat2];
    }
    
}