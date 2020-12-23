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
import filesystem.FileSystemI;

/**
 * Represents an Echo command.
 * 
 * @version 2.0
 */

public class Echo extends Command {

  // Stores command line written at command call
  private String commandLine;

  /**
   * Echo constructor that initializes the current working directory from point of command call and
   * sets minimum parameters since it requires a string.
   * 
   * @param cwd stores current working directory from command call
   * @param fs stores current FileSystem instance with design
   */
  public Echo(FileSystemI fs, CurrentWorkingDirectoryI cwd) {

    super(fs, cwd);

    // Set max parameters
    this.setMinParameters(1);
  }

  /**
   * Method that returns the description of the echo command.
   * 
   * @return the description of the command
   */
  public static String getDescriptionOfCommand() {
    return "Name of command: echo\n\n"
        + "Functionality: Writes string arguments (MUST be encased in quotation marks)\n"
        + "to standard output if no destination file is provided. If a '>', followed by\n"
        + "a destination file path is included in the arguments, writes string arguments to\n"
        + "destination file (overwrites previous contents of destination file). If a '>>',\n"
        + "followed by a destination file path is included in the arguments, appends string\n"
        + "arguments to destination file.\n"
        + "Note: If the destination file path does not exist, echo will create it first.\n\n"
        + "Command syntax: echo stringArgs [> OR >> destinationFilePath]";
  }

  /**
   * Sets command line from command call.
   * 
   * @param commandLine the command line from command call
   */
  public void setLine(String line) {
    this.commandLine = line;
  }

  /**
   * Gets command line from command call.
   * 
   * @return the command line from command call
   */
  public String getLine() {
    return this.commandLine;
  }

  /**
   * Method to determine if quotation marks are placed in string in a valid way for command call
   * according to Assignment2A documentation and FAQ.
   * 
   * @return the number to signify outcome: 0 if invalid; 1 if quotations signify printing phrase;
   *         2 if quotations signify writing phrase to another file; 3 if quotations signify
   *         appending phrase to another file.
   */
  private int checkQuotations() {

    // Initialize count variables
    int quotationMarks = 0;
    int symbolIndex = 0;

    // Iterate through each word in given parameters
    for (String s : this.getCurrentParameters()) {

      // Check if first word in string starts with quotation marks; return error if it does not
      if (quotationMarks == 0) {
        if (!((s.substring(0, 1)).equals("\""))) {
          super.printErrorMessage("String does not start with quotation mark");
          return 0;
        }
      }

      // If two quotation marks found, investigate following word
      if (quotationMarks == 2) {
        return this.checkSymbolIndex(symbolIndex, s, "Part of string outside of quotation marks");

        // If the current word contains a quotation mark, add it to total amount
      } else if (s.contains("\"")) {
        quotationMarks += Validation.countQuoteOccurences(s);

        // If quotation marks brings value to 2, ensure that word ends in quotation marks;
        // return error otherwise
        if (quotationMarks == 2) {
          if (!((s.substring(s.length() - 1)).equals("\""))) {
            super.printErrorMessage("String does not end with quotation marks");
            return 0;
          }
        } else if (quotationMarks > 2) {

          // If quotation marks exceed 2; return error for having too many
          super.printErrorMessage("Too many quotation marks in string");
          return 0;
        }
      }

      // Increment index for finding symbol
      symbolIndex++;
    }

    // If quotation marks reach correct value and no append/write character, return print code
    if (quotationMarks == 2)
      return 1;

    // If not 2 quotation marks; return error
    super.printErrorMessage("String not inside quotation marks");
    return 0;
  }

  /**
   * When two quotation marks are reached, check the area around the next parameter to see what
   * type of redirection will occur
   * 
   * @param symbolIndex the index at which the redirection symbol occurs
   * @param potentialSymbol the string to be checked for redirection symbol
   * @param errorMessage the message to be displayed upon error
   * @return the success or error code in relation to check quotations method
   */
  protected int checkSymbolIndex(int symbolIndex, String potentialSymbol, String errorMessage) {

    // Call parent method
    int checkVal = super.checkSymbolIndex(symbolIndex, potentialSymbol);

    // If a word is found outside quotation marks return error
    if (checkVal == 0) {
      super.printErrorMessage(errorMessage);
    }
    return checkVal;

  }

  /**
   * Method to determine the string that is to be written to the output file from
   * currentParameters.
   * 
   * @return the string to be written as per command requirements
   */
  private String createWritableString() {

    // Keep track of beginning to end of string
    int start = this.getLine().indexOf("\"");
    int end = this.getLine().indexOf("\"", start + 1);

    // Return string between indexes
    return this.getLine().substring(start + 1, end);
  }

  /**
   * Action method that runs the main functionality of the command echo; there are several cases:
   * <p>
   * <ul>
   * <li>1. If just a string is provided, print the string to console.</li>
   * <li>2. If a string is provided followed by a ">" symbol and an output file/path: overwrite the
   * contents of the file with the new string. If the file does not exist; create it first.</li>
   * <li>3. If a string is provided followed by a ">>" symbol and an output file/path: append the
   * contents of the file with the new string. If the file does not exist; create it first.</li>
   * </ul>
   * 
   * @return the string that's written upon a successful call; null on a failed call
   */
  public String execute() {

    // Return error is echo has no parameters
    if (this.getCurrentParameters() == null) {
      return null;
    }

    // Declare int for type of echo command to initiate (print/write/append)
    int typeCheck = this.checkQuotations();

    // If quotation are placed successfully run function; return error otherwise
    if (typeCheck > 0) {

      // Get the string to be written
      String writeString = this.createWritableString();

      // return redirection results
      return super.redirect(writeString);
    }
    return null;
  }

}
