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

import command.Validation;

/**
 * Represents file redirection.
 * 
 * @version 1.0
 */

public class Redirection {

  // Parameters from initial command call
  String[] comParams;

  // String to be redirected
  String writeString;

  // Index at which redirection symbol occurs
  int symbolIndex;

  // Current directory of function call
  Directory curDir;

  // Stores the current working directory.
  CurrentWorkingDirectoryI cwd;
  private FileSystemI fs;

  /**
   * Constructor that initializes instance variables as required above.
   * 
   * @param comParams the parameters from initial command call
   * @param writeString the string to be redirected
   * @param symbolIndex the index at which redirection symbol occurs
   * @param fs the current file system
   * @param cwd the current working directory of function call
   */
  public Redirection(String[] comParams, String writeString, int symbolIndex, FileSystemI fs,
      CurrentWorkingDirectoryI cwd) {

    // Set instance variables from constructor parameters
    this.comParams = comParams;
    this.writeString = writeString;
    this.symbolIndex = symbolIndex;
    this.curDir = cwd.getCWD();
    this.cwd = cwd;
    this.fs = fs;
  }

  /**
   * Gets symbolIndex from instance variables.
   * 
   * @return the index at which ">>" or ">" occurs
   */
  private int getSymbolIndex() {
    return this.symbolIndex;
  }

  /**
   * Sets current directory for the redirection
   * 
   * @param newDir the new directory for the command
   */
  private void setCurDir(Directory newDir) {
    this.curDir = newDir;
  }

  /**
   * Gets current directory for the command.
   * 
   * @return the directory for the command
   */
  private Directory getCurDir() {
    return this.curDir;
  }

  /**
   * Return the private variable comParams value.
   * 
   * @return the comParams variable of the class
   */
  private String[] getComParams() {
    return this.comParams;
  }

  /**
   * Method to determine if the name for the file as a result of redirection already exists as the
   * name for a directory.
   * 
   * @param fileName the name to look for in current directory
   * @return true if filename already exists; false otherwise
   */
  private boolean duplicateName(String fileName) {

    // If directory exists with name return error and true
    for (Directory d : this.getCurDir().getDirArray()) {
      if (d.getName().equals(fileName)) {
        System.out.println("Directory with this name already exists");
        return true;
      }
    }

    // Return false otherwise
    return false;
  }

  /**
   * Method to determine the file requested by the command to write/append to. If it does not exist
   * already; create it.
   * 
   * @return the file to be written/appended to
   */
  private File getOutfile() {

    // Ensure file is exactly one word in length; return error otherwise
    if ((this.getComParams().length - this.getSymbolIndex()) != 2) {
      System.out.println("Filename cannot contain whitespace");
      return null;
    }

    // If root is given return appropriate error
    if (this.getComParams()[this.getSymbolIndex() + 1].equals("/")) {
      System.out.println("Root is not a file");
      return null;
    }

    // Create strings/string lists to store output file name variables
    String pathString = this.getComParams()[this.getSymbolIndex() + 1];
    String[] pathList = pathString.split("/");
    String outfileString = pathList[pathList.length - 1];

    // If no path given, set output file name to the only word
    if (pathList.length == 1) {
      outfileString = pathString;

      // Otherwise, set end directory of path and get file name
    } else {
      this.setCurDir(
          FilePath.getEndDirectoryfromPath(pathList, this.cwd, this.getCurrentFileSystem()));

      // Return error if path does not exist
      if (this.getCurDir() == null) {
        return null;
      }
    }

    // Ensure file name is valid; return error otherwise
    if (!(Validation.containsValidCharacters(outfileString)) || 
        this.duplicateName(outfileString)) {
      return null;
    }

    // Check if the file exists already, if so, return it
    for (File f : this.getCurDir().getFileArray()) {
      if (f.getName().equals(outfileString)) {
        return f;
      }
    }

    // If the file does not exist, create it in directory and return it
    File newFile = new File(outfileString, "", this.getCurDir());
    this.getCurDir().addFile(newFile);
    return newFile;
  }

  /**
   * Method to redirect command call into an outgoing file in which it appends the contents to the
   * end of the file if it already exists or creates it if it does not.
   * 
   * @return the file created or appended to as required by redirection
   */
  public File redirectToAppend() {

    // Get the output file
    File outfile = this.getOutfile();

    // If output file exists append the string content
    if (outfile != null) {
      outfile.addContent(writeString);
    }
    // Return the output file
    return outfile;
  }

  /**
   * Method to redirect command call into an outgoing file in which it overwrites the contents to
   * the file if it already exists or creates it if it does not.
   * 
   * @return the file created or overwritten as required by redirection
   */
  public File redirectToOverwrite() {

    // Get the output file
    File outfile = this.getOutfile();

    // If output file exists overwrite the string content
    if (outfile != null) {
      outfile.resetContent(writeString);
    }

    // Return the output file
    return outfile;
  }

  /**
   * @return the file system
   */
  public FileSystemI getCurrentFileSystem() {
    return fs;
  }

}
