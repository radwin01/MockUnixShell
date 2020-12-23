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
import command.Curl;
import filesystem.CurrentWorkingDirectoryI;
import filesystem.Directory;
import filesystem.File;
import filesystem.FileSystemI;

public class CurlTest {

  private Curl curlCommand;
  private FileSystemI fs;
  private CurrentWorkingDirectoryI cwd;

  @Before
  public void setUp() {
    this.fs = new MockFileSystem();
    this.cwd = new MockCurrentWorkingDirectory();
  }

  @Test
  public void testWithNonWebsite() {
    Directory rootDir = new Directory("/", null);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(rootDir);

    this.curlCommand = new Curl(this.fs, this.cwd, this.cwd.getCWD());
    this.curlCommand.setCurrentParameters(1, new String[] {"hello"});
    String output = this.curlCommand.execute();
    assertEquals(null, output);
  }

  @Test
  public void testWithWebsiteWithNoFile() {
    Directory rootDir = new Directory("/", null);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(rootDir);

    this.curlCommand = new Curl(this.fs, this.cwd, this.cwd.getCWD());
    this.curlCommand.setCurrentParameters(1, new String[] {"https://www.google.com/"});
    String output = this.curlCommand.execute();
    assertEquals(null, output);
  }

  @Test
  public void testWithWebsiteWithValidTxtFile() {
    Directory rootDir = new Directory("/", null);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(rootDir);

    this.curlCommand = new Curl(this.fs, this.cwd, this.cwd.getCWD());
    this.curlCommand.setCurrentParameters(1,
        new String[] {"https://www.cs.utoronto.ca/~trebla/CSCB09-2020-Summer/a3/violinists.txt"});
    String output = this.curlCommand.execute();
    File resultantFile = rootDir.findInFiles("violiniststxt");
    String resultantFileContents = resultantFile.getContent();
    String actualContents = "Eddy\nHilary\nEddy\nBrett\nHilary\nZiyu\nRay\nBrett\n";

    boolean result = (output.equals("") && resultantFile != null && actualContents.equals(resultantFileContents));
    assertEquals(true, result);
  }

  @Test
  public void testWithWebsiteWithValidTxtFileAlreadyExistingInCWD() {
    Directory rootDir = new Directory("/", null);
    File f = new File("violiniststxt", "Hello", rootDir);
    rootDir.addFile(f);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(rootDir);

    this.curlCommand = new Curl(this.fs, this.cwd, this.cwd.getCWD());
    this.curlCommand.setCurrentParameters(1,
        new String[] {"https://www.cs.utoronto.ca/~trebla/CSCB09-2020-Summer/a3/violinists.txt"});
    String output = this.curlCommand.execute();
    String actualContents = "Eddy\nHilary\nEddy\nBrett\nHilary\nZiyu\nRay\nBrett\n";

    boolean result = (output.equals("") && f.getContent().equals(actualContents));
    assertEquals(true, result);
  }

  @Test
  public void testWithRandomWebsiteWithRandomTxtFile() {
    Directory rootDir = new Directory("/", null);
    ((MockCurrentWorkingDirectory) (this.cwd)).setDirReturn(rootDir);

    this.curlCommand = new Curl(this.fs, this.cwd, this.cwd.getCWD());
    this.curlCommand.setCurrentParameters(1, new String[] {"https://www.cscb07.com/isfun.txt"});
    String output = this.curlCommand.execute();
    assertEquals(null, output);
  }
}
