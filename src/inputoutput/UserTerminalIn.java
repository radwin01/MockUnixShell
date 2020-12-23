// **********************************************************
// Assignment2:
// Student1:
// UTORID user_name: glumacla
// UT Student #: 1006068461
// Author: Lazar Glumac
//
// Student2:
// UTORID user_name: radwanya
// UT Student #: 1006280748
// Author: Yara Radwan
//
// Student3:
// UTORID user_name: blasett1
// UT Student #: 1005991198
// Author: Ryan Blasetti
//
// Student4:
// UTORID user_name: laiking2
// UT Student #: 1006030723
// Author: King Lai
//
//
// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package inputoutput;

/**
 * This class handles getting the input from the user, and stores the given input
 * 
 * @version 1.0
 */
public class UserTerminalIn {

  // Stores the value of the current command
  private String currentCommand;

  // Input stream used to enter input
  private java.util.Scanner input;

  // array list to be accessed and store the history of commands
  private static java.util.ArrayList<String> commandHistory = new java.util.ArrayList<String>();

  /**
   * Constructor function used to create an instance of a UserTerminalIn object. Initializes the
   * input stream as well as the list which contains the history of commands.
   */
  public UserTerminalIn() {

    // Initializes a scanner object
    input = new java.util.Scanner(System.in);

  }

  /**
   * Allows the user to enter a line of input to be executed, and sets the value of the current
   * command to that line.
   * 
   * @return the line which the user has entered to be processed as a command.
   */
  public String getUserInput() {

    // Sets the current command string to be returned
    setCurrentCommand(input.nextLine());
    return getCurrentCommand();

  }

  /**
   * Method that allows the user to continuously type input until they type "QUIT".
   * 
   * @return arraylist of strings which contains everything the user inputed except for "QUIT"
   */
  public java.util.ArrayList<String> getContinuousUserInput() {

    // Stores user input
    String userInput;

    // Splits the user input into an array
    String[] userInputSplit;

    // Stores the total input
    java.util.ArrayList<String> totalInput = new java.util.ArrayList<String>();

    userInput = input.nextLine();
    userInputSplit = userInput.split(" ");

    // Continuously receiving input until "QUIT" is entered.
    while (!userInputSplit[userInputSplit.length - 1].equals("QUIT")) {
      totalInput.add(userInput);
      userInput = input.nextLine();
      userInputSplit = userInput.split(" ");
    }

    // Adding all input except for the terminating word, "QUIT"
    for (int i = 0; i < userInputSplit.length - 1; i++)
      totalInput.add(userInputSplit[i]);

    return totalInput;
  }

  /**
   * Sets the value of currentCommand to the users input, and adds the input to the history of
   * attempted commands.
   * 
   * @param command String that holds the string which the user has entered to be executed
   */
  public void setCurrentCommand(String command) {

    // Sets the current command to the string entered
    this.currentCommand = command;

    // Checks if the command is not an empty line
    if (!this.currentCommand.equals("")) {

      // Adds the command to the history queue
      addToHistory(command);

    }

  }

  /**
   * Returns the value of the users current command.
   * 
   * @return the command which the user has currently entered
   */
  public String getCurrentCommand() {

    return currentCommand;

  }

  /**
   * Adds the users command to the history of commands that have been entered.
   * 
   * @param newCommand String which contains the command the user has entered
   */
  private void addToHistory(String newCommand) {

    // Adds the given String to the list of entered strings
    commandHistory.add(newCommand);

  }

  /**
   * Returns the list of the users current command.
   * 
   * @return the list of commands the user has entered
   */
  public static java.util.ArrayList<String> getCommandHistory() {
    return commandHistory;
  }

}

/*
 *  NOTE: Due to this class relying on user input and the Scanner class,
 *  this class was unable to be JUnit tested. However, testing was done
 *  using print statements inside the class, which would print all of the lines
 *  entered by the user. In addition, the command history tracking was tested
 *  in the history class, and produced all expected output. 
 * 
 */
