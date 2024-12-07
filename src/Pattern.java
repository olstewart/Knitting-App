import java.util.ArrayList;

public class Pattern{
    String patternName;
    ArrayList<Row> rows;
    ArrayList<Stitch> stitches;

    /**
     * Creates a pattern under a specific name with a currentRow of 0 and initializes an ArrayList of Rows.
     * @param patternName The name of the pattern.
     */
    public Pattern(String patternName){
        this.patternName = patternName;
        rows = new ArrayList<>();
        ArrayList<Stitch> stitchList = new ArrayList<>();
        stitchList.add(new Stitch("knit",1, 1,  "basic knit stitch"));
        stitchList.add(new Stitch("purl",1, 1,  "basic purl stitch"));
        stitchList.add(new Stitch("cast-on",0, 1,  "simple cast-on"));
        stitchList.add(new Stitch("cast-off",1, 0,  "simple cast-off"));
        stitchList.add(new Stitch("decrease",2, 1,  "pick up two stitches but knit only one"));
        stitchList.add(new Stitch("increase", 1, 2, "knit two in same stitch"));
        stitches = stitchList;
    }

    public Pattern(String patternName, ArrayList<Row> rows, ArrayList<Stitch> stitches){
        this.patternName = patternName;
        this.rows = rows;
        this.stitches = stitches;
    }

    /**
     * Adds existing Row object to row ArrayList.
     * @param r Row you want to add.
     */
    public void addRow(Row r){
        rows.add(r);
    }

    /**
     * Adds existing Row object to row ArrayList at specific index
     * @param r Row you want to add
     * @param index Index to be added at
     */
    public void addRowAt(Row r, int index){
        rows.add(index, r);
    }

    /**
     * Adds existing stitch to stitch Arraylist.
     * @param s Stitch you want to add.
     */
    public void addStitch(Stitch s){
        stitches.add(s);
    }

    public ArrayList<Stitch> getStitches() {
        return stitches;
    }

    public void setStitches(ArrayList<Stitch> stitches) {
        this.stitches = stitches;
    }

    public ArrayList<Row> getRows() {
        return rows;
    }

    public String getPatternName() {
        return patternName;
    }

    public void setPatternName(String patternName) {
        this.patternName = patternName;
    }

    /**
     * Returns a display of the stitches used in this particular pattern
     * @return A display of the list of stitches used in this pattern
     */
    public String listOfStitches(){
        String str = "Stitches used in " + patternName + ":";
        for(Stitch s : stitches){
            str += "\n" + s.toString();
        }
        return str;
    }

    @Override
    public String toString() {
        String str = "Pattern: " + patternName + "\n";
        if(rows.size() > 0){
            for (int i = 0; i < rows.size(); i++) {
                str += "\nRow " + (i + 1) + ": " + rows.get(i).toString();
            }
        }
        else str = "Pattern " + patternName + " is currently empty!";
        return str;
    }

    public String toStitchUp(){
        String str = patternName + "\n";
        for(Stitch s : stitches){
            str += s.toStitchUp() + "\n";
        }
        for(Row r : rows){
            str += r.toStitchUp() + "\n";
        }
        return str;
    }
}