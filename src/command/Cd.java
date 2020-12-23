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
 * Represents a Cd command.
 * 
 * @version 1.0
 */

public class Cd extends Command {

  /**
   * Creates an instance of a Cd object. Sets the minimum number of parameters that the user can
   * enter.
   * 
   * @param fs the file system to be used
   * @param cwd the current working directory from command call
   */
  public Cd(FileSystemI fs, CurrentWorkingDirectoryI cwd) {

    super(fs, cwd);

    // Set min and max parameters
    this.setMinParameters(1);
    this.setMaxParameters(1);
  }

  /**
   * Method that returns the description of the cd command.
   * 
   * @return the description of the command
   */
  public static String getDescriptionOfCommand() {
    return "Name of command: cd\n\n"
        + "Functionality: Changes the current working directory to the destination\n"
        + "directory (as an absolute or relative path). Instead of inputting a\n"
        + "destination directory, the following options are also available:\n"
        + "/         change current directory to root directory\n"
        + "..        change current directory to current directory's parent" + " directory\n"
        + ".         current directory remains unchanged\n"
        + "If the user passes an invalid directory path, or a symbol not included\n"
        + "in the above list, cd will print an error message.\n\n"
        + "Command syntax: cd directoryPath (or one of the options listed above)";
  }

  /**
   * Changes the current working directory. The following cases are checked:
   * <p>
   * <ul>
   * <li>Case 1: "cd /" - The Current Working Directory is changed to the root.</li>
   * <li>Case 2: "cd ////.." - The user has entered a String of all slashes, and so an error
   * message is printed and the Current Working Directory is not changed.</li>
   * <li>Case 3: cd ././././" or "cd /./././" or "cd ." - The Current Working Directory is
   * unchanged.</li>
   * <li>Case 4: "cd .." or "cd ../../../" - The Current Working Directory is changed into the
   * parent directory specified by the "../". Thus, "cd ../../" will changed into the parent
   * directory of the parent directory. If there are more parent directories to be changed into
   * then specified, then the Current Working Directory will be set to root.</li>
   * <li>Case 5: "cd /dir1/dir2/.." - In this case, since the given file path starts with a slash
   * ('/'), the file path is treated as a full file path (meaning it starts searching for the final
   * directory starting from the root.</li>
   * <li>Case 6: "cd dir1/dir2/.." - In this case, since the given file path starts with a
   * directory name, the file path is treated as a relative file path. Thus, searching for the
   * existence of the given file path will start from the Current Working Directory (ex. The first
   * check will be searching for "dir1" in the Current Working Directory).</li>
   * </ul>
   * <p>
   * 
   * NOTE: In (4), (5) and (6) if the path ends with a '/', then it is treated as invalid.
   * 
   * @return the full path of the current working directory, null if path is invalid
   */
  public String execute() {
    // Storing the user specified directory.
    String newDir = super.getCurrentParameters()[0];

    // Case 1: Checking if the user entered 'cd /'
    if (newDir.length() == 1 && newDir.charAt(0) == '/') {
      this.getShellCWD().setCWD(super.getCurrentFileSystem().getRootDir());

      // Case 2: Checking if the user entered 'cd //////'
    } else if (FilePath.isAllChar(newDir, '/')) {
      return super.printErrorMessage(newDir + ": No such directory");

      // Case 3: Checking if the user inputed 'cd ././././.'
    } else if (FilePath.isAllStringBetweenSlash(newDir, ".")) {
      // Do nothing as you stay in the same directory

      // Case 4: Checking if the user had input 'cd ../../..'
    } else if (FilePath.isFullyDotted(newDir.split("/"))) {
      /*
       * Calling the convertDotToCurrentDir method from FilePath to find the destination directory
       * and assigning it to current working directory.
       */
      this.getShellCWD().setCWD(FilePath.convertDotToCurrentDir(newDir, this.getShellCWD()));

      /*
       * Case 5 and 6: At this point the user has either inputed a full or relative path, so it is
       * necessary to check for both, and determine if it is a valid path.
       */
    } else {
      // Splitting the path into an array of individual directories.
      String[] newDirSplit = newDir.split("/");

      // Finding the second last directory from the path inputed by the user.
      Directory secondLast = FilePath.getEndDirectoryfromPath(newDirSplit, this.getShellCWD(),
          this.getCurrentFileSystem());

      // Checking if the second last directory is null. If it is, then the path is invalid.
      if (secondLast != null) {
        /*
         * Since the second last directory is valid, check the last directory for validity by
         * checking if the last directory in newDirSplit is a sub-directory in secondLast.
         */
        if (newDirSplit[newDirSplit.length - 1].equals("..")) {
          this.getShellCWD().setCWD(secondLast.getParentDir());
          return this.getShellCWD().getCWDString();
        }

        if (secondLast.findInSubDirectories(newDirSplit[newDirSplit.length - 1]) == -1) {
          return super.printErrorMessage(newDir + ": No such directory");
        }

        /*
         * Changing the current working directory to the directory specified by the user as it is
         * found to be valid. The current working directory directory is first set to secondLast,
         * and is then set to the final directory by using setDirByString, which sets the current
         * working directory to a specified sub-directory.
         */
        this.getShellCWD().setCWD(secondLast);
        this.getShellCWD().setToSubDirectoryByString(newDirSplit[newDirSplit.length - 1]);
      } else {
        return null;
      }
    }

    // Returning the full path of the new current working directory.
    return this.getShellCWD().getCWDString();
  }
}
