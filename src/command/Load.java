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
import filesystem.DirectoryStack;
import filesystem.FilePath;
import filesystem.FileSystemI;
import inputoutput.UserTerminalIn;

/**
 * Represents a Load command.
 * 
 * @version 1.0
 */
public class Load extends Command {

  // the current directory stack
  private DirectoryStack dirStack;

  // the current user terminal in
  private UserTerminalIn terminal;

  /**
   * Creates an instance of a Save object. Sets the minimum number of parameters that the user can
   * enter.
   * 
   * @param fs the current FileSystem in use
   * @param cwd the current CWD in use
   * @param dirStack the current directory stack
   * @param terminal the current user terminal in
   */
  public Load(FileSystemI fs, CurrentWorkingDirectoryI cwd, DirectoryStack dirStack,
      UserTerminalIn terminal) {

    super(fs, cwd);
    this.dirStack = dirStack;
    this.terminal = terminal;

    // Set min and max parameters
    this.setMinParameters(1);
    this.setMaxParameters(1);
  }

  /**
   * Returns a description of the save command.
   * 
   * @return the description of the command
   */
  public static String getDescriptionOfCommand() {
    return "Name of command: load\n\n"
        + "Functionality: Loads a previous filesystem and all of it's contents from a file on "
        + "the\ncomputer of the user. This file is given as a path by the user as the second "
        + "parameter.\nThis command will only execute if it is the first command input upon "
        + "starting\nup the JShell and will return an error otherwise\n\n" 
        + "Command syntax: load fileName";
  }

  /**
   * Command to ensure that Load only works if it is the first command that is called so as not to
   * cause issues as per assignment details
   * 
   * @return boolean true if it is the first command, false otherwise
   */
  private boolean verifyUsage() {

    // Get command history
    int comCheck = UserTerminalIn.getCommandHistory().size();

    // If length is greater than 1 return false
    if (comCheck > 1) {
      return false;
    }
    return true;
  }


  /**
   * Command to populate current FileSystem from given file on computer from previous session. Does
   * so with a loop to add all contents of previous file system line by line from file.
   * 
   * @param curLine the current line from the file for processing
   * @return String string of commands called to populate file system
   */
  private String readContents(String curLine, String dir, String place, String retString) {

    // Attempt to read the file
    try {

      // Open file for reading
      java.io.FileReader reader = new java.io.FileReader(this.getCurrentParameters()[0]);
      java.io.BufferedReader br = new java.io.BufferedReader(reader);

      // Loop while not at end of file
      while ((curLine = br.readLine()) != null) {

        // If line signals directory, reset location
        if (curLine.equals("This Directory has the path:")) {
          place = null;

          // Create directory with following line
          curLine = br.readLine();
          dir = curLine + "/";
          Validation.interpretCommand("mkdir " + curLine, null, this.getShellCWD(),
              this.getCurrentFileSystem()).execute();

          // Add command call to return string
          retString += ("mkdir " + curLine + "\n");

          // If line signals file, update location with following line
        } else if (curLine.equals("This File has the name:")) {
          curLine = br.readLine();
          place = curLine;

          // Create file with following line and dir path; also add to return string
          Validation.interpretCommand("echo \"\" > " + dir + curLine, null, this.getShellCWD(),
              this.getCurrentFileSystem()).execute();
          retString += ("echo \"\" > " + curLine + "\n");

          // If inside file still, append contents to file
        } else if (curLine.equals("The Current Directory is:"))
          retString += this.updateState(br);

        else if (place != null) {
          Validation.interpretCommand("echo \"" + curLine + "\" >> " + dir + place, null,
              this.getShellCWD(), this.getCurrentFileSystem()).execute();

          // Add contents to return string
          retString += ("echo \"" + curLine + "\" >> " + place + "\n");
        }
      }

      // Close file and return results
      br.close();
      return retString;

      // If file path error is found, print it for user and return null
    } catch (java.io.IOException e) {
      System.out.println(e);
      return null;
    }
  }

  /**
   * Method to update the state of current directory, directory stack and history from given file
   * 
   * @param br buffered reader from previous command call
   * @return String the string of values read from file
   */
  private String updateState(java.io.BufferedReader br) {
    try {

      // Set variables for control
      String retString = "";
      boolean inDirStack = false;

      // Set current working directory
      String curLine = br.readLine();
      this.getShellCWD().setCWD(
          FilePath.convertAnyPathToDirectory(curLine, getShellCWD(), getCurrentFileSystem()));

      // For each line, read as long as not at end of file
      while ((curLine = br.readLine()) != null) {
        retString += curLine + "\n";

        // If not in directory stack, update history
        if (!inDirStack) {
          if (curLine.equals("The directory stack is:"))
            inDirStack = true;
          else {
            this.getTerminal().setCurrentCommand(curLine);
          }

          // If in directory stack add to directory stack
        } else {
          if (!curLine.equals("")) {
            dirStack.getDirStack().add(
                FilePath.convertAnyPathToDirectory(curLine, getShellCWD(), 
                    getCurrentFileSystem()));
          }
        }
      }

      // Return string of values
      return retString;

      // Catch failed open of file
    } catch (java.io.IOException e) {
      System.out.println(e);
      return null;
    }
  }

  /**
   * Method to load new session and populate it with contents of previous session by saved file on
   * computer of user.
   * 
   * @return String commands called from old file to populate new file system; null if error
   */
  public String execute() {

    // Check to see if first command
    if (this.verifyUsage()) {
      String retString = "";
      String toAdd = UserTerminalIn.getCommandHistory().get(0);
      UserTerminalIn.getCommandHistory().clear();

      // Populate file system and return results
      String curLine = null;
      retString = this.readContents(curLine, "", null, "");
      UserTerminalIn.getCommandHistory().add(toAdd);
      return retString;
    }

    // Return error if not first command
    return this.printErrorMessage("Load must be first command called");
  }

  /**
   * @return the current directory stack
   */
  public DirectoryStack getDirStack() {
    return dirStack;
  }

  /**
   * @return the terminal
   */
  public UserTerminalIn getTerminal() {
    return terminal;
  }
}
