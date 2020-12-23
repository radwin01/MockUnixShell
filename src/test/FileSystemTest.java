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

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import filesystem.Directory;
import filesystem.File;
import filesystem.FileSystem;

public class FileSystemTest {

  private FileSystem fs;

  @Before
  public void setUp() {

    fs = FileSystem.createInstanceOfFileSystem();

    Directory root = new Directory("", null);

    Directory A = new Directory("A", root);
    root.addDirectory(A);

    File fileA = new File("fileA", "", root);
    root.addFile(fileA);

    Directory B = new Directory("B", root);
    root.addDirectory(B);

    Directory A1 = new Directory("A1", A);
    A.addDirectory(A1);

    File fileC = new File("fileC", "", A);
    A.addFile(fileC);

    fs.setRootDir(root);

  }

  @Test
  public void testStartingAtRootDirectoryExists() {

    int result = fs.checkDirPath(fs.getRootDir(), "A");

    assertEquals(0, result);

  }

  @Test
  public void testStartingAtRootEntirePathDoesNotExist() {

    int result = fs.checkDirPath(fs.getRootDir(), "X/Y");

    assertEquals(2, result);

  }

  @Test
  public void testStartingAtRootDirectoryFinalDirecDoesNotExist() {

    int result = fs.checkDirPath(fs.getRootDir(), "A/Y");

    assertEquals(1, result);

  }

  @Test
  public void testStartingAtRootFileExists() {

    int result = fs.checkFilePath(fs.getRootDir(), "fileA");

    assertEquals(0, result);

  }

  @Test
  public void testStartingAtRootFinalFileDoesNotExist() {

    int result = fs.checkFilePath(fs.getRootDir(), "B/fileA");

    assertEquals(1, result);

  }

  @Test
  public void testStartingAtRootFullPathDoesNotExist() {

    int result = fs.checkFilePath(fs.getRootDir(), "X/fileA");

    assertEquals(2, result);

  }


  @Test
  public void testStartingAtRootDirectoryDoesNotExistAndFileSameNameExists() {

    int result = fs.checkDirPath(fs.getRootDir(), "A/fileC");

    assertEquals(1, result);

  }

  @Test
  public void testStartingAtRootFileDoesNotExistAndDirectorySameNameExists() {

    int result = fs.checkFilePath(fs.getRootDir(), "A/A1");

    assertEquals(1, result);

  }



}
