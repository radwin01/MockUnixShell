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

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import command.Cd;
import filesystem.CurrentWorkingDirectoryI;
import filesystem.Directory;
import filesystem.FileSystemI;

public class CdTest {

  private Cd cdCommand;
  private FileSystemI fs;
  private CurrentWorkingDirectoryI cwd;

  @Before
  public void setUp() {
    this.fs = new MockFileSystem();
    this.cwd = new MockCurrentWorkingDirectory();
  }

  @Test
  public void testCdIntoRootFromRoot() {
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");

    this.cdCommand = new Cd(this.fs, this.cwd);
    this.cdCommand.setCurrentParameters(1, new String[] {"/"});
    String output = this.cdCommand.execute();

    assertEquals("/", output);
  }

  @Test
  public void testCdWithFullPath() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("one", curDir);
    curDir.addDirectory(dirOne);

    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/one/");
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);

    this.cdCommand = new Cd(this.fs, this.cwd);
    this.cdCommand.setCurrentParameters(1, new String[] {"/one"});
    String output = this.cdCommand.execute();

    assertEquals("/one/", output);
  }

  @Test
  public void testCdWithRelativePath() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("one", curDir);
    curDir.addDirectory(dirOne);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/one/");
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);

    this.cdCommand = new Cd(this.fs, this.cwd);
    this.cdCommand.setCurrentParameters(1, new String[] {"one"});
    String output = this.cdCommand.execute();

    assertEquals("/one/", output);
  }

  @Test
  public void testCdIntoNonExistentDirectory() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("real", curDir);
    curDir.addDirectory(dirOne);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);

    this.cdCommand = new Cd(this.fs, this.cwd);
    this.cdCommand.setCurrentParameters(1, new String[] {"notReal"});
    String output = this.cdCommand.execute();

    assertEquals(null, output);
  }

  @Test
  public void testCdBackIntoParentDirectories() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("one", curDir);
    curDir.addDirectory(dirOne);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);

    this.cdCommand = new Cd(this.fs, this.cwd);
    this.cdCommand.setCurrentParameters(1, new String[] {"../../../"});
    String output = this.cdCommand.execute();

    assertEquals("/", output);
  }

  @Test
  public void testCdIntoMultipleDepths() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("folder1", curDir);
    curDir.addDirectory(dirOne);
    Directory dirTwo = new Directory("folder2", dirOne);
    dirOne.addDirectory(dirTwo);

    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/folder1/folder2/");
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);

    this.cdCommand = new Cd(this.fs, this.cwd);
    this.cdCommand.setCurrentParameters(1, new String[] {"/folder1/folder2"});
    String output = this.cdCommand.execute();

    assertEquals("/folder1/folder2/", output);
  }

  @Test
  public void testCdToParentDirectory() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("folder1", curDir);
    curDir.addDirectory(dirOne);
    Directory dirTwo = new Directory("folder2", dirOne);
    dirOne.addDirectory(dirTwo);

    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/folder1/");
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(dirTwo);

    this.cdCommand = new Cd(this.fs, this.cwd);
    this.cdCommand.setCurrentParameters(1, new String[] {".."});
    String output = this.cdCommand.execute();

    assertEquals("/folder1/", output);
  }

}
