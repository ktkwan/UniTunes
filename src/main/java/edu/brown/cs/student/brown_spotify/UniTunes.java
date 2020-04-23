package edu.brown.cs.student.brown_spotify;
import edu.brown.cs.student.commands.Command;
import org.eclipse.jetty.server.Authentication;

import java.util.HashMap;

public class UniTunes {

  UserCommand userCommand;
  DatabaseCommand dbCommand;
  SuggestCommand suggestCommand;
  ConnectCommand connectCommand;
  
  public UniTunes() {
    userCommand = new UserCommand();
    dbCommand = new DatabaseCommand();
    suggestCommand = new SuggestCommand();
    connectCommand = new ConnectCommand();

  }

  public Command getUserCommand(){
    return userCommand;
  }

  public Command getDatabaseCommand() {
    return dbCommand;
  }

  public Command getSuggestCommand() {
    return suggestCommand;
  }

  public Command getConnectCommand() {
    return connectCommand;
  }

  /**
   * Nested class for the "User Command"
   * This allows users to create new profiles for themselves.
   */
  class UserCommand implements Command {

      public UserCommand() {

      }

      @Override
      public String runCommand(String[] args) {
        /*
        TO DO: Add user to the database (name, brownu email, profile picture)
         */
        System.out.println("running user command"); 
        return "Running user command";
      }
    }

  /**
   * Nested class for the "Database Command"
   * This allows users to load the song database.
   */
  class DatabaseCommand implements Command {
    public DatabaseCommand(){

    }

    @Override
    public String runCommand(String[] args) {
      /*
      TO DO: Load the database
       */
      System.out.println("running database command");
      return "running database command";
    }
  }

  /**
   * Nested class for the "Suggest Command"
   * This suggests student made songs to the user based on a non-student-produced song they like.
   */
  class SuggestCommand implements Command {

    @Override
    public String runCommand(String[] args) {
      /*
      TO DO: Implement kNN and algorithm lol 
       */
      System.out.println("running suggest command");
      return "running suggest command";
    }
  }

  /**
   * Nested class for the "Connect Command"
   * This allows student artists to connect with other student artists.
   */
  class ConnectCommand implements Command {

    @Override
    public String runCommand(String[] args) {
      /*
      TO DO: Implement a feature where users can send a message to other student artists wanting
      to collaborate (like linkedin)
       */
      System.out.println("running connect command");
      return "running connect command";
    }
  }

}

