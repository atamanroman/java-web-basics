package com.github.atamanroman.workshops.banking.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.atamanroman.workshops.banking.domain.Account;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AccountServlet extends HttpServlet {

  private Map<String, Account> accounts = new HashMap<>();
  private ObjectMapper objectMapper = new ObjectMapper();

  public AccountServlet() {
    accounts.put("DE1234567890", new Account("DE1234567890", 1000, "EUR", "Roman"));
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    var pathInfo = req.getPathInfo().substring(1);
    resp.setHeader("content-type", "application/json");
    if (accounts.get(pathInfo) != null) {
      resp.getWriter().write(objectMapper.writeValueAsString(accounts.get(pathInfo)));
    } else {
      resp.setStatus(404);
      var errorJson = objectMapper.writeValueAsString(
          new HttpError(404, "Account with id=" + pathInfo + " not found!")
      );
      resp.getWriter().write(errorJson);
    }

  }
}
