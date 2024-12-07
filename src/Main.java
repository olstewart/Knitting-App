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
                    System.out.println("Exporting...");
                    break;
                // Tells users how to navigate the app.
                case 5:
                    System.out.println("Displaying manual...");
                    break;
                default: System.out.println("Invalid choice");
            }
            displayMainMenu();
            choice = intScanner.nextInt();
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
                    System.out.print("Enter new pattern name: ");
                    String newPatternName = strScanner.nextLine();
                    pattern.setPatternName(newPatternName);
                    System.out.println("Pattern is now called " + pattern.getPatternName() + "!\n");
                }
                // Allows user to add a new row
                case 3: {
                    System.out.println("[1] Add row to end of pattern");
                    System.out.println("[2] Add row to specific place in pattern");
                    System.out.println("[0] Cancel");
                    int rowOption = intScanner.nextInt();
                    while(rowOption > 2 || rowOption < 0){
                        System.out.print("Invalid choice, please input again: ");
                        rowOption = intScanner.nextInt();
                    }
                    switch(rowOption){
                        // Adds a new row to the end of the pattern
                        case 1: {
                            pattern.addRow(new Row());
                            System.out.println("New row has been added!");
                        } break;
                        //  
                        case 2: {
                            if(pattern.getRows().isEmpty()){
                                System.out.println("Row is empty, no index exists yet!\n");
                                break;
                            }
                            System.out.print("Desired index of your new row: ");
                            int desiredIndex = intScanner.nextInt();
                            while (desiredIndex < 1 || desiredIndex > pattern.getRows().size()) {
                                System.out.print("Invalid index. Please try again: ");
                                desiredIndex = intScanner.nextInt();
                            }
                            pattern.addRowAt(new Row(), desiredIndex);
                            System.out.println("New row " + desiredIndex + " has been added!");
                        } break;
                        default:{
                            System.out.print("Invalid choice.");
                        }
                    }
                } break;
                // Allows user to edit a selected row
                case 4: {
                    displayRows(pattern);
                    System.out.println("Which row do you want edit? (Type 0 to cancel)");
                    int rowToEdit = intScanner.nextInt();
                    while(rowToEdit > pattern.getRows().size() || rowToEdit < 0){
                        System.out.print("Invalid choice, please input again: ");
                        rowToEdit = intScanner.nextInt();
                    }
                    if(rowToEdit == 0){
                        break;
                    }
                    editRow(pattern.getRows().get(rowToEdit - 1));
                } break;
                // Allows user to delete a selected row
                case 5: {
                    displayRows(pattern);
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
                // Allows the user to edit which stitches are used in the pattern.
                case 7: {
                    System.out.print(pattern.listOfStitches());
                } break;
                default: System.out.println("Invalid choice");
            }
            displayPatternEditMenu();
            choice = intScanner.nextInt();
        }
    }

    // Allows user to edit attributes of a particular row.
    public static void editRow(Row row){
        Scanner intScanner = new Scanner(System.in);
        Scanner strScanner = new Scanner(System.in);
        displayRowEditMenu();
        int choice = intScanner.nextInt();
        while(choice != 0){
            switch (choice){
                default: System.out.println("Sorry, nothing.");
            }
            displayRowEditMenu();
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
        System.out.println("[1]: Add Instruction\n" +
                "[2]: Edit Instruction\n" +
                "[3]: Delete Instruction\n" +
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

    // Prints a list rows in a particular pattern
    public static void displayRows(Pattern pattern){
        if(pattern.getRows().isEmpty()){
            System.out.println("This pattern has no rows yet.\n");
        }else{
            for(int i = 0; i < pattern.getRows().size(); i++){
                System.out.println("[" + (i + 1) + "] " + pattern.getRows().get(i).toString());
            }
        }
    }

    // Prints a list instructions in a particular row
    public static void displayRows(Row row){
        if(row.getInstructions().isEmpty()){
            System.out.println("This row has no instructions yet.\n");
        }else{
            for(int i = 0; i < row.getInstructions().size(); i++){
                System.out.println("[" + (i + 1) + "] " + row.getInstructions().get(i).toString());
            }
        }
    }

}
