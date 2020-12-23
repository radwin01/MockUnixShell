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
package filesystem;

/**
 * Represents the current working directory of the JShell.
 * 
 * @version 1.0
 */

public class CurrentWorkingDirectory implements CurrentWorkingDirectoryI {

  // Stores the current working directory
  private Directory curWorkingDir;

  /**
   * Creates an instance of a CurrentWorkingDirectory object. Sets the initial current working
   * directory.
   * 
   * @param initialDir the starting directory
   */
  public CurrentWorkingDirectory(Directory initialDir) {
    this.curWorkingDir = initialDir;
  }

  /**
   * Sets the current working directory to dir.
   * 
   * @param the directory to be set as the new current working directory
   */
  public void setCWD(Directory dir) {
    this.curWorkingDir = dir;
  }

  /**
   * Returns the current working directory.
   * 
   * @return the current working directory
   */
  public Directory getCWD() {
    return this.curWorkingDir;
  }

  /**
   * Sets the current working directory to a sub-directory, 'newDir'.
   * 
   * @param the name of the sub-directory
   */
  public void setToSubDirectoryByString(String newDir) {

    // Finding the index of newDir.
    int index = this.curWorkingDir.findInSubDirectories(newDir);

    // Checking if an index was found. -1 indicates newDir does not exist.
    if (index != -1) {
      this.curWorkingDir = this.curWorkingDir.getSubDirectoryByIndex(index);
    }
  }

  /**
   * Returns the full path of the current working directory as a String.
   * 
   * @return the full path of the current working directory
   */
  public String getCWDString() {
    return this.curWorkingDir.getFullPathString();
  }
}
