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
import filesystem.File;
import filesystem.FileSystemI;

/**
 * This class handles printing the file system as a tree
 * 
 * @version 1.0
 */

public class Tree extends Command {

  // Variable containing the tab character
  private final String tabChar = "\t";

  // String accumulator used for printing and returning
  private String returnString = "";

  /**
   * Constructor function used to create an instance of a Tree object. Sets the number of maximum
   * parameters that the user can enter, the file system, and current working directory
   * 
   * @param fs the current FileSystem in use
   * @param cwd the current CWD in use
   */
  public Tree(FileSystemI fs, CurrentWorkingDirectoryI cwd) {

    super(fs, cwd);

    // Sets the max parameters to 0
    this.setMaxParameters(2);

  }

  /**
   * Returns a description of the tree command.
   * 
   * @return the description of the command
   */
  public static String getDescriptionOfCommand() {
    return "Name of command: tree\n\n"
        + "Functionality: Displays the entire filesystem as a tree, starting from the\n"
        + "root directory (represented as a \\) to the current working directory. Contents\n"
        + "of a directory are presented on a lower level of the tree, indented with a tab.\n\n"
        + "Command syntax: tree";
  }

  /**
   * Prints the file system in a "tree" shape, separating directories and sub directories by
   * newline and tab characters, respectively.
   * 
   * @return the file system, formatted appropriately.
   */
  public String execute() {

    // Sets up the string to be returned with the appropriate formatting
    traverseFileSystem(super.getCurrentFileSystem().getRootDir(), 0);

    // Ensure only extra parameter is for redirection
    if (this.getNumParameters() == 1) {
      return super.printErrorMessage("Invalid parameter for function call.");
    }

    // Returns the file system (as printed above)
    return super.redirect(returnString);
  }

  /**
   * Adds to the returnString accumulator the file system formatted appropriately, separated by
   * tabs and newlines.
   * 
   * @param dir Directory object containing the current directory and its sub directories.
   * @param depth integer holding how deep in the file system the function has traversed.
   */
  private void traverseFileSystem(Directory dir, int depth) {

    // Stores the number of tabs (based on depth) to be printed
    String numTabs = "";

    // Adds depth * tab characters to numTabs
    for (int i = 0; i < depth; i++) {
      numTabs += tabChar;
    }

    // Checks if first call of function, print "\" for root
    if (depth == 0) {
      returnString += ("/" + "\n");
    } else {
      // Otherwise, add the number of tabs, the current directories name, and a newline
      returnString += String.format(numTabs + dir.getName() + "\n");
    }

    // Increment the depth by 1, used for going deeper into the file system
    depth++;

    // Iterate through each sub directory in the current directory
    for (Directory subDir : dir.getDirArray()) {
      // Continue traversing the file system with the new depth
      traverseFileSystem(subDir, depth);
    }

    numTabs += tabChar;

    // Iterate through each file in the current directory and print the file name
    for (File subFile : dir.getFileArray()) {
      returnString += String.format(numTabs + subFile.getName() + "\n");
    }

  }

}
