import java.util.ArrayList;

public class Row{
    private ArrayList<Instruction> instructions;

    // Constructors
    /**
     * Default constructor for Row, just initializes the array
     */
    public Row(){
        instructions = new ArrayList<>();
    }

    /**
     * Copies an existing array of instructions into the array
     * @param instructions
     */
    public Row(ArrayList<Instruction> instructions){
        this.instructions = instructions;
    }

    // Methods

    //-- EXPORT METHOD--
    public String toStitchUp(){
        String str = "";
        for(int i = 0; i < instructions.size() - 1; i++){
            str += instructions.get(i).toStitchUp() + ",,";
        }
        if(instructions.size() > 0){
            str += instructions.getLast().toStitchUp();
        }
        return str;
    }

    //-- ADD METHODS --
    /**
     * Adds an existing instruction to the array
     * @param instruction The instruction to be added
     */
    public void addInstruction(Instruction instruction){
        instructions.add(instruction);
    }

    /**
     * Creates a new instruction and adds it to the array.
     * @param stitch The stitch being used in this instruction.
     * @param stitchCount The number of times to perform this stitch.
     */
    public void addNewInstruction(Stitch stitch, int stitchCount){
        instructions.add(new Instruction(stitch, stitchCount));
    }

    /**
     * Creates a new instruction and adds it to the array.
     * @param stitch The stitch being used in this instruction.
     * @param stitchCount The number of times to perform this stitch.
     * @param color The color of these stitches
     */
    public void addNewInstruction(Stitch stitch, int stitchCount, String color){
        instructions.add(new Instruction(stitch, stitchCount, color));
    }

    /**
     * Creates a new instruction and adds it to the array at a specific index.
     * @param stitch The stitch being used in this instruction.
     * @param stitchCount The number of times to perform this stitch.
     * @param index The index to insert the new instruction
     */
    public void addNewInstructionAt(Stitch stitch, int stitchCount, int index){
        instructions.add(index, new Instruction(stitch, stitchCount));
    }

    /**
     * Creates a new instruction and adds it to the array at a specific index.
     * @param stitch The stitch being used in this instruction.
     * @param stitchCount The number of times to perform this stitch.
     * @param color The color of these stitches.
     * @param index The index to insert the new instruction.
     */
    public void addNewInstructionAt(Stitch stitch, int stitchCount, String color, int index){
        instructions.add(index, new Instruction(stitch, stitchCount, color));
    }


    /**
     * Creates a new SpecialInstruction and adds it to the array.
     * @param instruction A description of what is supposed to happen at this point.
     */
    public void addNewSpecialInstruction(String instruction){
        instructions.add(new SpecialInstruction(instruction));
    }

    /**
     * Creates a new SpecialInstruction and adds it to the array.
     * @param instruction A description of what is supposed to happen at this point.
     * @param stitchSpan The amount of stitches this instruction will take up.
     * @param stitchResult The amount of stitches this instruction will result in.
     */
    public void addNewSpecialInstruction(String instruction, int stitchSpan, int stitchResult){
        instructions.add(new SpecialInstruction(instruction, stitchSpan, stitchResult));
    }

    //--REMOVAL METHOD--

    /**
     * Removes an instruction at a specific index
     * @param index the index of the instruction in question
     */
    public void removeInstruction(int index){
        if(index >= 0 && index < instructions.size()){
            instructions.remove(index);
        }
    }

    //--DISPLAY METHOD--
    @Override
    public String toString() {
        String str = "";
        if (instructions.isEmpty()) {
            return "Row is currently empty.";
        }
        for(int i = 0; i < instructions.size() - 1; i++){
            str += instructions.get(i).toString() + ", ";
        }
        str += instructions.getLast().toString();
        str += " (" + this.getStitchResult() + " st)";
        return str;
    }

    //--STITCH COUNTING METHODS--
    public int getStitchResult(){
        int count = 0;
        for (Instruction i : instructions) {
            count += i.getStitchResult();
        }
        return count;
    }

    public int getStitchSpan(){
        int count = 0;
        for (Instruction i : instructions) {
            count += i.getStitchSpan();
        }
        return count;
    }

    // Getters and setters
    public ArrayList<Instruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(ArrayList<Instruction> instructions) {
        this.instructions = instructions;
    }

    public Instruction getInstructionAt(int index){
        if(index >= 0 && index < instructions.size()){
            return instructions.get(index);
        }
        Instruction i = null;
        return i;
    }

    public boolean setInstructionAt(int index, Instruction instruction){
        if(index >= 0 && index < instructions.size()){
            instructions.set(index, instruction);
            //TODO: If instructions.get(index) is equal to the instruction asked for, return true
            if(instructions.get(index).equals(instruction)){
                return true;
            }
        }
        return false;
    }
}
