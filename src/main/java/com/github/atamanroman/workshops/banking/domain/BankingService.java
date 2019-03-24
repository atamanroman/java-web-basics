package com.github.atamanroman.workshops.banking.domain;

import java.util.List;
import java.util.Optional;

public class BankingService {

  private AccountRepository accountRepository;

  public BankingService(AccountRepository accountRepository) {
    if (accountRepository == null) {
      throw new IllegalArgumentException("accountRepository must not be null");
    }
    this.accountRepository = accountRepository;
  }

  public List<Account> readAccounts() {
    return accountRepository.getAccounts();
  }

  public Optional<Account> readAccount(String iban) {
    if (iban == null) {
      throw new IllegalArgumentException("iban must not be null");
    }
    return accountRepository.getAccountByIban(iban);
  }
}
