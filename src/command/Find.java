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
import filesystem.FilePath;
import filesystem.FileSystemI;

/**
 * Represents a Find command.
 * 
 * @version 1.0
 */

public class Find extends Command {

  /**
   * Creates an instance of a Find object. Sets the minimum number of parameters that the user can
   * enter.
   * 
   * @param fs the current FileSystem in use
   * @param cwd the current CWD in use
   */
  public Find(FileSystemI fs, CurrentWorkingDirectoryI cwd) {
    super(fs, cwd);

    this.setMinParameters(5);
  }

  /**
   * Returns a description of the find command.
   * 
   * @return the description of the command
   */
  public static String getDescriptionOfCommand() {
    return "Name of command: find\n\n"
        + "Functionality: Finds the directories or files with a specified name given a list\n"
        + "of paths (relative or full) to search in.\n\n"
        + "Command syntax: find path(s)' -type [f/d] -name nameToFind";
  }

  /**
   * Searches for files or directories in a list of directories. The name to search for and type
   * (file/directory) is specified by the user. Prints the full path of the item in the files or
   * directories if it has the same name as the specified name. If it cannot be found, it prints an
   * error message.
   * 
   * @return a message indicating the search was successful, null otherwise
   */
  public String execute() {
    String output = "";

    // Checking if the user entered valid parameters.
    if (validParameters()) {

      // Storing the paths to search for as a list of directories and names.
      java.util.ArrayList<Directory> pathsToSearch = getPaths();
      java.util.ArrayList<String> pathStringsToSearch = getPathStrings();

      // Storing the name to be searched for.
      String name = extractName();

      // Checking if the name is validly specified (in quotations).
      if (name != null) {

        // Checking if the item to be searched for is a file.
        if (super.getCurrentParameters()[super.redirectLoopEnd() - 3].equals("f")) {

          for (int i = 0; i < pathsToSearch.size(); i++) {
            if (pathsToSearch.get(i) != null) {
              output += searchForFile(pathsToSearch.get(i), name, "");
            } else {
              output += "Invalid path: " + pathStringsToSearch.get(i);
            }
          }

          // The item to be searched for is a directory.
        } else {

          for (int i = 0; i < pathsToSearch.size(); i++) {
            if (pathsToSearch.get(i) != null) {
              output += searchForDirectory(pathsToSearch.get(i), name, "");
            } else {
              output += "Invalid path: " + pathStringsToSearch.get(i);
            }
          }
        }

        // Handle case when output is empty
        if (output.isEmpty())
          output += emptyOutputMessage(name);

        return super.redirect(output);

      }

      System.out.println("The name of the file or directory specified is not correctly quoted.");
    } else {
      System.out.println("Invalid parameters. Format is: "
          + "'path(s)' -type [f/d] -name 'expression'");
    }
    return null;
  }

  /**
   * Creates an output message when absolutely no files/directories are found with name, 'name'.
   * 
   * @param name the name of the file or directory being searched
   * @return the output message when absolutely no files/directories are found with name, 'name'
   */
  private String emptyOutputMessage(String name) {
    if (super.getCurrentParameters()[super.redirectLoopEnd() - 3].equals("f")) {
      return "No files found with name: " + name;
    } else {
      return "No directories found with name: " + name;
    }
  }

  /**
   * Extracts the contents of a quoted String.
   * 
   * @return the contents of the String excluding quotes if validly quoted, null otherwise
   */
  private String extractName() {
    String name = super.getCurrentParameters()[super.redirectLoopEnd() - 1];

    if (Validation.countQuoteOccurences(name) != 2 || name.charAt(0) != '"'
        || name.charAt(name.length() - 1) != '"') {
      return null;
    }

    return name.substring(1, name.length() - 1);
  }

  /**
   * Checks if the user inputed parameters are valid. Input contains valid parameters if the last 4
   * parameters are of the format: -type [f/d] -name "expression"
   * 
   * @return true if valid, false otherwise
   */
  private boolean validParameters() {
    int loopEnd = super.redirectLoopEnd();
    if (super.getCurrentParameters()[loopEnd - 2].equals("-name")) {
      if (super.getCurrentParameters()[loopEnd - 4].equals("-type")) {
        if (super.getCurrentParameters()[loopEnd - 3].equals("f")
            || super.getCurrentParameters()[loopEnd - 3].equals("d")) {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Takes the user input and extracts just the paths as directories into an ArrayList.
   * 
   * @return an ArrayList of the paths as directories inputed by the user
   */
  private java.util.ArrayList<Directory> getPaths() {
    java.util.ArrayList<Directory> pathsToSearch = new java.util.ArrayList<Directory>();

    for (int i = 0; i < super.redirectLoopEnd() - 4; i++) {
      pathsToSearch.add(FilePath.convertAnyPathToDirectory(super.getCurrentParameters()[i],
          super.getShellCWD(), this.getCurrentFileSystem()));
    }

    return pathsToSearch;
  }

  /**
   * Takes the user input and extracts just the paths into an ArrayList.
   * 
   * @return an ArrayList of the paths inputed by the user
   */
  private java.util.ArrayList<String> getPathStrings() {
    java.util.ArrayList<String> pathsToSearch = new java.util.ArrayList<String>();

    for (int i = 0; i < super.redirectLoopEnd() - 4; i++) {
      pathsToSearch.add(super.getCurrentParameters()[i]);
    }

    return pathsToSearch;
  }

  /**
   * Searches for a file with the name, fileName, in dirToSearch. Prints the full path of the file
   * if such a file is found, an error message otherwise.
   * 
   * @param dirToSearch the directory to search for fileName in
   * @param fileName the file name to search for in dirToSearch
   */
  private String searchForFile(Directory dirToSearch, String fileName, String output) {
    if (dirToSearch.findInFiles(fileName) != null) {
      output += "Found: " + dirToSearch.getFullPathString() + fileName + "\n";
    }

    if (dirToSearch.getDirArray().size() > 0) {
      for (Directory dir : dirToSearch.getDirArray()) {
        output += searchForFile(dir, fileName, "");
      }
    }

    return output;
  }

  /**
   * Searches for a directory with the name, dirName, in dirToSearch. Prints the full path of the
   * directory if such a directory is found, an error message otherwise.
   * 
   * @param dirToSearch the directory to search for dirName in
   * @param dirName the directory name to search for in dirToSearch
   */
  private String searchForDirectory(Directory dirToSearch, String dirName, String output) {
    if (dirToSearch.findInSubDirectories(dirName) != -1) {
      output += "Found: " + dirToSearch.getFullPathString() + dirName + "\n";
    }

    if (dirToSearch.getDirArray().size() > 0) {
      for (Directory dir : dirToSearch.getDirArray()) {
        output += searchForDirectory(dir, dirName, "");
      }
    }

    return output;
  }
}
