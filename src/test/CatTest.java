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
import org.junit.Test;
import org.junit.Before;
import command.Cat;
import filesystem.CurrentWorkingDirectoryI;
import filesystem.Directory;
import filesystem.File;
import filesystem.FileSystemI;

public class CatTest {

  private Cat catCommand;
  private FileSystemI fs;
  private CurrentWorkingDirectoryI cwd;

  @Before
  public void setUp() {
    this.fs = new MockFileSystem();
    this.cwd = new MockCurrentWorkingDirectory();
  }

  @Test
  public void testWithDirectory() {
    Directory rootDir = new Directory("/", null);
    Directory dirOne = new Directory("one", rootDir);
    rootDir.addDirectory(dirOne);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(rootDir);

    this.catCommand = new Cat(this.fs, this.cwd);
    this.catCommand.setCurrentParameters(1, new String[] {"one"});
    String output = this.catCommand.execute();

    assertEquals(null, output);
  }

  @Test
  public void testWithNonexistentFile() {
    Directory rootDir = new Directory("/", null);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(rootDir);
    this.catCommand = new Cat(this.fs, this.cwd);
    this.catCommand.setCurrentParameters(1, new String[] {"one"});
    String output = this.catCommand.execute();

    assertEquals(null, output);
  }

  @Test
  public void testWithOneFileInCurrentDirectory() {
    Directory rootDir = new Directory("/", null);
    File newFile = new File("new", "this is a file!", rootDir);
    rootDir.addFile(newFile);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(rootDir);

    this.catCommand = new Cat(this.fs, this.cwd);
    this.catCommand.setCurrentParameters(1, new String[] {"new"});
    String output = this.catCommand.execute();

    assertEquals("this is a file!\n", output);
  }

  @Test
  public void testWithOneFileWithEndSlashes() {
    Directory rootDir = new Directory("/", null);
    File newFile = new File("new", "this is a file!", rootDir);
    rootDir.addFile(newFile);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(rootDir);

    this.catCommand = new Cat(this.fs, this.cwd);
    this.catCommand.setCurrentParameters(1, new String[] {"new/"});
    String output = this.catCommand.execute();

    assertEquals(null, output);
  }

  @Test
  public void testWithOneFileInDifferentDirectoryUsingRelativePath() {
    Directory rootDir = new Directory("/", null);
    Directory newDir = new Directory("one", rootDir);
    rootDir.addDirectory(newDir);
    File newFile = new File("one", "this is a file!", newDir);
    newDir.addFile(newFile);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(rootDir);

    this.catCommand = new Cat(this.fs, this.cwd);
    this.catCommand.setCurrentParameters(1, new String[] {"one/one"});
    String output = this.catCommand.execute();

    assertEquals("this is a file!\n", output);
  }

  @Test
  public void testWithOneFileInDifferentDirectoryUsingFullPath() {
    Directory rootDir = new Directory("/", null);
    Directory dirOne = new Directory("one", rootDir);
    rootDir.addDirectory(dirOne);
    Directory dirTwo = new Directory("two", dirOne);
    dirOne.addDirectory(dirTwo);
    File newFile = new File("one", "this is a file!\n", dirOne);
    dirOne.addFile(newFile);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(dirTwo);
    ((MockFileSystem) this.fs).setRootDir(rootDir);

    this.catCommand = new Cat(this.fs, this.cwd);
    this.catCommand.setCurrentParameters(1, new String[] {"/one/one"});
    String output = this.catCommand.execute();

    assertEquals("this is a file!\n\n", output);
  }

  @Test
  public void testWithTwoFilesInSameDirectory() {
    Directory rootDir = new Directory("/", null);
    File fileOne = new File("a", "CSCB07", rootDir);
    File fileTwo = new File("b", "CSCB09", rootDir);
    rootDir.addFile(fileOne);
    rootDir.addFile(fileTwo);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(rootDir);

    this.catCommand = new Cat(this.fs, this.cwd);
    this.catCommand.setCurrentParameters(2, new String[] {"a", "b"});
    String output = this.catCommand.execute();

    assertEquals("CSCB07\n\n\n\nCSCB09\n", output);
  }

  @Test
  public void testWithTwoFilesInDifferentDirectories() {
    Directory rootDir = new Directory("/", null);
    Directory dirOne = new Directory("one", rootDir);
    Directory dirTwo = new Directory("two", rootDir);
    rootDir.addDirectory(dirOne);
    rootDir.addDirectory(dirTwo);
    File fileOne = new File("a", "CSCB07", dirOne);
    File fileTwo = new File("b", "CSCB09", dirTwo);
    dirOne.addFile(fileOne);
    dirTwo.addFile(fileTwo);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(rootDir);

    this.catCommand = new Cat(this.fs, this.cwd);
    this.catCommand.setCurrentParameters(2, new String[] {"one/a", "two/b"});
    String output = this.catCommand.execute();

    assertEquals("CSCB07\n\n\n\nCSCB09\n", output);
  }

  @Test
  public void testWithOneFileOneDirectory() {
    Directory rootDir = new Directory("/", null);
    Directory dirOne = new Directory("one", rootDir);
    Directory dirTwo = new Directory("two", rootDir);
    rootDir.addDirectory(dirOne);
    rootDir.addDirectory(dirTwo);
    File fileOne = new File("a", "CSCB07", dirOne);
    dirOne.addFile(fileOne);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(rootDir);

    this.catCommand = new Cat(this.fs, this.cwd);
    this.catCommand.setCurrentParameters(2, new String[] {"one/a", "two"});
    String output = this.catCommand.execute();

    assertEquals("CSCB07\n", output);
  }

  @Test
  public void testWithOneFileOneNonexistent() {
    Directory rootDir = new Directory("/", null);
    Directory dirOne = new Directory("one", rootDir);
    rootDir.addDirectory(dirOne);
    File fileOne = new File("a", "CSCB07", dirOne);
    dirOne.addFile(fileOne);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(dirOne);
    ((MockFileSystem) this.fs).setRootDir(rootDir);

    this.catCommand = new Cat(this.fs, this.cwd);
    this.catCommand.setCurrentParameters(2, new String[] {"../one/a", "../one/b"});
    String output = this.catCommand.execute();

    assertEquals("CSCB07\n", output);
  }

  @Test
  public void testWithSomeValidSomeInvalid() {
    Directory rootDir = new Directory("/", null);
    Directory dirOne = new Directory("one", rootDir);
    Directory dirTwo = new Directory("two", dirOne);
    Directory dirThree = new Directory("three", dirTwo);
    Directory dirFour = new Directory("four", dirOne);
    rootDir.addDirectory(dirOne);
    dirOne.addDirectory(dirFour);
    dirOne.addDirectory(dirTwo);
    dirTwo.addDirectory(dirThree);
    File fileOne = new File("a", "CSCB07", rootDir);
    File fileTwo = new File("b", "CSCB09", dirOne);
    File fileThree = new File("c", "MATB41", dirTwo);
    File fileFour = new File("d", "STAB52", dirThree);
    File fileFive = new File("e", "LINA01", dirFour);
    rootDir.addFile(fileOne);
    dirOne.addFile(fileTwo);
    dirTwo.addFile(fileThree);
    dirThree.addFile(fileFour);
    dirFour.addFile(fileFive);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(rootDir);
    ((MockFileSystem) this.fs).setRootDir(rootDir);

    this.catCommand = new Cat(this.fs, this.cwd);
    this.catCommand.setCurrentParameters(8, new String[] {"a", "lol", "one/four/e", "/one/b",
        "/one/two/three", "one/two/c", "/one/two/three/d", "one"});
    String output = this.catCommand.execute();
    assertEquals("CSCB07\n\n\n\nLINA01\n\n\n\nCSCB09\n\n\n\nMATB41\n\n\n\nSTAB52\n", output);
  }
}
