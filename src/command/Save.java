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
import filesystem.File;
import filesystem.FileSystemI;
import inputoutput.UserTerminalIn;

/**
 * Represents a Save command.
 * 
 * @version 1.0
 */
public class Save extends Command {


  private DirectoryStack dirStack;

  /**
   * Creates an instance of a Save object. Sets the minimum number of parameters that the user can
   * enter.
   * 
   * @param fs the current FileSystem in use
   * @param cwd the current CWD in use
   * @param dirStack the current directory stack
   */
  public Save(FileSystemI fs, CurrentWorkingDirectoryI cwd, DirectoryStack dirStack) {

    super(fs, cwd);
    this.dirStack = dirStack;

    // Set min and max parameters
    this.setMinParameters(1);
    this.setMaxParameters(1);
  }

  /**
   * @return the current directory stack
   */
  public DirectoryStack getDirStack() {
    return dirStack;
  }

  /**
   * Returns a description of the save command.
   * 
   * @return the description of the command
   */
  public static String getDescriptionOfCommand() {
    return "Name of command: save\n\n"
        + "Functionality: Saves the current filesystem and all of it's contents to a file on the\n"
        + "computer of the user. This file is given by the user and is created if it does not\n"
        + "already exist. The type of file produced on the computer in the given path will be a\n"
        + "text file as named by the user.\n\n" + "Command syntax: save fileName";
  }

  /**
   * Method that creates a string containing all contents of current directory and sub directories
   * contents recursively from given directory.
   * 
   * @param curDir the current directory to start generation of string
   * @param curPath the current path of the function
   * @return the String containing all contents of all directories
   */
  private String saveCurrentDirectory(Directory curDir, String curPath) {

    // Initialize return string form first directory
    String returnString = "";
    if (!(curPath.equals("/"))) {
      returnString += ("This Directory has the path:\n" + curPath + "\n");
    }

    // Add each file and contents to string
    for (File f : curDir.getFileArray()) {
      returnString += "This File has the name:\n" + f.getName() + "\n" + f.getContent() + "\n";
    }

    // If root don't double write slash
    if (curPath.equals("/")) {
      curPath = "";
    }

    // Call function again for each sub-directory and return result
    for (Directory d : curDir.getDirArray()) {
      returnString += this.saveCurrentDirectory(d, curPath + "/" + d.getName());
    }
    return returnString;
  }

  /**
   * Method to save the current state of the Jshell including current directory history and
   * directory stack
   * 
   * @return String values written to file
   */
  public String saveState() {

    // Save current working directory
    String retString = "The Current Directory is:\n";
    retString += this.getShellCWD().getCWDString();
    retString += "\n";

    // Save command history
    for (String s : UserTerminalIn.getCommandHistory()) {
      retString += s + "\n";
    }

    // Save directory stack
    retString += "\nThe directory stack is:\n";
    retString += this.getDirStack().getDirStackAsString();
    return retString;
  }

  /**
   * Method that saves current file system session to text file in storage system of user on
   * computer.
   * 
   * @return the String that is written to the file with all FileSystem contents
   */
  public String execute() {

    // Initialize write string from save current directory from root
    String writeString = saveCurrentDirectory(super.getCurrentFileSystem().getRootDir(), "/");
    writeString += this.saveState();
    writeString = writeString.substring(0, writeString.length() - 1);

    // Try to write to given file path
    try {
      java.io.FileWriter writing = new java.io.FileWriter(this.getCurrentParameters()[0]);
      java.io.BufferedWriter bw = new java.io.BufferedWriter(writing);
      bw.write(writeString);
      bw.close();

      // If file path error is found, print it for user and return null
    } catch (java.io.IOException e) {
      System.out.println(e);
      return null;
    }

    // Return output file contents
    return writeString;

  }
}
