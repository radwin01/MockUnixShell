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
import command.Man;
import command.Cat;
import filesystem.CurrentWorkingDirectoryI;
import filesystem.FileSystemI;

public class ManTest {

  private Man manCommand;
  private FileSystemI fs;
  private CurrentWorkingDirectoryI cwd;

  @Before
  public void setUp() {
    this.fs = new MockFileSystem();
    this.cwd = new MockCurrentWorkingDirectory();
  }

  @Test
  public void testWithValidCommandCalledWithinMan() {
    this.manCommand = new Man(this.fs, this.cwd);
    this.manCommand.setCurrentParameters(1, new String[] {"man"});

    String output = this.manCommand.execute();
    assertEquals(Man.getDescriptionOfCommand(), output);
  }

  @Test
  public void testWithValidCommandCalledFromAnotherCommand() {
    this.manCommand = new Man(this.fs, this.cwd);
    this.manCommand.setCurrentParameters(1, new String[] {"cat"});

    String output = this.manCommand.execute();
    assertEquals(Cat.getDescriptionOfCommand(), output);
  }

  @Test
  public void testWithInvalidCommand() {
    this.manCommand = new Man(this.fs, this.cwd);
    this.manCommand.setCurrentParameters(1, new String[] {"unknown"});

    String output = this.manCommand.execute();
    assertEquals(null, output);
  }
}


