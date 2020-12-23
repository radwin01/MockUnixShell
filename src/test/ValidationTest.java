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
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import command.Command;
import command.Echo;
import command.Validation;
import filesystem.CurrentWorkingDirectoryI;
import filesystem.FileSystemI;
import inputoutput.UserTerminalIn;

public class ValidationTest {

  // After testing, dependencies on directory stack and userTerminalIn are assumed to be working
  // properly
  private FileSystemI fs;
  private CurrentWorkingDirectoryI cwd;
  private UserTerminalIn terminal;

  @Before
  public void setUp() {
    this.fs = new MockFileSystem();
    this.cwd = new MockCurrentWorkingDirectory();
    this.terminal = new UserTerminalIn();
  }

  @Test
  public void testCountQuoteOccurencesNoQuotes() {
    int output = Validation.countQuoteOccurences("hello i am a test message with no quotes");
    assertEquals(0, output);
  }

  @Test
  public void testCountQuoteOccurencesOneQuote() {
    int output = Validation.countQuoteOccurences("hello i am a test message with one quote\"");
    assertEquals(1, output);
  }

  @Test
  public void testCountQuoteOccurencesManyQuotes() {
    int output =
        Validation.countQuoteOccurences("\"\"hello i \"\"am a test \"message with \"two quotes");
    assertEquals(6, output);
  }

  @Test
  public void testContainsValidCharactersWithValidName() {
    boolean output = Validation.containsValidCharacters("validName");
    assertTrue(output);
  }

  @Test
  public void testContainsValidCharactersWithInvalidCharacter() {
    boolean output = Validation.containsValidCharacters("Invalid Name");
    assertTrue(!output);
  }

  @Test
  public void testContainsValidCharactersWithInvalidCharacters() {
    boolean output = Validation.containsValidCharacters("|InvalidName?");
    assertTrue(!output);
  }

  @Test
  public void testIntepretCommandInvalidCommand() {
    Command output = Validation.interpretCommand("print hi", this.terminal, this.cwd, this.fs);
    assertEquals(null, output);
  }

  @Test
  public void testIntepretCommandValidCommand() {
    Command output = Validation.interpretCommand("", this.terminal, this.cwd, this.fs);
    assertTrue(output instanceof Command);
  }

  @Test
  public void testIntepretCommandInvalidCommandParametersTooMany() {
    Command output = Validation.interpretCommand("exit invalid", this.terminal, this.cwd, this.fs);
    assertEquals(null, output);
  }

  @Test
  public void testIntepretCommandInvalidCommandParametersTooFew() {
    Command output = Validation.interpretCommand("rm", this.terminal, this.cwd, this.fs);
    assertEquals(null, output);
  }

  @Test
  public void testIntepretCommandValidEcho() {
    Command output = Validation.interpretCommand("  echo \"hi\"", this.terminal, this.cwd,
        this.fs);
    assertTrue(output instanceof Echo);
  }

}
