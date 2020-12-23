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
package command;

import filesystem.CurrentWorkingDirectoryI;
import filesystem.FileSystemI;
import inputoutput.UserTerminalIn;

/**
 * This class handles displaying the list of commands executed
 * 
 * @version 2.0
 */

public class History extends Command {

  // Stores how much of the history the user wishes to show
  private int historyDepth;

  // Stores the number of commands the user has entered
  private int sizeOfHistory = UserTerminalIn.getCommandHistory().size();


  /**
   * Constructor function used to create an instance of a history object. Sets the maximum amount
   * of parameters the command accepts.
   * 
   * @param cwd stores current working directory from command call
   * @param fs stores current FileSystem instance with design
   */
  public History(FileSystemI fs, CurrentWorkingDirectoryI cwd) {

    super(fs, cwd);

    // Sets the max number of parameters to 1
    this.setMaxParameters(4);

  }

  /**
   * Method that returns the description of the history command.
   * 
   * @return the description of the command
   */
  public static String getDescriptionOfCommand() {
    return "Name of command: history\n\n"
        + "Functionality: Prints out the user command history. If a positive integer (>=0)\n"
        + "'n' is included in the arguments, prints the n most recent commands.\n"
        + "Note: if the integer n is greater than the total number of commands in history,\n"
        + "history will print the complete history of commands. However, if the user enters\n"
        + "a negative number n, history will print an error message.\n\n"
        + "Command syntax: history [n]";
  }

  /**
   * Allows the user to display the history of commands they have entered, regardless if errors
   * were thrown, or the command was completed. Allows the user to enter an optional command to
   * restrict the number of commands shown.
   * 
   * @return the history of commands entered or null upon error
   */
  public String execute() {

    // Checks if the user entered the optional parameter
    if (getNumParameters() == 1) {

      try {
        // Sets the number of commands to be shown
        historyDepth = Integer.parseInt(getCurrentParameters()[0]);

        // If the user enters a number larger then the number of commands, display all commands
        if (historyDepth >= sizeOfHistory) {
          historyDepth = sizeOfHistory;

          // Checks if the user enters a negative number
        } else if (historyDepth < 0) {
          return super.printErrorMessage("Enter a number >= 0!");
        }

        // If the user enters an invalid parameter
      } catch (NumberFormatException nfe) {
        return super.printErrorMessage("Invalid argument, enter just a number!");
      }

      // If not, display the entire history
    } else
      historyDepth = sizeOfHistory;

    // String accumulator for the total history
    String totalHistory = "";

    // String formatting variable
    String formattedHistory;

    // Iterates through the history of commands until the depth desired or end has been reached
    for (int i = sizeOfHistory - historyDepth; i < sizeOfHistory; i++) {

      // String containing the formated line
      formattedHistory =
          String.format("%d. %s\n", i + 1, inputoutput.UserTerminalIn.getCommandHistory().get(i));

      // Append to the total history
      totalHistory += (formattedHistory);
    }

    // Ensure only extra parameter is for redirection
    super.redirectLoopEnd();
    if (this.getNumParameters() == 2 && (!this.getCurrentParameters()[0].equals(this.getSymbol())))
      return super.printErrorMessage("Invalid parameters for function call.");

    return super.redirect(totalHistory);
  }
}
