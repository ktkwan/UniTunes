package edu.brown.cs.student.database;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

/**
 * The Main class of our project. This is where execution begins.
 */
public final class MainCopy {

  private static final int DEFAULT_PORT = 4567;

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) throws SQLException, ClassNotFoundException {
    new MainCopy(args).run();
  }

  private String[] args;

  private MainCopy(String[] args) {
    this.args = args;
  }

  private void run() throws SQLException, ClassNotFoundException {

    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
            .defaultsTo(DEFAULT_PORT);
    parser.accepts("prefix");
    parser.accepts("whitespace");
    parser.accepts("stats");
    OptionSpec<Integer> ledSpec =
            parser.accepts("led").withRequiredArg().ofType(Integer.class);
    OptionSpec<String> dataSpec =
            parser.accepts("data").withRequiredArg().ofType(String.class);

    OptionSpec<String> databaseSpec =
            parser.accepts("database").withRequiredArg().ofType(String.class);
    OptionSpec<String> deleteSpec = parser.accepts("delete").withRequiredArg().ofType(String.class);

    OptionSet options = parser.parse(args);

    if (options.has("gui")) {

    } else if (options.has("data") || options.has("database")) {

      Database db;


      if (options.has("database")) {

        try {
          db = new Database(options.valueOf(databaseSpec));

          if (options.has("data")) {
        	
            String files = options.valueOf(dataSpec);
            System.out.println(files);
            List<String> fileNames = new ArrayList<String>(Arrays.asList(files.split(",")));

            for (String file : fileNames) {
              db.read_csv(file);
            }
          }
     
        } catch (SQLException e) {
          System.err.println("SQLite error: " + e.getMessage());
          System.exit(1);
        }
      } else {
        String files = options.valueOf(dataSpec);
      }

      try (BufferedReader br = new BufferedReader(
              new InputStreamReader(System.in))) {
        String input;
        while ((input = br.readLine()) != null) {

        }
        br.close();
      } catch (Exception e) {
        System.out.println("ERROR: Invalid input for REPL");
        System.out.println(e.getClass());

        for (StackTraceElement i : e.getStackTrace()) {
          System.out.println(i.toString());
        }

      }
    } else {
      System.out.println("ERROR: usage");
      System.out.print("./run --data=<list of files> \n[--prefix] [--whitespace] [--led=<led>]\n");
    }
  }

}
