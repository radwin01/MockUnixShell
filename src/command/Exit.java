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
 * Executes the exit command
 * 
 * @version 2.0
 */
public class Exit extends Command {

  /**
   * Constructor used to create an instance of an Exit object. Sets the number of maximum
   * parameters that the user can enter after the command.
   * 
   * @param fs the current FileSystem in use
   * @param cwd the current CWD in use
   */
  public Exit(FileSystemI fs, CurrentWorkingDirectoryI cwd) {

    super(fs, cwd);

    // sets the maximum number of parameters to be accepted
    this.setMaxParameters(0);
  }

  /**
   * Returns the description of the exit command.
   * 
   * @return the description of the command
   */
  public static String getDescriptionOfCommand() {
    return "Name of command: exit\n\n" + "Functionality: Exits the program.\n\n"
        + "Command syntax: exit";
  }

  /**
   * Allows the user to successfully exit the "terminal".
   * 
   * @return String returns an empty string once the command has been executed
   */
  public String execute() {

    System.out.println("Goodbye!");
    // terminate the program and indicate successful termination
    System.exit(0);
    return "";
  }
}

