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
import command.Load;
import filesystem.CurrentWorkingDirectoryI;
import filesystem.Directory;
import filesystem.DirectoryStack;
import filesystem.FileSystemI;
import inputoutput.UserTerminalIn;

public class LoadTest {

  // After testing, dependencies on directory stack and userTerminalIn are assumed to be working
  // properly
  private Load loadCommand;
  private FileSystemI fs;
  private CurrentWorkingDirectoryI cwd;
  private DirectoryStack dirStack;
  private UserTerminalIn terminal;

  @Before
  public void setUp() {
    this.fs = new MockFileSystem();
    this.cwd = new MockCurrentWorkingDirectory();
    this.dirStack = new DirectoryStack();
    this.terminal = new UserTerminalIn();
  }

  public void makeFile(String writeString) {
    try {
      java.io.FileWriter writing = new java.io.FileWriter("testfile");
      java.io.BufferedWriter bw = new java.io.BufferedWriter(writing);
      bw.write(writeString);
      bw.close();

      // If file path error is found, print it for user and return null
    } catch (java.io.IOException e) {
      System.out.println(e);
    }
  }

  @Test
  /*
   * Run save on an empty file system. This is the equivalent of starting the Jshell and calling
   * the following commands: save testfile
   * 
   * This test case calls load on the session of the saved file system described above
   */
  public void testWithEmptyFileSystem() {
    UserTerminalIn.getCommandHistory().clear();
    UserTerminalIn.getCommandHistory().add("load testfile");
    this.makeFile("The Current Directory is:\n/\nsave testfile\n\nThe directory stack is:");
    Directory curDir = new Directory("/", null);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    this.loadCommand = new Load(this.fs, this.cwd, this.dirStack, this.terminal);
    this.loadCommand.setCurrentParameters(1, new String[] {"testfile"});
    String output = this.loadCommand.execute();
    assertEquals("save testfile\n\nThe directory stack is:\n", output);
  }

  @Test
  /*
   * Run save on an empty file system after calling a commmand that does not exist. This is the
   * equivalent of starting the Jshell and calling the following commands: print hi, save testfile
   * 
   * This test case calls load on the session of the saved file system described above
   */
  public void testWithInvalidCommandInHistory() {
    UserTerminalIn.getCommandHistory().clear();
    UserTerminalIn.getCommandHistory().add("load testfile");
    this.makeFile(
        "The Current Directory is:\n/\nprint hi\nsave testfile\n\nThe directory stack is:");
    Directory curDir = new Directory("/", null);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    this.loadCommand = new Load(this.fs, this.cwd, this.dirStack, this.terminal);
    this.loadCommand.setCurrentParameters(1, new String[] {"testfile"});
    String output = this.loadCommand.execute();
    assertEquals("print hi\nsave testfile\n\nThe directory stack is:\n", output);
  }

  @Test
  /*
   * Run save on a file system containing a directory. This is the equivalent of starting the Jshell
   * and calling the following commands: mkdir secondDir, save testfile
   * 
   * This test case calls load on the session of the saved file system described above
   */
  public void testWithOneDirectory() {
    UserTerminalIn.getCommandHistory().clear();
    UserTerminalIn.getCommandHistory().add("load testfile");
    this.makeFile("This Directory has the path:\n/secondDir\nThe Current Directory "
        + "is:\n/\nmkdir secondDir\nsave testfile\n\nThe directory stack is:");
    Directory curDir = new Directory("/", null);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    this.loadCommand = new Load(this.fs, this.cwd, this.dirStack, this.terminal);
    this.loadCommand.setCurrentParameters(1, new String[] {"testfile"});
    String output = this.loadCommand.execute();
    assertEquals("mkdir /secondDir\nmkdir secondDir\nsave testfile\n\nThe directory stack is:\n",
        output);
  }

  @Test
  /*
   * Run save on a file system containing a directory and a directory on the directory stack. This
   * is the equivalent of starting the Jshell and calling the following commands: mkdir secondDir,
   * pushd secondDir, save testfile
   * 
   * This test case calls load on the session of the saved file system described above
   */
  public void testWithOneDirectoryOnDirectoryStack() {
    UserTerminalIn.getCommandHistory().clear();
    UserTerminalIn.getCommandHistory().add("load testfile");
    this.makeFile("This Directory has the path:\n/secondDir\nThe Current Directory "
        + "is:\n/\nmkdir secondDir\npushd secondDir\nsave testfile\n\nThe directory stack is:\n/"
        + "secondDir/");
    Directory curDir = new Directory("/", null);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    this.loadCommand = new Load(this.fs, this.cwd, this.dirStack, this.terminal);
    this.loadCommand.setCurrentParameters(1, new String[] {"testfile"});
    String output = this.loadCommand.execute();
    assertEquals(
        "mkdir /secondDir\nmkdir secondDir\npushd secondDir\nsave testfile\n\nThe directory "
            + "stack is:\n/secondDir/\n",
        output);
  }

  @Test
  /*
   * Run save on a file system containing a directory that contains a file. This is the equivalent
   * of starting the Jshell and calling the following commands: mkdir secondDir, echo "line" >
   * secondDir/file1, save testfile
   * 
   * This test case calls load on the session of the saved file system described above
   */
  public void testWithOneDirectoryContainingOneFile() {
    UserTerminalIn.getCommandHistory().clear();
    UserTerminalIn.getCommandHistory().add("load testfile");
    this.makeFile(
        "This Directory has the path:\n/secondDir\nThis File has the name:\nfile1\nline\nThe "
            + "Current Directory is:\n/\nmkdir secondDir\necho \"line\" > secondDir/file1\n"
            + "save testfile\n\nThe directory stack is:");
    Directory curDir = new Directory("/", null);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    this.loadCommand = new Load(this.fs, this.cwd, this.dirStack, this.terminal);
    this.loadCommand.setCurrentParameters(1, new String[] {"testfile"});
    String output = this.loadCommand.execute();
    assertEquals(
        "mkdir /secondDir\necho \"\" > file1\necho \"line\" >> file1\nmkdir secondDir\necho "
            + "\"line\" > secondDir/file1\nsave testfile\n\nThe directory stack is:\n",
        output);
  }

  @Test
  /*
   * Run save on a file system containing a directory but the file path for the file does not exist
   * on the computer. This is the equivalent of starting the Jshell and calling the following
   * commands: mkdir secondDir, echo "line" > secondDir/file1, save testfile
   * 
   * This test case calls load on the session of the saved file system described above but with an
   * invalid path on the computer of the user
   */
  public void testWithNonExistantComputerPath() {
    UserTerminalIn.getCommandHistory().clear();
    UserTerminalIn.getCommandHistory().add("load testfile");
    this.makeFile(
        "This Directory has the path:\n/secondDir\nThis File has the name:\nfile1\nline\nThe "
            + "Current Directory is:\n/\nmkdir secondDir\necho \"line\" > secondDir/file1\n"
            + "save testfile\n\nThe directory stack is:");
    Directory curDir = new Directory("/", null);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    this.loadCommand = new Load(this.fs, this.cwd, this.dirStack, this.terminal);
    this.loadCommand.setCurrentParameters(1, new String[] {"/hi/hi/wrongfolder/testfile"});
    String output = this.loadCommand.execute();
    assertEquals(null, output);
  }

  @Test
  /*
   * Run save on a file system containing a directory where the current working directory is not the
   * root. This is the equivalent of starting the Jshell and calling the following commands: mkdir
   * secondDir, cd secondDir, save testfile
   */
  public void testWithCWDNotAsRoot() {
    UserTerminalIn.getCommandHistory().clear();
    UserTerminalIn.getCommandHistory().add("load testfile");
    this.makeFile(
        "This Directory has the path:\n/secondDir\nThe " + "Current Directory is:\nsecondDir\n"
            + "mkdir secondDir\ncd secondDir\nsave testfile\n\n" + "The directory stack is:");
    Directory curDir = new Directory("/", null);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    this.loadCommand = new Load(this.fs, this.cwd, this.dirStack, this.terminal);
    this.loadCommand.setCurrentParameters(1, new String[] {"testfile"});
    String output = this.loadCommand.execute();
    assertEquals("mkdir /secondDir\nmkdir secondDir\ncd secondDir\n"
        + "save testfile\n\nThe directory stack is:\n", output);
  }

}
