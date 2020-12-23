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
package test;

import filesystem.Directory;
import filesystem.FileSystemI;

public class MockFileSystem implements FileSystemI {

  private int checkFilePathReturn;
  private int checkDirPathReturn;
  private Directory rootDir;


  @Override
  public int checkFilePath(Directory currentDir, String path) {
    return this.checkFilePathReturn;
  }

  @Override
  public Directory getRootDir() {
    return this.rootDir;
  }

  public void setRootDir(Directory rootDir) {
    this.rootDir = rootDir;
  }

  public int checkDirPath(Directory currentDir, String path) {
    return this.checkDirPathReturn;
  }

  /**
   * @param checkFilePathReturn the checkFilePathReturn to set
   */
  public void setCheckFilePathReturn(int checkFilePathReturn) {
    this.checkFilePathReturn = checkFilePathReturn;
  }

  /**
   * @param checkDirPathReturn the checkDirPathReturn to set
   */
  public void setCheckDirPathReturn(int checkDirPathReturn) {
    this.checkDirPathReturn = checkDirPathReturn;
  }

}
