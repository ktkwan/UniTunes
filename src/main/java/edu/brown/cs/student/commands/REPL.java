package edu.brown.cs.student.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.regex.Pattern;

public class REPL {

  public HashMap<String, Command> commands;
  Pattern regex = Pattern.compile("\\s+(?=(?:[^\"]*[\"][^\"]*[\"])*[^\"]*$)");

  // Takes in a hashmap of commands to pass the input to
  public REPL(HashMap<String, Command> commandable) {
    commands = commandable;
  }

  public void runRepl() {
    BufferedReader reader = new BufferedReader(
        new InputStreamReader(System.in));
    try {
      String input;
      // Using a regex pattern to make sure strings within quotation marks are
      // preserved.
      while ((input = reader.readLine()) != null) {
        String[] replInput = input.split(regex.pattern());
        this.runCommand(replInput);
      }
      // If the input is invalid, raise an input exception
    } catch (IOException io) {
      System.err.println("ERROR: runRepl IOException");
      System.exit(1);

    }

  }

  // This calls the runCommand function in any "Commandable" class/object.
  public void runCommand(String[] input) {
    Command validCommand;
    if (commands.containsKey(input[0])) {
      validCommand = commands.get(input[0]);
      validCommand.runCommand(input);
    } else {
      System.err.println("ERROR: invalid command");
    }

  }


}

