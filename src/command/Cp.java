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
package command;

import filesystem.CurrentWorkingDirectoryI;
import filesystem.Directory;
import filesystem.File;
import filesystem.FilePath;
import filesystem.FileSystemI;

/**
 * Represents a Cp command.
 * 
 * @version 1.0
 */
public class Cp extends Command {

  /**
   * Creates an instance of a Cp object. Sets the minimum and maximum number of parameters that the
   * user can enter.
   * 
   * @param fs the current FileSystem in use
   * @param cwd the current CWD in use
   */
  public Cp(FileSystemI fs, CurrentWorkingDirectoryI cwd) {
    super(fs, cwd);

    this.setMinParameters(2);
    this.setMaxParameters(2);
  }

  /**
   * Returns a description of the cp command.
   * 
   * @return the description of the command
   */
  public static String getDescriptionOfCommand() {
    return "Name of command: cp\n\n"
        + "Functionality: Copies a specified directory/file path to the new specified\n"
        + "directory/file path (can be relative or absolute). If the user passes an\n"
        + "invalid path (either as the source or destination), or if the destination\n"
        + "directory path already contains the a directory/file with the same name as the\n"
        + "source directory/file path, cat will print an error message. If both the source and\n"
        + "destination paths specify a file, cat will overwrite the destination file to contain\n"
        + "the source file's contents."
        + "Note: If the destination file does not exist, cp will first create the destination"
        + "file\nand write the source file's contents to it.\n\n"
        + "Command syntax: cp sourcePath destinationPath";
  }

  /**
   * Copies an item into another item. This item can be either a file or directory. The following
   * cases are valid copies (item1 is the item to move, item2 is the destination):
   * <p>
   * <ul>
   * <li>Case 1: item1 and item2 are both directories. All contents of item1 are moved into item2 if
   * there isn't an item in item2 with the same name as item1, item1 isn't a parent directory of
   * item2, and item1 isn't item2.
   * <li>Case 2: item1 is a file and item2 is a directory. item1 is copied into item2 if there isn't
   * already an item in item2 with the same name as item1.
   * <li>Case 3: item1 and item2 are both files. The contents of item2 are overwritten with the
   * contents of item1.
   * </ul>
   * 
   * @return the message describing what item was copied and where it was copied to if the copy is
   *         successful, null otherwise
   */
  public String execute() {

    // Storing the directory to move.
    String pathToCopy = super.getCurrentParameters()[0];

    // Storing the destination directory.
    String destPath = super.getCurrentParameters()[1];

    Directory destDir = FilePath.convertAnyPathToDirectory(destPath, super.getShellCWD(),
        this.getCurrentFileSystem());

    Directory dirToCopy = FilePath.convertAnyPathToDirectory(pathToCopy, super.getShellCWD(),
        this.getCurrentFileSystem());

    File fileToCopy =
        FilePath.convertAnyPathToFile(pathToCopy, super.getShellCWD(), this.getCurrentFileSystem());

    // Checking if the destination is a directory.
    if (destDir != null) {

      // Checking if the pathToMove is a directory.
      if (dirToCopy != null) {
        return executeDirectory(dirToCopy, destDir, pathToCopy, destPath);
      } else {

        // Checking if the pathToMove is a file.
        if (fileToCopy != null) {
          return executeFile(fileToCopy, destDir, pathToCopy);
        }
      }

      // Checking if a file overwrite is necessary.
    } else {
      File destFile =
          FilePath.convertAnyPathToFile(destPath, super.getShellCWD(), this.getCurrentFileSystem());

      if (destFile != null) {

        if (fileToCopy != null) {
          return executeFileOverwrite(fileToCopy, destFile);
        }
      }

      if (dirToCopy != null)
        return createNewDirectory(dirToCopy, destPath);
    }

    System.out.println("Cannot copy: Invalid path(s).");
    return null;

  }

  /**
   * Copies fileToMove into destDir if there isn't an item in destDir with the same name as
   * fileToMove.
   * 
   * @param fileToCopy the file to be copied into destDir
   * @param destDir the directory fileToCopy is to be copied into
   * @param pathToCopy the path of fileToCopy
   * @return the message describing where fileToCopy was copied to if the copy is successful, null
   *         otherwise
   */
  private String executeFile(File fileToCopy, Directory destDir, String pathToCopy) {
    if (!destDir.nameAlreadyExists(fileToCopy.getName())) {
      File tempFile = fileToCopy;
      tempFile.setParentDirectory(destDir);
      destDir.addFile(tempFile);

      return "Copied " + pathToCopy + " into " + destDir + ".";
    } else if (destDir.findInFiles(fileToCopy.getName()) != null) {
      return executeFileOverwrite(fileToCopy, destDir.findInFiles(fileToCopy.getName()));
    }

    System.out.println("Cannot copy: A directory with the same name as " + fileToCopy.getName()
        + " already exists in " + destDir.getName() + ".");
    return null;
  }

  /**
   * Overwrites the contents of destFile with the contents of fileToMove.
   * 
   * @param fileToCopy the file containing the contents to be used to overwrite destFile
   * @param destFile the file to be overwritten
   * @return the message describing that destFile was overwritten by fileToMove.
   */
  private String executeFileOverwrite(File fileToCopy, File destFile) {
    destFile.resetContent(fileToCopy.getContent());

    return "Overwrote " + destFile.getName() + " with the contents of " + fileToCopy.getContent()
        + ".";
  }

  /**
   * Copies all the contents of dirToCopy into destDir if there isn't an item in destDir with the
   * same name as dirToCopy, dirToCopy isn't a parent directory of destDir, and dirToCopy isn't
   * destDir.
   * 
   * @param dirToCopy the directory to be copied into destDir
   * @param destDir the directory dirToCopy is to be copied into
   * @param pathToCopy the path of dirToCopy
   * @param destPath the path of destDir
   * @return the message describing where dirToCopy was copied to if the move is successful, null
   *         otherwise
   */
  private String executeDirectory(Directory dirToCopy, Directory destDir, String pathToCopy,
      String destPath) {

    if (dirToCopy.equals(destDir)) {
      System.out.println("Cannot copy directory into itself.");
    } else if (destDir.nameAlreadyExists(dirToCopy.getName())) {
      Directory copiedDirectory = new Directory(dirToCopy.getName(), destDir);
      copiedDirectory.setFileArray(dirToCopy.getFileArray());
      copiedDirectory.setDirArray(dirToCopy.getDirArray());

      destDir.removeDirectory(dirToCopy.getName());
      destDir.addDirectory(copiedDirectory);

      return "Overwrote " + destPath + " with " + pathToCopy + ".";

    } else if (destDir.isOneOfTheParentDirectories(dirToCopy, this.getCurrentFileSystem())) {
      System.out.println(
          "Cannot copy: " + pathToCopy + " is one of the parent directories of " + destPath + ".");
    } else {
      Directory copiedDirectory = new Directory(dirToCopy.getName(), destDir);
      copiedDirectory.setFileArray(dirToCopy.getFileArray());
      copiedDirectory.setDirArray(dirToCopy.getDirArray());

      destDir.addDirectory(copiedDirectory);

      return "Copied " + pathToCopy + " into " + destPath + ".";
    }

    return null;
  }

  /**
   * Creates a new directory to copy dirToCopy into, if and only if the second last directory of
   * destPath is a valid directory.
   * 
   * @param dirToCopy the directory to be moved
   * @param destPath the path which dirToMove is to be moved into.
   * @return the message describing where dirToMove was moved to if the move is successful, null
   *         otherwise
   */
  private String createNewDirectory(Directory dirToCopy, String destPath) {
    Directory possibleParent = FilePath.getEndDirectoryfromPath(destPath.split("/"),
        super.getShellCWD(), this.getCurrentFileSystem());

    if (possibleParent != null && dirToCopy != null) {
      String newDirectoryName = destPath.split("/")[destPath.split("/").length - 1];

      Directory newDirectory = new Directory(newDirectoryName, possibleParent);
      possibleParent.getDirArray().add(newDirectory);

      return executeDirectory(dirToCopy, newDirectory, dirToCopy.getFullPathString(), destPath);
    }

    System.out.println("Cannot move: Invalid path(s).");
    return null;
  }
}
