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
import command.Mv;
import filesystem.CurrentWorkingDirectoryI;
import filesystem.Directory;
import filesystem.File;
import filesystem.FileSystemI;

public class MvTest {

  private Mv mvCommand;
  private FileSystemI fs;
  private CurrentWorkingDirectoryI cwd;

  @Before
  public void setUp() {
    this.fs = new MockFileSystem();
    this.cwd = new MockCurrentWorkingDirectory();
  }

  @Test
  public void testMvWithRelativePathsForDirectory() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("folder1", curDir);
    curDir.addDirectory(dirOne);
    Directory dirTwo = new Directory("folder2", curDir);
    curDir.addDirectory(dirTwo);

    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);

    this.mvCommand = new Mv(this.fs, this.cwd);
    this.mvCommand.setCurrentParameters(2, new String[] {"folder2", "folder1"});
    this.mvCommand.execute();

    int inDirOne = dirOne.findInSubDirectories("folder2");
    int inCurDir = curDir.findInSubDirectories("folder2");

    boolean result = inDirOne == 0 && inCurDir == -1;

    assertEquals(true, result);
  }

  @Test
  public void testMvWithRelativePathsForFile() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("folder1", curDir);
    curDir.addDirectory(dirOne);
    File file1 = new File("file1", "hello world\n", curDir);
    curDir.addFile(file1);

    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);

    this.mvCommand = new Mv(this.fs, this.cwd);
    this.mvCommand.setCurrentParameters(2, new String[] {"file1", "folder1"});
    this.mvCommand.execute();

    File inDirOne = dirOne.findInFiles("file1");
    File inCurDir = curDir.findInFiles("file1");

    boolean result = inDirOne.equals(file1) && inCurDir == null;

    assertEquals(true, result);
  }

  @Test
  public void testMvToRenameFileWithFullPath() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("folder1", curDir);
    curDir.addDirectory(dirOne);
    File file1 = new File("file1", "hello world\n", dirOne);
    dirOne.addFile(file1);

    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);

    this.mvCommand = new Mv(this.fs, this.cwd);
    this.mvCommand.setCurrentParameters(2, new String[] {"/folder1/file1", "folder1/renamed"});
    this.mvCommand.execute();

    File inDirOne = dirOne.findInFiles("renamed");
    File inDirOneOld = dirOne.findInFiles("file1");

    boolean result = inDirOne.equals(file1) && inDirOneOld == null;

    assertEquals(true, result);
  }

  @Test
  public void testMvToRenameDirectoryWithRelativePath() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("folder1", curDir);
    curDir.addDirectory(dirOne);
    Directory dirTwo = new Directory("folder2", curDir);
    curDir.addDirectory(dirTwo);

    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);

    this.mvCommand = new Mv(this.fs, this.cwd);
    this.mvCommand.setCurrentParameters(2, new String[] {"folder2", "renamed"});
    this.mvCommand.execute();

    int inCurDir = curDir.findInSubDirectories("renamed");
    int inCurDirOld = curDir.findInSubDirectories("folder2");

    boolean result = inCurDir == 1 && inCurDirOld == -1;

    assertEquals(true, result);
  }

  @Test
  public void testMvToMoveDirectoryIntoItself() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("folder1", curDir);
    curDir.addDirectory(dirOne);
    Directory dirTwo = new Directory("folder2", dirOne);
    dirOne.addDirectory(dirTwo);

    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);

    this.mvCommand = new Mv(this.fs, this.cwd);
    this.mvCommand.setCurrentParameters(2, new String[] {"folder1", "folder1"});

    String output = this.mvCommand.execute();

    assertEquals(null, output);
  }

  @Test
  public void testMvToMoveDirectoryIntoNonExistentDirectory() {
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

    this.mvCommand = new Mv(this.fs, this.cwd);
    this.mvCommand.setCurrentParameters(2, new String[] {"folder3", "/folder1/folder4"});

    this.mvCommand.execute();

    int inCurDir = curDir.findInSubDirectories("folder3");
    int inDirOne = dirOne.findInSubDirectories("folder4");
    String newDir = dirThree.getParentDir().getName();

    boolean result = inCurDir == -1 && inDirOne == 1 && newDir.equals("folder4");

    assertEquals(true, result);
  }

  @Test
  public void testMvToMoveDirectoryIntoDottedPath() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("folder1", curDir);
    curDir.addDirectory(dirOne);
    Directory dirTwo = new Directory("folder2", dirOne);
    dirOne.addDirectory(dirTwo);

    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/folder1/");
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(dirOne);

    this.mvCommand = new Mv(this.fs, this.cwd);
    this.mvCommand.setCurrentParameters(2, new String[] {"folder2", "../"});
    this.mvCommand.execute();

    int inCurDir = curDir.findInSubDirectories("folder2");
    int inDirOne = dirOne.findInSubDirectories("folder2");

    boolean result = inCurDir == 1 && inDirOne == -1;

    assertEquals(true, result);
  }

  @Test
  public void testMvToMoveNonExistentDirectory() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("folder1", curDir);
    curDir.addDirectory(dirOne);
    Directory dirTwo = new Directory("folder2", dirOne);
    dirOne.addDirectory(dirTwo);

    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/folder1/");
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(dirOne);

    this.mvCommand = new Mv(this.fs, this.cwd);
    this.mvCommand.setCurrentParameters(2, new String[] {"folder3", "/"});

    String output = this.mvCommand.execute();

    assertEquals(null, output);
  }

  @Test
  public void testMvToOverwriteDirectory() {
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

    this.mvCommand = new Mv(this.fs, this.cwd);
    this.mvCommand.setCurrentParameters(2, new String[] {"folder2", "folder1"});

    this.mvCommand.execute();

    int inCurDir = curDir.findInSubDirectories("folder2");
    int inDirOne = dirOne.findInSubDirectories("folder2");
    int inDirTwo = dirOne.getSubDirectoryFromString("folder2").findInSubDirectories("folder3");

    boolean result = inCurDir == -1 && inDirOne == 0 && inDirTwo == -1;

    assertEquals(true, result);
  }

  @Test
  public void testMvToOverwriteFile() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("folder1", curDir);
    curDir.addDirectory(dirOne);
    curDir.addFile(new File("file1", "hello", curDir));
    dirOne.addFile(new File("file1", "bye", dirOne));

    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);

    this.mvCommand = new Mv(this.fs, this.cwd);
    this.mvCommand.setCurrentParameters(2, new String[] {"file1", "folder1/file1"});

    this.mvCommand.execute();

    File inCurDir = curDir.findInFiles("file1");
    File inDirOne = dirOne.findInFiles("file1");
    String newContents = inDirOne.getContent();

    System.out.println(inCurDir == null);
    System.out.println(inDirOne != null);
    System.out.println(newContents.equals("hello"));

    boolean result = inCurDir == null && inDirOne != null && newContents.equals("hello");

    assertEquals(true, result);
  }

  @Test
  public void testMvToMoveFileIntoDirectoryWithFileOfTheSameName() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("folder1", curDir);
    File file1 = new File("file1", "hello", curDir);
    curDir.addFile(file1);
    File file2 = new File("file1", "bye", dirOne);
    dirOne.addFile(file2);

    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    ((MockCurrentWorkingDirectory) (this.cwd)).setStringReturn("/");
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(curDir);

    this.mvCommand = new Mv(this.fs, this.cwd);
    this.mvCommand.setCurrentParameters(2, new String[] {"file1", "folder1"});

    this.mvCommand.execute();

    File inCurDir = curDir.findInFiles("file1");
    File inDirOne = dirOne.findInFiles("file1");
    String newContents = inDirOne.getContent();

    boolean result = inCurDir == null && inDirOne != null && newContents.equals("bye");

    assertEquals(true, result);
  }
}
