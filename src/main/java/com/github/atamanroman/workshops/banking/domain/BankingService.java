package com.github.atamanroman.workshops.banking.domain;

import com.github.atamanroman.workshops.banking.infrastructure.Params;
import com.github.atamanroman.workshops.banking.infrastructure.Transactional;
import java.util.List;
import java.util.Optional;

public class BankingService {

  private AccountRepository accountRepository;

  public BankingService(AccountRepository accountRepository) {
    this.accountRepository = Params.notNull(accountRepository, "accountRepository");
  }

  public List<Account> readAccounts() {
    return Transactional.run(em -> {
      return accountRepository.getAccounts(em);
    });
  }

  public Optional<Account> readAccount(String iban) {
    Params.notNull(iban, "iban");
    return Transactional.run(em -> {
      return accountRepository.getAccountByIban(iban, em);
    });

  }
}
