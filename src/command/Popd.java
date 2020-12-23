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
import filesystem.Directory;
import filesystem.DirectoryStack;
import filesystem.FileSystemI;
import filesystem.ItemList;

/**
 * Represents a Popd command.
 * 
 * @version 1.0
 */

public class Popd extends Command {

  // Stores the current directory stack.
  private DirectoryStack dirStack;

  /**
   * Creates an instance of a Popd object. Sets the maximum number of parameters that the user can
   * enter.
   * 
   * @param fs the current FileSystem in use
   * @param cwd the current CWD in use
   */
  public Popd(FileSystemI fs, CurrentWorkingDirectoryI cwd, DirectoryStack dirStack) {

    super(fs, cwd);
    this.dirStack = dirStack;

    // Set max parameters
    this.setMaxParameters(0);
  }

  /**
   * Method that returns the description of the popd command.
   * 
   * @return the description of the command
   */
  public static String getDescriptionOfCommand() {
    return "Name of command: popd\n\n" + "Functionality: Changes the current working directory"
        + " to the top\nentry of the directory stack and removes the entry from\n"
        + "the stack. If the user uses this command when there are no more\n"
        + "directories in the directory stack, popd will print an error message.\n\n"
        + "Command syntax: popd";
  }

  /**
   * Removes the top entry from the directory stack and changes directory into it (AKA: makes the
   * current working directory the top entry). If there are no directories in the directory stack,
   * an error message is printed.
   * 
   * @return the current directory stack, or null upon failure
   */
  public String execute() {
    // Getting the current directory stack from DirectoryStack.
    ItemList<Directory> dirS = dirStack.getDirStack();

    // Checking if the directory stack is empty.
    if (dirS.size() == 0) {
      return super.printErrorMessage("There are no directories in the directory stack.");
    }

    // Popping the top directory from dirStack and setting it as the current working directory.
    this.getShellCWD().setCWD(dirS.get(dirS.size() - 1));
    dirS.remove(dirS.size() - 1);

    return dirStack.getDirStackAsString();
  }
}
