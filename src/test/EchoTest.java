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
import command.Echo;
import filesystem.CurrentWorkingDirectoryI;
import filesystem.Directory;
import filesystem.FileSystemI;

public class EchoTest {

  private Echo echoCommand;
  private FileSystemI fs;
  private CurrentWorkingDirectoryI cwd;

  @Before
  public void setUp() {
    this.fs = new MockFileSystem();
    this.cwd = new MockCurrentWorkingDirectory();
    this.echoCommand = new Echo(this.fs, this.cwd);
  }

  @Test
  public void testWithSimpleMessage() {
    this.echoCommand.setCurrentParameters(1, new String[] {"\"This is a basic message\""});
    this.echoCommand.setLine("echo \"This is a basic message\"");
    String output = this.echoCommand.execute();
    assertEquals("This is a basic message", output);
  }

  @Test
  public void testWithInvalidQuotes() {
    this.echoCommand.setCurrentParameters(1, new String[] {"\"This is a basic message"});
    this.echoCommand.setLine("echo \"This is a basic message");
    String output = this.echoCommand.execute();
    assertEquals(null, output);
  }

  @Test
  public void testWithNoParameters() {
    this.echoCommand.setCurrentParameters(0, new String[] {});
    this.echoCommand.setLine("echo");
    String output = this.echoCommand.execute();
    assertEquals(null, output);
  }

  @Test
  public void testWithBasicRedirection() {
    this.echoCommand.setCurrentParameters(3, new String[] {"\"redirect\"", ">", "out"});
    this.echoCommand.setLine("echo \"redirect\" > out");
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(new Directory("", null));

    String output = this.echoCommand.execute();
    assertEquals("redirect", output);
  }

  @Test
  public void testWithInvalidRedirection() {
    this.echoCommand.setCurrentParameters(3, new String[] {"\"failed redirect\"", ">out"});
    this.echoCommand.setLine("echo \"redirect\" > out");
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(new Directory("", null));

    String output = this.echoCommand.execute();
    assertEquals(null, output);
  }

  @Test
  public void testWithSeveralOverwriteRedirection() {
    String output = "";
    Directory curDir = new Directory("", null);
    for (int i = 0; i < 4; i++) {
      this.echoCommand.setCurrentParameters(3, new String[] {"\"redirect\"", ">", "out"});
      this.echoCommand.setLine("echo \"redirect\" > out");
      ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
      output = this.echoCommand.execute();
    }
    assertEquals("redirect", output);
  }

  @Test
  public void testWithSeveralAppendRedirection() {
    String output = "";
    Directory curDir = new Directory("", null);
    for (int i = 0; i < 4; i++) {
      this.echoCommand.setCurrentParameters(3, new String[] {"\"redirect\"", ">>", "out"});
      this.echoCommand.setLine("echo \"redirect\" >> out");
      ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
      output = this.echoCommand.execute();
    }
    assertEquals("redirect\nredirect\nredirect\nredirect", output);
  }



}
