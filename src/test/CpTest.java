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
import command.Cp;
import filesystem.CurrentWorkingDirectoryI;
import filesystem.Directory;
import filesystem.File;
import filesystem.FileSystemI;

public class CpTest {

  private Cp cpCommand;
  private FileSystemI fs;
  private CurrentWorkingDirectoryI cwd;

  @Before
  public void setUp() {
    this.fs = new MockFileSystem();
    this.cwd = new MockCurrentWorkingDirectory();
  }

  @Test
  public void testCpWithRelativePathsForDirectory() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("folder1", curDir);
    curDir.addDirectory(dirOne);
    Directory dirTwo = new Directory("folder2", curDir);
    curDir.addDirectory(dirTwo);

    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);

    this.cpCommand = new Cp(this.fs, this.cwd);
    this.cpCommand.setCurrentParameters(2, new String[] {"folder2", "folder1"});
    this.cpCommand.execute();

    int inDirOne = dirOne.findInSubDirectories("folder2");
    int inCurDir = curDir.findInSubDirectories("folder2");

    boolean result = inDirOne == 0 && inCurDir == 1;

    assertEquals(true, result);
  }

  @Test
  public void testCpWithRelativePathsForFile() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("folder1", curDir);
    curDir.addDirectory(dirOne);
    File file1 = new File("file1", "hello world\n", curDir);
    curDir.addFile(file1);

    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);

    this.cpCommand = new Cp(this.fs, this.cwd);
    this.cpCommand.setCurrentParameters(2, new String[] {"file1", "folder1"});
    this.cpCommand.execute();

    File inDirOne = dirOne.findInFiles("file1");
    File inCurDir = curDir.findInFiles("file1");

    boolean result = inDirOne.equals(file1) && inCurDir.equals(file1);

    assertEquals(true, result);
  }

  @Test
  public void testCpToCopyDirectoryIntoItself() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("folder1", curDir);
    curDir.addDirectory(dirOne);
    Directory dirTwo = new Directory("folder2", dirOne);
    dirOne.addDirectory(dirTwo);

    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);

    this.cpCommand = new Cp(this.fs, this.cwd);
    this.cpCommand.setCurrentParameters(2, new String[] {"folder1", "folder1"});

    String output = this.cpCommand.execute();

    assertEquals(null, output);
  }

  @Test
  public void testCpToCopyDirectoryIntoNonExistentDirectory() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("folder1", curDir);
    curDir.addDirectory(dirOne);
    Directory dirTwo = new Directory("folder2", dirOne);
    dirOne.addDirectory(dirTwo);
    Directory dirThree = new Directory("folder3", curDir);
    curDir.addDirectory(dirThree);

    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);

    this.cpCommand = new Cp(this.fs, this.cwd);
    this.cpCommand.setCurrentParameters(2, new String[] {"folder3", "/folder1/folder4"});

    this.cpCommand.execute();

    int inCurDir = curDir.findInSubDirectories("folder3");
    int inDirOne = dirOne.findInSubDirectories("folder4");
    int inNewDir = dirOne.getSubDirectoryFromString("folder4").findInSubDirectories("folder3");

    boolean result = inCurDir == 1 && inDirOne == 1 && inNewDir == 0;

    assertEquals(true, result);
  }

  @Test
  public void testCpToCopyDirectoryIntoDottedPath() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("folder1", curDir);
    curDir.addDirectory(dirOne);
    Directory dirTwo = new Directory("folder2", dirOne);
    dirOne.addDirectory(dirTwo);

    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/folder1/");
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(dirOne);

    this.cpCommand = new Cp(this.fs, this.cwd);
    this.cpCommand.setCurrentParameters(2, new String[] {"folder2", "../"});
    this.cpCommand.execute();

    int inCurDir = curDir.findInSubDirectories("folder2");
    int inDirOne = dirOne.findInSubDirectories("folder2");

    boolean result = inCurDir == 1 && inDirOne == 0;

    assertEquals(true, result);
  }

  @Test
  public void testCpToCopyNonExistentDirectory() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("folder1", curDir);
    curDir.addDirectory(dirOne);
    Directory dirTwo = new Directory("folder2", dirOne);
    dirOne.addDirectory(dirTwo);

    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/folder1/");
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(dirOne);

    this.cpCommand = new Cp(this.fs, this.cwd);
    this.cpCommand.setCurrentParameters(2, new String[] {"folder3", "/"});

    String output = this.cpCommand.execute();

    assertEquals(null, output);
  }

  @Test
  public void testCpToOverwriteDirectory() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("folder1", curDir);
    curDir.addDirectory(dirOne);
    Directory dirTwo = new Directory("folder2", dirOne);
    dirOne.addDirectory(dirTwo);
    dirTwo.addDirectory(new Directory("folder3", dirTwo));
    Directory toMove = new Directory("folder2", curDir);
    curDir.addDirectory(toMove);

    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);

    this.cpCommand = new Cp(this.fs, this.cwd);
    this.cpCommand.setCurrentParameters(2, new String[] {"folder2", "folder1"});

    this.cpCommand.execute();

    int inCurDir = curDir.findInSubDirectories("folder2");
    int inDirOne = dirOne.findInSubDirectories("folder2");
    int inDirTwo = dirOne.getSubDirectoryFromString("folder2").findInSubDirectories("folder3");

    boolean result = inCurDir == 1 && inDirOne == 0 && inDirTwo == -1;

    assertEquals(true, result);
  }

  @Test
  public void testCpToOverwriteFile() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("folder1", curDir);
    curDir.addDirectory(dirOne);
    curDir.addFile(new File("file1", "hello", curDir));
    dirOne.addFile(new File("file1", "bye", dirOne));

    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);

    this.cpCommand = new Cp(this.fs, this.cwd);
    this.cpCommand.setCurrentParameters(2, new String[] {"file1", "folder1/file1"});

    this.cpCommand.execute();

    File inCurDir = curDir.findInFiles("file1");
    File inDirOne = dirOne.findInFiles("file1");
    String newContents = inDirOne.getContent();

    System.out.println(inCurDir == null);
    System.out.println(inDirOne != null);
    System.out.println(newContents.equals("hello"));

    boolean result = inCurDir != null && inDirOne != null && newContents.equals("hello");

    assertEquals(true, result);
  }

  @Test
  public void testCpToCopyFileIntoDirectoryWithFileOfTheSameName() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("folder1", curDir);
    File file1 = new File("file1", "hello", curDir);
    curDir.addFile(file1);
    File file2 = new File("file1", "bye", dirOne);
    dirOne.addFile(file2);

    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);

    this.cpCommand = new Cp(this.fs, this.cwd);
    this.cpCommand.setCurrentParameters(2, new String[] {"file1", "folder1"});

    this.cpCommand.execute();

    File inCurDir = curDir.findInFiles("file1");
    File inDirOne = dirOne.findInFiles("file1");
    String newContents = inDirOne.getContent();

    boolean result = inCurDir != null && inDirOne != null && newContents.equals("bye");

    assertEquals(true, result);
  }
}
