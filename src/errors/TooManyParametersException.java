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
package errors;

/**
 * Represents a TooManyParametersException error
 * 
 * @version 1.0
 */
@SuppressWarnings("serial")
public class TooManyParametersException extends Exception {

  /**
   * Constructor used to create the TooManyParametersException, invoked when the user passes too
   * many arguments after a known command to the terminal.
   * 
   * @param errorMessage the error message to be printed to the terminal (stdout)
   */
  public TooManyParametersException(String errorMessage) {
    super(errorMessage);
  }

  /**
   * Returns the message associated with the exception
   * 
   * @return the error message parameter when the exception was created
   */
  public String toString() {
    return super.getMessage();
  }
}
