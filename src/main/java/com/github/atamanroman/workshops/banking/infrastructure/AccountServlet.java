package com.github.atamanroman.workshops.banking.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.atamanroman.workshops.banking.domain.Account;
import com.github.atamanroman.workshops.banking.domain.BankingService;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountServlet extends HttpServlet {

  private static Logger log = LoggerFactory.getLogger(AccountServlet.class);

  private ObjectMapper objectMapper = new ObjectMapper();
  private BankingService bankingService;

  public AccountServlet(BankingService bankingService) {
    this.bankingService = bankingService;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    resp.setHeader("content-type", "application/json");

    Optional<String> iban = getIbanFromPath(req);

    if (iban.isEmpty()) {
      resp.getWriter().write(writeJson(bankingService.readAccounts()));
      return;
    }

    Optional<Account> account = iban.flatMap(bankingService::readAccount);
    account.ifPresentOrElse(s -> resp.setStatus(200), () -> resp.setStatus(404));
    String json = account
        .map(this::writeJson)
        .orElseGet(
            () -> writeJson(new HttpError(404, "Account with iban=" + iban.get() + " not found!"))
        );

    resp.getWriter().write(json);
  }

  private Optional<String> getIbanFromPath(HttpServletRequest req) {
    if (req.getPathInfo() == null || req.getPathInfo().equals("/")) {
      return Optional.empty();
    }
    var iban = req.getPathInfo().substring(1);
    return Optional.of(iban);
  }

  private String writeJson(Object body) {
    try {
      return objectMapper.writeValueAsString(body);
    } catch (JsonProcessingException e) {
      log.info("Could not serialize {} to JSON", body);
      try {
        return objectMapper.writeValueAsString(new HttpError(500, "Could not serialize response"));
      } catch (JsonProcessingException inner) {
        log.warn("Serializing HttpError to JSON failed!");
        return "{\"code\": 500, \"message\":\"Could not serialize response\"}";
      }
    }
  }
}
