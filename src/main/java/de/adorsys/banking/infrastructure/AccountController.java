package de.adorsys.banking.infrastructure;

import de.adorsys.banking.domain.Account;
import de.adorsys.banking.domain.BankingService;
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

  private BankingService bankingService;

  public AccountController(BankingService bankingService) {
    this.bankingService = Params.notNull(bankingService, "bankingService");
  }

  @GetMapping("/accounts")
  public List<Account> getAccountList() {
    return bankingService.readAccounts();
  }

  @GetMapping("/accounts/{iban}")
  public ResponseEntity<Account> getAccount(@PathVariable String iban) {

    return bankingService.readAccount(iban)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }
}
