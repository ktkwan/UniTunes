package edu.brown.cs.student.front_end;

import com.google.common.collect.ImmutableMap;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

import java.util.Map;

public class Login {


  public Login(){

  }

  public static TemplateViewRoute getLoginHandler(){
    return new FrontHandler();
  }

  private static class FrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title",
          "uniTunes", "status", "coming soon");
      return new ModelAndView(variables, "query.ftl");
    }
  }


}
