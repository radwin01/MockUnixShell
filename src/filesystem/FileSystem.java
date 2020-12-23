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
 * This class is the file system which the JShell uses. Allows for creation of files, directories,
 * and holds the head of the file system
 * 
 * @version 1.0
 */
public class FileSystem implements FileSystemI {

  // Stores the root directory (head) of the file system
  private Directory rootDir;

  // Initializes the FileSystem object as null when compiled, to be instantiated again at runtime
  private static FileSystem currentFileSystem = null;

  /**
   * Constructor function used to create an instance of a FileSysten object. Initializes the root
   * directory with name "" and parent directory null.
   */
  private FileSystem() {

    // Sets the root directory name and parent directory
    setRootDir(new Directory("", null));

  }

  /**
   * Factory method which creates an instance of the new FileSystem. If an instance already exists,
   * return that instead.
   * 
   * @return the instance of the FileSystem that was created
   */
  public static FileSystem createInstanceOfFileSystem() {

    // Checks if the file system is still declared as null
    if (currentFileSystem == null) {

      // Creates a new FileSystem
      currentFileSystem = new FileSystem();
    }

    // Returns the newly created or pre existing file system
    return currentFileSystem;

  }

  /**
   * Checks if the most top directory in the given path is a sub directory of the currentDir. If
   * the sub directory is found, recursively sends the rest of the path to be searched until a
   * base case has been reached.
   * 
   * @param currentDir Directory object containing the current directory and its sub directories.
   * @param path String which contains the desired path to a directory that must be checked.
   * @return 0 if the path and directory exist, 1 if the path exists but the final directory does
   *         not, and 2 if the path does not exist.
   */
  public int checkDirPath(Directory currentDir, String path) {

    // Splits the path parameter by the delimiter ("/")
    String[] pathSeperated = path.split("/");
    // Stores the size of the split array
    int sizeOfSplit = pathSeperated.length;

    // Checks if the base case has been reached, and the file path has been found
    if (sizeOfSplit == 1) {

      for (Directory subDirs : currentDir.getDirArray())

        // Checks if the path is looking for the sub directory directory instead
        if (pathSeperated[0].equals(subDirs.getName())) {
          return 0;
        }

      // If the path exists, but the final directory does not return 1
      return 1;

    }

    // Checks through each sub directory in the current directory
    for (int i = 0; i < currentDir.getDirArray().size(); i++) {

      // Checks if the indexed directory is the next directory in the path
      if (currentDir.getDirArray().get(i).getName().equals(pathSeperated[0])) {

        // String accumulator to hold the remaining path to be searched
        String remainingPath = "";

        for (int j = 1; j < sizeOfSplit; j++) {

          // Concatenates the next n-1 elements in the array into a string again
          remainingPath += pathSeperated[j];
        }

        // Passes the shortened string recursively to the next directory
        return checkDirPath(currentDir.getDirArray().get(i), remainingPath);

      }

    }

    // Returns 2 if the path does not exist, and its previous do no either
    return 2;


  }

  /**
   * Checks if the most top directory in the given path is a sub directory of the currentDir. If
   * the sub directory is found, recursively sends the rest of the path to be searched until a
   * base case has been reached. Once at the final directory, checks if the desired file name
   * exists.
   * 
   * @param currentDir Directory object containing the current directory and its sub directories.
   * @param path String which contains the desired path to a File path that must be checked.
   * @return 0 if the path and File exist,1 if the path exists but the File does not, and 2 if the
   *         path does not exist.
   */
  public int checkFilePath(Directory currentDir, String path) {


    // Splits the path parameter by the delimiter ("/")
    String[] pathSeperated = path.split("/");

    // Stores the size of the split array
    int sizeOfSplit = pathSeperated.length;

    // Checks if the base case has been reached, and the file path has been found
    if (sizeOfSplit == 1) {

      for (File file : currentDir.getFileArray()) {

        if (file.getName().equals(path)) {
          // Return 0 if the file has been found
          return 0;
        }

      }
      // Return 1 if all previous directories exist, but, the file does not
      return 1;
    } else {

      // Checks through each sub directory in the current directory
      for (int i = 0; i < currentDir.getDirArray().size(); i++) {

        // Checks if the indexed directory is the next directory in the path
        if (currentDir.getDirArray().get(i).getName().equals(pathSeperated[0])) {

          // String accumulator to hold the remaining path to be searched
          String remainingPath = "";

          for (int j = 1; j < sizeOfSplit; j++) {

            // Concatenates the next n-1 elements in the array into a string again
            remainingPath += pathSeperated[j];
          }

          // Passes the shortened string recursively to the next directory
          return checkFilePath(currentDir.getDirArray().get(i), remainingPath);

        }

      }

    }

    // Return 2 if the path does not exist
    return 2;

  }

  /**
   * Returns the root directory of the file system.
   * 
   * @return the head of the file system
   */
  public Directory getRootDir() {
    return rootDir;
  }

  /**
   * Sets the root directory to the rootDir parameter.
   * 
   * @param rootDir Directory which is the head of the file system
   */
  public void setRootDir(Directory rootDir) {
    this.rootDir = rootDir;
  }

}
