package edu.brown.cs.student.application;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.*;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.student.brown_spotify.UniTunes;
import edu.brown.cs.student.commands.Command;
import edu.brown.cs.student.commands.REPL;
import edu.brown.cs.student.database.SongDatabase;
import freemarker.template.Configuration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

/**
 * The Main class of our project. This is where execution begins.
 *
 */
public final class Main {

  private static final int DEFAULT_PORT = 4567;

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;

  private Main(String[] args) {
    this.args = args;
  }

  private void run() {
    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);
    OptionSpec<String> dataSpec =
        parser.accepts("data").withRequiredArg().ofType(String.class);

    OptionSpec<String> databaseSpec =
        parser.accepts("database").withRequiredArg().ofType(String.class);
    OptionSet options = parser.parse(args);

    if (options.has("gui")) {
      runSparkServer((int) options.valueOf("port"));

    } else if (options.has("data") || options.has("database")) {

      SongDatabase db;
      
      if (options.has("database")) {

        try {
          db = new SongDatabase(options.valueOf(databaseSpec));

          if (options.has("data")) {

            String files = options.valueOf(dataSpec);
            System.out.println(files);
            List<String> fileNames = new ArrayList<String>(Arrays.asList(files.split(",")));

            for (String file : fileNames) {
              db.read_csv(file);
            }
          }

        } catch (SQLException | ClassNotFoundException e) {
          System.err.println("SQLite error: " + e.getMessage());
          System.exit(1);
        }
      } else {
        String files = options.valueOf(dataSpec);
      }
    }
    // Creating a hashmap of commands that corresponds to different classes that implement the
    // Command interface
    HashMap<String, Command> map = new HashMap<String, Command>();
    UniTunes uniTunesProgram = new UniTunes();
    map.put("user", uniTunesProgram.getUserCommand());
    map.put("db", uniTunesProgram.getDatabaseCommand());
    map.put("suggest", uniTunesProgram.getSuggestCommand());
    map.put("connect", uniTunesProgram.getConnectCommand());
    REPL repl = new REPL(map);
    repl.runRepl();

  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private void runSparkServer(int port) {
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    // Setup Spark Routes
    Spark.get("/unitunes", new FrontHandler(), freeMarker);
  }

  private static class FrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title",
          "uniTunes", "status", "coming soon");
      return new ModelAndView(variables, "query.ftl");
    }
  }

  /**
   * Display an error page when an exception occurs in the server.
   *
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }

}