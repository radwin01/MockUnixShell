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
import command.Find;
import filesystem.CurrentWorkingDirectoryI;
import filesystem.Directory;
import filesystem.File;
import filesystem.FileSystemI;

public class FindTest {

  private Find findCommand;
  private FileSystemI fs;
  private CurrentWorkingDirectoryI cwd;

  @Before
  public void setUp() {
    this.fs = new MockFileSystem();
    this.cwd = new MockCurrentWorkingDirectory();
  }

  @Test
  public void testFindToSearchForNonExistentDirectory() {
    Directory curDir = new Directory("", null);

    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);

    this.findCommand = new Find(this.fs, this.cwd);
    this.findCommand.setCurrentParameters(5, new String[] {"/", "-type", "d", "-name", "\"dne\""});
    String output = this.findCommand.execute();

    String correctOutput = "No directories found with name: dne";

    assertEquals(correctOutput, output);
  }

  @Test
  public void testFindToSearchForDirectoryInMoreThanOneLocation() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("folder1", curDir);
    curDir.addDirectory(dirOne);
    Directory dirTwo = new Directory("folder2", curDir);
    curDir.addDirectory(dirTwo);
    Directory dirThree = new Directory("folder3", dirTwo);
    dirTwo.addDirectory(dirThree);
    Directory toFind1 = new Directory("findMe", curDir);
    curDir.addDirectory(toFind1);
    Directory toFind2 = new Directory("findMe", dirOne);
    dirOne.addDirectory(toFind2);
    Directory toFind3 = new Directory("findMe", dirThree);
    dirThree.addDirectory(toFind3);


    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);

    this.findCommand = new Find(this.fs, this.cwd);
    this.findCommand.setCurrentParameters(5,
        new String[] {"/", "-type", "d", "-name", "\"findMe\""});

    String output = this.findCommand.execute();

    String correctOutput =
        "Found: /findMe\n" + "Found: /folder1/findMe\n" + "Found: /folder2/folder3/findMe\n";

    assertEquals(correctOutput, output);
  }

  @Test
  public void testFindToLookForDirectoryInMulitplePaths() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("folder1", curDir);
    curDir.addDirectory(dirOne);
    Directory dirTwo = new Directory("folder2", curDir);
    curDir.addDirectory(dirTwo);
    Directory dirThree = new Directory("folder3", dirTwo);
    dirTwo.addDirectory(dirThree);
    Directory toFind1 = new Directory("findMe", curDir);
    curDir.addDirectory(toFind1);
    Directory toFind2 = new Directory("findMe", dirOne);
    dirOne.addDirectory(toFind2);
    Directory toFind3 = new Directory("findMe", dirThree);
    dirThree.addDirectory(toFind3);
    Directory dirFour = new Directory("folder4", curDir);
    curDir.addDirectory(dirFour);


    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);

    this.findCommand = new Find(this.fs, this.cwd);
    this.findCommand.setCurrentParameters(7,
        new String[] {"folder1", "folder2", "folder4", "-type", "d", "-name", "\"findMe\""});

    String output = this.findCommand.execute();

    String correctOutput = "Found: /folder1/findMe\n" + "Found: /folder2/folder3/findMe\n";

    assertEquals(correctOutput, output);
  }

  @Test
  public void testFindToSearchForFileInMoreThanOneLocation() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("folder1", curDir);
    curDir.addDirectory(dirOne);
    Directory dirTwo = new Directory("folder2", curDir);
    curDir.addDirectory(dirTwo);
    Directory dirThree = new Directory("folder3", dirTwo);
    dirTwo.addDirectory(dirThree);
    File toFind1 = new File("findMe", null, curDir);
    curDir.addFile(toFind1);
    File toFind2 = new File("findMe", null, dirOne);
    dirOne.addFile(toFind2);
    File toFind3 = new File("findMe", null, dirThree);
    dirThree.addFile(toFind3);


    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);

    this.findCommand = new Find(this.fs, this.cwd);
    this.findCommand.setCurrentParameters(5,
        new String[] {"/", "-type", "f", "-name", "\"findMe\""});

    String output = this.findCommand.execute();

    String correctOutput =
        "Found: /findMe\n" + "Found: /folder1/findMe\n" + "Found: /folder2/folder3/findMe\n";

    assertEquals(correctOutput, output);
  }

  @Test
  public void testFindToSearchForFileInMultiplePaths() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("folder1", curDir);
    curDir.addDirectory(dirOne);
    Directory dirTwo = new Directory("folder2", curDir);
    curDir.addDirectory(dirTwo);
    Directory dirThree = new Directory("folder3", dirTwo);
    dirTwo.addDirectory(dirThree);
    File toFind1 = new File("findMe", null, curDir);
    curDir.addFile(toFind1);
    File toFind2 = new File("findMe", null, dirOne);
    dirOne.addFile(toFind2);
    File toFind3 = new File("findMe", null, dirThree);
    dirThree.addFile(toFind3);
    Directory dirFour = new Directory("folder4", curDir);
    curDir.addDirectory(dirFour);

    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);

    this.findCommand = new Find(this.fs, this.cwd);
    this.findCommand.setCurrentParameters(7,
        new String[] {"folder1", "folder2", "folder4", "-type", "f", "-name", "\"findMe\""});

    String output = this.findCommand.execute();

    String correctOutput = "Found: /folder1/findMe\n" + "Found: /folder2/folder3/findMe\n";

    assertEquals(correctOutput, output);
  }

  @Test
  public void testFindWithIncorrectParamters() {
    Directory curDir = new Directory("", null);

    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);

    this.findCommand = new Find(this.fs, this.cwd);
    this.findCommand.setCurrentParameters(5, new String[] {"/", "-type", "f", "-name", "findMe"});

    String output = this.findCommand.execute();

    assertEquals(null, output);
  }

  @Test
  public void testFindToSearchForDirectoryGivenValidAndNonValidPaths() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("folder1", curDir);
    curDir.addDirectory(dirOne);
    Directory dirTwo = new Directory("folder2", curDir);
    curDir.addDirectory(dirTwo);
    Directory toFind1 = new Directory("findMe", curDir);
    curDir.addDirectory(toFind1);
    Directory toFind2 = new Directory("findMe", dirOne);
    dirOne.addDirectory(toFind2);

    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);

    this.findCommand = new Find(this.fs, this.cwd);
    this.findCommand.setCurrentParameters(6,
        new String[] {"/", "/folder1/notHere", "-type", "d", "-name", "\"findMe\""});

    String output = this.findCommand.execute();

    String correctOutput =
        "Found: /findMe\n" + "Found: /folder1/findMe\n" + "Invalid path: /folder1/notHere";

    assertEquals(correctOutput, output);
  }

  @Test
  public void testFindToSearchForFileGivenValidAndNonValidPaths() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("folder1", curDir);
    curDir.addDirectory(dirOne);
    Directory dirTwo = new Directory("folder2", curDir);
    curDir.addDirectory(dirTwo);
    File toFind1 = new File("findMe", null, curDir);
    curDir.addFile(toFind1);
    File toFind2 = new File("findMe", null, dirOne);
    dirOne.addFile(toFind2);

    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);

    this.findCommand = new Find(this.fs, this.cwd);
    this.findCommand.setCurrentParameters(6,
        new String[] {"/", "/folder1/notHere", "-type", "f", "-name", "\"findMe\""});

    String output = this.findCommand.execute();

    String correctOutput =
        "Found: /findMe\n" + "Found: /folder1/findMe\n" + "Invalid path: /folder1/notHere";

    assertEquals(correctOutput, output);
  }

  @Test
  public void testFindToSearchForNonExistentFile() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("folder1", curDir);
    curDir.addDirectory(dirOne);
    Directory dirTwo = new Directory("folder2", curDir);
    curDir.addDirectory(dirTwo);
    File toFind1 = new File("findMe", null, curDir);
    curDir.addFile(toFind1);
    File toFind2 = new File("findMe", null, dirOne);
    dirOne.addFile(toFind2);

    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);

    this.findCommand = new Find(this.fs, this.cwd);
    this.findCommand.setCurrentParameters(5,
        new String[] {"/", "-type", "f", "-name", "\"notHere\""});

    String output = this.findCommand.execute();

    String correctOutput = "No files found with name: notHere";

    assertEquals(correctOutput, output);
  }
}
