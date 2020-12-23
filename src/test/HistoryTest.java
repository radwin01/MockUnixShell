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
import command.History;
import filesystem.CurrentWorkingDirectoryI;
import filesystem.FileSystemI;
import inputoutput.UserTerminalIn;

public class HistoryTest {

  private History hsCommand;
  private FileSystemI fs;
  private CurrentWorkingDirectoryI cwd;

  @Before
  public void setUp() {
    this.fs = new MockFileSystem();
    this.cwd = new MockCurrentWorkingDirectory();
  }

  @Test
  public void testHistoryWithMultipleCommandsNoParam() {

    UserTerminalIn.getCommandHistory().clear();

    UserTerminalIn.getCommandHistory().add("echo \"this works\" > file");
    UserTerminalIn.getCommandHistory().add("mkdir test");
    UserTerminalIn.getCommandHistory().add("mkdir hello");
    UserTerminalIn.getCommandHistory().add("rm hello");
    UserTerminalIn.getCommandHistory().add("history");

    hsCommand = new History(this.fs, this.cwd);
    hsCommand.setCurrentParameters(0, null);

    String output = hsCommand.execute();

    assertEquals(
        "1. echo \"this works\" > file\n2. mkdir test\n3. mkdir hello\n4. rm hello\n5. history\n",
        output);

  }

  @Test
  public void testHistoryWithHistoryCalledPreviouslyNoParam() {

    UserTerminalIn.getCommandHistory().clear();

    UserTerminalIn.getCommandHistory().add("echo \"this works\" > file");
    UserTerminalIn.getCommandHistory().add("history");
    UserTerminalIn.getCommandHistory().add("mkdir hello");
    UserTerminalIn.getCommandHistory().add("rm hello");
    UserTerminalIn.getCommandHistory().add("history");

    hsCommand = new History(this.fs, this.cwd);
    hsCommand.setCurrentParameters(0, null);

    String output = hsCommand.execute();

    assertEquals(
        "1. echo \"this works\" > file\n2. history\n3. mkdir hello\n4. rm hello\n5. history\n",
        output);

  }

  @Test
  public void testHistoryWithInvalidCommandNoParam() {

    UserTerminalIn.getCommandHistory().clear();

    UserTerminalIn.getCommandHistory().add("echo \"this works\" > file");
    UserTerminalIn.getCommandHistory().add("invalid command!!!!");
    UserTerminalIn.getCommandHistory().add("mkdir hello");
    UserTerminalIn.getCommandHistory().add("rm hello");
    UserTerminalIn.getCommandHistory().add("history");

    hsCommand = new History(this.fs, this.cwd);
    hsCommand.setCurrentParameters(0, null);

    String output = hsCommand.execute();

    assertEquals("1. echo \"this works\" > file\n2. invalid command!!!!"
        + "\n3. mkdir hello\n4. rm hello\n5. history\n", output);

  }

  @Test
  public void testHistoryWithMultipleCommandsAndNumParamLessThenTotal() {

    UserTerminalIn.getCommandHistory().clear();

    UserTerminalIn.getCommandHistory().add("echo \"this works\" > file");
    UserTerminalIn.getCommandHistory().add("invalid command!!!!");
    UserTerminalIn.getCommandHistory().add("mkdir hello");
    UserTerminalIn.getCommandHistory().add("rm hello");
    UserTerminalIn.getCommandHistory().add("history 3");

    hsCommand = new History(this.fs, this.cwd);
    hsCommand.setCurrentParameters(1, new String[] {"3"});

    String output = hsCommand.execute();

    assertEquals("3. mkdir hello\n4. rm hello\n5. history 3\n", output);

  }

  @Test
  public void testHistoryWithMultipleCommandsAndNumParamGreaterThenTotal() {

    UserTerminalIn.getCommandHistory().clear();

    UserTerminalIn.getCommandHistory().add("echo \"this works\" > file");
    UserTerminalIn.getCommandHistory().add("invalid command!!!!");
    UserTerminalIn.getCommandHistory().add("mkdir hello");
    UserTerminalIn.getCommandHistory().add("rm hello");
    UserTerminalIn.getCommandHistory().add("history 10");

    hsCommand = new History(this.fs, this.cwd);
    hsCommand.setCurrentParameters(1, new String[] {"10"});

    String output = hsCommand.execute();

    assertEquals("1. echo \"this works\" > file\n2. invalid command!!!!"
        + "\n3. mkdir hello\n4. rm hello\n5. history 10\n", output);
  }

  @Test
  public void testHistoryWithInvalidParam() {

    UserTerminalIn.getCommandHistory().clear();

    UserTerminalIn.getCommandHistory().add("echo \"this works\" > file");
    UserTerminalIn.getCommandHistory().add("invalid command!!!!");
    UserTerminalIn.getCommandHistory().add("mkdir hello");
    UserTerminalIn.getCommandHistory().add("rm hello");
    UserTerminalIn.getCommandHistory().add("history invalidParam");

    hsCommand = new History(this.fs, this.cwd);
    hsCommand.setCurrentParameters(1, new String[] {"invalidParam"});

    String output = hsCommand.execute();

    assertEquals(null, output);

  }



}
