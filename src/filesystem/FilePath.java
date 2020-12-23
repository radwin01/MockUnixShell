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
package filesystem;

/**
 * Contains methods which execute file path operations.
 * 
 * @version 1.0
 */

public final class FilePath {

  /**
   * Empty constructor as FilePath is a final class.
   */
  private FilePath() {}

  /**
   * Takes in a String path and finds the file given by the path, if it exists.
   * 
   * @param path the String path to be traversed through
   * @param cwd the current working directory
   * @param fs the current file system
   * @return the file given by the path, null if the file doesn't exist
   */
  public static File convertAnyPathToFile(String path, CurrentWorkingDirectoryI cwd,
      FileSystemI fs) {

    // If the path is just a single item
    if (path.split("/").length == 1) {
      if (path.charAt(0) == '/') {
        return fs.getRootDir().getFileFromDirectory(path.split("/")[0]);
      }
      return cwd.getCWD().getFileFromDirectory(path.split("/")[0]);
    }

    // If the path has multiple directories to traverse through
    Directory secondLast = FilePath.getEndDirectoryfromPath(path.split("/"), cwd, fs);

    if (secondLast != null) {
      return secondLast.getFileFromDirectory(path.split("/")[path.split("/").length - 1]);
    }

    return null;
  }

  /**
   * Takes in a String path and finds the directory given by the path, if it exists.
   * 
   * @param path the String path to be traversed through
   * @param cwd the current working directory
   * @param fs the current file system
   * @return the directory given by the path, null if the directory doesn't exist
   */
  public static Directory convertAnyPathToDirectory(String path, CurrentWorkingDirectoryI cwd,
      FileSystemI fs) {

    // Root directory
    if (path.equals("/")) {
      return fs.getRootDir();

      // "../../../"
    } else if (isFullyDotted(path.split("/"))) {
      return convertDotToCurrentDir(path, cwd);

      // full or relative path
    } else {
      if (path.split("/").length == 1) {
        if (path.charAt(0) == '/') {
          return FileSystem.createInstanceOfFileSystem().getRootDir()
              .getSubDirectoryFromString(path.split("/")[0]);
        }
        return cwd.getCWD().getSubDirectoryFromString(path.split("/")[0]);
      }

      Directory secondLast = FilePath.getEndDirectoryfromPath(path.split("/"), cwd, fs);

      if (secondLast != null) {
        if (path.split("/")[path.split("/").length - 1].equals("..")) {
          return secondLast.getParentDir();
        }

        return secondLast.getSubDirectoryFromString(path.split("/")[path.split("/").length - 1]);
      }
    }

    return null;
  }

  /**
   * Converts a file path of the form '../../../' into the full file path of the final destination
   * directory. For example, if your current working directory is 'root/dir1/dir2/dir3'. Then, the
   * file path '../../' will result in the final destination directory of 'root/dir1/', which is
   * then returned. If there are more '../../../' then the number of parent directories from the
   * current working directory, then the path of the root directory is returned (since there are no
   * more parent directories to change into).
   * 
   * @param dottedPath a file path of the form '../../../'
   * @param cwd the current working directory from command call
   * @return the full path of the destination directory
   */
  public static String convertDotToFullPath(String dottedPath, CurrentWorkingDirectoryI cwd) {

    // Getting the number of traversals needed to find the destination directory.
    int numBack = dottedPath.split("/").length;

    // Storing the current working directory.
    Directory currentTraversal = cwd.getCWD();

    // Traversing backwards towards root directory starting from the current working directory.
    for (int i = 0; i < numBack; i++) {

      // Checking if the root directory is reached before the end of the loop.
      if (currentTraversal.getParentDir() == null) {
        return currentTraversal.getFullPathString();
      }

      currentTraversal = currentTraversal.getParentDir();
    }

    return currentTraversal.getFullPathString();
  }

  /**
   * Takes a file path of the form '../../../' and returns the final destination directory. For
   * example, if your current working directory is 'root/dir1/dir2/dir3'. Then, the file path
   * '../../' will result in the final destination directory, dir1, which is then returned. If
   * there are more '../../../' then the number of parent directories from the current working
   * directory, then the root directory is returned (since there are no more parent directories
   * to change into).
   * 
   * @param dottedPath a file path of the form '../../../'
   * @param cwd the current working directory from command call
   * @return the destination directory
   */
  public static Directory convertDotToCurrentDir(String dottedPath, CurrentWorkingDirectoryI cwd) {

    // Getting the number of traversals needed to find the destination directory.
    int numBack = dottedPath.split("/").length;

    // Storing the current working directory.
    Directory currentTraversal = cwd.getCWD();

    // Traversing backwards towards root directory starting from the current working directory.
    for (int i = 0; i < numBack; i++) {

      // Checking if the root directory is reached before the end of the loop.
      if (currentTraversal.getParentDir() == null) {
        return currentTraversal;
      }
      currentTraversal = currentTraversal.getParentDir();
    }

    return currentTraversal;
  }

  /**
   * Method to get a resulting directory from a part of a path being in dot form. Takes the current
   * directory and the current part of the path and returns the next associated directory if it
   * exists.
   * 
   * @param dirStr the string that is the next part of the directory path to check
   * @param curDir the current directory from the given path to check in
   * @return the directory referred to by dirStr, null if it isn't in dot form
   */
  public static Directory convertDotToDir(String dirStr, Directory curDir) {

    // Refer current directory if . is given
    if (dirStr.equals(".")) {
      return curDir;
    }

    // Return parent directory if .. is given
    else if (dirStr.equals("..")) {
      Directory parentDir = curDir.getParentDir();

      // If current directory is root, return itself
      if (parentDir == null
          || (curDir.getName().equals("") && parentDir.getParentDir().getName().equals(""))) {
        return curDir;
      }
      return curDir.getParentDir();
    }

    // Return null if not in dot form
    return null;
  }

  /**
   * Prints error message for getEndDirectoryFromPath
   * 
   * @param message the message to be printed for user
   * @return null for directory so that previous method can return result
   */
  private static Directory printErrorMessage(String message) {
    System.out.println(message);
    return null;
  }

  /**
   * Method that gives the second last directory from a given string list path.
   * 
   * @param pathList the path in an array that was split at the slashes
   * @param cwd the current working directory from command call
   * @param fs the current file system
   * @return the second last directory from given string list path
   */
  public static Directory getEndDirectoryfromPath(String[] pathList, CurrentWorkingDirectoryI cwd,
      FileSystemI fs) {

    // Initialize string to track directory and set current directory
    String errorString = "";
    Directory pathHead = cwd.getCWD();
    int index = 0;

    // If path list is empty, return error for empty path
    if (pathList.length == 0)
      return FilePath.printErrorMessage("Empty path is invalid.");

    // If absolute path given, start from root
    else if (pathList[0].equals("")) {
      FileSystemI currentFileSystem = fs;
      pathHead = currentFileSystem.getRootDir();
      index++;
    }

    // Iterate through path list
    while (index < pathList.length - 1) {

      // Add to current path string and if double slash is found, return error
      errorString += (pathList[index] + "/");
      if (pathList[index].equals(""))
        return FilePath.printErrorMessage("Error with double slashes at: " + errorString);

      // Keep track of previous directory and get next directory
      Directory lastDir = pathHead;
      Directory subDir = FilePath.convertDotToDir(pathList[index], pathHead);

      // Use dot from of directory if given
      if (subDir != null)
        pathHead = subDir;
      else {

        // Otherwise see if directory exists
        if (pathList[index].equals(".."))
          return null;

        for (int j = 0; j < pathHead.getDirArray().size(); j++)
          if (pathHead.getDirArray().get(j).getName().equals(pathList[index]))
            // Changes the current directory to the next sub directory
            pathHead = pathHead.getDirArray().get(j);

        // If next part of directory path not found return error
        if (lastDir.equals(pathHead))
          return FilePath.printErrorMessage("Path does not exist, error at: " + errorString);
      }

      // Increment index
      index++;
    }
    return pathHead;
  }

  /**
   * Takes a String of the form '#/#/#/#/' where '#' can be any String. This String is checked to
   * see if all the '#'s are the same String, where the String being compared for is specified by
   * 'check'.
   * 
   * @param input the String to be analyzed
   * @param check the String to be compared to the '#'s in 'input'
   * @return true if all '#'s are equal to 'check', false otherwise
   */
  public static boolean isAllStringBetweenSlash(String input, String check) {
    String[] inputSplit = input.split("/");

    int startIndex = 0;

    if (inputSplit[0].isEmpty()) {
      startIndex = 1;
    }

    for (int i = startIndex; i < inputSplit.length; i++) {
      if (!inputSplit[i].equals(check)) {
        return false;
      }
    }

    return true;
  }

  /**
   * Takes a String and checks if all the characters in the String are the same character. The
   * character being searched for is specified by 'check'. If it is return true, else, return
   * false.
   * 
   * @param input the String to be analyzed
   * @param check the character to be checked for repetition throughout input
   * @return true if all the characters of 'input' are equal to 'check'
   */
  public static boolean isAllChar(String input, char check) {
    for (int i = 0; i < input.length(); i++) {
      if (input.charAt(i) != check) {
        return false;
      }
    }

    return true;
  }

  /**
   * Takes a file path in the form of an array and checks if the entire path is of the pattern,
   * '../../..'. If it is, return true, else, return false.
   * 
   * @param pathSplit the file path to be analyzed
   * @return true if the given file path matches the pattern, false otherwise
   */
  public static boolean isFullyDotted(String[] pathSplit) {
    for (int i = 0; i < pathSplit.length; i++) {
      if (!pathSplit[i].equals("..")) {
        return false;
      }
    }
    return true;
  }
}
