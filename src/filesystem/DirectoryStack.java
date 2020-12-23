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
 * Represents the directory stack of the JShell.
 * 
 * @version 1.0
 */

public final class DirectoryStack {

  // Stores the directory stack.
  private ItemList<Directory> dirStack;

  /**
   * Creates an instance of a DirectoryStacks object. Initializes the ArrayList storing the
   * directories.
   */
  public DirectoryStack() {
    dirStack = new ItemList<Directory>();
  }

  /**
   * Returns the current directory stack in its ArrayList form.
   * 
   * @return the current directory stack
   */
  public ItemList<Directory> getDirStack() {
    return this.dirStack;
  }

  /**
   * Method that returns the current directory stack as a list of the full paths of all the
   * directories in the stack.
   * 
   * @return the list of the full paths of all directories in the directory stack
   */
  public String getDirStackAsString() {

    // Initializing the String to store the directory stack as a String.
    String dirStackAsString = "";

    /*
     * Traversing through the directory stack and adding the full path of each directory to
     * dirStackAsString.
     */
    for (int i = 0; i < this.dirStack.size(); i++) {
      dirStackAsString = dirStackAsString + this.dirStack.get(i).getFullPathString() + "\n";
    }

    return dirStackAsString;
  }

  /*
   * IMPORTANT All the methods in this class are getters and setters with the exception of
   * getDirStackAsString(). However, getDirStackAsString is a very straight forward method. It
   * collects the full paths of all the directories in the directory stack into one String. So, it
   * was necessary to do JUnit testing for DirectoryStack, just to test one simple method.
   * getDirStackAsString was tested on console, and it working as expected.
   */
}

