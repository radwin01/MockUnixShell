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
import filesystem.CurrentWorkingDirectory;
import filesystem.Directory;

public class CurrentWorkingDirectoryTest {

  private CurrentWorkingDirectory cwd;

  @Before
  public void setup() {
    this.cwd = new CurrentWorkingDirectory(new Directory("", null));
  }

  @Test
  public void testSetToSubDirectoryByStringWithValidName() {
    this.cwd.getCWD().addDirectory(new Directory("folder1", this.cwd.getCWD()));
    this.cwd.getCWD().addDirectory(new Directory("folder2", this.cwd.getCWD()));
    Directory findMe = new Directory("findMe", this.cwd.getCWD());
    this.cwd.getCWD().addDirectory(findMe);
    this.cwd.getCWD().addDirectory(new Directory("folder4", this.cwd.getCWD()));

    this.cwd.setToSubDirectoryByString("findMe");

    assertEquals(findMe, this.cwd.getCWD());
  }

  @Test
  public void testSetToSubDirectoryByStringWithInvalidName() {
    Directory before = this.cwd.getCWD();

    this.cwd.getCWD().addDirectory(new Directory("folder1", this.cwd.getCWD()));
    this.cwd.getCWD().addDirectory(new Directory("folder2", this.cwd.getCWD()));
    this.cwd.getCWD().addDirectory(new Directory("folder3", this.cwd.getCWD()));

    this.cwd.setToSubDirectoryByString("findMe");

    assertEquals(before, this.cwd.getCWD());
  }

  @Test
  public void testSetToSubDirectoryByStringWithEmptySubDirectoryList() {
    Directory before = this.cwd.getCWD();

    this.cwd.setToSubDirectoryByString("notHere");

    assertEquals(before, this.cwd.getCWD());
  }

}
