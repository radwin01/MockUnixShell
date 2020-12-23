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

import command.Command;
import filesystem.CurrentWorkingDirectoryI;
import filesystem.FileSystemI;

public class CommandTestChild extends Command {

  /**
   * Default constructor method to initialize file system and current working directory
   * 
   * @param cwd stores current working directory from command call
   * @param fs stores current FileSystem instance with design
   */
  public CommandTestChild(FileSystemI fs, CurrentWorkingDirectoryI cwd) {
    super(fs, cwd);
  }

  protected int checkSymbolIndex(int symbolIndex, String potentialSymbol) {
    return super.checkSymbolIndex(symbolIndex, potentialSymbol);
  }

  protected int redirectLoopEnd() {
    return super.redirectLoopEnd();
  }

  protected String printErrorMessage(String error) {
    return super.printErrorMessage(error);
  }

  protected String redirect(String output) {
    return super.redirect(output);
  }
}
