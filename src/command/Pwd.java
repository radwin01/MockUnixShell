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

/**
 * Executes the pwd command
 * 
 * @version 2.0
 */
public class Pwd extends Command {

  /**
   * Constructor used to create an instance of a Pwd object. Sets the number of maximum parameters
   * that the user can enter after the command.
   * 
   * @param fs the current FileSystem in use
   * @param cwd the current CWD in use
   */
  public Pwd(FileSystemI fs, CurrentWorkingDirectoryI cwd) {

    super(fs, cwd);

    // sets the maximum number of parameters to be accepted
    this.setMaxParameters(2);
  }

  /**
   * Method that returns the description of the pwd command.
   * 
   * @return the description of the command
   */
  public static String getDescriptionOfCommand() {
    return "Name of command: pwd\n\n" + "Functionality: Prints the current working directory.\n\n"
        + "Command syntax: pwd";
  }

  /**
   * Allows the user to print the current working directory.
   * 
   * @return the current working directory represented as a string
   */
  public String execute() {

    // Ensure only extra parameter is for redirection
    if (this.getNumParameters() == 1) {
      return super.printErrorMessage("Invalid parameter for function call.");
    }

    // print the current working directory
    String contents = this.getShellCWD().getCWDString();
    return super.redirect(contents);
  }
}

