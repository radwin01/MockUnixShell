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
 * Represents a Mv command.
 * 
 * @version 1.0
 */
public class Mv extends Command {

  /**
   * Creates an instance of a Mv object. Sets the minimum and maximum number of parameters that the
   * user can enter.
   */
  public Mv(FileSystemI fs, CurrentWorkingDirectoryI cwd) {
    super(fs, cwd);

    this.setMinParameters(2);
    this.setMaxParameters(2);
  }

  /**
   * Returns a description of the mv command.
   * 
   * @return the description of the command
   */
  public static String getDescriptionOfCommand() {
    return "Name of command: mv\n\n"
        + "Functionality: Transports files and directories from one directory to another\n"
        + "(formatted as a relative or absolute path). If the destination\n"
        + "directory does not exist, mv will rename the source file/directory to the name of\n"
        + "the destination directory. If the destination directory path is the same as the\n"
        + "source file/directory's current directory, mv will print an error message.\n\n"
        + "Command syntax: mv sourcePath destinationPath";
  }

  /**
   * Moves an item into another item. This item can be either a file or directory. The following
   * cases are valid moves (item1 is the item to move, item2 is the destination):
   * <p>
   * <ul>
   * <li>Case 1: item1 and item2 are both directories. All contents of item1 are moved into item2 if
   * there isn't an item in item2 with the same name as item1, item1 isn't a parent directory of
   * item2, and item1 isn't item2.
   * <li>Case 2: item1 is a file and item2 is a directory. item1 is moved into item2 if there isn't
   * already an item in item2 with the same name as item1.
   * <li>Case 3: item1 and item2 are both files. The contents of item2 are overwritten with the
   * contents of item1. item1 is removed.
   * <li>Case 4: item1 is an existing directory or file, and item2 is in the same directory as item1
   * but not existent. Then item1 is renamed to that of item2.
   * </ul>
   * 
   * @return the message describing what item was moved and where it was moved to if the move is
   *         successful, null otherwise
   */
  public String execute() {

    // Storing the directory to move.
    String pathToMove = super.getCurrentParameters()[0];

    // Storing the destination directory
    String destPath = super.getCurrentParameters()[1];

    Directory destDir = FilePath.convertAnyPathToDirectory(destPath, super.getShellCWD(),
        this.getCurrentFileSystem());

    Directory dirToMove = FilePath.convertAnyPathToDirectory(pathToMove, super.getShellCWD(),
        this.getCurrentFileSystem());

    File fileToMove =
        FilePath.convertAnyPathToFile(pathToMove, super.getShellCWD(), this.getCurrentFileSystem());

    // Checking if the destination is a Directory
    if (destDir != null) {

      // Moving directory into another directory
      if (dirToMove != null)
        return executeDirectory(dirToMove, destDir, pathToMove, destPath);

      // Moving file into another directory
      if (fileToMove != null)
        return executeFile(fileToMove, destDir, pathToMove, destPath);

      // Checking of the destination is a File
    } else if (FilePath.convertAnyPathToFile(destPath, super.getShellCWD(),
        this.getCurrentFileSystem()) != null) {
      File destFile =
          FilePath.convertAnyPathToFile(destPath, super.getShellCWD(), this.getCurrentFileSystem());

      if (fileToMove != null)
        return executeFileOverwrite(fileToMove, destFile);

      // Destination is neither a file or directory, so check if a rename for folder creation is
      // necessary.
    } else {

      // Check if the pathtoMove is a directory to be renamed or created.

      if (dirToMove != null)
        return renameOrCreateNewDirectory(dirToMove, destPath);

      // Check if the pathToMove is a file to be renamed.
      if (fileToMove != null)
        return renameFile(fileToMove, destPath);
    }

    System.out.println("Cannot move: Invalid path(s).");
    return null;
  }


  /**
   * Moves fileToMove into destDir if there isn't an item in destDir with the same name as
   * fileToMove.
   * 
   * @param fileToMove the file to be moved
   * @param destDir the directory fileToMove is to be moved to
   * @param pathToMove the path of fileToMove
   * @param destPath the path of destDir
   * @return the message describing where fileToMove was moved to if the move is successful, null
   *         otherwise
   */
  private String executeFile(File fileToMove, Directory destDir, String pathToMove,
      String destPath) {
    if (!destDir.nameAlreadyExists(fileToMove.getName())) {
      fileToMove.getParentDirectory().removeFile(fileToMove.getName());
      fileToMove.setParentDirectory(destDir);
      destDir.addFile(fileToMove);

      return "Moved " + pathToMove + " into " + destPath + ".";
    } else if (destDir.findInFiles(fileToMove.getName()) != null) {
      return executeFileOverwrite(fileToMove, destDir.findInFiles(fileToMove.getName()));
    }

    System.out.println("Cannot move: A directory with the same name as " + fileToMove.getName()
        + " already exists in " + destDir.getName() + ".");
    return null;
  }

  /**
   * Overwrites the contents of destFile with the contents of fileToMove and removes fileToMove.
   * 
   * @param fileToMove the file containing the contents to be used to overwrite destFile
   * @param destFile the file to be overwritten
   * @return the message describing that destFile was overwritten by fileToMove.
   */
  private String executeFileOverwrite(File fileToMove, File destFile) {
    destFile.resetContent(fileToMove.getContent());
    fileToMove.getParentDirectory().removeFile(fileToMove.getName());

    return "Overwrote " + destFile.getName() + " with the contents of " + fileToMove.getName() + "."
        + " Removed " + fileToMove.getName() + ".";
  }

  /**
   * Moves all the contents of dirToMove into destDir if there isn't an item in destDir with the
   * same name as dirToMove, dirToMove isn't a parent directory of destDir, and dirToMove isn't
   * destDir.
   * 
   * @param dirToMove the directory to be moved
   * @param destDir the destination directory for dirToMove
   * @param pathToMove the path of dirToMove
   * @param destPath the path of destDir
   * @return the message describing where dirToMove was moved to if the move is successful, null
   *         otherwise
   */
  private String executeDirectory(Directory dirToMove, Directory destDir, String pathToMove,
      String destPath) {

    if (dirToMove.equals(destDir)) {
      System.out.println("Cannot move directory into itself.");
    } else if (destDir.nameAlreadyExists(dirToMove.getName())) {
      dirToMove.getParentDir().removeDirectory(dirToMove.getName());
      dirToMove.setParentDir(destDir);
      destDir.removeDirectory(dirToMove.getName());
      destDir.addDirectory(dirToMove);

      return "Overwrote " + destPath + " with " + pathToMove + ".";
    } else if (destDir.isOneOfTheParentDirectories(dirToMove, super.getCurrentFileSystem())) {
      System.out.println(
          "Cannot move: " + pathToMove + " is one of the parent directories of " + destPath + ".");
    } else {
      dirToMove.getParentDir().removeDirectory(dirToMove.getName());
      dirToMove.setParentDir(destDir);
      destDir.addDirectory(dirToMove);

      return "Moved " + pathToMove + " into " + destPath + ".";
    }

    return null;
  }

  /**
   * Creates a new directory to move dirToMove into, if and only if the second last directory of
   * destPath is a valid directory.
   * 
   * @param dirToMove the directory to be moved
   * @param destPath the path which dirToMove is to be moved into.
   * @return the message describing where dirToMove was moved to if the move is successful, null
   *         otherwise
   */
  private String createNewDirectory(Directory dirToMove, String destPath) {
    Directory possibleParent = FilePath.getEndDirectoryfromPath(destPath.split("/"),
        super.getShellCWD(), this.getCurrentFileSystem());

    if (possibleParent != null && dirToMove != null) {
      String newDirectoryName = destPath.split("/")[destPath.split("/").length - 1];

      Directory newDirectory = new Directory(newDirectoryName, possibleParent);
      possibleParent.getDirArray().add(newDirectory);

      return executeDirectory(dirToMove, newDirectory, dirToMove.getFullPathString(), destPath);
    }

    System.out.println("Cannot move: Invalid path(s).");
    return null;
  }

  /**
   * Renames a directory to a given path, only if the path contains the same parent directory as the
   * directory. If a rename is not necessary, then check for a possible creation of a new directory.
   * 
   * @param toRename the directory to be renamed
   * @param destPath the path to be used to rename the directory
   * @return a message indicating whether the rename was successful
   */
  private String renameOrCreateNewDirectory(Directory toMove, String destPath) {
    Directory destParentDir = FilePath.getEndDirectoryfromPath(destPath.split("/"),
        super.getShellCWD(), this.getCurrentFileSystem());

    if (destParentDir != null && destParentDir.equals(toMove.getParentDir())) {
      toMove.setName(destPath.split("/")[destPath.split("/").length - 1]);
      return "Renamed directory successfully.";
    }

    if (destParentDir != null) {
      return createNewDirectory(toMove, destPath);
    }

    System.out.println("Cannot move: Invalid path(s).");
    return null;
  }

  /**
   * Renames a file to a given path, only if the path contains the same parent directory as the
   * file.
   * 
   * @param toRename the file to be renamed
   * @param destPath the path to be used to rename the file
   * @return a message indicating whether the rename was successful
   */
  private String renameFile(File toRename, String destPath) {
    Directory destParentDir = FilePath.getEndDirectoryfromPath(destPath.split("/"),
        super.getShellCWD(), this.getCurrentFileSystem());

    if (destParentDir != null && destParentDir.equals(toRename.getParentDirectory())) {
      toRename.setName(destPath.split("/")[destPath.split("/").length - 1]);
      return "Renamed file successfully.";
    }

    System.out.println("Cannot move: Invalid path(s).");
    return null;
  }
}
