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
import filesystem.FileSystemI;

/**
 * Represents a Man command
 * 
 * @version 2.0
 */
public class Man extends Command {

  private java.util.HashMap<String, String> commandHashMap;

  /* Setters and Getters */

  /**
   * Method that gets the commandHashMap.
   * 
   * @return the commandHashMap
   */
  public java.util.HashMap<String, String> getCommandHashMap() {
    return commandHashMap;
  }

  /**
   * Method that sets the commandHashMap to take different values.
   * 
   * @param commandHashMap the new commandHashMap
   */
  public void setCommandHashMap(java.util.HashMap<String, String> commandHashMap) {
    this.commandHashMap = commandHashMap;
  }

  /**
   * Constructor used to create an instance of a Man object. Sets the number of minimum and maximum
   * parameters that the user can enter after the command.
   * 
   * @param fs the current FileSystem in use
   * @param cwd the current CWD in use
   */
  public Man(FileSystemI fs, CurrentWorkingDirectoryI cwd) {

    super(fs, cwd);

    // sets the minimum number of parameters to be accepted
    this.setMinParameters(1);

    // sets the maximum number of parameters to be accepted
    this.setMaxParameters(3);
  }

  /**
   * Method that returns the description of the cat command.
   * 
   * @return the description of the command
   */
  public static String getDescriptionOfCommand() {
    return "Name of command: man\n\n"
        + "Functionality: Prints the functionality of a command. The user may\n"
        + "learn about the following commands:\n"
        + "cat, cd, echo, exit, history, ls, man, mkdir, popd, pushd, pwd, speak\n"
        + "If the user passes a command that is not part of the existing list, man\n"
        + "will print an error message.\n\n" + "Command syntax: man commandName";
  }

  /**
   * Method that fills the hashmap with the commands as keys and the descriptions as values.
   */
  private static void fillHashMap(java.util.HashMap<String, String> commandHashMap) {
    commandHashMap.put("cat", Cat.getDescriptionOfCommand());
    commandHashMap.put("cd", Cd.getDescriptionOfCommand());
    commandHashMap.put("echo", Echo.getDescriptionOfCommand());
    commandHashMap.put("exit", Exit.getDescriptionOfCommand());
    commandHashMap.put("history", History.getDescriptionOfCommand());
    commandHashMap.put("ls", Ls.getDescriptionOfCommand());
    commandHashMap.put("man", Man.getDescriptionOfCommand());
    commandHashMap.put("mkdir", Mkdir.getDescriptionOfCommand());
    commandHashMap.put("popd", Popd.getDescriptionOfCommand());
    commandHashMap.put("pushd", Pushd.getDescriptionOfCommand());
    commandHashMap.put("pwd", Pwd.getDescriptionOfCommand());
    commandHashMap.put("rm", Rm.getDescriptionOfCommand());
    commandHashMap.put("cp", Cp.getDescriptionOfCommand());
    commandHashMap.put("mv", Mv.getDescriptionOfCommand());
    commandHashMap.put("curl", Curl.getDescriptionOfCommand());
    commandHashMap.put("speak", Speak.getDescriptionOfCommand());
    commandHashMap.put("save", Save.getDescriptionOfCommand());
    commandHashMap.put("find", Find.getDescriptionOfCommand());
    commandHashMap.put("load", Load.getDescriptionOfCommand());
    commandHashMap.put("tree", Tree.getDescriptionOfCommand());
  }

  /**
   * Allows the user to read about the functionality of relevant shell/terminal commands. If the
   * user enters a command that is not any of the known commands, the method prints an error
   * message.
   * 
   * @return an empty string once the command has been executed or null upon failure
   */
  public String execute() {

    String command = super.getCurrentParameters()[0];
    java.util.HashMap<String, String> commandHashMap = new java.util.HashMap<>();
    // populate the commandHashMap with the commands as keys and their descriptions as values

    fillHashMap(commandHashMap);

    // Ensure only extra parameter is for redirection
    if (this.getNumParameters() == 2) {
      return super.printErrorMessage("Invalid parameters for function call.");
    }

    // if the user passes a known command, print its description
    if (commandHashMap.containsKey(command))
      return super.redirect(commandHashMap.get(command));

    // if the user passes an unknown command, print error message and return null
    return super.printErrorMessage("No manual entry for " + command);
  }

}


