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

import driver.JShell;
import errors.InvalidCommandException;
import errors.TooLittleParametersException;
import errors.TooManyParametersException;
import filesystem.CurrentWorkingDirectoryI;
import filesystem.FileSystemI;
import inputoutput.UserTerminalIn;

/**
 * Represents validation required for commands to execute.
 * 
 * @version 1.0
 */

public class Validation {

  /**
   * Method that populates a hashmap to associate command inputs with the actual type of command for
   * successful return.
   * 
   * @param commandHashMap the map to be populated from string input to new command
   * @param terminal the input terminal defined in jshell
   */
  private static void populateCommandMap(java.util.HashMap<String, Command> commandHashMap,
      UserTerminalIn terminal, CurrentWorkingDirectoryI cwd, FileSystemI currentFS) {

    // commandHashMap.put("speak", new Speak(currentFS, cwd, terminal));
    commandHashMap.put("curl", new Curl(currentFS, cwd, cwd.getCWD()));
    commandHashMap.put("exit", new Exit(currentFS, cwd));
    commandHashMap.put("echo", new Echo(currentFS, cwd));
    commandHashMap.put("mkdir", new Mkdir(currentFS, cwd));
    commandHashMap.put("cd", new Cd(currentFS, cwd));
    commandHashMap.put("history", new History(currentFS, cwd));
    commandHashMap.put("cat", new Cat(currentFS, cwd));
    commandHashMap.put("man", new Man(currentFS, cwd));
    commandHashMap.put("ls", new Ls(currentFS, cwd));
    commandHashMap.put("pwd", new Pwd(currentFS, cwd));
    commandHashMap.put("popd", new Popd(currentFS, cwd, JShell.getDirStackFromShell()));
    commandHashMap.put("pushd", new Pushd(currentFS, cwd, JShell.getDirStackFromShell()));
    commandHashMap.put("rm", new Rm(currentFS, cwd));
    commandHashMap.put("mv", new Mv(currentFS, cwd));
    commandHashMap.put("cp", new Cp(currentFS, cwd));
    commandHashMap.put("find", new Find(currentFS, cwd));
    commandHashMap.put("save", new Save(currentFS, cwd, JShell.getDirStackFromShell()));
    commandHashMap.put("load", new Load(currentFS, cwd, JShell.getDirStackFromShell(), terminal));
    commandHashMap.put("tree", new Tree(currentFS, cwd));
    commandHashMap.put("", new Command(currentFS, cwd));

  }


  /**
   * Method that takes a string to see what type of command is to be created as a result and returns
   * it. Invalid commands return null.
   * 
   * @param potentialCommand the string that may hold a valid command to be created
   * @param terminal the input terminal defined in jshell
   * @return Command The new type of command to be created
   * @throws InvalidCommandException when the command entered does not exist
   */
  private static Command createCommand(String potentialCommand, UserTerminalIn terminal,
      CurrentWorkingDirectoryI cwd, FileSystemI currentFS) throws InvalidCommandException {

    java.util.HashMap<String, Command> commandHashMap = new java.util.HashMap<>();
    Validation.populateCommandMap(commandHashMap, terminal, cwd, currentFS);
    Command newCommand = commandHashMap.get(potentialCommand);
    if (newCommand == null) {
      throw new InvalidCommandException("Invalid Command: " + potentialCommand);
      // System.out.println("Invalid Command: " + potentialCommand);
    }
    return newCommand;
  }

  /**
   * Takes in the command line input from Jshell as a list excluding the command name. Check to make
   * sure that it is the valid number of parameters for the specified command type. Returns a
   * message if too many or too few. If either no max is declared or amount is valid, return the
   * amount.
   * 
   * @param parameters the command line strings that follow initial command
   * @param currentCommand the new command produced by interpretCommand
   * @return the number of parameters if successful or a negative number associated with the reason
   *         for failure
   * @throws TooManyParametersException when the minimum number of parameters is not met
   * @throws TooLittleParametersException when the maximum number of parameters is exceeded
   */
  private static int checkNumParameters(String[] parameters, Command currentCommand)
      throws TooManyParametersException, TooLittleParametersException {

    int[] comParams = currentCommand.getParameterRestrictions();
    int numParams = 0;

    if (parameters != null) {
      numParams = parameters.length;
    }

    // If too many or too few parameters return an error message
    if (numParams < comParams[0]) {
      throw new TooLittleParametersException("Not enough parameters");

      // If function does not declare max, return number of parameters
    } else if (comParams[1] >= 0) {
      if (numParams > comParams[1]) {
        throw new TooManyParametersException("Too many parameters");
      }
    }

    // Return the number of parameters if valid amount
    return numParams;
  }

  /**
   * Method that takes a command line input by the user and extracts the command to create a new
   * valid command and saves the rest of the words as parameters under the commands
   * current_parameters. It returns the command if valid otherwise it returns null.
   * 
   * @param commandLine the string that contains user input to create a command.
   * @param terminal the input terminal defined in jshell
   * @return the new command that is created
   */
  public static Command interpretCommand(String commandLine, UserTerminalIn terminal,
      CurrentWorkingDirectoryI cwd, FileSystemI currentFS) {

    // Initialize strings
    String[] splitLine;
    String command;

    // Remove white space and find end of first word
    commandLine = commandLine.trim();
    int index = commandLine.indexOf(" ");

    // If more than one input split up command form what follow otherwise take the full string
    if (index > 0) {
      splitLine = commandLine.substring(index).trim().split("\\s+");
      command = commandLine.substring(0, index);
    } else {
      splitLine = null;
      command = commandLine;
    }

    // Create new command
    Command newCommand = null;
    try {
      newCommand = createCommand(command, terminal, cwd, currentFS);
    } catch (InvalidCommandException e) {
      System.out.println(e.toString());
    }

    // If command is invalid, return null
    if (newCommand != null) {

      // If number of parameters are valid, return the command
      int parameterCheck = -1;
      try {
        parameterCheck = checkNumParameters(splitLine, newCommand);
      } catch (TooManyParametersException | TooLittleParametersException e) {
        System.out.println(e.toString());
      }

      if (parameterCheck >= 0) {
        newCommand.setCurrentParameters(parameterCheck, splitLine);
        if (newCommand instanceof Echo)
          ((Echo) newCommand).setLine(commandLine);
        return newCommand;
      }
    }
    return null;
  }

  /**
   * Method to determine occurrences of quotation marks in a given string.
   * 
   * @param stringToSearch the string to check for quotation mark occurrences
   * @return the number of quotation marks in string
   */
  public static int countQuoteOccurences(String stringToSearch) {

    // Initialize counter variables
    int index = 0;
    int count = 0;

    // Loop until quotation mark is no longer found
    while (index != -1) {

      // Search for index of quotation mark.
      index = stringToSearch.indexOf("\"", index);

      // If quotation mark exists, update count and move to next index
      if (index != -1) {
        count++;
        index += 1;
      }
    }

    // Return quote occurrences
    return count;
  }


  /**
   * Method that takes a potential name for a file or directory and check to see if it contains
   * invalid characters. Returns false and error message if invalid character is found, true
   * otherwise.
   * 
   * @param potentialName the string that may hold a file/directory name to be created
   * @return returns whether characters are valid for file/directory name (true/false)
   */
  public static boolean containsValidCharacters(String potentialName) {

    // Create list of invalid characters
    String[] invalidCharacters = {"/", ".", " ", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")",
        "{", "}", "~", "|", "<", ">", "?"};

    // Check if any of the invalid characters are found in the string; return false if found
    for (String s : invalidCharacters) {
      if (potentialName.contains(s)) {
        System.out.println(potentialName + " contains invalid character: " + s);
        return false;
      }
    }

    // Return true if no invalid characters are found
    return true;
  }

}
