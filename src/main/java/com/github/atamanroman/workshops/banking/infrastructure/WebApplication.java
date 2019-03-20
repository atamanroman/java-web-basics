package com.github.atamanroman.workshops.banking.infrastructure;

import com.zaxxer.hikari.HikariDataSource;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.flywaydb.core.Flyway;

public class WebApplication {

  private static HikariDataSource dataSource;

  public static void main(String[] args) throws Exception {

    dataSource = new HikariDataSource();
    dataSource.setJdbcUrl("jdbc:h2:mem:banking;DB_CLOSE_DELAY=-1");
    dataSource.setUsername("admin");
    dataSource.setPassword("secret");

    Flyway flyway = Flyway.configure().dataSource(dataSource).load();
    flyway.migrate();

    var server = new Server(8080);
    var servletHandler = new ServletHandler();
    servletHandler.addServletWithMapping(AccountServlet.class, "/accounts/*");
    servletHandler.addServletWithMapping(HelloWorldServlet.class, "/");
    server.setHandler(servletHandler);
    server.start();
    server.join();
  }

}
