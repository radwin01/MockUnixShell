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

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import filesystem.CurrentWorkingDirectoryI;
import filesystem.FileSystemI;
import inputoutput.UserTerminalIn;

/**
 * Represents a Speak command.
 * 
 * @version 1.0
 */

public class Speak extends Command {

  // The voice to be used to speak the text-to-speech output
  private Voice voice;

  // Stores the voices which will be determined by voice
  private VoiceManager voiceManager;

  // Allows for the use of the getContinuousInput method in UserTerminalIn
  private UserTerminalIn terminalIn;

  /**
   * Create an instance of the Speak object. It takes in a UserTerminalIn as a parameter to
   * initialize terminalIn, in order to use the getContinuousInput method. This constructor also
   * initializes voice and voiceManager.
   */
  public Speak(FileSystemI fs, CurrentWorkingDirectoryI cwd, UserTerminalIn terminalIn) {

    super(fs, cwd);

    // Set voice and user terminal
    this.voiceManager = VoiceManager.getInstance();
    this.voice = voiceManager.getVoice("kevin16");
    voice.allocate();
    this.terminalIn = terminalIn;
  }

  /**
   * Method that returns the description of the speak command.
   * 
   * @return the description of the command
   */
  public static String getDescriptionOfCommand() {
    return "Name of command: speak\n\n"
        + "Functionality: Converts string to audible speech. If the user passes\n"
        + "all string arguments in one line, they MUST be encased with quotation marks.\n"
        + "However, if the user passes string arguments spanning multiple lines, they do NOT\n"
        + "have to be encased in quotation marks, but MUST end with the word QUIT to obtain\n"
        + "the audible version of all the string arguments. \n\n"
        + "Command syntax: speak stringArgs [... QUIT]";
  }

  /**
   * Takes the phrase inputed by the user (from super.current_parameters) and converts it from text
   * to speech.
   * 
   * @return the spoken phrase, null if the input is invalid
   */
  public String execute() {

    // Checking if the user has inputed a quoted phrase to speak.
    if (super.getNumParameters() > 0) {

      // Storing the phrase as a single String.
      String possibleQuote = "";
      for (int i = 0; i < super.getNumParameters(); i++)
        possibleQuote = possibleQuote + super.getCurrentParameters()[i] + " ";

      // Checking if the phrase is quoted validly.
      if (!isCorrectlyQuotedInput(possibleQuote)) {
        return super.printErrorMessage("Phrase is incorrectly quoted");
      } else {
        voice.speak(possibleQuote);
        return possibleQuote;
      }
    }

    // Since the function has not returned, the user has opted to input continuously.
    java.util.ArrayList<String> continuousInput = terminalIn.getContinuousUserInput();

    // Storing the phrase from continuousInput as a single String.
    String phrase = "";

    /*
     * Calling the speak function from Voice which does the text to speech conversion and adding
     * each spoken line to phrase.
     */
    for (String toSay : continuousInput) {
      voice.speak(toSay);
      phrase = phrase + toSay + "\n";
    }

    return phrase;
  }


  /**
   * Takes a String, 'input', and checks if it is correctly quoted. A correctly quoted String
   * contains two quotation marks. One at the first letter, and one at the last letter. For
   * example, "hello world" is correctly quoted, but h"ello world" is not.
   * 
   * @param input the String to be checked for correctly quoted format
   * @return true if input is correctly quoted, false otherwise.
   */
  private boolean isCorrectlyQuotedInput(String input) {

    /*
     * Checking if there are exactly two quotes in input and if they occur at the beginning and end
     * of input.
     */
    if (Validation.countQuoteOccurences(input) != 2 || input.charAt(0) != '"'
        || input.charAt(input.length() - 2) != '"')
      return false;

    return true;
  }
}
