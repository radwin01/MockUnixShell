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
import command.Tree;
import filesystem.CurrentWorkingDirectoryI;
import filesystem.Directory;
import filesystem.File;
import filesystem.FileSystemI;

public class TreeTest {

  private Tree treeCommand;
  private FileSystemI fs;
  private CurrentWorkingDirectoryI cwd;

  @Before
  public void setUp() {
    this.fs = new MockFileSystem();
    this.cwd = new MockCurrentWorkingDirectory();
  }

  @Test
  public void testOnlyRootDirectory() {
    Directory root = new Directory("", null);
    ((MockCurrentWorkingDirectory) this.cwd).setDirReturn(root);
    ((MockFileSystem) this.fs).setRootDir(root);

    this.treeCommand = new Tree(this.fs, this.cwd);
    this.treeCommand.setCurrentParameters(0, new String[] {});
    String output = this.treeCommand.execute();

    assertEquals("/\n", output);
  }

  @Test
  public void testRootWithOneSubDirec() {
    Directory root = new Directory("", null);
    Directory direcA = new Directory("A", root);
    root.addDirectory(direcA);
    ((MockCurrentWorkingDirectory) this.cwd).setDirReturn(root);
    ((MockFileSystem) this.fs).setRootDir(root);

    this.treeCommand = new Tree(this.fs, this.cwd);
    this.treeCommand.setCurrentParameters(0, new String[] {});
    String output = this.treeCommand.execute();

    assertEquals("/\n\tA\n", output);
  }

  @Test
  public void testRootWithMultipleSubDirec() {
    Directory root = new Directory("", null);
    Directory direcA = new Directory("A", root);
    root.addDirectory(direcA);
    Directory direcB = new Directory("B", root);
    root.addDirectory(direcB);
    ((MockCurrentWorkingDirectory) this.cwd).setDirReturn(root);
    ((MockFileSystem) this.fs).setRootDir(root);

    this.treeCommand = new Tree(this.fs, this.cwd);
    this.treeCommand.setCurrentParameters(0, new String[] {});
    String output = this.treeCommand.execute();

    assertEquals("/\n\tA\n\tB\n", output);
  }

  @Test
  public void testRootWithChildAndGrandChildDirec() {
    Directory root = new Directory("", null);
    Directory direcA = new Directory("A", root);
    root.addDirectory(direcA);
    Directory direcB = new Directory("B", root);
    root.addDirectory(direcB);
    Directory direcC = new Directory("C", direcB);
    direcB.addDirectory(direcC);
    ((MockCurrentWorkingDirectory) this.cwd).setDirReturn(root);
    ((MockFileSystem) this.fs).setRootDir(root);

    this.treeCommand = new Tree(this.fs, this.cwd);
    this.treeCommand.setCurrentParameters(0, new String[] {});
    String output = this.treeCommand.execute();

    assertEquals("/\n\tA\n\tB\n\t\tC\n", output);
  }

  @Test
  public void testRootWithTwoChildAndChildWithTwoChildren() {
    Directory root = new Directory("", null);
    Directory direcA = new Directory("A", root);
    root.addDirectory(direcA);
    Directory direcB = new Directory("B", root);
    root.addDirectory(direcB);
    Directory direcB1 = new Directory("B1", direcB);
    direcB.addDirectory(direcB1);
    Directory direcB2 = new Directory("B2", direcB);
    direcB.addDirectory(direcB2);
    ((MockCurrentWorkingDirectory) this.cwd).setDirReturn(root);
    ((MockFileSystem) this.fs).setRootDir(root);

    this.treeCommand = new Tree(this.fs, this.cwd);
    this.treeCommand.setCurrentParameters(0, new String[] {});
    String output = this.treeCommand.execute();

    assertEquals("/\n\tA\n\tB\n\t\tB1\n\t\tB2\n", output);
  }


  @Test
  public void testRootWithChildrenAndGrandChildAndRootWithFile() {
    Directory root = new Directory("", null);
    Directory direcA = new Directory("A", root);
    root.addDirectory(direcA);
    Directory direcB = new Directory("B", root);
    root.addDirectory(direcB);
    Directory direcC = new Directory("C", direcB);
    direcB.addDirectory(direcC);
    File fileInRoot = new File("rootFile", "content", root);
    root.addFile(fileInRoot);
    ((MockCurrentWorkingDirectory) this.cwd).setDirReturn(root);
    ((MockFileSystem) this.fs).setRootDir(root);

    this.treeCommand = new Tree(this.fs, this.cwd);
    this.treeCommand.setCurrentParameters(0, new String[] {});
    String output = this.treeCommand.execute();

    assertEquals("/\n\tA\n\tB\n\t\tC\n\trootFile\n", output);
  }

}
