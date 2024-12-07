import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main{
    public static void main(String[] args){
        Scanner intScanner = new Scanner(System.in);
        Scanner strScanner = new Scanner(System.in);

        ArrayList<Pattern> userPatterns = new ArrayList<>();

        System.out.println("Welcome to StitchUp, the free digital yarnwork pattern-making app!");
        displayMainMenu();
        int choice = intScanner.nextInt();
        while(choice != 0){
            switch(choice){
                // Creates a new empty pattern under the specified name.
                case 1: {
                    System.out.print("Enter new pattern name: ");
                    String newPatternName = strScanner.nextLine();
                    if (userPatterns.add(new Pattern(newPatternName))){
                        System.out.println("New pattern " + userPatterns.getLast().getPatternName() + " created!\n");
                    }
                    else {
                        System.out.println("Pattern creation failed.\n");
                    }
                    break;
                }
                // Displays patterns that have been loaded into the app.
                case 2: {
                    if (userPatterns.isEmpty()){
                        System.out.println("No patterns found.\n");
                        break;
                    }
                    displayPatterns(userPatterns);
                    System.out.println("Choose a pattern to view or type 0 to exit.");
                    int patternToView = intScanner.nextInt();
                    while (patternToView != 0){
                        if(patternToView <= userPatterns.size() && patternToView > 0){
                            System.out.println(userPatterns.get(patternToView - 1));
                            editPattern(userPatterns.get(patternToView - 1));
                            break;
                        }
                        else {
                            System.out.print("Invalid choice, please re-enter or type 0 to exit: ");
                            patternToView = intScanner.nextInt();
                        }
                    }
                    break;
                }
                // Takes in a StitchUp file and converts it into a pattern object
                case 3:
                    System.out.println("Importing...");
                    break;
                // Exports a pattern into a human-readable file.
                case 4:
                    if (userPatterns.isEmpty()){
                        System.out.println("No patterns found.\n");
                        break;
                    }
                    displayPatterns(userPatterns);
                    System.out.println("Choose a pattern to export or type 0 to exit.");
                    int patternToExport = intScanner.nextInt();
                    if(patternToExport == 0){
                        System.out.println("Export cancelled.\n");
                        break;
                    }
                    while (patternToExport < 1 || patternToExport > userPatterns.size()){
                        System.out.print("Invalid choice, please input again: ");
                        patternToExport = intScanner.nextInt();
                    }
                    exportPattern(userPatterns.get(patternToExport - 1));
                    break;
                // Tells users how to navigate the app.
                case 5:
                    displayManual();
                    break;
                default: System.out.println("Invalid choice");
            }
            displayMainMenu();
            choice = intScanner.nextInt();
        }

    }

    public static void exportPattern(Pattern pattern){
        System.out.println("Exporting pattern " + pattern.getPatternName() + "...");
        String filename = pattern.getPatternName() + ".txt";
        try {
            FileOutputStream fileOut = new FileOutputStream(filename);
            PrintWriter printer = new PrintWriter(fileOut);

            printer.print(pattern);

            printer.close();
            fileOut.close();
        } catch (FileNotFoundException e){
            System.out.println("Pattern not found");
        } catch(IOException e){
            System.out.println("IO error");
        }
    }

    // Given a pattern object, this function allows users to edit its attributes.
    public static void editPattern(Pattern pattern){
        Scanner intScanner = new Scanner(System.in);
        Scanner strScanner = new Scanner(System.in);
        displayPatternEditMenu();
        int choice = intScanner.nextInt();
        while(choice != 0){
            switch(choice){
                // Displays the pattern in a human-readable way. This is what the pattern will look like when exported.
                case 1: {
                    System.out.println(pattern.toString() + "\n");
                } break;
                // Changes pattern name
                case 2: {
                    System.out.print("Enter new pattern name, or enter 0 to cancel: ");
                    String newPatternName = strScanner.nextLine();
                    if(newPatternName.equals("0")){
                            System.out.print("Name change cancelled.\n");
                            break;
                    }
                    pattern.setPatternName(newPatternName);
                    System.out.println("Pattern is now called " + pattern.getPatternName() + "!\n");
                } break;
                // Allows user to add a new row
                case 3: {
                    System.out.println("[1] Add row to end of pattern");
                    System.out.println("[2] Add row to specific index");
                    System.out.println("[0] Cancel");
                    int rowOption = intScanner.nextInt();
                    if(rowOption == 0){
                        System.out.println("Row addition cancelled.\n");
                        break;
                    }
                    while(rowOption > 2 || rowOption < 0){
                        System.out.print("Invalid choice, please input again: ");
                        rowOption = intScanner.nextInt();
                    }
                    switch(rowOption){
                        // Adds a new row to the end of the pattern
                        case 1: {
                            pattern.addRow(new Row());
                            System.out.println("New row has been added!\n");
                        } break;
                        //  
                        case 2: {
                            if(pattern.getRows().isEmpty()){
                                System.out.println("Pattern is empty, no row index exists yet!\n");
                                break;
                            }
                            displayRows(pattern);
                            System.out.print("Desired index of your new row: ");
                            int desiredIndex = intScanner.nextInt();
                            if(desiredIndex == 0){
                                System.out.println("Row addition cancelled.\n");
                                break;
                            }
                            while (desiredIndex < 1 || desiredIndex > pattern.getRows().size()) {
                                System.out.print("Invalid index. Please try again: ");
                                desiredIndex = intScanner.nextInt();
                            }
                            pattern.addRowAt(new Row(), desiredIndex - 1);
                            System.out.println("New row [" + desiredIndex + "] has been added!\n");
                        } break;
                        default:{
                            System.out.print("Invalid choice.");
                        }
                    }
                } break;
                // Allows user to edit a selected row
                case 4: {
                    displayRows(pattern);
                    if(pattern.getRows().isEmpty()){
                        break;
                    }
                    System.out.println("Which row do you to want edit? (Type 0 to cancel)");
                    int rowToEdit = intScanner.nextInt();
                    while(rowToEdit > pattern.getRows().size() || rowToEdit < 0){
                        System.out.print("Invalid choice, please input again: ");
                        rowToEdit = intScanner.nextInt();
                    }
                    if(rowToEdit == 0){
                        break;
                    }
                    editRow(pattern.getRows().get(rowToEdit - 1), pattern);
                } break;
                // Allows user to delete a selected row
                case 5: {
                    displayRows(pattern);
                    if(pattern.getRows().isEmpty()){
                        break;
                    }
                    System.out.println("Which row do you want delete? (Type 0 to cancel)");
                    int rowToDelete = intScanner.nextInt();
                    while(rowToDelete > pattern.getRows().size() || rowToDelete < 0){
                        System.out.print("Invalid choice, please input again: ");
                        rowToDelete = intScanner.nextInt();
                    }
                    if(rowToDelete == 0){
                        System.out.println("Row deletion canceled.\n");
                        break;
                    }
                    pattern.getRows().remove(rowToDelete - 1);
                    System.out.println("Row deleted!\n");

                } break;
                // Allows the user to add a new stitch
                case 6: {
                    System.out.println(pattern.listOfStitches());
                    System.out.println("This adds a stitch");
                } break;
                // Allows the user to edit which stitches are used in the pattern.
                case 7: {
                    System.out.print(pattern.listOfStitches());
                    System.out.println("This edits a stitch");
                } break;
                // Deletes a stitch, but only if the stitch isn't used in the pattern.
                case 8: {
                    System.out.print(pattern.listOfStitches());
                    System.out.println("This deletes an unused stitch");
                } break;

                default: System.out.println("Invalid choice");
            }
            displayPatternEditMenu();
            choice = intScanner.nextInt();
        }
    }

    // Allows user to edit attributes of a particular row.
    public static void editRow(Row row, Pattern pattern){
        Scanner intScanner = new Scanner(System.in);
        Scanner strScanner = new Scanner(System.in);
        displayInstructions(row);
        displayRowEditMenu();
        int choice = intScanner.nextInt();
        while(choice != 0){
            switch (choice){
                // Adds an instruction
                case 1: {
                    // If there are no stitches, an instruction cannot be made.
                    if(pattern.getRows().isEmpty()){
                        System.out.println("There are no stitches defined.\n");
                        break;
                    }

                    displayStitches(pattern);

                    // Saves the new instruction's stitch
                    System.out.println("Choose a stitch: ");
                    int stitchChoice = intScanner.nextInt();
                    while(stitchChoice < 0 || stitchChoice > pattern.getStitches().size()){
                        System.out.print("Invalid choice, please input again: ");
                        stitchChoice = intScanner.nextInt();
                    }
                    Stitch stitch = pattern.getStitches().get(stitchChoice - 1);

                    // Saves the new stitches stitch count
                    System.out.println("Stitch count: ");
                    int stitchCount = intScanner.nextInt();
                    while(stitchCount < 1){
                        System.out.print("Stitch count must be greater than 0: ");
                        stitchCount = intScanner.nextInt();
                    }

                    // Saves color, if any
                    System.out.println("Enter stitch color, or 0 to make stitch with no color:");
                    String stitchColor = strScanner.nextLine();

                    // Saves index to place the instruction
                    System.out.println("Enter instruction index, or 0 to add it at the end of the row: ");
                    int instructionIndex = intScanner.nextInt();
                    if(instructionIndex == 0){
                        if(stitchColor.equals("0")){
                            row.addNewInstruction(stitch, stitchCount);
                        }
                        else{
                            row.addNewInstruction(stitch, stitchCount, stitchColor);
                        }
                        System.out.println("New instruction added!\n");
                        break;
                    }
                    while(instructionIndex < 1 || instructionIndex > row.getInstructions().size()){
                        System.out.print("Invalid index. Please input again: ");
                        instructionIndex = intScanner.nextInt();
                    }
                    if(stitchColor.equals("0")){
                        row.addNewInstructionAt(stitch, stitchCount, instructionIndex - 1);
                    }
                    else{
                        row.addNewInstructionAt(stitch, stitchCount, stitchColor, instructionIndex - 1);
                    }
                    System.out.println("New instruction added!\n");
                } break;
                // Edits an instruction
                case 2: {
                    if(row.getInstructions().isEmpty()){
                        break;
                    }
                    displayInstructions(row);

                    System.out.println("Which instruction do you want to edit? (Type 0 to cancel)");
                    int instructionToEdit = intScanner.nextInt();
                    while(instructionToEdit > row.getInstructions().size() || instructionToEdit < 0){
                        System.out.print("Invalid choice, please input again: ");
                        instructionToEdit = intScanner.nextInt();
                    }
                    if(instructionToEdit == 0){
                        break;
                    }
                    editInstruction(row.getInstructionAt(instructionToEdit - 1), pattern);
                } break;
                // Deletes an instruction
                case 3: {
                    displayInstructions(row);
                    if(row.getInstructions().isEmpty()){
                        break;
                    }
                    System.out.println("Which instruction do you want delete? (Type 0 to cancel)");
                    int instructionToDelete = intScanner.nextInt();
                    while(instructionToDelete > row.getInstructions().size() || instructionToDelete < 0){
                        System.out.print("Invalid choice, please input again: ");
                        instructionToDelete = intScanner.nextInt();
                    }
                    if(instructionToDelete == 0){
                        System.out.println("Instruction deletion canceled.\n");
                        break;
                    }
                    row.getInstructions().remove(instructionToDelete - 1);
                    System.out.println("Instruction deleted!\n");

                } break;
                default: System.out.println("Invalid choice.");
            }
            displayInstructions(row);
            displayRowEditMenu();
            choice = intScanner.nextInt();
        }
    }

    // Allows user to edit instructions
    public static void editInstruction(Instruction instruction, Pattern pattern){
        Scanner intScanner = new Scanner(System.in);
        Scanner strScanner = new Scanner(System.in);
        System.out.println(instruction + "\n");
        displayInstructionEditMenu();
        int choice = intScanner.nextInt();
        while(choice != 0){
            switch (choice){
                // Changes the stitch type of the instruction
                case 1: {
                    displayStitches(pattern);
                    System.out.print("Choose a stitch, or enter 0 to cancel: ");
                    int stitchChoice = intScanner.nextInt();
                    if(stitchChoice == 0){
                        System.out.println("Stitch change cancelled.\n");
                        break;
                    }
                    while(stitchChoice < 1 || stitchChoice > pattern.getStitches().size()){
                        System.out.print("Invalid choice, please input again: ");
                        stitchChoice = intScanner.nextInt();
                    }
                    instruction.setStitch(pattern.getStitches().get(stitchChoice - 1));
                } break;
                // Changes stitch count of the instruction
                case 2: {
                    System.out.print("Enter the desired stitch count, or enter 0 to cancel: ");
                    int stitchCount = intScanner.nextInt();
                    if(stitchCount == 0){
                        System.out.println("Stitch count change cancelled.\n");
                        break;
                    }
                    while(stitchCount < 0){
                        System.out.print("Stitch count must be greater than 0: ");
                        stitchCount = intScanner.nextInt();
                    }
                    instruction.setStitchCount(stitchCount);
                } break;
                // Changes color of the instruction
                case 3: {
                    System.out.print("Enter the desired stitch color, enter \"none\" to remove color, enter 0 to cancel: ");
                    String stitchColor = strScanner.nextLine();
                    if(stitchColor.equals("0")){
                        System.out.println("Color change cancelled.\n");
                    }
                    else if(stitchColor.equals("none")){
                        instruction.setColor("");
                    }
                    else{
                        instruction.setColor(stitchColor);
                    }
                } break;
                default: System.out.println("Invalid choice.");
            }
            System.out.println(instruction + "\n");
            displayInstructionEditMenu();
            choice = intScanner.nextInt();
        }
    }



    // Displays main menu
    public static void displayMainMenu() {
        System.out.println("[1]: Create New Pattern\n" +
                "[2]: View/Edit Existing Pattern\n" +
                "[3]: Import Pattern\n" +
                "[4]: Export Pattern\n" +
                "[5]: User Manual\n" +
                "[0]: Exit Program\n");
    }

    // Displays pattern editing menu
    public static void displayPatternEditMenu(){
        System.out.println("[1]: View Pattern\n" +
                "[2]: Edit Pattern Name\n" +
                "---------------------------\n"+
                "[3]: Add New Row\n" +
                "[4]: Edit Row\n" +
                "[5]: Delete Row\n" +
                "---------------------------\n"+
                "[6]: Add New Stitch\n" +
                "[7]: Edit Stitch\n" +
                "[8]: Delete Stitch\n" +
                "[0]: Back\n");
    }

    // Displays row editing menu
    public static void displayRowEditMenu(){
        System.out.println("[1]: Add Instruction\n" +
                "[2]: Edit Instruction\n" +
                "[3]: Delete Instruction\n" +
                "[0]: Back\n");
    }

    // Displays instruction editing menu
    public static void displayInstructionEditMenu(){
        System.out.println("[1]: Change Stitch\n" +
                "[2]: Change Stitch Count\n" +
                "[3]: Change Color\n" +
                "[0]: Back\n");
    }

    //Displays stitch editing menu
    public static void displayStitchEditMenu(){
        System.out.println("[1]: Add New Stitch\n" +
                "[2]: Edit Stitch\n" +
                "[3]: Delete Stitch\n" +
                "[0]: Back\n");
    }

    // Prints a list of the patterns currently loaded in
    public static void displayPatterns(ArrayList<Pattern> userPatterns){
        System.out.println("Your patterns:");
        for(int i = 0; i<userPatterns.size(); i++){
            System.out.println("[" + (i + 1) + "] " + userPatterns.get(i).getPatternName());
        }
    }

    // Prints a list of rows in a particular pattern
    public static void displayRows(Pattern pattern){
        System.out.println("Rows:");
        if(pattern.getRows().isEmpty()){
            System.out.println("This pattern has no rows yet.\n");
        }else{
            for(int i = 0; i < pattern.getRows().size(); i++){
                System.out.println("[" + (i + 1) + "] " + pattern.getRows().get(i).toString());
            }
        }
    }

    // Prints a list of instructions in a particular row
    public static void displayInstructions(Row row){
        System.out.println("Instructions:");
        if(row.getInstructions().isEmpty()){
            System.out.println("This row has no instructions yet.\n");
        }else{
            for(int i = 0; i < row.getInstructions().size(); i++){
                System.out.println("[" + (i + 1) + "] " + row.getInstructions().get(i).toString());
            }
            System.out.println();
        }

    }

    // Prints a  of stitches in a particular pattern
    public static void displayStitches(Pattern pattern){
        if(pattern.getStitches().isEmpty()){
            System.out.println("This row has no instructions yet.\n");
        }else{
            for(int i = 0; i < pattern.getStitches().size(); i++){
                System.out.println("[" + (i + 1) + "] " + pattern.getStitches().get(i).toString());
            }
        }
    }

    public static void displayManual(){
        System.out.println("New to StitchUp? Here's how to use it!");
        System.out.println("The first thing you're going to do is create a pattern in the main menu,\n" +
                "and then give your new pattern a title.");
        System.out.println("Next, enter the pattern viewer and editor. Your new pattern will be completely \n" +
                "empty, but it comes with a library of 6 preset knitting stitches that you can write your \n" +
                "pattern with.");
        System.out.println("To start populating your pattern, you have to create an empty row to put some stitches \n" +
                "in. After creating an empty row, you can navigate to the row editor and add Instructions.");
        System.out.println("Instructions are an object in StitchUp used to represent a line of stitches, all \n" +
                "of the same stitch type (ex. 6 knit st, 2 purl st, etc). By utilizing stitch groupings this way, \n" +
                "you avoid having to manually write down every solitary stitch in your pattern, streamlining the \n" +
                "process of development.");
        System.out.println("When you create an Instruction, you will have to specify what stitch and what amount of \n" +
                "stitches you want this instruction to contain, then it will be put into the row you selected earlier.");
        System.out.println("After adding some instructions to your row, you can go back to the pattern editing window\n" +
                "to add another row or view your pattern so far.");
        System.out.println("Once you're done, you can use the Export Pattern option to print your pattern into a \n" +
                "text file.");
        System.out.println("If you have any other files from StitchUp that you have saved, you can import it using \n" +
                "the Import pattern feature.");
    }

}
