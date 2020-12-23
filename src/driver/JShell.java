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
package driver;

import command.Command;
import command.Validation;
import filesystem.CurrentWorkingDirectory;
import filesystem.DirectoryStack;
import filesystem.FileSystem;
import inputoutput.UserTerminalIn;

/**
 * Represents the JShell.
 * 
 * @version 1.0
 */

public class JShell {

  // Holds current working directory as command go through file system
  private static CurrentWorkingDirectory cwd;

  // Holds current directory stack class statically for access by push and pop
  private static DirectoryStack dirStack;

  /**
   * Gets Current working directory from instance variables.
   * 
   * @return the Current working directory from instance variables
   */
  public static CurrentWorkingDirectory getCWDFromShell() {
    return JShell.cwd;
  }

  /**
   * Gets directory stack from instance variables.
   * 
   * @return the directory stack from instance variables
   */
  public static DirectoryStack getDirStackFromShell() {
    return JShell.dirStack;
  }

  /**
   * Executes the JShell to mimic a terminal.
   * 
   * @param args
   */
  public static void main(String[] args) {

    // Creates an instance of a UserTerminalIn object to be used to grab user input
    UserTerminalIn userInput = new UserTerminalIn();

    // Instantiates the file system, creating the file system to be used throughout the shell
    FileSystem currentFileSystem = FileSystem.createInstanceOfFileSystem();

    // Sets instance of CurrentWorkingDirectory, setting the initial cwd to root.
    cwd = new CurrentWorkingDirectory(currentFileSystem.getRootDir());

    // Creates an instance of DirectoryStack.
    dirStack = new DirectoryStack();

    // Continuously checks for user input to validate and execute commands
    while (true) {

      // Starts each line with the current directory path
      System.out.print(cwd.getCWDString() + ": ");

      // Creates a command using the input the user enters, and sends the command to be validated
      Command com = Validation.interpretCommand(userInput.getUserInput(), userInput,
          JShell.getCWDFromShell(), currentFileSystem);

      // Checks if the command was deemed valid
      if (com != null) {

        // Completes the commands action
        com.execute();
      }

    }

  }

  /*
   * TEST CASES Note: These tests are cumulative.
   * 
   * (1) asdfasd fasdfasdf
   * 
   * ^should warn user it is an invalid command
   * 
   * (2) mkdir folder1 cd folder1 mkdir folder2 cd folder2 mkdir folder3 cd folder3
   * 
   * ^cwd should be in folder3
   * 
   * FOLLOW-UP from (2): (3) pwd ^ should print "/folder1/folder2/folder3/"
   * 
   * (4) cd ../../../folder1/folder2 cd ../ cd ../ cd folder2/folder3 cd / cd ../../
   * 
   * ^cwd should be the root directory
   * 
   * (5) cd folder1/folder2 pushd /folder1 pushd folder1 pushd folder2
   * 
   * ^cwd should be in folder2
   * 
   * (6) popd popd 123 popd
   * 
   * ^cwd should be in folder2
   * 
   * (7) echo "text" > file1 echo "test" >> file1 echo again >> file1 cat file1
   * 
   * ^the output should contain: text test
   * 
   * (8) man man
   * 
   * ^should output the manual for man
   * 
   * (9) speak "hello world" QUIT
   * 
   * ^should warn user the phrase must be in quotes and not speak
   * 
   * (10) speak "hello world"
   * 
   * ^speaks hello world
   * 
   * (11) speak hello i QUIT
   * 
   * ^speaks "hello i"
   * 
   * (12) echo "overwrite" > file1 cat file1
   * 
   * ^output should contain: overwrite
   * 
   * (13) ls file1
   * 
   * ^ output should be path for file 1
   */

}
