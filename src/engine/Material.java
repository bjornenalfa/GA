package engine;

/**
 *
 * @author pastapojken
 */
public class Material {

    private final double staticFriction, dynamicFriction;
    
    public static Material SteelOnSteel = new Material(0.74, 0.57);
    public static Material AluminumOnSteel = new Material(0.61, 0.47);
    public static Material CopperOnSteel = new Material(0.53, 0.36);
    public static Material RubberOnDryConcrete = new Material(1, 0.8);
    public static Material RubberOnWetConcrete = new Material(0.3, 0.25);
    public static Material WoodOnWood = new Material(0.375, 0.2);
    public static Material GlassOnGlass = new Material(0.94, 0.4);
    public static Material IceOnIce = new Material(0.1, 0.03);
    public static Material JointsInHumans = new Material(0.01, 0.003);
    
    private Material(double statFr, double dynFr){
        dynamicFriction=dynFr;
        staticFriction=statFr;
    }
}
