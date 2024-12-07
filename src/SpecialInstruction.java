public class SpecialInstruction extends Instruction {
    private String description;
    private int stitchSpan;
    private int stitchResult;

    // Constructor
    /**
     * This object represents a non-stitch instruction that may need to happen during the project, Ex. storing stitches on a new needle, placing buttons
     * @param description A description of what is supposed to happen at this point.
     * @param stitchSpan The amount of stitches this instruction will take up.
     * @param stitchResult The amount of stitches this instruction will result in.
     */
    public SpecialInstruction(String description, int stitchSpan, int stitchResult) {
        super(null, 1, "");
        this.description = description;
        this.stitchSpan = stitchSpan;
        this.stitchResult = stitchResult;
    }

    /**
     * This SpecialInstruction constructor takes only a String describing the instruction and automatically sets span and result to 0
     * @param instruction A description of what is supposed to happen at this point.
     */
    public SpecialInstruction(String instruction) {
        super(null, 1, "");
        this.description = instruction;
        stitchSpan = 0;
        stitchResult = 0;
    }

    //Methods
    @Override
    public String toString() {
        return description;
    }


    @Override
    public boolean equals(Object obj) {
        if(obj instanceof SpecialInstruction) {
            SpecialInstruction i = (SpecialInstruction)obj;
            return (i.getStitchSpan() == stitchSpan && i.getStitchResult() == stitchResult && i.getDescription().equals(description));
        }
        else return false;
    }

    // Getters and setters
    public String getDescription() {
        return description;
    }
    public void setInstruction(String instruction) {
        this.description = instruction;
    }

    public int getStitchSpan() {
        return stitchSpan;
    }
    public void setStitchSpan(int stitchSpan) {
        this.stitchSpan = stitchSpan;
    }

    public int getStitchResult() {
        return stitchResult;
    }
    public void setStitchResult(int stitchResult) {
        this.stitchResult = stitchResult;
    }
}
