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
import command.Save;
import filesystem.CurrentWorkingDirectoryI;
import filesystem.Directory;
import filesystem.DirectoryStack;
import filesystem.File;
import filesystem.FileSystemI;
import inputoutput.UserTerminalIn;

public class SaveTest {

  // After testing, dependencies on directory stack are assumed to be working properly
  private Save saveCommand;
  private FileSystemI fs;
  private CurrentWorkingDirectoryI cwd;
  private DirectoryStack dirStack;

  @Before
  public void setUp() {
    this.fs = new MockFileSystem();
    this.cwd = new MockCurrentWorkingDirectory();
    this.dirStack = new DirectoryStack();
  }

  @Test
  /*
   * Run save on an empty file system. This is the equivalent of starting the Jshell and calling the
   * following commands: save testfile
   */
  public void testWithEmptyFileSystem() {
    UserTerminalIn.getCommandHistory().clear();
    UserTerminalIn.getCommandHistory().add("save testfile");
    Directory curDir = new Directory("", null);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    this.saveCommand = new Save(this.fs, this.cwd, this.dirStack);
    this.saveCommand.setCurrentParameters(1, new String[] {"testfile"});
    String output = this.saveCommand.execute();
    assertEquals("The Current Directory is:\n/\nsave testfile\n\nThe directory stack is:", output);
  }

  @Test
  /*
   * Run save on an empty file system after calling a commmand that does not exist. This is the
   * equivalent of starting the Jshell and calling the following commands: print hi, save testfile
   */
  public void testWithInvalidCommandInHistory() {
    UserTerminalIn.getCommandHistory().clear();
    UserTerminalIn.getCommandHistory().add("print hi");
    UserTerminalIn.getCommandHistory().add("save testfile");
    Directory curDir = new Directory("", null);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    this.saveCommand = new Save(this.fs, this.cwd, this.dirStack);
    this.saveCommand.setCurrentParameters(1, new String[] {"testfile"});
    String output = this.saveCommand.execute();
    assertEquals("The Current Directory is:\n/\nprint hi\nsave "
        + "testfile\n\nThe directory stack is:", output);
  }

  @Test
  /*
   * Run save on a file system containing a directory. This is the equivalent of starting the
   * Jshell and calling the following commands: mkdir secondDir, save testfile
   */
  public void testWithOneDirectory() {
    UserTerminalIn.getCommandHistory().clear();
    UserTerminalIn.getCommandHistory().add("mkdir secondDir");
    UserTerminalIn.getCommandHistory().add("save testfile");
    Directory curDir = new Directory("", null);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    Directory secDir = new Directory("secondDir", curDir);
    curDir.addDirectory(secDir);
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    this.saveCommand = new Save(this.fs, this.cwd, this.dirStack);
    this.saveCommand.setCurrentParameters(1, new String[] {"testfile"});
    String output = this.saveCommand.execute();
    assertEquals("This Directory has the path:\n/secondDir\nThe Current Directory "
        + "is:\n/\nmkdir secondDir\nsave testfile\n\nThe directory stack is:", output);
  }

  @Test
  /*
   * Run save on a file system containing a directory and a directory on the directory stack. This
   * is the equivalent of starting the Jshell and calling the following commands: mkdir secondDir,
   * pushd secondDir, save testfile
   */
  public void testWithOneDirectoryOnDirectoryStack() {
    UserTerminalIn.getCommandHistory().clear();
    UserTerminalIn.getCommandHistory().add("mkdir secondDir");
    UserTerminalIn.getCommandHistory().add("pushd secondDir");
    UserTerminalIn.getCommandHistory().add("save testfile");
    Directory curDir = new Directory("", null);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    Directory secDir = new Directory("secondDir", curDir);
    curDir.addDirectory(secDir);
    this.dirStack.getDirStack().add(secDir);
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    this.saveCommand = new Save(this.fs, this.cwd, this.dirStack);
    this.saveCommand.setCurrentParameters(1, new String[] {"testfile"});
    String output = this.saveCommand.execute();
    assertEquals("This Directory has the path:\n/secondDir\nThe Current Directory "
        + "is:\n/\nmkdir secondDir\npushd secondDir\nsave "
        + "testfile\n\nThe directory stack is:\n/secondDir/",
        output);
  }

  @Test
  /*
   * Run save on a file system containing a directory that contains a file. This is the equivalent
   * of starting the Jshell and calling the following commands: mkdir secondDir, echo "line" >
   * secondDir/file1, save testfile
   */
  public void testWithOneDirectoryContainingOneFile() {
    UserTerminalIn.getCommandHistory().clear();
    UserTerminalIn.getCommandHistory().add("mkdir secondDir");
    UserTerminalIn.getCommandHistory().add("echo \"line\" > secondDir/file1");
    UserTerminalIn.getCommandHistory().add("save testfile");
    Directory curDir = new Directory("", null);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    Directory secDir = new Directory("secondDir", curDir);
    File newfile = new File("file1", "line", secDir);
    curDir.addDirectory(secDir);
    secDir.addFile(newfile);
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    this.saveCommand = new Save(this.fs, this.cwd, this.dirStack);
    this.saveCommand.setCurrentParameters(1, new String[] {"testfile"});
    String output = this.saveCommand.execute();
    String check =
        "This Directory has the path:\n/secondDir\nThis File has the name:\nfile1\nline\nThe "
            + "Current Directory is:\n/\nmkdir secondDir\necho \"line\" > secondDir/file1\n"
            + "save testfile\n\nThe directory stack is:";
    assertEquals(check, output);
  }

  @Test
  /*
   * Run save on a file system containing a directory but the file path for the file does not exist
   * on the computer. This is the equivalent of starting the Jshell and calling the following
   * commands: mkdir secondDir, echo "line" > secondDir/file1, save
   * /testfile/hi/hi/hi/fakedirectoryname
   */
  public void testWithNonExistantComputerPath() {
    UserTerminalIn.getCommandHistory().clear();
    UserTerminalIn.getCommandHistory().add("mkdir secondDir");
    UserTerminalIn.getCommandHistory().add("echo \"line\" > secondDir/file1");
    UserTerminalIn.getCommandHistory().add("save /testfile/hi/hi/hi/fakedirectoryname");
    Directory curDir = new Directory("", null);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    Directory secDir = new Directory("secondDir", curDir);
    File newfile = new File("file1", "line", secDir);
    curDir.addDirectory(secDir);
    secDir.addFile(newfile);
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    this.saveCommand = new Save(this.fs, this.cwd, this.dirStack);
    this.saveCommand.setCurrentParameters(1, new String[] {"/testfile/hi"
        + "/hi/hi/fakedirectoryname"});
    String output = this.saveCommand.execute();
    assertEquals(null, output);
  }

  @Test
  /*
   * Run save on a file system containing a directory where the current working directory is not
   * the root. This is the equivalent of starting the Jshell and calling the following commands:
   * mkdir secondDir, cd secondDir, save testfile
   */
  public void testWithCWDNotAsRoot() {
    UserTerminalIn.getCommandHistory().clear();
    Directory curDir = new Directory("", null);
    Directory secDir = new Directory("secondDir", curDir);
    curDir.addDirectory(secDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(secDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("secondDir");
    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    UserTerminalIn.getCommandHistory().add("mkdir secondDir");
    UserTerminalIn.getCommandHistory().add("cd secondDir");
    UserTerminalIn.getCommandHistory().add("save testfile");
    this.saveCommand = new Save(this.fs, this.cwd, this.dirStack);
    this.saveCommand.setCurrentParameters(1, new String[] {"testfile"});
    String output = this.saveCommand.execute();
    assertEquals(
        "This Directory has the path:\n/secondDir\nThe " + "Current Directory is:\nsecondDir\n"
            + "mkdir secondDir\ncd secondDir\nsave testfile\n\n" + "The directory stack is:",
        output);
  }

}
