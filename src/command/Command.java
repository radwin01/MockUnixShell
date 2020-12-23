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

import filesystem.CurrentWorkingDirectory;
import filesystem.CurrentWorkingDirectoryI;
import filesystem.File;
import filesystem.FileSystemI;
import filesystem.Redirection;

/**
 * Represents a generic command.
 * 
 * @version 2.0
 */

public class Command {

  // Stores maximum and minimum parameters for command call
  private int minParameters;
  private int maxParameters = -1;

  // Stores value and contents of parameters on function call
  private int numParameters;
  private String[] currentParameters;

  // Stores symbol Index for redirection
  private int symbolIndex;
  private String symbol;

  // Stores current FileSystem instance with singleton design
  private FileSystemI currentFileSystem;

  // Stores current working directory from command call
  private CurrentWorkingDirectoryI shellCWD;

  /**
   * Default constructor method to initialize file system and current working directory
   * 
   * @param cwd stores current working directory from command call
   * @param fs stores current FileSystem instance with design
   */
  public Command(FileSystemI fs, CurrentWorkingDirectoryI cwd) {
    this.currentFileSystem = fs;
    this.shellCWD = cwd;
  }

  /**
   * Sets current parameters of command call and sets amount.
   * 
   * @param numParameters the number of parameters in parameters
   * @param parameters the parameter list given at command line call
   */
  public void setCurrentParameters(int numParameters, String[] parameters) {
    this.numParameters = numParameters;
    this.currentParameters = parameters;
  }

  /**
   * Returns a list containing the minimum and maximum parameters for this command's call.
   * 
   * @return the array of minimum and max parameters in that order
   */
  public int[] getParameterRestrictions() {
    int[] retList = {this.minParameters, this.maxParameters};
    return retList;
  }

  /**
   * Return the private variable currentParameters value.
   * 
   * @return the currentParameters variable of the class
   */
  public String[] getCurrentParameters() {
    return this.currentParameters;
  }

  /**
   * Return the private variable currentParameters value.
   * 
   * @return the currentParameters variable of the class
   */
  public int getNumParameters() {
    return numParameters;
  }

  /**
   * Sets minimum parameters required for command call.
   * 
   * @param the minimum parameters required for command call
   */
  public void setMinParameters(int minParameters) {
    this.minParameters = minParameters;
  }

  /**
   * Sets maximum parameters required for command call.
   * 
   * @param maxParameters the minimum parameters required for command call
   */
  public void setMaxParameters(int maxParameters) {
    this.maxParameters = maxParameters;
  }

  /**
   * Default command action that carries out command functionality.
   * 
   * @return the empty string to signify command has not failed
   */
  public String execute() {
    return "";
  }

  /**
   * Return the private variable currentFileSystem value.
   * 
   * @return the currentFileSystem variable of the class.
   */
  public FileSystemI getCurrentFileSystem() {
    return currentFileSystem;
  }

  /**
   * Return the private variable cwd value.
   * 
   * @return the cwd variable of the class
   */
  public CurrentWorkingDirectoryI getShellCWD() {
    return shellCWD;
  }

  /**
   * Set the private variable cwd value.
   * 
   * @param cwd the new cwd variable of the class
   */
  public void setShellCWD(CurrentWorkingDirectory cwd) {
    this.shellCWD = cwd;
  }

  /**
   * Method to check for Redirection symbol
   * 
   * @param symbolIndex the index at which the redirection symbol occurs
   * @param potentialSymbol the string to be checked for redirection symbol
   * @return the success or error code in relation to check quotations method
   */
  protected int checkSymbolIndex(int symbolIndex, String potentialSymbol) {

    // If it's an append/write symbol keep track of index and return appropriate success value
    this.setSymbolIndex(symbolIndex);
    if (potentialSymbol.equals(">")) {
      this.setSymbol(">");
      return 2;
    } else if (potentialSymbol.contentEquals(">>")) {
      this.setSymbol(">>");
      return 3;
    }

    // If not a redirection symbol return 0
    return 0;

  }

  /**
   * method to determine the value that a loop should terminate given it could contain a
   * redirection symbol.
   * 
   * @return parameter length if no redirection, index of redirection otherwise
   */
  protected int redirectLoopEnd() {

    int retIndex = -1;

    // if no parameters, return 0
    if (this.getNumParameters() == 0) {
      return 0;
    }

    // If redirection symbol found, return its index
    for (int i = 0; i < this.getNumParameters(); i++) {
      if (this.checkSymbolIndex(i, this.currentParameters[i]) != 0) {
        retIndex = i;
      } else {
        this.setSymbolIndex(retIndex);
      }
    }

    if (retIndex > 0) {
      return retIndex;
    }

    // If not found return number of parameters
    return this.getNumParameters();
  }

  /**
   * Method to check if output needs to be redirected to another file, otherwise, return null
   * 
   * @param writeString the string to write to redirect file if redirection required
   * @return String contents of file written to by redirection; null if redirection failure; empty
   *         string if no redirection
   */
  private String redirectHelp(String writeString) {

    // Declare index variable
    int index = 0;

    if (this.getNumParameters() == 0) {
      return "\n\n";
    }

    // Check each parameter
    for (String s : this.getCurrentParameters()) {

      // If parameter is a redirection symbol, prepare redirect
      int typeCheck = this.checkSymbolIndex(index, s);
      if (typeCheck != 0) {

        this.redirectLoopEnd();

        // Initialize redirection and file
        Redirection output = new Redirection(this.getCurrentParameters(), writeString,
            this.getSymbolIndex(), this.getCurrentFileSystem(), this.getShellCWD());
        File outfile;

        // Check if requires appending or overwriting
        if (typeCheck == 2) {
          outfile = output.redirectToOverwrite();
        } else {
          outfile = output.redirectToAppend();
        }

        // If error with redirection return null
        if (outfile == null) {
          return null;
        }

        // Return contents of redirect file
        return outfile.getContent();
      }

      // Increment index
      index++;
    }
    return "\n\n";
  }

  /**
   * Method to get redirection return for all commands that have output written to console. Returns
   * based on output of redirectHelp.
   * 
   * @param output the string to write to redirect file if redirection required
   * @return String contents of file written to by redirection; null if redirection failure; empty
   *         string if no redirection
   */
  protected String redirect(String output) {

    // Get string from redirect help
    String retString = this.redirectHelp(output);

    // If it's null return null
    if (retString == null) {
      return null;
    }

    // If string not empty, print original string and return
    if (retString.equals("\n\n")) {
      System.out.println(output);
      return output;
    }

    // Return redirection help value
    return retString;
  }

  /**
   * Prints the error message when a command fails to execute
   * 
   * @param error the error to be printed
   * @return null on all calls
   */
  protected String printErrorMessage(String error) {

    System.out.println(error);
    return null;

  }

  /**
   * @return the symbolIndex
   */
  public int getSymbolIndex() {
    return symbolIndex;
  }

  /**
   * @param symbolIndex the symbolIndex to set
   */
  public void setSymbolIndex(int symbolIndex) {
    this.symbolIndex = symbolIndex;
  }

  /**
   * @return the symbol
   */
  public String getSymbol() {
    return symbol;
  }

  /**
   * @param symbol the symbol to set
   */
  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

}
