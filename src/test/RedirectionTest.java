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
import filesystem.CurrentWorkingDirectoryI;
import filesystem.Directory;
import filesystem.File;
import filesystem.FileSystemI;
import filesystem.Redirection;

public class RedirectionTest {

  private Redirection redirect;
  private FileSystemI fs;
  private CurrentWorkingDirectoryI cwd;

  @Before
  public void setUp() {
    this.fs = new MockFileSystem();
    this.cwd = new MockCurrentWorkingDirectory();
  }

  @Test
  public void testWithOverwriteSimpleStringNonExistantFile() {
    Directory curDir = new Directory("", null);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    String[] comParams = {"hi", ">", "out"};
    this.redirect = new Redirection(comParams, "hi", 1, this.fs, this.cwd);
    String output = redirect.redirectToOverwrite().getContent();
    assertEquals("hi", output);
  }

  @Test
  public void testWithOverwriteSimpleStringExistantFile() {
    Directory curDir = new Directory("", null);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    curDir.addFile(new File("out", "", curDir));
    String[] comParams = {"hi", ">", "out"};
    this.redirect = new Redirection(comParams, "hi", 1, this.fs, this.cwd);
    String output = redirect.redirectToOverwrite().getContent();
    assertEquals("hi", output);
  }

  @Test
  public void testWithOverwriteSimpleStringExistantFileWithContent() {
    Directory curDir = new Directory("", null);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    curDir.addFile(new File("out", "this data is overwritten", curDir));
    String[] comParams = {"hi", ">", "out"};
    this.redirect = new Redirection(comParams, "hi", 1, this.fs, this.cwd);
    String output = redirect.redirectToOverwrite().getContent();
    assertEquals("hi", output);
  }

  @Test
  public void testWithSeveralOverwritesOnMultiLineString() {
    Directory curDir = new Directory("", null);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    curDir.addFile(new File("out", "this data is overwritten", curDir));
    String[] comParams = {"this\nappears\nonce", ">", "out"};
    String output = "";
    for (int i = 0; i < 5; i++) {
      this.redirect = new Redirection(comParams, "this\nappears\nonce", 1, this.fs, this.cwd);
      output = redirect.redirectToOverwrite().getContent();
    }
    assertEquals("this\nappears\nonce", output);
  }

  @Test
  public void testWithAppendSimpleStringNonExistantFile() {
    Directory curDir = new Directory("", null);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    String[] comParams = {"hi", ">>", "out"};
    this.redirect = new Redirection(comParams, "hi", 1, this.fs, this.cwd);
    String output = redirect.redirectToAppend().getContent();
    assertEquals("hi", output);
  }

  @Test
  /*
   * Test redirection with a single >> sign to append to a file called out which exists
   */
  public void testWithAppendSimpleStringExistantFile() {
    Directory curDir = new Directory("", null);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    curDir.addFile(new File("out", "", curDir));
    String[] comParams = {"hi", ">>", "out"};
    this.redirect = new Redirection(comParams, "hi", 1, this.fs, this.cwd);
    String output = redirect.redirectToAppend().getContent();
    assertEquals("hi", output);
  }

  @Test
  public void testWithAppendSimpleStringExistantFileWithContent() {
    Directory curDir = new Directory("", null);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    curDir.addFile(new File("out", "this data stays", curDir));
    String[] comParams = {"hi", ">>", "out"};
    this.redirect = new Redirection(comParams, "hi", 1, this.fs, this.cwd);
    String output = redirect.redirectToAppend().getContent();
    assertEquals("this data stays\nhi", output);
  }

  @Test
  public void testWithSeveralAppendsOnMultiLineString() {
    Directory curDir = new Directory("", null);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    curDir.addFile(new File("out", "", curDir));
    String[] comParams = {"this\nappears\n2times\n", ">>", "out"};
    String output = "";
    for (int i = 0; i < 2; i++) {
      this.redirect = new Redirection(comParams, "this\nappears\n2times\n", 1, this.fs, this.cwd);
      output = redirect.redirectToAppend().getContent();
    }
    assertEquals("this\nappears\n2times\n\nthis\nappears\n2times\n", output);
  }

  @Test
  public void testWithRedirectionWithSameNameAsDirectory() {
    Directory curDir = new Directory("", null);
    curDir.addDirectory(new Directory("out", curDir));
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    String[] comParams = {"hi", ">", "out"};
    this.redirect = new Redirection(comParams, "hi", 1, this.fs, this.cwd);
    File output = redirect.redirectToOverwrite();
    assertEquals(null, output);
  }

  @Test
  public void testWithOverwriteSimpleStringNonExistantFiles() {
    Directory curDir = new Directory("", null);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    String[] comParams = {"hi", ">", "out", ">", "out"};
    this.redirect = new Redirection(comParams, "hi", 1, this.fs, this.cwd);
    File output = redirect.redirectToAppend();
    assertEquals(null, output);
  }

  @Test
  public void testWithRedirectionWithInvalidCharacter() {
    Directory curDir = new Directory("", null);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    String[] comParams = {"hi", ">", "out?|"};
    this.redirect = new Redirection(comParams, "hi", 1, this.fs, this.cwd);
    File output = redirect.redirectToOverwrite();
    assertEquals(null, output);
  }

}
