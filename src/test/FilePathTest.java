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
import filesystem.CurrentWorkingDirectoryI;
import filesystem.Directory;
import filesystem.File;
import filesystem.FilePath;
import filesystem.FileSystemI;

public class FilePathTest {

  private FileSystemI fs;
  private CurrentWorkingDirectoryI cwd;

  @Before
  public void setUp() {
    this.fs = new MockFileSystem();
    this.cwd = new MockCurrentWorkingDirectory();
    this.cwd.setCWD(new Directory("", null));
    this.fs.setRootDir(this.cwd.getCWD());
  }

  // TEST convertAnyPathToFile
  @Test
  public void testConvertAnyPathToFileWithRelativePath() {
    this.cwd.getCWD().addDirectory(new Directory("folder1", this.cwd.getCWD()));
    Directory folder2 = new Directory("folder2", this.cwd.getCWD());
    this.cwd.getCWD().addDirectory(folder2);
    folder2.addDirectory(new Directory("folder3", folder2));
    File file1 = new File("file1", null, folder2);
    folder2.addFile(file1);

    assertEquals(file1, FilePath.convertAnyPathToFile("folder2/file1", this.cwd, this.fs));
  }

  @Test
  public void testConvertAnyPathToFileWithFullPath() {
    this.cwd.getCWD().addDirectory(new Directory("folder1", this.cwd.getCWD()));
    Directory folder2 = new Directory("folder2", this.cwd.getCWD());
    this.cwd.getCWD().addDirectory(folder2);
    folder2.addDirectory(new Directory("folder3", folder2));
    File file1 = new File("file1", null, folder2);
    folder2.addFile(file1);

    this.cwd.setCWD(folder2);

    assertEquals(file1, FilePath.convertAnyPathToFile("/folder2/file1", this.cwd, this.fs));
  }

  @Test
  public void testConvertAnyPathToFileWithDottedFilePath() {
    this.cwd.getCWD().addDirectory(new Directory("folder1", this.cwd.getCWD()));
    Directory folder2 = new Directory("folder2", this.cwd.getCWD());
    this.cwd.getCWD().addDirectory(folder2);
    folder2.addDirectory(new Directory("folder3", folder2));
    File file1 = new File("file1", null, this.cwd.getCWD());
    this.cwd.getCWD().addFile(file1);

    this.cwd.setCWD(folder2);

    assertEquals(file1, FilePath.convertAnyPathToFile("../file1", this.cwd, this.fs));
  }

  @Test
  public void testConvertAnyPathToFileWithInvalidFilePath() {
    this.cwd.getCWD().addDirectory(new Directory("folder1", this.cwd.getCWD()));
    Directory folder2 = new Directory("folder2", this.cwd.getCWD());
    this.cwd.getCWD().addDirectory(folder2);
    folder2.addDirectory(new Directory("folder3", folder2));
    File file1 = new File("file1", null, folder2);
    folder2.addFile(file1);

    this.cwd.setCWD(folder2);

    assertEquals(null, FilePath.convertAnyPathToFile("folder3/notHere/file1", this.cwd, this.fs));
  }

  // TEST convertAnyPathToDirectory
  @Test
  public void testConvertAnyPathToDirectoryWithRelativePath() {
    this.cwd.getCWD().addDirectory(new Directory("folder1", this.cwd.getCWD()));
    Directory folder2 = new Directory("folder2", this.cwd.getCWD());
    this.cwd.getCWD().addDirectory(folder2);
    folder2.addDirectory(new Directory("folder3", folder2));
    Directory findMe = new Directory("findMe", folder2);
    folder2.addDirectory(findMe);

    assertEquals(findMe, FilePath.convertAnyPathToDirectory("folder2/findMe", this.cwd, this.fs));
  }

  @Test
  public void testConvertAnyPathToDirectoryWithFullPath() {
    this.cwd.getCWD().addDirectory(new Directory("folder1", this.cwd.getCWD()));
    Directory folder2 = new Directory("folder2", this.cwd.getCWD());
    this.cwd.getCWD().addDirectory(folder2);
    folder2.addDirectory(new Directory("folder3", folder2));
    Directory findMe = new Directory("findMe", folder2);
    folder2.addDirectory(findMe);

    this.cwd.setCWD(folder2);

    assertEquals(findMe, FilePath.convertAnyPathToDirectory("/folder2/findMe", this.cwd, this.fs));
  }

  @Test
  public void testConvertAnyPathToDirectoryWithDottedFilePath() {
    this.cwd.getCWD().addDirectory(new Directory("folder1", this.cwd.getCWD()));
    Directory folder2 = new Directory("folder2", this.cwd.getCWD());
    this.cwd.getCWD().addDirectory(folder2);
    folder2.addDirectory(new Directory("folder3", folder2));
    Directory findMe = new Directory("findMe", this.cwd.getCWD());
    this.cwd.getCWD().addDirectory(findMe);

    this.cwd.setCWD(folder2);

    assertEquals(findMe, FilePath.convertAnyPathToDirectory("../findMe", this.cwd, this.fs));
  }

  @Test
  public void testConvertAnyPathToDirectoryWithInvalidFilePath() {
    this.cwd.getCWD().addDirectory(new Directory("folder1", this.cwd.getCWD()));
    Directory folder2 = new Directory("folder2", this.cwd.getCWD());
    this.cwd.getCWD().addDirectory(folder2);
    folder2.addDirectory(new Directory("folder3", folder2));

    this.cwd.setCWD(folder2);

    assertEquals(null,
        FilePath.convertAnyPathToDirectory("folder3/notHere/file1", this.cwd, this.fs));
  }

  // TEST convertDotToFullPath
  @Test
  public void testConvertDotToFullPathMultipleDots() {
    this.cwd.getCWD().addDirectory(new Directory("folder1", this.cwd.getCWD()));
    Directory folder2 = new Directory("folder2", this.cwd.getCWD());
    this.cwd.getCWD().addDirectory(folder2);
    Directory folder3 = new Directory("folder3", folder2);
    folder2.addDirectory(folder3);
    folder3.addDirectory(new Directory("folder4", folder3));

    this.cwd.setCWD(folder3);

    assertEquals("/", FilePath.convertDotToFullPath("../../", this.cwd));
  }

  @Test
  public void testConvertDotToFullPathSingleDot() {
    this.cwd.getCWD().addDirectory(new Directory("folder1", this.cwd.getCWD()));
    Directory folder2 = new Directory("folder2", this.cwd.getCWD());
    this.cwd.getCWD().addDirectory(folder2);
    Directory folder3 = new Directory("folder3", folder2);
    folder2.addDirectory(folder3);
    folder3.addDirectory(new Directory("folder4", folder3));

    this.cwd.setCWD(folder3);

    assertEquals("/folder2/", FilePath.convertDotToFullPath("../", this.cwd));
  }

  // TEST convertDotToCurrentDir
  @Test
  public void testConvertDotToCurrentDirMultipleDots() {
    this.cwd.getCWD().addDirectory(new Directory("folder1", this.cwd.getCWD()));
    Directory folder2 = new Directory("folder2", this.cwd.getCWD());
    this.cwd.getCWD().addDirectory(folder2);
    Directory folder3 = new Directory("folder3", folder2);
    folder2.addDirectory(folder3);
    folder3.addDirectory(new Directory("folder4", folder3));

    this.cwd.setCWD(folder3);

    assertEquals(this.fs.getRootDir(), FilePath.convertDotToCurrentDir("../../", this.cwd));
  }

  @Test
  public void testConvertDotToCurrentDirSingleDot() {
    this.cwd.getCWD().addDirectory(new Directory("folder1", this.cwd.getCWD()));
    Directory folder2 = new Directory("folder2", this.cwd.getCWD());
    this.cwd.getCWD().addDirectory(folder2);
    Directory folder3 = new Directory("folder3", folder2);
    folder2.addDirectory(folder3);
    folder3.addDirectory(new Directory("folder4", folder3));

    this.cwd.setCWD(folder3);

    assertEquals(folder2, FilePath.convertDotToCurrentDir("../", this.cwd));
  }

  // TEST convertDotToDir
  @Test
  public void testConvertDotToDirWithSingularDot() {
    this.cwd.getCWD().addDirectory(new Directory("folder1", this.cwd.getCWD()));
    Directory folder2 = new Directory("folder2", this.cwd.getCWD());
    this.cwd.getCWD().addDirectory(folder2);
    Directory folder3 = new Directory("folder3", folder2);
    folder2.addDirectory(folder3);

    this.cwd.setCWD(folder2);

    assertEquals(this.cwd.getCWD(), FilePath.convertDotToDir(".", this.cwd.getCWD()));
  }

  @Test
  public void testConvertDotToDirWithDoubleDot() {
    this.cwd.getCWD().addDirectory(new Directory("folder1", this.cwd.getCWD()));
    Directory folder2 = new Directory("folder2", this.cwd.getCWD());
    this.cwd.getCWD().addDirectory(folder2);
    Directory folder3 = new Directory("folder3", folder2);
    folder2.addDirectory(folder3);

    this.cwd.setCWD(folder3);

    assertEquals(folder2, FilePath.convertDotToDir("..", this.cwd.getCWD()));
  }

  @Test
  public void testConvertDotToDirOnRootDir() {
    assertEquals(this.cwd.getCWD(), FilePath.convertDotToDir("..", this.cwd.getCWD()));
  }

  @Test
  public void testConvertDotToDirWithoutDottedString() {
    this.cwd.getCWD().addDirectory(new Directory("folder1", this.cwd.getCWD()));
    Directory folder2 = new Directory("folder2", this.cwd.getCWD());
    this.cwd.getCWD().addDirectory(folder2);
    Directory folder3 = new Directory("folder3", folder2);
    folder2.addDirectory(folder3);

    assertEquals(null, FilePath.convertDotToDir("invalid", this.cwd.getCWD()));
  }

  // TEST getEndDirectoryfromPath
  @Test
  public void testGetEndDirectoryFromPathWithRelativePath() {
    this.cwd.getCWD().addDirectory(new Directory("folder1", this.cwd.getCWD()));
    Directory folder2 = new Directory("folder2", this.cwd.getCWD());
    this.cwd.getCWD().addDirectory(folder2);
    Directory folder3 = new Directory("folder3", folder2);
    folder2.addDirectory(folder3);
    Directory folder4 = new Directory("folder4", folder3);
    folder3.addDirectory(folder4);

    this.cwd.setCWD(folder2);

    String[] pathList = "folder3/folder4".split("/");

    assertEquals(folder3, FilePath.getEndDirectoryfromPath(pathList, this.cwd, this.fs));
  }

  @Test
  public void testGetEndDirectoryFromPathWithFullPath() {
    this.cwd.getCWD().addDirectory(new Directory("folder1", this.cwd.getCWD()));
    Directory folder2 = new Directory("folder2", this.cwd.getCWD());
    this.cwd.getCWD().addDirectory(folder2);
    Directory folder3 = new Directory("folder3", folder2);
    folder2.addDirectory(folder3);
    Directory folder4 = new Directory("folder4", folder3);
    folder3.addDirectory(folder4);

    String[] pathList = "/folder2/folder3/folder4".split("/");

    assertEquals(folder3, FilePath.getEndDirectoryfromPath(pathList, this.cwd, this.fs));
  }

  @Test
  public void testGetEndDirectoryFromPathWithFullDottedPath() {
    this.cwd.getCWD().addDirectory(new Directory("folder1", this.cwd.getCWD()));
    Directory folder2 = new Directory("folder2", this.cwd.getCWD());
    this.cwd.getCWD().addDirectory(folder2);
    Directory folder3 = new Directory("folder3", folder2);
    folder2.addDirectory(folder3);
    Directory folder4 = new Directory("folder4", folder3);
    folder3.addDirectory(folder4);

    this.cwd.setCWD(folder3);

    String[] pathList = "../../".split("/");

    assertEquals(folder2, FilePath.getEndDirectoryfromPath(pathList, this.cwd, this.fs));
  }

  @Test
  public void testGetEndDirectoryFromPathWithPartialDottedPath() {
    this.cwd.getCWD().addDirectory(new Directory("folder1", this.cwd.getCWD()));
    Directory folder2 = new Directory("folder2", this.cwd.getCWD());
    this.cwd.getCWD().addDirectory(folder2);
    Directory folder3 = new Directory("folder3", folder2);
    folder2.addDirectory(folder3);
    Directory folder4 = new Directory("folder4", folder3);
    folder3.addDirectory(folder4);

    this.cwd.setCWD(folder3);

    String[] pathList = "../../folder1".split("/");

    assertEquals(this.fs.getRootDir(),
        FilePath.getEndDirectoryfromPath(pathList, this.cwd, this.fs));
  }

  @Test
  public void testGetEndDirectoryFromPathWithSinglePath() {
    this.cwd.getCWD().addDirectory(new Directory("folder1", this.cwd.getCWD()));
    Directory folder2 = new Directory("folder2", this.cwd.getCWD());
    this.cwd.getCWD().addDirectory(folder2);
    Directory folder3 = new Directory("folder3", folder2);
    folder2.addDirectory(folder3);
    Directory folder4 = new Directory("folder4", folder3);
    folder3.addDirectory(folder4);

    this.cwd.setCWD(folder3);

    String[] pathList = "folder4".split("/");

    assertEquals(folder3, FilePath.getEndDirectoryfromPath(pathList, this.cwd, this.fs));
  }

  @Test
  public void testGetEndDirectoryFromPathWithInvalidPath() {
    this.cwd.getCWD().addDirectory(new Directory("folder1", this.cwd.getCWD()));
    Directory folder2 = new Directory("folder2", this.cwd.getCWD());
    this.cwd.getCWD().addDirectory(folder2);
    Directory folder3 = new Directory("folder3", folder2);
    folder2.addDirectory(folder3);
    Directory folder4 = new Directory("folder4", folder3);
    folder3.addDirectory(folder4);

    this.cwd.setCWD(folder2);

    String[] pathList = "../invalid/notHere".split("/");

    assertEquals(null, FilePath.getEndDirectoryfromPath(pathList, this.cwd, this.fs));
  }

  @Test
  public void testGetEndDirectoryFromPathOnRootDir() {
    String[] pathList = "..".split("/");

    assertEquals(this.fs.getRootDir(),
        FilePath.getEndDirectoryfromPath(pathList, this.cwd, this.fs));
  }

  // TEST isAllStringBetweenSlash
  @Test
  public void testIsAllStringBetweenSlashWithConsistentStringBetweenSlash() {
    assertEquals(true, FilePath.isAllStringBetweenSlash("same/same/same/same/", "same"));
  }

  @Test
  public void testIsAllStringBetweenSlashWithInconsistentStringBetweenSlash() {
    assertEquals(false, FilePath.isAllStringBetweenSlash("same/same/different/same/", "same"));
  }

  @Test
  public void testIsAllStringBetweenSlashWithEmptyString() {
    assertEquals(true, FilePath.isAllStringBetweenSlash("", "findMe"));
  }

  // TEST isAllChar
  @Test
  public void testIsAllCharWithConsistentChar() {
    assertEquals(true, FilePath.isAllChar("ppppppppppppppppppp", 'p'));
  }

  @Test
  public void testIsAllCharWithInconsistentChar() {
    assertEquals(false, FilePath.isAllChar("ppppppqpppppppppppp", 'p'));
  }

  @Test
  public void testIsAllCharWithEmptyString() {
    assertEquals(true, FilePath.isAllChar("", 'p'));
  }

  // Test isFullyDotted
  @Test
  public void testIsFullyDottedWithConsistentDots() {
    String[] pathSplit = "../../../../../".split("/");

    assertEquals(true, FilePath.isFullyDotted(pathSplit));
  }

  @Test
  public void testIsFullyDottedWithInconsistentDots() {
    String[] pathSplit = "../../../notADot/../".split("/");

    assertEquals(false, FilePath.isFullyDotted(pathSplit));
  }

  @Test
  public void testIsFullyDottedWithEmptyPath() {
    String[] pathSplit = "".split("/");

    assertEquals(false, FilePath.isFullyDotted(pathSplit));
  }
}
