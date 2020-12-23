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

public interface FileSystemI {

  /**
   * Checks if the most top directory in the given path is a sub directory of the currentDir. If
   * the sub directory is found, recursively sends the rest of the path to be searched until a base
   * case has been reached. Once at the final directory, checks if the desired file name exists.
   * 
   * @param currentDir Directory object containing the current directory and its sub directories.
   * @param path String which contains the desired path to a File path that must be checked.
   * @return 0 if the path and File exist,1 if the path exists but the File does not, and 2 if the
   *         path does not exist.
   */
  public int checkFilePath(Directory currentDir, String path);

  /**
   * Returns the root directory of the file system.
   * 
   * @return the head of the file system
   */
  public Directory getRootDir();

  /**
   * Sets the root directory to the rootDir parameter.
   * 
   * @param rootDir Directory which is the head of the file system
   */
  public void setRootDir(Directory rootDir);

  /**
   * Checks if the most top directory in the given path is a sub directory of the currentDir. If 
   * the sub directory is found, recursively sends the rest of the path to be searched until a base
   * case has been reached.
   * 
   * @param currentDir Directory object containing the current directory and its sub directories.
   * @param path String which contains the desired path to a directory that must be checked.
   * @return 0 if the path and directory exist, 1 if the path exists but the final directory does
   *         not, and 2 if the path does not exist.
   */
  public int checkDirPath(Directory currentDir, String path);

}
