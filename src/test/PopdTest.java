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
import command.Popd;
import filesystem.CurrentWorkingDirectoryI;
import filesystem.Directory;
import filesystem.FileSystemI;
import filesystem.ItemList;
import filesystem.DirectoryStack;

public class PopdTest {

  private Popd popdCommand;
  private FileSystemI fs;
  private CurrentWorkingDirectoryI cwd;
  private DirectoryStack dirStack;

  @Before
  public void setUp() {
    this.fs = new MockFileSystem();
    this.cwd = new MockCWDPopAndPush();
    this.dirStack = new DirectoryStack();
  }
  
  @Test
  public void testPopWithNonEmptyDirectoryStack() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("folder1", curDir);
    curDir.addDirectory(dirOne);

    ItemList<Directory> testList = this.dirStack.getDirStack();
    testList.add(dirOne);

    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    this.cwd.setCWD(curDir);
    
    this.popdCommand = new Popd(this.fs, this.cwd, dirStack);
    this.popdCommand.setCurrentParameters(0, new String[] {});
    this.popdCommand.execute();
    
    boolean result = dirStack.getDirStack().size() == 0 && this.cwd.getCWD().equals(dirOne);
    
    assertEquals(true, result);
  }
  
  @Test
  public void testPopWithEmptyDirectoryStack() {
    Directory curDir = new Directory("", null);

    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    this.cwd.setCWD(curDir);
    
    this.popdCommand = new Popd(this.fs, this.cwd, dirStack);
    this.popdCommand.setCurrentParameters(0, new String[] {});
    this.popdCommand.execute();
    
    boolean result = dirStack.getDirStack().size() == 0 && this.cwd.getCWD().equals(curDir);
    
    assertEquals(true, result);
  }
  
  @Test
  public void testPopWithMultiplePopsInARowUntilEmpty() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("folder1", curDir);
    curDir.addDirectory(dirOne);
    Directory dirTwo = new Directory("folder2", dirOne);
    dirOne.addDirectory(dirTwo);
    Directory dirThree = new Directory("folder3", curDir);
    curDir.addDirectory(dirThree);
    Directory dirFour = new Directory("folder4", curDir);
    curDir.addDirectory(dirFour);

    ItemList<Directory> testList = this.dirStack.getDirStack();
    testList.add(dirOne);
    testList.add(dirThree);
    testList.add(curDir);
    testList.add(dirFour);
    testList.add(dirTwo);
    testList.add(curDir);

    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    this.cwd.setCWD(dirFour);
    
    this.popdCommand = new Popd(this.fs, this.cwd, dirStack);
    this.popdCommand.setCurrentParameters(0, new String[] {});
    
    for (int i = 0; i < 10; i++) {
      this.popdCommand.execute();
    }
    
    boolean result = dirStack.getDirStack().size() == 0 && this.cwd.getCWD().equals(dirOne);
    
    assertEquals(true, result);
  }
  
  @Test
  public void testPopWithMultiplePopsInARow() {
    Directory curDir = new Directory("", null);
    Directory dirOne = new Directory("folder1", curDir);
    curDir.addDirectory(dirOne);
    Directory dirTwo = new Directory("folder2", dirOne);
    dirOne.addDirectory(dirTwo);
    Directory dirThree = new Directory("folder3", curDir);
    curDir.addDirectory(dirThree);
    Directory dirFour = new Directory("folder4", curDir);
    curDir.addDirectory(dirFour);

    ItemList<Directory> testList = this.dirStack.getDirStack();
    testList.add(dirFour);
    testList.add(dirThree);
    testList.add(curDir);
    testList.add(dirFour);

    ((MockFileSystem) (this.fs)).setRootDir(curDir);
    this.cwd.setCWD(dirFour);
    
    this.popdCommand = new Popd(this.fs, this.cwd, dirStack);
    this.popdCommand.setCurrentParameters(0, new String[] {});
    
    for (int i = 0; i < 2; i++) {
      this.popdCommand.execute();
    }
    
    boolean result = dirStack.getDirStack().size() == 2 && this.cwd.getCWD().equals(curDir);
    
    assertEquals(true, result);
  }
}
