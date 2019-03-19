package com.github.atamanroman.workshops.banking.infrastructure;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

public class WebApplication {

  public static void main(String[] args) throws Exception {
    var server = new Server(8080);
    var servletHandler = new ServletHandler();
    servletHandler.addServletWithMapping(AccountServlet.class, "/accounts/*");
    servletHandler.addServletWithMapping(HelloWorldServlet.class, "/");
    server.setHandler(servletHandler);
    server.start();
    server.join();
  }

}
