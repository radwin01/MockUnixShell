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
import filesystem.Directory;
import filesystem.File;
import filesystem.FileSystemI;

public class DirectoryTest {

  private Directory root;
  private FileSystemI fs;

  @Before
  public void setup() {
    this.fs = new MockFileSystem();
    this.fs.setRootDir(new Directory("", null));
    this.root = this.fs.getRootDir();
  }

  // TESTING findInFiles
  @Test
  public void testFindInFilesWithValidName() {
    File file3 = new File("file3", null, root);
    root.addFile(new File("file1", null, root));
    root.addFile(new File("file2", null, root));
    root.addFile(file3);
    root.addFile(new File("file4", null, root));
    root.addFile(new File("file5", null, root));

    assertEquals(file3, root.findInFiles("file3"));
  }

  @Test
  public void testFindInFilesWithInvalidName() {
    root.addFile(new File("file1", null, root));
    root.addFile(new File("file2", null, root));

    assertEquals(null, root.findInFiles("file32"));
  }

  @Test
  public void testFindInFilesWithEmptyFileList() {
    assertEquals(null, root.findInFiles("random"));
  }

  // TESTING nameAlreadyExists
  @Test
  public void testNameAlreadyExistsWithValidNameAndExistentName() {
    root.addFile(new File("file1", null, root));
    root.addFile(new File("file2", null, root));
    root.addDirectory(new Directory("folder1", root));
    root.addDirectory(new Directory("folder2", root));
    root.addDirectory(new Directory("folder3", root));

    assertEquals(true, root.nameAlreadyExists("folder2") && root.nameAlreadyExists("file2"));
  }

  @Test
  public void testNameAlreadyExistsWithInvalidName() {
    root.addFile(new File("file1", null, root));
    root.addFile(new File("file2", null, root));
    root.addDirectory(new Directory("folder1", root));
    root.addDirectory(new Directory("folder2", root));
    root.addDirectory(new Directory("folder3", root));

    assertEquals(false, root.nameAlreadyExists("nothere"));
  }

  @Test
  public void testNameAlreadyExistsWithEmptyDirectory() {
    assertEquals(false, root.nameAlreadyExists("random"));
  }

  // TESTING getFileFromDirectory
  @Test
  public void testGetFileFromDirectoryWithValidFileName() {
    File file3 = new File("file3", null, root);
    root.addFile(new File("file1", null, root));
    root.addFile(new File("file2", null, root));
    root.addFile(file3);

    assertEquals(file3, root.getFileFromDirectory("file3"));
  }

  @Test
  public void testGetFileFromDirectoryWithInvalidFileName() {
    root.addFile(new File("file1", null, root));
    root.addFile(new File("file2", null, root));

    assertEquals(null, root.getFileFromDirectory("findMe"));
  }

  @Test
  public void testGetFileFromDirectoryWithEmptyFileList() {
    assertEquals(null, root.getFileFromDirectory("searchMe"));
  }

  // TESTING isOneOfTheParentDirectories
  @Test
  public void testIsOneOfTheParentDirectoriesWithValidPotentialParentDir() {
    Directory folder1 = new Directory("folder1", root);
    root.addDirectory(folder1);
    Directory folder2 = new Directory("fodler2", folder1);
    folder1.addDirectory(folder2);

    assertEquals(true, folder2.isOneOfTheParentDirectories(folder1, fs));
  }

  @Test
  public void testIsOneOfTheParentDirectoriesWithInvalidPotentialParentDir() {
    Directory folder1 = new Directory("folder1", root);
    root.addDirectory(folder1);
    Directory folder2 = new Directory("fodler2", folder1);
    folder1.addDirectory(folder2);

    assertEquals(false, folder1.isOneOfTheParentDirectories(folder2, fs));
  }

  // TESTING getSubDirectoryFromString
  @Test
  public void testGetSubDirectoryFromStringWithValidDirectoryName() {
    root.addDirectory(new Directory("folder1", root));
    root.addDirectory(new Directory("folder2", root));
    root.addDirectory(new Directory("folder3", root));
    Directory toFind = new Directory("findMe", root);
    root.addDirectory(toFind);

    assertEquals(toFind, root.getSubDirectoryFromString("findMe"));
  }

  @Test
  public void testGetSubDirectoryFromStringWithInvalidDirectoryName() {
    root.addDirectory(new Directory("folder1", root));
    root.addDirectory(new Directory("folder2", root));
    root.addDirectory(new Directory("folder3", root));

    assertEquals(null, root.getSubDirectoryFromString("findMe"));
  }

  @Test
  public void testGetSubDirectoryFromStringWithEmptyDirList() {
    assertEquals(null, root.getSubDirectoryFromString("findMe"));
  }

  // TESTING getFullPathString
  @Test
  public void testGetFullPathString() {
    Directory folder1 = new Directory("folder1", root);
    root.addDirectory(folder1);
    Directory folder2 = new Directory("folder2", folder1);
    folder1.addDirectory(folder2);

    assertEquals("/folder1/folder2/", folder2.getFullPathString());
  }

  // TESTING findInSubDirectories
  @Test
  public void testFindInSubDirectoriesWithValidDirName() {
    root.addDirectory(new Directory("folder1", root));
    root.addDirectory(new Directory("folder2", root));
    root.addDirectory(new Directory("folder3", root));

    assertEquals(2, root.findInSubDirectories("folder3"));
  }

  @Test
  public void testFindInSubDirectoriesWithInvalidDirName() {
    root.addDirectory(new Directory("folder1", root));
    root.addDirectory(new Directory("folder2", root));
    root.addDirectory(new Directory("folder3", root));

    assertEquals(-1, root.findInSubDirectories("findMe"));
  }

  @Test
  public void testFindInSubDirectoriesWithEmptyDirList() {
    assertEquals(-1, root.findInSubDirectories("findMe"));
  }

  // TESTING getSubDirectoryByIndex
  @Test
  public void testGetSubDirectoryByIndex() {
    root.addDirectory(new Directory("folder1", root));
    Directory findMe = new Directory("findMe", root);
    root.addDirectory(findMe);
    root.addDirectory(new Directory("folder3", root));

    assertEquals(findMe, root.getSubDirectoryByIndex(1));
  }

}
