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
import command.Ls;
import filesystem.CurrentWorkingDirectoryI;
import filesystem.Directory;
import filesystem.FileSystemI;

public class LsTest {

  private Ls lsCommand;
  private FileSystemI fs;
  private CurrentWorkingDirectoryI cwd;

  @Before
  public void setUp() {
    this.fs = new MockFileSystem();
    this.cwd = new MockCurrentWorkingDirectory();
  }

  @Test
  /*
   * Ls test to see if ls works on an empty file system from root. Equivalent to starting up Jshell
   * and calling the following lines: ls
   */
  public void testWithEmptyFileSystemNoParams() {
    Directory curDir = new Directory("", null);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    this.lsCommand = new Ls(this.fs, this.cwd);
    this.lsCommand.setCurrentParameters(0, new String[] {""});
    String output = this.lsCommand.execute();
    assertEquals("\n", output);
  }

  @Test
  /*
   * Ls test to see if ls works on a file system from root with a single directory by calling with
   * no parameters. Equivalent to starting up Jshell and calling the following lines: mkdir
   * secondDir, ls
   */
  public void testWithOneDirectoryNoParams() {
    Directory curDir = new Directory("", null);
    Directory secDir = new Directory("secondDir", curDir);
    curDir.addDirectory(secDir);

    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    this.lsCommand = new Ls(this.fs, this.cwd);
    this.lsCommand.setCurrentParameters(0, new String[] {""});
    String output = this.lsCommand.execute();
    assertEquals("secondDir\n\n", output);
  }

  @Test
  /*
   * Ls test to see if ls works on a file system from root with a single directory by giving it the
   * root path. Equivalent to starting up Jshell and calling the following lines: mkdir secondDir,
   * ls /
   */
  public void testWithOneDirectoryFromRoot() {
    Directory curDir = new Directory("", null);
    Directory secDir = new Directory("secondDir", curDir);
    curDir.addDirectory(secDir);

    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    this.lsCommand = new Ls(this.fs, this.cwd);
    this.lsCommand.setCurrentParameters(1, new String[] {"/"});
    String output = this.lsCommand.execute();
    assertEquals("/:\nsecondDir\n\n", output);
  }

  @Test
  /*
   * Ls test to see if ls works on a file system from root with a few directories by giving it the
   * root path. Equivalent to starting up Jshell and calling the following lines: mkdir secondDir,
   * mkdir thirdDir, mkdir thirdDir/fourthDir, ls /
   */
  public void testWithSeveralDirectoriesFromRoot() {
    Directory curDir = new Directory("", null);
    Directory secDir = new Directory("secondDir", curDir);
    Directory thirdDir = new Directory("thirdDir", curDir);
    Directory fourthDir = new Directory("fourthDir", thirdDir);
    curDir.addDirectory(secDir);
    curDir.addDirectory(thirdDir);
    thirdDir.addDirectory(fourthDir);

    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    this.lsCommand = new Ls(this.fs, this.cwd);
    this.lsCommand.setCurrentParameters(1, new String[] {"/"});
    String output = this.lsCommand.execute();
    assertEquals("/:\nsecondDir\nthirdDir\n\n", output);
  }

  @Test
  /*
   * Ls test to see if ls works on a file system from root with a few directories by giving it the
   * the path of a directory inside of root. Equivalent to starting up Jshell and calling the
   * following lines: mkdir secondDir, mkdir thirdDir, mkdir thirdDir/fourthDir, ls /thirdDir
   */
  public void testWithSeveralDirectoriesFromInteriorDirectory() {
    Directory curDir = new Directory("", null);
    Directory secDir = new Directory("secondDir", curDir);
    Directory thirdDir = new Directory("thirdDir", curDir);
    Directory fourthDir = new Directory("fourthDir", thirdDir);
    curDir.addDirectory(secDir);
    curDir.addDirectory(thirdDir);
    thirdDir.addDirectory(fourthDir);

    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(thirdDir);
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    this.lsCommand = new Ls(this.fs, this.cwd);
    this.lsCommand.setCurrentParameters(1, new String[] {"/thirdDir"});
    String output = this.lsCommand.execute();
    assertEquals("thirdDir:\nfourthDir\n\n", output);
  }

  @Test
  /*
   * Ls test to see if ls recursively works on a file system from root with a few directories by
   * giving it the root path. Equivalent to starting up Jshell and calling the following lines:
   * mkdir secondDir, mkdir thirdDir, mkdir thirdDir/fourthDir, ls -R /
   */
  public void testRecursivelyWithFourDirectories() {
    Directory curDir = new Directory("", null);
    Directory secDir = new Directory("secondDir", curDir);
    Directory thirdDir = new Directory("thirdDir", curDir);
    curDir.addDirectory(secDir);
    curDir.addDirectory(thirdDir);
    Directory fourthDir = new Directory("fourthDir", thirdDir);
    thirdDir.addDirectory(fourthDir);

    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    this.lsCommand = new Ls(this.fs, this.cwd);
    this.lsCommand.setCurrentParameters(1, new String[] {"-R"});
    String output = this.lsCommand.execute();
    assertEquals(
        "/:\nsecondDir\nthirdDir\n\nsecondDir:\n\nthirdDir:\nfourthDir\n\nfourthDir:\n\n\n",
        output);
  }

  @Test
  /*
   * Ls test to see if ls recursively works on a file system from root with a few directories by
   * giving it an invalid path. Equivalent to starting up Jshell and calling the following lines:
   * mkdir secondDir, mkdir thirdDir, mkdir thirdDir/fourthDir, ls -R hello
   */
  public void testInvalidRecursivelyWithFourDirectories() {
    Directory curDir = new Directory("", null);
    Directory secDir = new Directory("secondDir", curDir);
    Directory thirdDir = new Directory("thirdDir", curDir);
    curDir.addDirectory(secDir);
    curDir.addDirectory(thirdDir);
    Directory fourthDir = new Directory("fourthDir", thirdDir);
    thirdDir.addDirectory(fourthDir);

    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    this.lsCommand = new Ls(this.fs, this.cwd);
    this.lsCommand.setCurrentParameters(2, new String[] {"-R", "hello"});
    String output = this.lsCommand.execute();
    assertEquals(null, output);
  }

  @Test
  /*
   * Ls test to see if ls works across 2 successful calls. Equivalent to starting up Jshell and
   * calling the following lines: mkdir secondDir, mkdir thirdDir, mkdir thirdDir/fourthDir, ls
   * secondDir thirdDir
   */
  public void testWithTwoValidDirectoriesGiven() {
    Directory curDir = new Directory("", null);
    Directory secDir = new Directory("secondDir", curDir);
    Directory thirdDir = new Directory("thirdDir", curDir);
    curDir.addDirectory(secDir);
    curDir.addDirectory(thirdDir);
    Directory fourthDir = new Directory("fourthDir", thirdDir);
    thirdDir.addDirectory(fourthDir);

    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    this.lsCommand = new Ls(this.fs, this.cwd);
    this.lsCommand.setCurrentParameters(2, new String[] {"secondDir", "thirdDir"});
    String output = this.lsCommand.execute();
    assertEquals("secondDir:\n\nthirdDir:\nfourthDir\n\n", output);
  }

  @Test
  /*
   * Ls test to see if ls works across 2 calls where 1 is unsuccessful. Equivalent to starting up
   * Jshell and calling the following lines: mkdir secondDir, mkdir thirdDir, mkdir
   * thirdDir/fourthDir, ls secondDir hi
   */
  public void testWithTwoDirectoriesOneInvalid() {
    Directory curDir = new Directory("", null);
    Directory secDir = new Directory("secondDir", curDir);
    Directory thirdDir = new Directory("thirdDir", curDir);
    curDir.addDirectory(secDir);
    curDir.addDirectory(thirdDir);
    Directory fourthDir = new Directory("fourthDir", thirdDir);
    thirdDir.addDirectory(fourthDir);

    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    this.lsCommand = new Ls(this.fs, this.cwd);
    this.lsCommand.setCurrentParameters(2, new String[] {"secondDir", "hi"});
    String output = this.lsCommand.execute();
    assertEquals(null, output);
  }

}
