public class Stitch {
    private int stitchSpan; // number of stitches that will get stitched over
    private int stitchResult; // amount of stitches these instructions should result in
    private String type; // type of stitch, eg. knit, purl, invdec, sl1wyif, etc
    private String description = "none";


    // Constructor

    /**
     * This object represents one stitch
     * @param stitchSpan   The amount of stitches you will pick up/yarn over to perform this stitch, Ex. a decrease spans 2 stitches
     * @param stitchResult The amount of stitches this stitch results in, Ex. a decrease results in 1 stitch
     * @param type         The name of the stitch, EX. knit, cast-on, s1wyif
     */
    public Stitch(String type, int stitchSpan, int stitchResult, String description) {
        this.type = type;
        this.stitchSpan = stitchSpan;
        this.stitchResult = stitchResult;
        this.description = description;
    }

    // Methods
    public String toString() {
        return type;
    }

    public String toStitchUp(){
        return "stitch:" + type + "," + stitchSpan + "," + stitchResult + "," + description;
    }


    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Stitch) {
            Stitch stitch = (Stitch)obj;
            return (stitch.getStitchSpan() == stitchSpan && stitch.getStitchResult() == stitchResult && stitch.type.equals(type));
        }
        else return false;
    }


    // Getters and setters for all variables
    public void setStitchSpan(int stitchSpan) {
        this.stitchSpan = stitchSpan;
    }

    public int getStitchSpan() {
        return stitchSpan;
    }

    public void setStitchResult(int stitchResult) {
        this.stitchResult = stitchResult;
    }

    public int getStitchResult() {
        return stitchResult;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}