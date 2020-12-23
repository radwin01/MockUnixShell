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
 * Represents a file of the file system.
 * 
 * @version 1.0
 */

public class File {

  // Stores the name of the file.
  private String name;

  // Stores the contents of the file.
  private String content;

  private Directory parentDirectory;

  /**
   * Creates an instance of a File object. Sets the name and contents of the file to 'name' and
   * 'content' respectively.
   *
   * @param name the name of the file
   * @param content the contents of the file
   */
  public File(String name, String content, Directory parentDirectory) {
    this.name = name;
    this.content = content;
    this.parentDirectory = parentDirectory;
  }

  /**
   * Sets the name of the file.
   * 
   * @param setName the name to be set as the new name of the file
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the name of the file.
   * 
   * @return the name of the file
   */
  public String getName() {
    return this.name;
  }

  /**
   * Resets the contents of the file by overwriting it with 'newContent'.
   * 
   * @param newContent the new contents of the file
   */
  public void resetContent(String newContent) {
    this.content = newContent;
  }

  /**
   * Appends 'newContent' to the end of the file.
   * 
   * @param newContent the contents to be added to the end of the file
   */
  public void addContent(String newContent) {
    if (this.content.isEmpty()) {
      this.content = newContent;
      return;
    }
    this.content = this.content + "\n" + newContent;
  }

  /**
   * Returns the contents of the file.
   * 
   * @return the contents of the file
   */
  public String getContent() {
    return this.content;
  }

  /**
   * Sets the parent directory of the file to newParentDirectory
   * 
   * @param newParentDirectory the directory to be set as the parent directory of the file
   */
  public void setParentDirectory(Directory newParentDirectory) {
    this.parentDirectory = newParentDirectory;
  }

  /**
   * Returns the parent directory of the file.
   * 
   * @return the parent directory of the file
   */
  public Directory getParentDirectory() {
    return this.parentDirectory;
  }
  
  /*
   * IMPORTANT
   * The methods in this class are all getters and setters, with the exception of addContent() and 
   * resetContent(). However, those two methods are very simple and we felt that it was necessary
   * to do JUnit testing for File. File was tested on console, and is working as expected.
   */
}
