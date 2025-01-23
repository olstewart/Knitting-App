import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class Main{
    public static void main(String[] args){
        Scanner strScanner = new Scanner(System.in);

        ArrayList<Pattern> userPatterns = new ArrayList<>();

        System.out.println("Welcome to StitchUp, the free digital yarn work pattern-making app!");
        displayMainMenu();
        
        int choice = grabInt(strScanner.nextLine());
        //This while loop controls the main program loop
        while(choice != 0){
            switch(choice){
                // Creates a new empty pattern under the specified name.
                case 1: {
                    System.out.print("Enter new pattern name: ");
                    String newPatternName = strScanner.nextLine();
                    if(newPatternName.equals("0")){
                        System.out.println("Pattern creation cancelled.");
                        break;
                    }
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
                    int patternToView = grabInt(strScanner.nextLine());
                    while (patternToView != 0){
                        if(patternToView <= userPatterns.size() && patternToView > 0){
                            System.out.println(userPatterns.get(patternToView - 1));
                            editPattern(userPatterns.get(patternToView - 1));
                            break;
                        }
                        else {
                            System.out.print("Invalid choice, please re-enter or type 0 to exit: ");
                            patternToView = grabInt(strScanner.nextLine());
                        }
                    }
                    break;
                }
                // Takes in a StitchUp file and converts it into a pattern object
                case 3: {
                    System.out.print("Enter the directory of the file you wish to import: ");
                    String fileName = strScanner.nextLine();
                    importPattern(fileName, userPatterns);
                } break;
                // Saves file into a StitchUp file, so it can be imported back into the app.
                case 4: {
                    if (userPatterns.isEmpty()) {
                        System.out.println("No patterns found.\n");
                        break;
                    }
                    displayPatterns(userPatterns);
                    System.out.println("Choose a pattern to save or type 0 to exit.");
                    int patternToSave = grabInt(strScanner.nextLine());
                    if (patternToSave == 0) {
                        System.out.println("Export cancelled.\n");
                        break;
                    }
                    while (patternToSave < 1 || patternToSave > userPatterns.size()) {
                        System.out.print("Invalid choice, please input again: ");
                        patternToSave = grabInt(strScanner.nextLine());
                    }
                    savePattern(userPatterns.get(patternToSave - 1));
                    System.out.println("Pattern saved!\n");
                } break;
                // Exports a pattern into a human-readable file.
                case 5: {
                    if (userPatterns.isEmpty()) {
                        System.out.println("No patterns found.\n");
                        break;
                    }
                    displayPatterns(userPatterns);
                    System.out.println("Choose a pattern to export or type 0 to exit.");
                    int patternToExport = grabInt(strScanner.nextLine());
                    if (patternToExport == 0) {
                        System.out.println("Export cancelled.\n");
                        break;
                    }
                    while (patternToExport < 1 || patternToExport > userPatterns.size()) {
                        System.out.print("Invalid choice, please input again: ");
                        patternToExport = grabInt(strScanner.nextLine());
                    }
                    exportPattern(userPatterns.get(patternToExport - 1));
                    System.out.println("Pattern exported!\n");
                } break;
                // Tells users how to navigate the app.
                case 6:
                    displayManual();
                    break;
                default: System.out.println("Invalid choice");
            }
            displayMainMenu();
            choice = grabInt(strScanner.nextLine());
        }

    }

    public static int grabInt(String input){
        try{
            return parseInt(input);
        } catch (NumberFormatException e){
            return -1;
        }
    }

    public static void importPattern(String filename, ArrayList<Pattern> userPatterns){
        try {
            Scanner fileScanner = new Scanner(new File(filename));
            Pattern newPattern = new Pattern(fileScanner.nextLine(), new ArrayList<Row>(), new ArrayList<Stitch>());
            while(fileScanner.hasNext()){
                String next = fileScanner.nextLine();
                //Import stitches
                if(next.startsWith("stitch:")){
                    next = next.replace("stitch:", "");
                    String[] stitchStrings = next.split(",");
                    newPattern.addStitch(new Stitch(stitchStrings[0], parseInt(stitchStrings[1]), parseInt(stitchStrings[2]), stitchStrings[3]));
                }
                else{
                    String[] row = next.split(",,");
                    ArrayList<Instruction> instructions = new ArrayList<>();
                    // Go through every instruction in the array
                    for(int i = 0; i < row.length; i++){
                        if(row[i].startsWith("@sp@")){
                            row[i] = row[i].replace("@sp@", "");
                            String[] instructionStrings = row[i].split(",");
                            instructions.add(new SpecialInstruction(instructionStrings[0], parseInt(instructionStrings[1]), parseInt(instructionStrings[2])));
                        }
                        else {// This will split the instruction into the type, count, and color
                            String[] instructionStrings = row[i].split(",");
                            // This loop will find whichever stitch matches the stitch name and create the instruction using that stitch.
                            for (int x = 0; x < newPattern.getStitches().size(); x++) {
                                if (newPattern.getStitches().get(x).getType().equals(instructionStrings[0])) {
                                    instructions.add(new Instruction(newPattern.getStitches().get(x), parseInt(instructionStrings[1]), instructionStrings[2]));
                                }
                            }
                        }
                    }
                    //Add array to new row
                    newPattern.addRow(new Row(instructions));
                }
            }
            userPatterns.add(newPattern);
            System.out.println("Pattern " + userPatterns.getLast().getPatternName() + " imported successfully!\n");
            fileScanner.close();
        } catch (FileNotFoundException e){
            System.out.println("Pattern not found.");
        } catch (Exception e){
            System.out.println("There was an issue.");
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

    public static void savePattern(Pattern pattern){
        System.out.println("Saving pattern " + pattern.getPatternName() + "...");
        String filename = pattern.getPatternName() + " - StitchUp" + ".txt";
        try {
            FileOutputStream fileOut = new FileOutputStream(filename);
            PrintWriter printer = new PrintWriter(fileOut);

            printer.print(pattern.toStitchUp());

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
        Scanner strScanner = new Scanner(System.in);
        displayPatternEditMenu();
        int choice = grabInt(strScanner.nextLine());
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
                // Saves current pattern into a StitchUp!
                case 3: {
                    savePattern(pattern);
                    System.out.println("Pattern saved!\n");
                } break;
                // Allows user to add a new row
                case 4: {
                    System.out.println("[1] Add row to end of pattern");
                    System.out.println("[2] Add row to specific index");
                    System.out.println("[3] Duplicate row");
                    System.out.println("[0] Cancel");
                    int rowOption = grabInt(strScanner.nextLine());
                    if(rowOption == 0){
                        System.out.println("Row addition cancelled.\n");
                        break;
                    }
                    while(rowOption > 3 || rowOption < 0){
                        System.out.print("Invalid choice, please input again: ");
                        rowOption = grabInt(strScanner.nextLine());
                    }
                    switch(rowOption){
                        // Adds a new row to the end of the pattern
                        case 1: {
                            pattern.addRow(new Row());
                            System.out.println("New row has been added!\n");
                            editRow(pattern.getRows().getLast(), pattern);
                        } break;
                        //  
                        case 2: {
                            if(pattern.getRows().isEmpty()){
                                System.out.println("Pattern is empty, no row index exists yet!\n");
                                break;
                            }
                            displayRows(pattern);
                            System.out.print("Desired index of your new row: ");
                            int desiredIndex = grabInt(strScanner.nextLine());
                            if(desiredIndex == 0){
                                System.out.println("Row addition cancelled.\n");
                                break;
                            }
                            while (desiredIndex < 1 || desiredIndex > pattern.getRows().size()) {
                                System.out.print("Invalid index. Please try again: ");
                                desiredIndex = grabInt(strScanner.nextLine());
                            }
                            pattern.addRowAt(new Row(), desiredIndex - 1);
                            System.out.println("New row [" + desiredIndex + "] has been added!\n");
                            editRow(pattern.getRows().get(desiredIndex - 1), pattern);
                        } break;
                        case 3:{
                            if(pattern.getRows().isEmpty()){
                                System.out.println("Pattern is empty, no row index exists yet!\n");
                                break;
                            }
                            displayRows(pattern);
                            System.out.print("Index of row to duplicate: ");
                            int copyIndex = grabInt(strScanner.nextLine());
                            if(copyIndex == 0){
                                System.out.println("Row duplication cancelled.\n");
                                break;
                            }
                            while (copyIndex < 1 || copyIndex > pattern.getRows().size()) {
                                System.out.print("Invalid index. Please try again: ");
                                copyIndex = grabInt(strScanner.nextLine());
                            }

                            System.out.print("Index of new row (0 to place it at the end): ");
                            int pasteIndex = grabInt(strScanner.nextLine());
                            while (pasteIndex < 0 || pasteIndex > pattern.getRows().size()) {
                                System.out.print("Invalid index. Please try again: ");
                                pasteIndex = grabInt(strScanner.nextLine());
                            }
                            if(pasteIndex == 0){
                                pattern.addRow(pattern.getRows().get(copyIndex - 1));
                            }
                            else{
                                pattern.addRowAt(pattern.getRows().get(copyIndex - 1), pasteIndex - 1);
                            }
                        }break;
                        default:{
                            System.out.print("Invalid choice.");
                        }
                    }
                } break;
                // Allows user to edit a selected row
                case 5: {
                    displayRows(pattern);
                    if(pattern.getRows().isEmpty()){
                        break;
                    }
                    System.out.print("Enter which row you wish to edit or enter 0 to cancel: ");
                    int rowToEdit = grabInt(strScanner.nextLine());
                    while(rowToEdit > pattern.getRows().size() || rowToEdit < 0){
                        System.out.print("Invalid choice, please input again: ");
                        rowToEdit = grabInt(strScanner.nextLine());
                    }
                    if(rowToEdit == 0){
                        break;
                    }
                    editRow(pattern.getRows().get(rowToEdit - 1), pattern);
                } break;
                // Allows user to delete a selected row
                case 6: {
                    displayRows(pattern);
                    if(pattern.getRows().isEmpty()){
                        break;
                    }
                    System.out.print("Enter which row you wish to delete or enter 0 to cancel: ");
                    int rowToDelete = grabInt(strScanner.nextLine());
                    while(rowToDelete > pattern.getRows().size() || rowToDelete < 0){
                        System.out.print("Invalid choice, please input again: ");
                        rowToDelete = grabInt(strScanner.nextLine());
                    }
                    if(rowToDelete == 0){
                        System.out.println("Row deletion canceled.\n");
                        break;
                    }
                    pattern.getRows().remove(rowToDelete - 1);
                    System.out.println("Row deleted!\n");

                } break;
                // Allows the user to add a new stitch
                case 7: {
                    System.out.print("Enter the name of the new stitch, or 0 to cancel: ");
                    String stitchName = strScanner.nextLine();
                    if(stitchName.equals("0")){
                        System.out.print("New stitch addition cancelled.\n");
                        break;
                    }
                    System.out.print("How many stitches do you need to pick up to perform this stitch?: ");
                    int stitchSpan = grabInt(strScanner.nextLine());
                    while(stitchSpan < 0){
                        System.out.print("Stitch span should at least be 0: ");
                        stitchSpan = grabInt(strScanner.nextLine());
                    }
                    System.out.print("How many stitches does this stitch leave after being completed?: ");
                    int stitchCount = grabInt(strScanner.nextLine());
                    while(stitchCount < 0){
                        System.out.print("Stitch count should at least be 0: ");
                        stitchCount = grabInt(strScanner.nextLine());
                    }
                    System.out.println("Add a short description of the stitch here, or enter 0 to add stitch without a description:");
                    String stitchDescription = strScanner.nextLine();
                    if(stitchDescription.equals("0")){
                        stitchDescription = "none";
                    }
                    pattern.addStitch(new Stitch(stitchName, stitchSpan, stitchCount, stitchDescription));
                    System.out.println("New stitch " + pattern.getStitches().getLast().getType() + " has been added!\n");
                } break;
                // Allows the user to edit which stitches are used in the pattern.
                case 8: {
                    if(pattern.getStitches().isEmpty()){
                        System.out.println("There are no stitches to edit!\n");
                        break;
                    }
                    displayStitches(pattern);
                    System.out.print("Enter the stitch you wish to edit or enter 0 to go back: ");
                    int stitchToEdit = grabInt(strScanner.nextLine());
                    if(stitchToEdit == 0){
                        break;
                    }
                    while(stitchToEdit > pattern.getStitches().size() || stitchToEdit < 0){
                        System.out.print("Invalid choice, please enter again: ");
                        stitchToEdit = grabInt(strScanner.nextLine());
                    }
                    editStitch(pattern.getStitches().get(stitchToEdit - 1));
                } break;
                // Deletes a stitch, but only if the stitch isn't used in the pattern.
                case 9: {
                    if(pattern.getStitches().isEmpty()){
                        System.out.println("There are no stitches to delete!\n");
                        break;
                    }
                    displayStitches(pattern);
                    System.out.println("\nYou cannot delete a stitch that is currently being used in this pattern.");
                    System.out.println("Enter which stitch you wish to delete, or enter 0 to cancel: ");
                    int stitchToDelete = grabInt(strScanner.nextLine());
                    while(stitchToDelete > pattern.getStitches().size() || stitchToDelete < 0){
                        System.out.print("Invalid choice, please enter again: ");
                        stitchToDelete = grabInt(strScanner.nextLine());
                    }
                    if(stitchToDelete == 0){
                        System.out.print("Stitch deletion cancelled.\n");
                        break;
                    }
                    boolean deletionAllowed = true;
                    for(Row r : pattern.getRows()){
                        for(Instruction i : r.getInstructions()){
                            if(i.getStitch().equals(pattern.getStitches().get(stitchToDelete - 1))){
                                deletionAllowed = false;
                            }
                        }
                    }
                    if(deletionAllowed){
                        pattern.getStitches().remove(stitchToDelete - 1);
                        System.out.println("Stitch deleted!\n");
                    }else {System.out.println("Stitch is currently being utilized in your pattern. Remove all uses before deletion.\n");}
                } break;

                default: System.out.println("Invalid choice");
            }
            displayPatternEditMenu();
            choice = grabInt(strScanner.nextLine());
        }
    }

    // Allows user to edit attributes of a particular row.
    public static void editRow(Row row, Pattern pattern){
        Scanner strScanner = new Scanner(System.in);
        displayInstructions(row);
        displayRowEditMenu();
        int choice =  grabInt(strScanner.nextLine());
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
                    int stitchChoice =  grabInt(strScanner.nextLine());
                    while(stitchChoice < 0 || stitchChoice > pattern.getStitches().size()){
                        System.out.print("Invalid choice, please input again: ");
                        stitchChoice =  grabInt(strScanner.nextLine());
                    }
                    if(stitchChoice == 0){break;}
                    Stitch stitch = pattern.getStitches().get(stitchChoice - 1);

                    // Saves the new stitches stitch count
                    System.out.println("Stitch count: ");
                    int stitchCount =  grabInt(strScanner.nextLine());
                    while(stitchCount < 1){
                        System.out.print("Stitch count must be greater than 0: ");
                        stitchCount =  grabInt(strScanner.nextLine());
                    }

                    // Saves color, if any
                    System.out.println("Enter stitch color, or 0 to make stitch with no color:");
                    String stitchColor = strScanner.nextLine();

                    // Saves index to place the instruction
                    System.out.println("Enter instruction index, or 0 to add it at the end of the row: ");
                    int instructionIndex = grabInt(strScanner.nextLine());
                    while(instructionIndex < 0 || instructionIndex > row.getInstructions().size()){
                        System.out.print("Invalid index. Please input again: ");
                        instructionIndex = grabInt(strScanner.nextLine());
                    }
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
                    if(stitchColor.equals("0")){
                        row.addNewInstructionAt(stitch, stitchCount, instructionIndex - 1);
                    }
                    else{
                        row.addNewInstructionAt(stitch, stitchCount, stitchColor, instructionIndex - 1);
                    }
                    System.out.println("New instruction added!\n");
                } break;
                // Edits an instruction
                case 3: {
                    if(row.getInstructions().isEmpty()){
                        break;
                    }
                    displayInstructions(row);

                    System.out.println("Which instruction do you want to edit? (Type 0 to cancel)");
                    int instructionToEdit =  grabInt(strScanner.nextLine());
                    while(instructionToEdit > row.getInstructions().size() || instructionToEdit < 0){
                        System.out.print("Invalid choice, please input again: ");
                        instructionToEdit =  grabInt(strScanner.nextLine());
                        if(instructionToEdit == 0){
                            break;
                        }
                    }
                    if(instructionToEdit == 0){
                        break;
                    }
                    editInstruction(row.getInstructionAt(instructionToEdit - 1), pattern);
                } break;
                //Adds a special Instruction
                case 2: {
                    // Saves description
                    System.out.println("Enter the description of the instruction (ex. repeat purl and stitch 7 times):");
                    String stitchDescription = strScanner.nextLine();
                    if(stitchDescription.equals("0")){
                        System.out.println("Special Instruction addition cancelled!");
                        break;
                    }

                    // Saves the new stitches stitch count
                    System.out.println("How many stitches should this result in: ");
                    int stitchResult =  grabInt(strScanner.nextLine());
                    while(stitchResult < 0){
                        System.out.print("Stitch count must be at least 0: ");
                        stitchResult =  grabInt(strScanner.nextLine());
                    }

                    // Saves the new stitches stitch count
                    System.out.println("How many stitches do you need to pick up to complete this: ");
                    int stitchSpan =  grabInt(strScanner.nextLine());
                    while(stitchSpan < 0){
                        System.out.print("Stitch count must be at least 0: ");
                        stitchSpan =  grabInt(strScanner.nextLine());
                    }

                    // Saves index to place the instruction
                    System.out.println("Enter instruction index, or 0 to add it at the end of the row: ");
                    int instructionIndex = grabInt(strScanner.nextLine());
                    while(instructionIndex < 0 || instructionIndex > row.getInstructions().size()){
                        System.out.print("Invalid index. Please input again: ");
                        instructionIndex = grabInt(strScanner.nextLine());
                    }
                    if(instructionIndex == 0){
                        row.addNewSpecialInstruction(stitchDescription, stitchSpan, stitchResult);
                    }
                    else{
                        row.addNewSpecialInstructionAt(stitchDescription, stitchSpan, stitchResult, instructionIndex - 1);
                    }
                    System.out.println("New instruction added!\n");
                } break;
                // Deletes an instruction
                case 4: {
                    if(row.getInstructions().isEmpty()){
                        break;
                    }
                    displayInstructions(row);
                    System.out.println("Which instruction do you want delete? (Type 0 to cancel)");
                    int instructionToDelete =  grabInt(strScanner.nextLine());
                    while(instructionToDelete > row.getInstructions().size() || instructionToDelete < 0){
                        System.out.print("Invalid choice, please input again: ");
                        instructionToDelete =  grabInt(strScanner.nextLine());
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
            choice =  grabInt(strScanner.nextLine());
        }
    }

    // Allows user to edit instructions
    public static void editInstruction(Instruction instruction, Pattern pattern){
        Scanner strScanner = new Scanner(System.in);
        System.out.println(instruction + "\n");
        if(instruction instanceof SpecialInstruction){
            displaySpecialInstructionEditMenu();
            int choice =  grabInt(strScanner.nextLine());
            while(choice != 0){
                switch (choice){
                    // Changes the description of the instruction
                    case 1: {
                        System.out.print("Enter description, or enter 0 to cancel: ");
                        String stitchDesc = strScanner.nextLine();
                        if(stitchDesc.equals("0")){
                            System.out.println("Description change cancelled.\n");
                            break;
                        }
                        ((SpecialInstruction)instruction).setDescription(stitchDesc);
                    } break;
                    // Changes stitch span of the instruction
                    case 2: {
                        System.out.println("Original stitch span: " + ((SpecialInstruction)instruction).getStitchSpan());
                        System.out.print("Enter the desired stitch span: ");
                        int stitchSpan =  grabInt(strScanner.nextLine());
                        if(stitchSpan < 0){
                            System.out.println("Stitch span change cancelled.\n");
                            break;
                        }
                        ((SpecialInstruction)instruction).setStitchSpan(stitchSpan);
                    } break;
                    // Changes stitch result of the instruction
                    case 3: {
                        System.out.println("Original stitch result: " + ((SpecialInstruction)instruction).getStitchResult());
                        System.out.print("Enter the desired stitch result: ");
                        int stitchResult =  grabInt(strScanner.nextLine());
                        if(stitchResult < 0){
                            System.out.println("Stitch result change cancelled.\n");
                            break;
                        }
                        ((SpecialInstruction)instruction).setStitchResult(stitchResult);
                    } break;
                    default: System.out.println("Invalid choice.");
                }
                System.out.println(instruction + "\n");
                displaySpecialInstructionEditMenu();
                choice =  grabInt(strScanner.nextLine());
            }
        }
        else{
            displayInstructionEditMenu();
            int choice = grabInt(strScanner.nextLine());
            while (choice != 0) {
                switch (choice) {
                    // Changes the stitch type of the instruction
                    case 1: {
                        displayStitches(pattern);
                        System.out.print("Choose a stitch, or enter 0 to cancel: ");
                        int stitchChoice = grabInt(strScanner.nextLine());
                        if (stitchChoice == 0) {
                            System.out.println("Stitch change cancelled.\n");
                            break;
                        }
                        while (stitchChoice < 1 || stitchChoice > pattern.getStitches().size()) {
                            System.out.print("Invalid choice, please input again: ");
                            stitchChoice = grabInt(strScanner.nextLine());
                        }
                        instruction.setStitch(pattern.getStitches().get(stitchChoice - 1));
                    }
                    break;
                    // Changes stitch count of the instruction
                    case 2: {
                        System.out.print("Enter the desired stitch count, or enter 0 to cancel: ");
                        int stitchCount = grabInt(strScanner.nextLine());
                        if (stitchCount == 0) {
                            System.out.println("Stitch count change cancelled.\n");
                            break;
                        }
                        while (stitchCount < 0) {
                            System.out.print("Stitch count must be greater than 0: ");
                            stitchCount = grabInt(strScanner.nextLine());
                        }
                        instruction.setStitchCount(stitchCount);
                    }
                    break;
                    // Changes color of the instruction
                    case 3: {
                        System.out.print("Enter the desired stitch color, enter \"none\" to remove color, enter 0 to cancel: ");
                        String stitchColor = strScanner.nextLine();
                        if (stitchColor.equals("0")) {
                            System.out.println("Color change cancelled.\n");
                        } else {
                            instruction.setColor(stitchColor);
                        }
                    }
                    break;
                    default:
                        System.out.println("Invalid choice.");
                }
                System.out.println(instruction + "\n");
                displayInstructionEditMenu();
                choice = grabInt(strScanner.nextLine());
            }
        }
    }

    public static void editStitch(Stitch stitch){
        Scanner strScanner = new Scanner(System.in);
        System.out.println(stitch + "\n");
        displayStitchEditMenu();
        int choice =  grabInt(strScanner.nextLine());
        while(choice != 0){
            switch (choice){
                case 1: {
                    System.out.print("Enter new stitch name, or enter 0 to cancel: ");
                    String stitchName = strScanner.nextLine();
                    if(stitchName.equals("0")){
                        System.out.println("Name change cancelled.\n");
                        break;
                    }
                    stitch.setType(stitchName.trim());
                } break;
                case 2: {
                    System.out.print("Enter desired stitch span, or enter 0 to cancel: ");
                    int stitchSpan = grabInt(strScanner.nextLine());
                    if(stitchSpan == 0){
                        System.out.println("Span change cancelled.\n");
                        break;
                    }
                    while(stitchSpan < 0){
                        System.out.print("Stitch span must be greater than 0: ");
                        stitchSpan =  grabInt(strScanner.nextLine());
                    }
                    stitch.setStitchSpan(stitchSpan);
                } break;
                case 3: {
                    System.out.print("Enter desired stitch result, or enter 0 to cancel: ");
                    int stitchResult = grabInt(strScanner.nextLine());
                    if(stitchResult == 0){
                        System.out.println("Result change cancelled.\n");
                        break;
                    }
                    while(stitchResult < 0){
                        System.out.print("Stitch result must be greater than 0: ");
                        stitchResult =  grabInt(strScanner.nextLine());
                    }
                    stitch.setStitchResult(stitchResult);
                } break;
                case 4: {
                    System.out.println("Enter new stitch description, or enter 0 to cancel: ");
                    String stitchDescription = strScanner.nextLine();
                    if(stitchDescription.equals("0")){
                        System.out.println("Description change cancelled.\n");
                        break;
                    }
                    stitch.setDescription(stitchDescription.trim());
                }
                default: System.out.println("Invalid choice.");
            }
            System.out.println(stitch + "\n");
            displayStitchEditMenu();
            choice =  grabInt(strScanner.nextLine());
        }
    }


    // Displays main menu
    public static void displayMainMenu() {
        System.out.println("[1]: Create New Pattern\n" +
                "[2]: View/Edit Existing Pattern\n" +
                "[3]: Import Pattern\n" +
                "[4]: Save Pattern to StitchUp File\n" +
                "[5]: Export Pattern\n" +
                "[6]: User Manual\n" +
                "[0]: Exit Program\n");
    }

    // Displays pattern editing menu
    public static void displayPatternEditMenu(){
        System.out.println("[1]: View Pattern\n" +
                "[2]: Edit Pattern Name\n" +
                "[3]: Save Pattern\n" +
                "---------------------------\n" +
                "[4]: Add New Row\n" +
                "[5]: Edit Row\n" +
                "[6]: Delete Row\n" +
                "---------------------------\n" +
                "[7]: Add New Stitch\n" +
                "[8]: View/Edit Stitches\n" +
                "[9]: Delete Stitch\n" +
                "[0]: Back\n");
    }

    // Displays row editing menu
    public static void displayRowEditMenu(){
        System.out.println("[1]: Add Instruction\n" +
                "[2]: Add Special Instruction\n" +
                "[3]: Edit Instruction\n" +
                "[4]: Delete Instruction\n" +
                "[0]: Back\n");
    }

    // Displays instruction editing menu
    public static void displayInstructionEditMenu(){
        System.out.println("[1]: Change Stitch\n" +
                "[2]: Change Stitch Count\n" +
                "[3]: Change Color\n" +
                "[0]: Back\n");
    }

    // Displays special instruction editing menu
    public static void displaySpecialInstructionEditMenu(){
        System.out.println("[1]: Change Description\n" +
                "[2]: Change Stitch Span\n" +
                "[3]: Change Stitch Result\n" +
                "[0]: Back\n");
    }

    //Displays stitch editing menu
    public static void displayStitchEditMenu(){
        System.out.println("[1]: Change Stitch Name\n" +
                "[2]: Change Stitch Span\n" +
                "[3]: Change Stitch Result\n" +
                "[4]: Change Stitch Description\n" +
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

    // Prints array of stitches in a particular pattern
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
