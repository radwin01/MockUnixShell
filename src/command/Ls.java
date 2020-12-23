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
 * Represents an Ls command.
 * 
 * @version 2.0
 */

public class Ls extends Command {

  // Stores the current directory that the command is working in
  private Directory curDir;

  /**
   * Ls constructor that initializes the current working directory from point of command call
   * 
   * @param cwd stores current working directory from command call
   * @param fs stores current FileSystem instance with design
   */
  public Ls(FileSystemI fs, CurrentWorkingDirectoryI cwd) {

    super(fs, cwd);

    // Set current direct
    this.curDir = cwd.getCWD();
  }

  /**
   * Method that returns the description of the history command.
   * 
   * @return the description of the command
   */
  public static String getDescriptionOfCommand() {
    return "Name of command: ls\n\n"
        + "Functionality: Lists the contents of the current working directory if\n"
        + "no parameters are given. Instead of printing the current directory's\n"
        + "contents, the following options are also available:\n"
        + "directoryPath                Lists the contents of the directory path\n"
        + "filePath                     Prints the file path\n"
        + "Note: either of the two paths may be absolute or relative. However,\n"
        + "if the user passes an invalid directory/file path as one of the parameters,\n"
        + "ls will print an error message.\n"
        + "Note: if -R is provided in the arguments, followed by a directoryPath, ls\n"
        + "will recursively list all subdriectories contained in the path.\n\n"
        + "Command syntax: ls [-R] [path1 ...]";
  }

  /**
   * Sets current directory for the command.
   * 
   * @param newDir the new directory for the command
   */
  private void setCurDir(Directory newDir) {
    this.curDir = newDir;
  }

  /**
   * Sets current directory for the command.
   * 
   * @return the directory for the command.
   */
  private Directory getCurDir() {
    return this.curDir;
  }

  /**
   * Method that takes the last part of the directory that checks if the final directory is given
   * in dot form, in which case it updates appropriately (and returns null) or returns the original
   * string if it is not given in dot form.
   * 
   * @param potentialDir the string to check for dot formatting
   * @return the original string if not a dot, null if it is a dot representation
   */
  private String convertEndDirToDirectory(String potentialDir) {

    // Convert string to directory from dots
    Directory newDir = FilePath.convertDotToDir(potentialDir, this.getCurDir());

    // If not a dot representation, return original string
    if (newDir == null) {
      return potentialDir;
    }

    // If a dot representation, set directory to it and return null
    this.setCurDir(newDir);
    return null;
  }

  /**
   * Method that updates the current directory based on the path to be used for action and returns
   * a string for printing based on several cases:
   * <p>
   * <ul>
   * <li>1. null if the path does not exist.</li>
   * <li>2. The name of the directory if it ends in a directory.</li>
   * <li>3. The contents of a file if it ends in a file.</li>
   * <li>4. Empty string if it is empty.</li>
   * </ul>
   * 
   * @return the printable string associated with above outcomes based on parameter contents; or
   *         null upon failure
   */
  private String updateCurDirForAction(String path) {

    // If given only a slash, return root in printable format
    if (path.equals("/")) {
      this.setCurDir(this.getCurrentFileSystem().getRootDir());
      return "/:\n";
    }

    if (path.substring(path.length() - 1).equals("/")) {
      return super.printErrorMessage("Error with " + path 
          + ". Path can't end cannot end in slash");
    }

    // Split path at slashes and set current directory from path
    String[] splitInput = path.split("/");
    this.setCurDir(FilePath.getEndDirectoryfromPath(splitInput, this.getShellCWD(),
        this.getCurrentFileSystem()));

    // Checks if the directory path was invalid
    if (this.getCurDir() == null)
      return null;

    // Initialize the name of the final part of the path
    String endName = this.convertEndDirToDirectory(splitInput[splitInput.length - 1]);

    // If endName is empty get directory name from curDir and return it
    if (endName == null) {
      if (this.getCurDir().getParentDir() == null || (this.getCurDir().getName().equals("")
          && this.getCurDir().getParentDir().getParentDir().getName().equals("")))
        return "/:\n";

      return (this.getCurDir().getName() + ":\n");
    }

    // If a file is given, return contents of the file
    for (File f : this.getCurDir().getFileArray()) {
      if (f.getName().equals(endName))
        return (path + ";");
    }

    // If a directory is given, update current directory and return its name
    for (Directory d : this.getCurDir().getDirArray()) {
      if (d.getName().equals(endName)) {
        this.setCurDir(d);
        if (d.getParentDir() == null)
          endName = "/";
        return (endName + ":\n");
      }
    }

    // If it does not exist, print error and return null
    return super.printErrorMessage("Last part of path does not exist in: " + path);

  }

  /**
   * Method that gets the string that Ls should print to the terminal after execution. If the last
   * part of the path is a file it returns the contents. If the last part is a directory it returns
   * contents. If there are no paths it returns contents of current working directory. Error
   * returns null.
   * 
   * @param parameter the current string path from parameters to be tested
   * @return the string to be written for Ls call; null if error
   */
  private String getPrintString(String parameter) {

    // Initialize return variable
    String printString = "";

    // If parameter is not null, update current directory
    if (parameter != null) {
      printString = this.updateCurDirForAction(parameter);
    }
    // If printString fails to initialize return error
    if (printString == null) {
      return null;
    }

    // If a directory is returned iterate through contents
    if (!((printString.length() > 0)
        && printString.substring(printString.length() - 1).equals(";"))) {

      // Add directory contents to printString
      for (File f : this.getCurDir().getFileArray()) {
        printString += f.getName() + "\n";
      }
      for (Directory d : this.getCurDir().getDirArray()) {
        printString += d.getName() + "\n";
      }
    }

    // If a file is returned, remove last character that identifies file
    else {

      if (parameter.substring(parameter.length() - 1).equals("/")) {
        System.out.println("Error with " + parameter + ". File cannot end cannot end in a slash");
        return null;
      }
      // Remove file determining character from file and add new line
      printString = printString.substring(0, printString.length() - 1) + "\n";
    }
    return printString;
  }

  /**
   * Method that gets the string that Ls should print to the terminal after execution. If the last
   * part of the path is a file it returns the contents. If the last part is a directory it returns
   * contents. It does this for the directory and all sub-directories recursively. Returns final
   * result.
   * 
   * @param parameter the current string path from parameters to be tested
   * @return the string to be written for Ls call; null if error
   */
  private String getRecursivePrintString(String parameter) {

    // Initialize return value and evaluate first directory
    String printString = "";
    String temp = this.getPrintString(parameter);

    // Return null if error
    if (temp != null) {

      // Add to printString from current directory
      printString += temp + "\n";
      Directory activeDir = this.getCurDir();

      // Get print string recursively form each sub-directory
      for (Directory d : activeDir.getDirArray()) {
        String newPath = parameter + "/" + d.getName();

        // If root do not add slash in between new path
        if (parameter.equals("/")) {
          newPath = parameter + d.getName();
        }
        printString += this.getRecursivePrintString(newPath);
      }

      // Return result
      return printString;
    }
    return temp;
  }

  /**
   * Main action command for Ls that carries out functionality according to several cases:
   * <p>
   * <ul>
   * <li>1. No path given prints and returns contents of current working directory.</li>
   * <li>2. Invalid path prints error and returns null.</li>
   * <li>3. Path to file prints contents of file.</li>
   * <li>4. Path to directory prints name of directory followed by directory contents and
   * additional newline character as requested in assignment documentation.</li>
   * <li>5. -R argument recursively lists all sub-directories</li>
   * </ul>
   * 
   * @return the value printed in accordance with cases above
   */
  public String execute() {

    // Initialize return value
    String printString = "";

    // If no parameters call function to get current working directory contents
    if (this.redirectLoopEnd() == 0) {
      printString = this.getPrintString(null) + "\n";
    }

    // If parameters get content of path
    else {

      // Check for -R argument
      if (this.getCurrentParameters()[0].equals("-R")) {

        // If only -R given list recursively from root
        if (this.redirectLoopEnd() == 1) {
          this.parametersUpdate();
        }

        // For each parameter get content of path and sub-directories
        for (int i = 1; i < this.redirectLoopEnd(); i++) {
          String temp = this.getRecursivePrintString(this.getCurrentParameters()[i]);
          if (temp == null) {
            return null;
          }
          printString += temp + "\n";
        }
      }

      // If any path should return an error, do not give results of any other path
      else {
        for (int i = 0; i < this.redirectLoopEnd(); i++) {
          String temp = this.getPrintString(this.getCurrentParameters()[i]);
          if (temp == null) {
            return null;
          }

          // If path works update printString with contents plus new line
          printString += temp + "\n";
        }
      }
    }
    // Print a return printable string with command results
    return super.redirect(printString);
  }

  /**
   * Update parameters given the ls as a call without any directories given
   */
  private void parametersUpdate() {

    // If no redirection call, make it recursive from root
    if (this.getNumParameters() == 1) {
      this.setCurrentParameters(2, new String[] {"-R", "/"});
      return;
    }

    // If redirection call, make it a redirection call from root
    this.setCurrentParameters(4, new String[] {"-R", "/", ">", this.getCurrentParameters()[2]});
  }
}
