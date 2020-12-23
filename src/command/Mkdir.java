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
import filesystem.FilePath;
import filesystem.FileSystemI;

/**
 * This class handles creating directories in the file system
 * 
 * @version 2.0
 */
public class Mkdir extends Command {

  /**
   * Constructor function used to create an instance of a Mkdir object. Sets the number of maximum
   * parameters that the user can enter, the file system, and current working directory
   * 
   * @param fs the current FileSystem in use
   * @param cwd the current CWD in use
   */
  public Mkdir(FileSystemI fs, CurrentWorkingDirectoryI cwd) {

    super(fs, cwd);

    // Sets the minimum of parameters to be accepted
    this.setMinParameters(1);

  }

  /**
   * Method that returns the description of the mkdir command.
   * 
   * @return the description of the command
   */
  public static String getDescriptionOfCommand() {
    return "Name of command: mkdir\n\n"
        + "Functionality: Creates a new directory if it does not already exist\n"
        + "in the designated directory path (can be an absolute or relative path).\n"
        + "If the user passes an invalid path, mkdir will print an error message.\n\n"
        + "Command syntax: mkdir directoryName";
  }

  /**
   * Allows the user to create an empty directory in the provided directory path. The user can
   * enter either an absolute path (path must start with /), or the user may enter a relative path
   * using the current working directory. If the user enters a list of directories, if all are
   * valid directories, all directories are created. If the user enters a invalid path or a
   * directory name that already exists, returns an error.
   * 
   * @return name of the directory (or directories) created or null if failure
   */
  public String execute() {

    // Checks if all directories are valid paths, if not, return null
    if (!checkLegalParams(this.getCurrentParameters())) {
      super.printErrorMessage("Invalid path given!");
      return null;
    }

    // String to be returned containing all directory names
    String createdDirs = "";

    // Stores the Directory of the current parent directory of the directory to be created
    Directory current;

    // Stores the split path of each parameter given
    String[] splitInput;

    // Stores the name of the directory to be created
    String dirName;

    for (String createDir : this.getCurrentParameters()) {


      // Splits the path given by the user
      splitInput = createDir.split("/");

      // Grabs the directory the user wants to create a directory in
      current = FilePath.getEndDirectoryfromPath(splitInput, this.getShellCWD(),
          this.getCurrentFileSystem());

      // The name of the directory to be created
      dirName = splitInput[splitInput.length - 1];

      createdDirs += dirName + " ";

      // Create the desired directory
      current.addDirectory(new Directory(dirName, current));
    }

    // Return list of created directory names since the command was executed correctly
    return createdDirs;

  }

  /**
   * Checks if all of the given paths are valid directories to create.
   * 
   * @param potentialPaths list of directory paths the user wants to create.
   * @return true if the path for all potential paths are legal directories to create, false
   * otherwise.
   */
  private boolean checkLegalParams(String[] potentialPaths) {

    String[] splitInput;

    for (String path : potentialPaths) {

      // Checks if the path is of the form "(path)//)
      try {

        if (path.substring(getCurrentParameters()[0].length() - 2).equals("//")) {
          return false;
        }
        // Catches the out of bounds error if the parameter is less then 2 characters long
      } catch (StringIndexOutOfBoundsException e) {

      }

      // Splits the path for directory by '\'
      splitInput = path.split("/");

      // Grabs the directory the user wants to create a directory in
      Directory current = FilePath.getEndDirectoryfromPath(splitInput, this.getShellCWD(),
          this.getCurrentFileSystem());

      // Checks if the directory path was invalid
      if (current == null) {
        return false;
      }

      String dirName = splitInput[splitInput.length - 1];

      if (!(Validation.containsValidCharacters(dirName)))
        // Return false since the directory name contains illegal characters
        return false;

      // Iterates through each directory in the current directory
      for (Directory subDir : current.getDirArray()) {

        // Checks if a sub directories name is the same as the one to be created
        if (subDir.getName().equals(dirName)) {
          // Return false since the directory name already exists
          return false;
        }

      }

      // Iterates through each file in the current directory
      for (File subFile : current.getFileArray()) {

        // Checks if there exists a file with the name for the desired directory
        if (subFile.getName().equals(dirName)) {

          // Return false since the directory name already exists as a file
          return false;

        }

      }

    }

    // Return true, as all paths are valid
    return true;
  }

}
