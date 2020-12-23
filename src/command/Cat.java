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
 * Represents a Cat command
 * 
 * @version 2.0
 */
public class Cat extends Command {

  /**
   * Constructor used to create an instance of a Cat object. Sets the number of minimum parameters
   * that the user can enter after the command.
   * 
   * @param fs the file system to be used
   * @param cwd the current working directory from command call
   */
  public Cat(FileSystemI fs, CurrentWorkingDirectoryI cwd) {

    super(fs, cwd);

    // sets the minimum number of parameters to be accepted
    this.setMinParameters(1);
  }

  /**
   * Returns a description of the cat command.
   * 
   * @return the description of the command
   */
  public static String getDescriptionOfCommand() {
    return "Name of command: cat\n\n"
        + "Functionality: Reads and/or concatenates data from one or more files\n"
        + "(formatted as a relative or absolute path) and prints their contents\n"
        + "to standard output (separated by three lines). If the user passes an\n"
        + "invalid file path, a directory, or a nonexistent file as one of the\n"
        + "parameters, cat will print an error message for that specific file\n\n"
        + "Command syntax: cat file1Path [file2Path ...]";
  }

  /**
   * Checks if the parameter ends with an unnecessary slash/slashes
   */
  private boolean checkForEndSlashes(int i) {
    return (getCurrentParameters()[i].substring(getCurrentParameters()[i].length() - 1)
        .equals("/"));
  }

  /**
   * Checks if fileName exists as a file object in the designated directory
   */
  private boolean checkNextFile(Directory designatedDir, String fileName) {
    for (File f : designatedDir.getFileArray()) {
      // if we find a file with its name being fileName
      if (fileName.equals(f.getName()))
        return true;
    }
    return false;
  }

  /**
   * Checks if fileName exists as a directory object in the designated directory
   */
  private boolean checkNextDirectory(Directory designatedDir, String fileName) {
    for (Directory d : designatedDir.getDirArray()) {
      // if we find a file with its name being fileName
      if (fileName.equals(d.getName())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if any of the following arguments is a valid file
   */
  private boolean checkNext(int i, boolean success) {
    if (success) {
      for (int j = i; j < super.getCurrentParameters().length - 1; j++) {
        String[] nextParam = getCurrentParameters()[j + 1].split("/");
        Directory nextDir = FilePath.getEndDirectoryfromPath(nextParam, this.getShellCWD(),
            this.getCurrentFileSystem());
        if (nextDir == null)
          continue;
        if (checkNextFile(nextDir, getCurrentParameters()[j + 1]
            .split("/")[getCurrentParameters()[j + 1].split("/").length - 1]))
          return true;
      }
    }
    return false;
  }

  /**
   * Allows the user to print the contents of a file and/or concatenate multiple files together
   * separated by three line breaks. The user can enter either an absolute file path (path must
   * start with /), or the user may enter a relative file path if the desired file(s) are are
   * located in the current working directory. If the user enters an invalid file path, a 
   * directory, or a file (or multiple files) that does not exist as one of the parameters, the
   * method prints an error message. However, if the only argument provided is invalid, the method
   * returns null.
   * 
   * @return the contents of all the files once the command has been executed or null upon failure
   */
  public String execute() {
    String contents = "";
    // for each argument provided
    for (int i = 0; i < super.redirectLoopEnd(); i++) {
      boolean success = false;
      String[] splitParam = getCurrentParameters()[i].split("/");
      Directory currentDir = FilePath.getEndDirectoryfromPath(splitParam, this.getShellCWD(),
          this.getCurrentFileSystem());
      String fileName = splitParam[splitParam.length - 1];

      // check if file has valid syntax i.e does not end in a slash
      if (checkForEndSlashes(i))
        System.out.println(fileName + ": File cannot end in a slash");
      else {
        if (currentDir == null)
          continue;

        // if you find the file, print its contents
        for (File f : currentDir.getFileArray()) {
          if (fileName.equals(f.getName())) {
            contents += f.getContent() + "\n";
            success = true;
          }
        }

        /**
         * if you haven't found a file with the name fileName but you find it as a directory, print
         * error
         */
        if (!success && checkNextDirectory(currentDir, fileName))
          System.out.println(fileName + ": Is a directory!");

        // if fileName doesn't exist as a file nor directory, print error
        else if (!success && !checkNextDirectory(currentDir, fileName))
          System.out.println(fileName + ": No such file or directory");
      }

      // if the only argument you get is invalid, return null
      if (i == super.getCurrentParameters().length - 1 && i == 0 && !success)
        return null;

      /**
       * otherwise, look at the next arguments (if they exist): if any of the following arguments
       * are a success (i.e is a valid file), print 3 lines to divide the file contents.
       */
      if (checkNext(i, success))
        contents += "\n\n\n";
    }
    return super.redirect(contents);
  }
}
