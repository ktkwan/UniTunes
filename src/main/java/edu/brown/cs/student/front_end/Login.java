package edu.brown.cs.student.front_end;

import com.google.common.collect.ImmutableMap;
import edu.brown.cs.student.database.UserDatabase;
import org.eclipse.jetty.server.Authentication;
import spark.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class Login {

  private static UserDatabase db;

  public Login(){

  }

  public static TemplateViewRoute getLoginHandler(){
    return new FrontHandler();
  }

  public static TemplateViewRoute getCreateHandler() { return new CreateHandler(); }

  /**
   * Handler that opens the login page
   */
  private static class FrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title",
          "uniTunes", "status", "coming soon");
      return new ModelAndView(variables, "query.ftl");
    }
  }

  /**
   * Handles the creation of a new account
   */
  private static class CreateHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) throws SQLException {
      String firstName = req.queryParams("firstName");
      String lastName = req.queryParams("lastName");
      String email = req.queryParams("email");
      String password = req.queryParams("password");
      String confirmPassword = req.queryParams("confirm-password");
      try {
        UserDatabase.addNewUser(firstName, lastName, email, password);
      } catch(SQLException e) {
        System.out.println("ERROR: Unable to add new user to User Database");
      }
      String welcomeMessage = "Welcome" + " " + firstName + "!";
      Map<String, Object> variables = ImmutableMap.of("status",
          welcomeMessage);
      return new ModelAndView(variables,"new_account.ftl");
    }
  }



}
