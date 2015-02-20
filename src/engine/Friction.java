package engine;

public class Friction {
    
    //Wood = 0, Steel = 1, Rubber = 2, Concrete = 3, Ice = 4, Glass = 5
    
    private static final double[][] staticFriction = {
    /*        W      S       R       C       I      G */    
    /* W */ {.375,  .4,     1.1,    .62,    .1,     .5}, 
    /* S */ {.4,    .74,    1.2,    .85,    .15,    .6},
    /* R */ {1.1,   1.2,    1.5,    1.3,    .8,     1.3},
    /* C */ {.62,   .85,    1.3,    .9,     .2,     .8},
    /* I */ {.1,    .2,     .8,     .2,     .1,     .08},
    /* G */ {.5,    .6,     1.3,    .8,     .08,    .94}
    };
    private static final double[][] dynamicFriction = {
    /*       W       S      R        C       I       G */        
    /* W */ {.2,    .25,    .7,     .42,    .03,    .4}, 
    /* S */ {.25,   .57,    .75,    .65,    .05,    .25},
    /* R */ {.7,    .75,    1,      .8,     .5,     .75},
    /* C */ {.42,   .65,    .8,     .75,    .07,    .5},
    /* I */ {.03,   .05,    .5,     .07,    .03,    .02},
    /* G */ {.4,    .25,    .75,    .5,     .02,    .4}
    };
    
    public static double getStatic(int mat1, int mat2){
        return staticFriction[mat1][mat2];
    }
    
    public static double getDynamic(int mat1, int mat2){
        return dynamicFriction[mat1][mat2];
    }
    
}