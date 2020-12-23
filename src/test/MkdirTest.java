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
import command.Mkdir;
import filesystem.CurrentWorkingDirectoryI;
import filesystem.Directory;
import filesystem.FileSystemI;

public class MkdirTest {

  private Mkdir mkdirCommand;
  private FileSystemI fs;
  private CurrentWorkingDirectoryI cwd;


  @Before
  public void setUp() {
    this.fs = new MockFileSystem();
    this.cwd = new MockCurrentWorkingDirectory();
  }

  @Test
  public void testCreatingDirectoryInRoot() {

    Directory root = new Directory("", null);

    ((MockCurrentWorkingDirectory) this.cwd).setDirReturn(root);
    ((MockFileSystem) this.fs).setRootDir(root);

    this.mkdirCommand = new Mkdir(this.fs, this.cwd);
    this.mkdirCommand.setCurrentParameters(1, new String[] {"testfolder1"});

    String output = this.mkdirCommand.execute();
    assertEquals("testfolder1 ", output);
  }

  @Test
  public void testCreatingIllegalDirectoryInRoot() {

    Directory root = new Directory("", null);

    ((MockCurrentWorkingDirectory) this.cwd).setDirReturn(root);
    ((MockFileSystem) this.fs).setRootDir(root);

    this.mkdirCommand = new Mkdir(this.fs, this.cwd);
    this.mkdirCommand.setCurrentParameters(1, new String[] {"."});

    String output = this.mkdirCommand.execute();
    assertEquals(null, output);
  }

  @Test
  public void testCreatingSubdirecWithAbsolutePath() {

    Directory root = new Directory("", null);
    Directory testDirec = new Directory("testfolder1", root);
    root.addDirectory(testDirec);

    ((MockCurrentWorkingDirectory) this.cwd).setDirReturn(root);
    ((MockFileSystem) this.fs).setRootDir(root);

    this.mkdirCommand = new Mkdir(this.fs, this.cwd);
    this.mkdirCommand.setCurrentParameters(1, new String[] {"testfolder1/testfolder2"});

    String output = this.mkdirCommand.execute();
    assertEquals("testfolder2 ", output);
  }

  @Test
  public void testCreatingDirectoryWithDuplicateName() {

    Directory root = new Directory("", null);
    Directory testDirec = new Directory("testfolder1", root);
    root.addDirectory(testDirec);

    ((MockCurrentWorkingDirectory) this.cwd).setDirReturn(root);
    ((MockFileSystem) this.fs).setRootDir(root);

    this.mkdirCommand = new Mkdir(this.fs, this.cwd);
    this.mkdirCommand.setCurrentParameters(1, new String[] {"testfolder1"});

    String output = this.mkdirCommand.execute();
    assertEquals(null, output);
  }

  @Test
  public void testCreatingMultipleDirectoriesInRootTogether() {

    Directory root = new Directory("", null);

    ((MockCurrentWorkingDirectory) this.cwd).setDirReturn(root);
    ((MockFileSystem) this.fs).setRootDir(root);

    this.mkdirCommand = new Mkdir(this.fs, this.cwd);
    this.mkdirCommand.setCurrentParameters(2, new String[] {"testfolder1", "testfolder2"});

    String output = this.mkdirCommand.execute();
    assertEquals("testfolder1 testfolder2 ", output);
  }

  @Test
  public void testCreatingMultipleDirectoriesOneInvalidDirectory() {

    Directory root = new Directory("", null);

    ((MockCurrentWorkingDirectory) this.cwd).setDirReturn(root);
    ((MockFileSystem) this.fs).setRootDir(root);

    this.mkdirCommand = new Mkdir(this.fs, this.cwd);
    this.mkdirCommand.setCurrentParameters(2, new String[] {"testfolder1", "."});

    String output = this.mkdirCommand.execute();
    assertEquals(null, output);
  }

  @Test
  public void testCreatingDirectoryInSubdirecWithRelativePath() {

    Directory root = new Directory("", null);
    Directory testDirec = new Directory("testfolder1", root);
    root.addDirectory(testDirec);

    ((MockCurrentWorkingDirectory) this.cwd).setDirReturn(testDirec);
    ((MockFileSystem) this.fs).setRootDir(root);

    this.mkdirCommand = new Mkdir(this.fs, this.cwd);
    this.mkdirCommand.setCurrentParameters(1, new String[] {"insidetestfolder1"});

    String output = this.mkdirCommand.execute();
    assertEquals("insidetestfolder1 ", output);
  }

  @Test
  public void testCreatingMultipleDirectoriesUsingRelativePath() {

    Directory root = new Directory("", null);
    Directory testDirec = new Directory("testfolder1", root);
    root.addDirectory(testDirec);

    ((MockCurrentWorkingDirectory) this.cwd).setDirReturn(testDirec);
    ((MockFileSystem) this.fs).setRootDir(root);

    this.mkdirCommand = new Mkdir(this.fs, this.cwd);
    this.mkdirCommand.setCurrentParameters(2, new String[] {"insidetestfolder1", "alsoinside"});

    String output = this.mkdirCommand.execute();
    assertEquals("insidetestfolder1 alsoinside ", output);
  }

  @Test
  public void testCreatingOneDirectoryInSubdirecOneInParent() {

    Directory root = new Directory("", null);
    Directory testDirec = new Directory("testfolder1", root);
    root.addDirectory(testDirec);

    ((MockCurrentWorkingDirectory) this.cwd).setDirReturn(testDirec);
    ((MockFileSystem) this.fs).setRootDir(root);

    this.mkdirCommand = new Mkdir(this.fs, this.cwd);
    this.mkdirCommand.setCurrentParameters(2,
        new String[] {"insidetestfolder1", "../outsidefolder1"});

    String output = this.mkdirCommand.execute();
    assertEquals("insidetestfolder1 outsidefolder1 ", output);
  }

  @Test
  public void testCreatingOneValidDirectoryOneAllSlash() {

    Directory root = new Directory("", null);
    Directory testDirec = new Directory("testfolder1", root);
    root.addDirectory(testDirec);

    ((MockCurrentWorkingDirectory) this.cwd).setDirReturn(testDirec);
    ((MockFileSystem) this.fs).setRootDir(root);

    this.mkdirCommand = new Mkdir(this.fs, this.cwd);
    this.mkdirCommand.setCurrentParameters(2, new String[] {"insidetestfolder1", "////"});

    String output = this.mkdirCommand.execute();
    assertEquals(null, output);
  }

  @Test
  public void testCreatingOneDirectoryInRootOneInSubdirecWithAbsolutePath() {

    Directory root = new Directory("", null);
    Directory testDirec = new Directory("testfolder1", root);
    root.addDirectory(testDirec);

    ((MockCurrentWorkingDirectory) this.cwd).setDirReturn(root);
    ((MockFileSystem) this.fs).setRootDir(root);

    this.mkdirCommand = new Mkdir(this.fs, this.cwd);
    this.mkdirCommand.setCurrentParameters(2,
        new String[] {"insidetheroot", "testfolder1/insidefolder1"});

    String output = this.mkdirCommand.execute();
    assertEquals("insidetheroot insidefolder1 ", output);
  }


}
