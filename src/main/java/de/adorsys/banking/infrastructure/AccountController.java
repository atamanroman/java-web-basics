package de.adorsys.banking.infrastructure;

import de.adorsys.banking.domain.Account;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

  private static Logger log = LoggerFactory.getLogger(AccountController.class);

//  private BankingService bankingService;

//  public AccountServlet(BankingService bankingService) {
//    this.bankingService = Params.notNull(bankingService, "bankingService");
//  }

  @GetMapping("/accounts")
  public List<Account> getAccountList() {
    return Arrays.asList(new Account("DE12345", 1000, "EUR", "Basti"));
  }

  @GetMapping("/accounts/{iban}")
  public Account getAccount(@PathVariable String iban) {
    if (iban.equals("DE12345")) {
      return new Account("DE12345", 1000, "EUR", "Basti");
    }
    return null;
  }
}
