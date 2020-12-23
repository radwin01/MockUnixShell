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
import filesystem.FileSystemI;

/**
 * Represents a Curl command.
 * 
 * @version 1.0
 */
public class Curl extends Command {

  private java.net.URL oracle;
  private java.io.BufferedReader in;
  private Directory parentDirectory;

  /**
   * Constructor used to create an instance of a Curl object. Sets the number of minimum parameters
   * that the user can enter after the command.
   * 
   * @param cwd stores current working directory from command call
   * @param fs stores current FileSystem instance with design
   */
  public Curl(FileSystemI fs, CurrentWorkingDirectoryI cwd, Directory parentDirectory) {

    super(fs, cwd);

    // sets the minimum number of parameters to be accepted
    this.setMinParameters(1);

    // sets the minimum number of parameters to be accepted
    this.setMaxParameters(1);

    // sets the parent directory
    this.parentDirectory = parentDirectory;
  }

  /**
   * Returns a description of the curl command.
   * 
   * @return the description of the command
   */
  public static String getDescriptionOfCommand() {
    return "Name of command: curl\n\n"
        + "Functionality: Retrieves a text file (must have the extension \".txt\")\n"
        + "at a URL and adds it to the current working directory. If the user passes\n"
        + "a URL that does not contain a text file at that address, an invalid site,\n"
        + "or passes a nonexistent text file at the specified address, curl will print\n"
        + "an error message.\n\n" + "Command syntax: curl URL";
  }

  /**
   * Overwrites the existing file's contents with the new file's contents
   */
  private void replaceFile(String newFileName, String fileContents) {
    for (File f : this.getCurDir().getFileArray()) {
      if (f.getName().equals(newFileName))
        f.resetContent(fileContents);
    }
  }

  /**
   * Checks if newFileName exists as a file in the current directory's file array
   */
  private boolean checkIfUniqueFile(String newFileName) {
    for (File f : this.getCurDir().getFileArray()) {
      if (f.getName().equals(newFileName))
        return false;
    }
    return true;
  }

  /**
   * Adds a new file to the current directory
   */
  private void addNewFile(String newFileName, String fileContents, Directory curDir) {
    File file = new File(newFileName, fileContents, curDir);
    parentDirectory.addFile(file);

  }

  /**
   * Returns the new file name (removing the dot from the ".txt" extension)
   */
  private String getNewFileName(String fileName) {
    // TODO Auto-generated method stub
    return fileName.substring(0, fileName.length() - 4)
        + fileName.substring(fileName.length() - 3, fileName.length());
  }

  /**
   * Returns the file from the end of the URL
   */
  private String getEndFile() {
    return super.getCurrentParameters()[0]
        .split("/")[super.getCurrentParameters()[0].split("/").length - 1];
  }

  /**
   * Checks if the url is properly formatted (i.e starts with http://, etc..)
   */
  public static boolean isValidURL(String url) {
    // try going to the specified url
    try {
      new java.net.URL(url).toURI();
      return true;

      // we catch the case that the url cannot be found i.e does not actually exist
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Checks if the file passed is a valid text (.txt) file
   */
  private boolean isValidFile(String fileName) {
    if (fileName.length() < 5)
      return false;
    if (".txt".equals(fileName.substring(fileName.length() - 4)))
      return true;
    return false;
  }

  /**
   * Gets the current directory
   */
  private Directory getCurDir() {
    return this.parentDirectory;
  }

  /**
   * Allows the user to retrieve a text file (.txt) from a specified URL and add the file to the
   * current working directory. The user should enter a URL address that contains a text file. If
   * the user passes an invalid/non-existing website, a URL address that contains a file not ending
   * with .txt, or a passes an address that points to a nonexistent text file, curl will print an
   * error message and return null.
   * 
   * @return an empty string for successful execution or null upon failure
   */
  public String execute() {

    // get the URL provided from the arguments
    String website = super.getCurrentParameters()[0];
    try {

      // check if the website has the proper formatting
      if (!isValidURL(website)) {
        System.out.println(website + ": Invalid website!");
        return null;
      }
      String fileName = getEndFile();

      // check if the website contains a text file at the end of its address
      if (!isValidFile(fileName)) {
        System.out.println(website + ": Does not contain a text file "
            + "(URL does not end with .txt)");
        return null;
      }
      oracle = new java.net.URL(website);
      oracle.openConnection();
      in = new java.io.BufferedReader(new java.io.InputStreamReader(oracle.openStream()));
      String inputLine;
      String fileContents = "";

      // concatenate all of the text file's contents into fileContents
      while ((inputLine = in.readLine()) != null)
        fileContents += inputLine + "\n";
      in.close();
      String newFileName = getNewFileName(fileName);
      // create a new file name that has the name fileName with the text file's contents
      if (!checkIfUniqueFile(newFileName)) {
        replaceFile(newFileName, fileContents);
      } else
        addNewFile(newFileName, fileContents, this.getCurDir());

      /**
       * we catch the case that the user passes a text file at the end of the URL address, but it
       * is not legitimate (i.e does not actually exist on the internet with the provided domain)
       */
    } catch (Exception e) {
      System.out.println(website + ": The domain with the specified text file does not exist");
      return null;
    }
    return "";
  }
}
