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
import filesystem.FileSystemI;

public class CommandTest {

  private CommandTestChild command;
  private FileSystemI fs;
  private CurrentWorkingDirectoryI cwd;

  @Before
  public void setUp() {
    this.fs = new MockFileSystem();
    this.cwd = new MockCurrentWorkingDirectory();
  }

  @Test
  public void testCommandExecute() {
    this.command = new CommandTestChild(fs, cwd);
    String output = this.command.execute();
    assertEquals("", output);
  }

  @Test
  public void testCheckSymbolIndexNoSymbol() {
    this.command = new CommandTestChild(fs, cwd);
    int output = this.command.checkSymbolIndex(0, "m");
    assertEquals(0, output);
  }

  @Test
  public void testCheckSymbolIndexOverwriteSymbol() {
    this.command = new CommandTestChild(fs, cwd);
    int output = this.command.checkSymbolIndex(0, ">");
    assertEquals(2, output);
  }

  @Test
  public void testCheckSymbolIndexAppendSymbol() {
    this.command = new CommandTestChild(fs, cwd);
    int output = this.command.checkSymbolIndex(0, ">>");
    assertEquals(3, output);
  }

  @Test
  public void testRedirectLoopEndWithRedirectSymbol() {
    this.command = new CommandTestChild(fs, cwd);
    this.command.setCurrentParameters(2, new String[] {"hello", ">"});
    int output = this.command.redirectLoopEnd();
    assertEquals(1, output);
  }

  @Test
  public void testRedirectLoopEndWithSeveralRedirectSymbols() {
    this.command = new CommandTestChild(fs, cwd);
    this.command.setCurrentParameters(7,
        new String[] {"hello", ">", "hello", ">", ">>", "hi", "hi"});
    int output = this.command.redirectLoopEnd();
    assertEquals(4, output);
  }

  @Test
  public void testRedirectLoopEndWithNoRedirectSymbols() {
    this.command = new CommandTestChild(fs, cwd);
    this.command.setCurrentParameters(7, new String[] {"hello", "q", "hello", "q", "q", "q", "q"});
    int output = this.command.redirectLoopEnd();
    assertEquals(7, output);
  }

  @Test
  public void testPrintErrorMessage() {
    this.command = new CommandTestChild(fs, cwd);
    String output = this.command.printErrorMessage("This is an error");
    assertEquals(null, output);
  }

  @Test
  public void testCommandRedirectWithOverwrite() {
    Directory curDir = new Directory("", null);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    this.command = new CommandTestChild(fs, cwd);
    this.command.setCurrentParameters(3, new String[] {"hello", ">", "out"});
    String output = this.command.redirect("hello");
    output = this.command.redirect("hello");
    assertEquals("hello", output);
  }

  @Test
  public void testCommandRedirectWithAppend() {
    Directory curDir = new Directory("", null);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    this.command = new CommandTestChild(fs, cwd);
    this.command.setCurrentParameters(3, new String[] {"hello", ">>", "out"});
    String output = this.command.redirect("hello");
    output = this.command.redirect("hello");
    assertEquals("hello\nhello", output);
  }

  @Test
  public void testCommandRedirectWhenNoRedirectRequired() {
    Directory curDir = new Directory("", null);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    this.command = new CommandTestChild(fs, cwd);
    this.command.setCurrentParameters(3, new String[] {"hello", "no", "redirect"});
    String output = this.command.redirect("hello no redirect");
    assertEquals("hello no redirect", output);
  }

  @Test
  public void testCommandRedirectWhenNonExistantPath() {
    Directory curDir = new Directory("", null);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    this.command = new CommandTestChild(fs, cwd);
    this.command.setCurrentParameters(3, new String[] {"hello", ">>", "folder/out"});
    String output = this.command.redirect("hello");
    output = this.command.redirect("hello");
    assertEquals(null, output);
  }

  @Test
  public void testCommandRedirectWithEmptyString() {
    Directory curDir = new Directory("", null);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    this.command = new CommandTestChild(fs, cwd);
    this.command.setCurrentParameters(2, new String[] {">", "out"});
    String output = this.command.redirect("");
    assertEquals("", output);
  }

}
