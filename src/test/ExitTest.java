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
import command.Exit;
import filesystem.CurrentWorkingDirectoryI;
import filesystem.FileSystemI;

public class ExitTest {

  private Exit exitCommand;
  private FileSystemI fs;
  private CurrentWorkingDirectoryI cwd;

  @Before
  public void setUp() {
    this.fs = new MockFileSystem();
    this.cwd = new MockCurrentWorkingDirectory();
  }

  /**
   * The following is a test for the exit command. However, exit only has one outcome: exiting the
   * terminal/program. This means that if this test were to run, exit would actually exit the
   * program, and the JUnit would not be able to actually assess the success of the test properly.
   * For this reason, running the test provided below will result in a "failure trace", which means
   * that exit has actually exited the program (which is what it is actually supposed to do).
   */
  @Test
  public void test() {
    this.exitCommand = new Exit(this.fs, this.cwd);
    this.exitCommand.setCurrentParameters(0, new String[] {""});

    String output = this.exitCommand.execute();
    assertEquals("", output);
  }
}
