package com.github.atamanroman.workshops.banking.infrastructure;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.github.atamanroman.workshops.banking.domain.AccountRepository;
import com.github.atamanroman.workshops.banking.domain.BankingService;
import com.zaxxer.hikari.HikariDataSource;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.flywaydb.core.Flyway;
import org.slf4j.LoggerFactory;

public class WebApplication {

  private static HikariDataSource dataSource;

  public static void main(String[] args) throws Exception {

    Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    root.setLevel(Level.INFO);

    dataSource = new HikariDataSource();
    dataSource.setJdbcUrl("jdbc:h2:mem:banking;DB_CLOSE_DELAY=-1");
    dataSource.setUsername("admin");
    dataSource.setPassword("secret");

    var flyway = Flyway.configure().dataSource(dataSource).load();
    flyway.migrate();

    var objectMapper = new ObjectMapper()
        .registerModule(new ParameterNamesModule())
        .registerModule(new Jdk8Module())
        .registerModule(new JavaTimeModule())
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    var accountRepository = new AccountRepository(dataSource);
    new SampleDataService(accountRepository).init();
    var bankingService = new BankingService(accountRepository);

    var server = new Server(8080);
    var servletHandler = new ServletHandler();
    servletHandler.addServletWithMapping(
        new ServletHolder(new AccountServlet(bankingService, objectMapper)),
        "/accounts/*"
    );
    server.setHandler(servletHandler);
    server.start();
    server.join();
  }

}
