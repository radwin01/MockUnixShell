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
import filesystem.FilePath;
import filesystem.FileSystemI;

/**
 * This class handles removing directories in the file system
 * 
 * @version 1.0
 */


public class Rm extends Command {

  /**
   * Constructor function used to create an instance of a R, object. Sets the number of maximum
   * parameters that the user can enter, the file system, and current working directory.
   * 
   * @param fs the current FileSystem in use
   * @param cwd the current CWD in use
   */
  public Rm(FileSystemI fs, CurrentWorkingDirectoryI cwd) {

    super(fs, cwd);

    // Sets the number of parameters to be accepted
    this.setMaxParameters(1);

    // Sets the minimum of parameters to be accepted
    this.setMinParameters(1);

  }

  /**
   * Returns a description of the rm command.
   * 
   * @return the description of the command
   */
  public static String getDescriptionOfCommand() {
    return "Name of command: rm\n\n"
        + "Functionality: Removes directories (formatted as a relative\n"
        + "or absolute path). If the user passes an invalid/nonexistent path as one of the\n"
        + "parameters or passes the current working directory (or any of its parent\n"
        + "directory paths, rm will print an error message.\n\n" + "Command syntax: rm path";
  }

  /**
   * Allow the user to input a directory they would like to delete from the file system. If the
   * directory is in the directory contains the current working directory, or the path is to a
   * file, function returns an error.
   * 
   * @return the exit status of the removal of a directory: complete or does not exist, or null
   *         upon error
   */
  public String execute() {

    Directory directoryToRemove = FilePath.convertAnyPathToDirectory(
        this.getCurrentParameters()[0], this.getShellCWD(), this.getCurrentFileSystem());

    String convertedPath = null;

    // Checks if the user specified a file, or directory does not exist, ie, directory to remove is
    // null
    try {
      convertedPath = directoryToRemove.getFullPathString();
    } catch (NullPointerException e) {
      super.printErrorMessage("That directory does not exist!");
      return null;
    }

    // Checks if the user is trying to delete a directory containing the cwd
    if (checkIfInCWD(this.getShellCWD(), convertedPath)) {
      System.out.println("Cant delete that directory!");
      return null;
    } else {
      convertedPath = convertedPath.substring(1);
      return removeDir(super.getCurrentFileSystem().getRootDir(), convertedPath);
    }


  }

  /**
   * Attempts to remove the directory at the given path from the file system, if it exists.
   * 
   * @param currentDir the current directory to be searched if contains the directory to delete
   *        recursively.
   * @param path the path of the directory to be deleted
   * @return the eventual exit status of deletion: complete or incomplete
   */
  private String removeDir(Directory currentDir, String path) {

    // Splits the path parameter by the delimiter ("/")
    String[] pathSeperated = path.split("/");

    // Stores the size of the split array
    int sizeOfSplit = pathSeperated.length;

    // Checks if the base case has been reached (only 1 directory in the path)
    if (sizeOfSplit == 1) {

      // Iterates through the subdirectories
      for (Directory subDirs : currentDir.getDirArray()) {

        // Checks if the subdirectory matches the desired directory to remove
        if (pathSeperated[0].equals(subDirs.getName())) {

          // Removes the directory
          currentDir.getDirArray().remove(subDirs);
          return subDirs.getName() + " deleted";
        }
      }
    } else {

      // Checks through each sub directory in the current directory
      for (int i = 0; i < currentDir.getDirArray().size(); i++) {

        // Checks if the indexed directory is the next directory in the path
        if (currentDir.getDirArray().get(i).getName().equals(pathSeperated[0])) {

          // String accumulator to hold the remaining path to be searched
          String remainingPath = "";

          for (int j = 1; j < sizeOfSplit; j++) {
            remainingPath += pathSeperated[j];
          }

          return removeDir(currentDir.getDirArray().get(i), remainingPath);
        }
      }
    }
    return "";
  }

  /**
   * Checks if the given path contains the current working directory.
   * 
   * @param path the path of the directory to be deleted
   * @return true if the path contains the current working directory, false otherwise
   */
  private boolean checkIfInCWD(CurrentWorkingDirectoryI cwd, String path) {

    // Checks if the current working directory path contains the path to delete
    if (cwd.getCWDString().indexOf(path) == 0) {
      return true;
    }
    return false;

  }

}
