import java.util.Locale;

public class Instruction{
    private int stitchCount;
    private Stitch stitch;
    private String color;

    // Constructors
    /**
     * The instruction object is used to represent a group of stitches of the same type and color, Ex. Knit 4 st in green
     * @param stitch The stitch being used in this instruction.
     * @param stitchCount The number of times to perform this stitch.
     * @param color The color of these stitches
     */
    Instruction(Stitch stitch, int stitchCount, String color){
        this.stitchCount = stitchCount;
        this.stitch = stitch;
        this.color = color;
    }

    /**
     * This instruction constructor is for knitting that does not have a specified color
     * @param stitch The stitch being used in this instruction.
     * @param stitchCount The number of times to perform this stitch.
     */
    Instruction(Stitch stitch, int stitchCount){
        this.stitchCount = stitchCount;
        this.stitch = stitch;
        this.color = "none";
    }

    //Methods
    @Override
    public String toString() {
        if(color.equals("none")){
            return stitch.toString() + " " + stitchCount;
        }
        else{
            return stitch.toString() + " " + stitchCount + " in " + color;
        }
    }

    public String toStitchUp() {
        return stitch.getType() + "," + stitchCount + "," + color;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Instruction) {
            Instruction i = (Instruction)obj;
            return (i.getStitch().equals(stitch) && i.getStitchCount() == stitchCount && i.getColor().equals(color));
        }
        else return false;
    }

    // Getters and setters
    public int getStitchSpan(){
        return stitchCount * stitch.getStitchSpan();
    }

    public int getStitchResult(){
        return stitchCount * stitch.getStitchResult();
    }


    public int getStitchCount() {
        return stitchCount;
    }
    public void setStitchCount(int stitchCount) {
        this.stitchCount = stitchCount;
    }

    public Stitch getStitch() {
        return stitch;
    }
    public void setStitch(Stitch stitch) {
        this.stitch = stitch;
    }

    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }


}
