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
import command.Pushd;
import filesystem.CurrentWorkingDirectoryI;
import filesystem.Directory;
import filesystem.DirectoryStack;
import filesystem.FileSystemI;

public class PushdTest {

  private Pushd pushdCommand;
  private FileSystemI fs;
  private CurrentWorkingDirectoryI cwd;
  private DirectoryStack dirStack;

  @Before
  public void setUp() {
    this.fs = new MockFileSystem();
    this.cwd = new MockCWDPopAndPush();
    this.dirStack = new DirectoryStack();
  }

  @Test
  public void testPushOntoEmptyStackWithValidPath() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("folder1", curDir);
    curDir.addDirectory(dirOne);

    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    this.cwd.setCWD(dirOne);

    this.pushdCommand = new Pushd(this.fs, this.cwd, dirStack);
    this.pushdCommand.setCurrentParameters(1, new String[] {"/"});
    this.pushdCommand.execute();

    boolean result = dirStack.getDirStack().size() == 1 && this.cwd.getCWD().equals(curDir);

    assertEquals(true, result);
  }

  @Test
  public void testPushOntoNonEmptyStackWithValidPath() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("folder1", curDir);
    curDir.addDirectory(dirOne);

    dirStack.getDirStack().add(dirOne);

    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    this.cwd.setCWD(curDir);

    this.pushdCommand = new Pushd(this.fs, this.cwd, dirStack);
    this.pushdCommand.setCurrentParameters(1, new String[] {"folder1"});
    this.pushdCommand.execute();

    boolean result = dirStack.getDirStack().size() == 2 && this.cwd.getCWD().equals(dirOne);

    assertEquals(true, result);
  }

  @Test
  public void testPushWithFullPath() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("folder1", curDir);
    curDir.addDirectory(dirOne);
    Directory dirTwo = new Directory("folder2", dirOne);
    dirOne.addDirectory(dirTwo);
    Directory dirThree = new Directory("folder3", curDir);
    curDir.addDirectory(dirThree);

    dirStack.getDirStack().add(dirThree);

    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    this.cwd.setCWD(dirOne);

    this.pushdCommand = new Pushd(this.fs, this.cwd, dirStack);
    this.pushdCommand.setCurrentParameters(1, new String[] {"/folder1/folder2"});
    this.pushdCommand.execute();

    boolean result = dirStack.getDirStack().size() == 2 && this.cwd.getCWD().equals(dirTwo);

    assertEquals(true, result);
  }

  @Test
  public void testPushWithRelativePath() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("folder1", curDir);
    curDir.addDirectory(dirOne);
    Directory dirTwo = new Directory("folder2", dirOne);
    dirOne.addDirectory(dirTwo);

    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    this.cwd.setCWD(dirOne);

    this.pushdCommand = new Pushd(this.fs, this.cwd, dirStack);
    this.pushdCommand.setCurrentParameters(1, new String[] {"folder2"});
    this.pushdCommand.execute();

    boolean result = dirStack.getDirStack().size() == 1 && this.cwd.getCWD().equals(dirTwo);

    assertEquals(true, result);
  }

  @Test
  public void testPushWithNonValidPath() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("folder1", curDir);
    curDir.addDirectory(dirOne);
    Directory dirTwo = new Directory("folder2", dirOne);
    dirOne.addDirectory(dirTwo);

    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    this.cwd.setCWD(dirOne);

    this.pushdCommand = new Pushd(this.fs, this.cwd, dirStack);
    this.pushdCommand.setCurrentParameters(1, new String[] {"folder1/folder2"});

    String output = this.pushdCommand.execute();

    boolean result = dirStack.getDirStack().size() == 0 && output == null;

    assertEquals(true, result);
  }
}
