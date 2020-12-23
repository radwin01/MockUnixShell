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
import filesystem.FilePath;
import filesystem.FileSystemI;
import filesystem.ItemList;

/**
 * Represents a Pushd command.
 * 
 * @version 1.0
 */

public class Pushd extends Command {

  // Stores the current directory stack.
  private DirectoryStack dirStack;

  /**
   * Creates an instance of a Pushd object. Sets the minimum and maximum number of parameters that
   * the user can enter.
   * 
   * @param fs the current FileSystem in use
   * @param cwd the current CWD in use
   */
  public Pushd(FileSystemI fs, CurrentWorkingDirectoryI cwd, DirectoryStack dirStack) {

    super(fs, cwd);
    this.dirStack = dirStack;

    // Set min and max parameters
    this.setMinParameters(1);
    this.setMaxParameters(1);
  }

  /*
   * m* Method that returns the description of the pushd command.
   * 
   * @return the description of the command
   */
  public static String getDescriptionOfCommand() {
    return "Name of command: pushd\n\n"
        + "Functionality: Saves the current working directory (or an absolute/relative\n"
        + "directory path) in memory to the stack. If the user passes an invalid\n"
        + "directory path, pushd will print an error message.\n\n"
        + "Command syntax: pushd directoryPath";
  }

  /**
   * Pushes the current working directory onto the directory stack (using LIFO behavior), and
   * changes the current working directory to the specified path.
   * 
   * @return the current directory stack to if the specified path is valid, null otherwise
   */
  public String execute() {
    // Storing the specified path.
    String newDirectory = this.getCurrentParameters()[0];

    // Getting the current directory stack from DirectoryStack.
    ItemList<Directory> dirS = dirStack.getDirStack();

    // Splitting the specified path into an array of individual directories.
    String[] newDirSplit = newDirectory.split("/");

    // Storing the current working directory.
    Directory originalDir = this.getShellCWD().getCWD();


    // Checking if the specified path is the root directory.
    if (newDirectory.length() == 1 && newDirectory.charAt(0) == '/') {
      this.getShellCWD().setCWD(this.getCurrentFileSystem().getRootDir());

      // Checking if the specified path is of the form, '/////'.
    } else if (FilePath.isAllChar(newDirectory, '/')) {
      return super.printErrorMessage(newDirectory + ": No such directory");

      // Checking if the specified path is of the form, '././././'.
    } else if (FilePath.isAllStringBetweenSlash(newDirectory, ".")) {

      // Checking if the specified path is of the form, '../../..'.
    } else if (FilePath.isFullyDotted(newDirSplit)) {
      this.getShellCWD().setCWD(FilePath.convertDotToCurrentDir(newDirectory, this.getShellCWD()));

      /*
       * At this point the specified path is either a full or relative path. So it is necessary to
       * check if the path inputed is valid.
       */
    } else {

      // Getting the second last directory of the specified path.
      Directory destDir = FilePath.convertAnyPathToDirectory(newDirectory, this.getShellCWD(),
          this.getCurrentFileSystem());

      // Checking if destination directory is null, indicating it is invalid.
      if (destDir != null) {
        this.getShellCWD().setCWD(destDir);
      } else {
        return null;
      }
    }

    /*
     * Method has not returned, so the specified path is valid and the original current working
     * directory is pushed onto the directory stack.
     */
    dirS.add(originalDir);
    return dirStack.getDirStackAsString();
  }
}
