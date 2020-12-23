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
 * Represents a directory in the file system.
 * 
 * @version 1.0
 */

public class Directory {

  // Stores the name of the directory.
  private String name;

  // Stores the parent directory of the directory.
  private Directory parentDir;

  // Stores the files inside the directory.
  private java.util.ArrayList<File> fileArray;

  // Stores the sub-directories of the directory.
  private java.util.ArrayList<Directory> dirArray;

  /**
   * Creates an instance of a Directory object. Sets the name and parent directory of the file to
   * 'name' and 'parentDir' respectively. It also initializes fileArray and dirArray.
   * 
   * @param name the name of the directory
   * @param parentDir the parent directory of the directory
   */
  public Directory(String name, Directory parentDir) {
    this.name = name;
    this.parentDir = parentDir;
    this.fileArray = new java.util.ArrayList<File>();
    this.dirArray = new java.util.ArrayList<Directory>();
  }

  /**
   * Searches for a file in fileArray with the name, fileName.
   * 
   * @param fileName the name of the file to search for
   * @return the file in fileArray with the name, fileName, if it exists, null otherwise
   */
  public File findInFiles(String fileName) {
    for (File curFile : fileArray) {
      if (curFile.getName().equals(fileName)) {
        return curFile;
      }
    }

    return null;
  }

  /**
   * Checks if a file or sub-directory in the directory has the name, nameToCheck.
   * 
   * @param nameToCheck the name to check for existent in the directory
   * @return true if a file or directory with the name, nameToCheck, exists, null otherwise
   */
  public boolean nameAlreadyExists(String nameToCheck) {
    for (File curFile : fileArray) {
      if (curFile.getName().equals(nameToCheck)) {
        return true;
      }
    }

    for (Directory curDirectory : dirArray) {
      if (curDirectory.getName().equals(nameToCheck)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Finds the file in fileArray with the name, fileName.
   * 
   * @param fileName the name of the file to search for
   * @return the file in fileArray with the name, fileName, if it exists, null otherwise
   */
  public File getFileFromDirectory(String fileName) {
    for (File curFile : fileArray) {
      if (curFile.getName().equals(fileName)) {
        return curFile;
      }
    }
    return null;
  }

  /**
   * Checks if potentialParentDir is one of the parent directories of the directory.
   * 
   * @param potentialParentDir the possible parent directory of the directory
   * @return true, if potentialParentDir is a parent directory of the directory, false otherwise
   */
  public boolean isOneOfTheParentDirectories(Directory potentialParentDir, FileSystemI fs) {
    Directory temp = this;

    while (!temp.equals(fs.getRootDir())) {
      if (temp.getParentDir().equals(potentialParentDir)) {
        return true;
      }

      temp = temp.getParentDir();
    }

    return false;
  }

  /**
   * Finds the sub-directory in the directory with the name, subDir.
   * 
   * @param subDir the name of the sub-directory to search for
   * @return the sub-directory with the name, subDir, if it exists, null otherwise
   */
  public Directory getSubDirectoryFromString(String subDir) {
    for (Directory potentialDir : this.dirArray) {
      if (potentialDir.getName().equals(subDir)) {
        return potentialDir;
      }
    }

    return null;
  }

  /**
   * Returns the full path of the directory as a String.
   * 
   * @return the full path of the directory
   */
  public String getFullPathString() {

    // Storing all the previous directories up to root including the current directory.
    java.util.ArrayList<String> parentDirs = new java.util.ArrayList<String>();

    // Storing the current directory.
    Directory curDir = this;

    // Looping through each successive parent directory until root is reached.
    while (!curDir.getName().equals("")) {
      parentDirs.add(curDir.getName());
      curDir = curDir.getParentDir();
    }

    // Adding the current directory to parentDirs.
    parentDirs.add(curDir.getName());

    // The full path of the directory as a String.
    String curWorkingDirString = "";

    // Looping through parentDirs in reverse and adding it ot curWorkingDirString.
    for (int i = parentDirs.size() - 1; i >= 0; i--) {
      curWorkingDirString = curWorkingDirString + parentDirs.get(i) + "/";
    }

    return curWorkingDirString;
  }

  // FILE METHODS
  /**
   * Adds a file to fileArray.
   * 
   * @param toAdd the file to be added
   */
  public void addFile(File toAdd) {
    fileArray.add(toAdd);
  }

  /**
   * Removes a file from fileArray. It will only remove it if the file exists.
   * 
   * @param toRemove the file to be removed
   */
  public void removeFile(String toRemove) {
    boolean found = false;
    int i = 0;

    while (this.fileArray.size() > i && !found) {
      if (this.fileArray.get(i).getName().equals(toRemove)) {
        this.fileArray.remove(i);
        found = true;
      }
      i++;
    }
  }

  /**
   * Prints all the files in the directory in a list.
   */
  public void printFiles() {
    int i = 0;
    while (this.fileArray.size() > i) {
      System.out.println(this.fileArray.get(i).getName());
    }
  }


  // DIRECTORY METHODS
  /**
   * Adds a sub-directory to directoryArray.
   * 
   * @param toAdd the sub-directory to be added
   */
  public void addDirectory(Directory toAdd) {

    if (Validation.containsValidCharacters(toAdd.getName())) {
      dirArray.add(toAdd);
    } else {
      System.out.println("Invalid directory name!");
    }

  }

  /**
   * Removes a sub-directory. It will only remove it if the directory exists.
   * 
   * @param toRemove the sub-directory to be removed
   */
  public void removeDirectory(String toRemove) {
    boolean found = false;
    int i = 0;

    while (this.dirArray.size() > i && !found) {
      if (this.dirArray.get(i).getName().equals(toRemove)) {
        this.dirArray.remove(i);
        found = true;
      }
      i++;
    }
  }

  /**
   * Returns the index of a sub-directory, given the name of the sub-directory.
   * 
   * @param check The sub-directory to be found.
   * 
   * @return the index of the sub-directory, -1 otherwise
   */
  public int findInSubDirectories(String check) {
    int i = 0;

    while (this.dirArray.size() > i) {
      if (this.dirArray.get(i).getName().equals(check)) {
        return i;
      }
      i++;
    }

    return -1;
  }

  /**
   * Returns a sub-directory given the correct index of the sub-directory.
   * 
   * @param index the correct index of a sub-directory
   * @return the index of the sub-directory, -1 otherwise
   */
  public Directory getSubDirectoryByIndex(int index) {
    return this.dirArray.get(index);
  }

  /**
   * Prints the sub-directories in a list.
   */
  public void printDirectories() {
    int i = 0;
    while (this.dirArray.size() > i) {
      System.out.println(this.dirArray.get(i).getName());
    }
  }

  /**
   * Prints the files and sub-directories in a list.
   */
  public void printFilesAndDirectories() {
    printDirectories();
    printFiles();
  }

  // GETTERS AND SETTERS
  /**
   * Sets the name of the directory to 'name'.
   * 
   * @param name the name to be set as the new name of the directory
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the name of the directory.
   * 
   * @return the name of the directory
   */
  public String getName() {
    return this.name;
  }

  /**
   * Sets the parent directory of the directory.
   * 
   * @param parentDir the parent directory to be set as the new parent directory of the directory
   */
  public void setParentDir(Directory parentDir) {
    this.parentDir = parentDir;
  }

  /**
   * Returns the parent directory of the directory.
   * 
   * @return the parent directory
   */
  public Directory getParentDir() {
    if (this.parentDir == null) {
      return this;
    }
    return this.parentDir;
  }

  /**
   * Returns the files of the directory as an ArrayList.
   * 
   * @return the files of the directory
   */
  public java.util.ArrayList<File> getFileArray() {
    return this.fileArray;
  }

  public void setFileArray(java.util.ArrayList<File> fileArray) {
    this.fileArray = fileArray;
  }

  /**
   * Returns the sub-directories of the directory as an ArrayList.
   * 
   * @return the sub-directories of the directory as an ArrayList
   */
  public java.util.ArrayList<Directory> getDirArray() {
    return this.dirArray;
  }

  public void setDirArray(java.util.ArrayList<Directory> dirArray) {
    this.dirArray = dirArray;
  }
}
