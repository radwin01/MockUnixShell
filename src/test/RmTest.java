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
import command.Rm;
import filesystem.CurrentWorkingDirectoryI;
import filesystem.Directory;
import filesystem.FileSystemI;

public class RmTest {

  private Rm rmCommand;
  private FileSystemI fs;
  private CurrentWorkingDirectoryI cwd;

  @Before
  public void setUp() {
    this.fs = new MockFileSystem();
    this.cwd = new MockCurrentWorkingDirectory();
  }


  @Test
  public void testRemovingDirectoryInRoot() {
    Directory root = new Directory("", null);
    Directory testDirec = new Directory("testfolder1", root);
    root.addDirectory(testDirec);
    ((MockCurrentWorkingDirectory) this.cwd).setDirReturn(root);
    ((MockCurrentWorkingDirectory) this.cwd).setStringReturn("/");
    ((MockFileSystem) this.fs).setRootDir(root);

    this.rmCommand = new Rm(this.fs, this.cwd);
    this.rmCommand.setCurrentParameters(1, new String[] {"testfolder1"});

    String output = rmCommand.execute();

    assertEquals("testfolder1 deleted", output);
  }

  @Test
  public void testRemovingInvalidDirectory() {
    Directory root = new Directory("", null);
    Directory testDirec = new Directory("testfolder1", root);
    root.addDirectory(testDirec);
    ((MockCurrentWorkingDirectory) this.cwd).setDirReturn(root);
    ((MockCurrentWorkingDirectory) this.cwd).setStringReturn("/");
    ((MockFileSystem) this.fs).setRootDir(root);

    this.rmCommand = new Rm(this.fs, this.cwd);
    this.rmCommand.setCurrentParameters(1, new String[] {"testfolder2"});

    String output = rmCommand.execute();

    assertEquals(null, output);
  }

  @Test
  public void testRemovingDirectoryInSubdirecWithRelativePath() {
    Directory root = new Directory("", null);
    Directory testDirec1 = new Directory("testfolder1", root);
    Directory testDirec2 = new Directory("insidefolder1", testDirec1);
    testDirec1.addDirectory(testDirec2);
    root.addDirectory(testDirec1);
    ((MockCurrentWorkingDirectory) this.cwd).setDirReturn(root);
    ((MockCurrentWorkingDirectory) this.cwd).setStringReturn("/");
    ((MockFileSystem) this.fs).setRootDir(root);

    this.rmCommand = new Rm(this.fs, this.cwd);
    this.rmCommand.setCurrentParameters(1, new String[] {"testfolder2"});

    String output = rmCommand.execute();

    assertEquals(null, output);
  }

  @Test
  public void testRemovingDirectoryInSubdirecWithAbsolutePath() {
    Directory root = new Directory("", null);
    Directory testDirec1 = new Directory("testfolder1", root);
    Directory testDirec2 = new Directory("insidefolder1", testDirec1);
    testDirec1.addDirectory(testDirec2);
    root.addDirectory(testDirec1);
    ((MockCurrentWorkingDirectory) this.cwd).setDirReturn(root);
    ((MockCurrentWorkingDirectory) this.cwd).setStringReturn("/");
    ((MockFileSystem) this.fs).setRootDir(root);

    this.rmCommand = new Rm(this.fs, this.cwd);
    this.rmCommand.setCurrentParameters(1, new String[] {"testfolder1/insidefolder1"});

    String output = rmCommand.execute();

    assertEquals("insidefolder1 deleted", output);
  }

  @Test
  public void testRemovingDirectoryContainingCWD() {
    Directory root = new Directory("", null);
    Directory testDirec1 = new Directory("testfolder1", root);
    Directory testDirec2 = new Directory("insidefolder1", testDirec1);
    testDirec1.addDirectory(testDirec2);
    root.addDirectory(testDirec1);
    ((MockCurrentWorkingDirectory) this.cwd).setDirReturn(testDirec2);
    ((MockCurrentWorkingDirectory) this.cwd).setStringReturn("/testfolder1/insidefolder1/");
    ((MockFileSystem) this.fs).setRootDir(root);

    this.rmCommand = new Rm(this.fs, this.cwd);
    this.rmCommand.setCurrentParameters(1, new String[] {"/testfolder1"});

    String output = rmCommand.execute();

    assertEquals(null, output);
  }


}
