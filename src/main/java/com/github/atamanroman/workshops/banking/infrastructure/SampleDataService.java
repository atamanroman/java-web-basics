package com.github.atamanroman.workshops.banking.infrastructure;

import com.github.atamanroman.workshops.banking.domain.Account;
import com.github.atamanroman.workshops.banking.domain.AccountReference;
import com.github.atamanroman.workshops.banking.domain.AccountRepository;
import com.github.atamanroman.workshops.banking.domain.BankingException;
import com.github.atamanroman.workshops.banking.domain.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleDataService {

  private static Logger log = LoggerFactory.getLogger(SampleDataService.class);

  private AccountRepository accountRepository;

  public SampleDataService(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }


  public void init() {
    log.info("Insert sample data");
    try {
      Account acc1 = accountRepository.createAccount("DE1234567890", "Roman", "EUR", 1000);
      acc1.sendMoney(new AccountReference("Basti", "DE1234567893"), new Money("EUR", 100));
      acc1.sendMoney(new AccountReference("Isabella", "DE1234567894"), new Money("EUR", 100));
      accountRepository.saveAccount(acc1);
      Account acc2 = accountRepository.createAccount("DE1234567891", "Andi", "EUR", 2000);
      acc2.sendMoney(new AccountReference("Basti", "DE1234567893"), new Money("EUR", 50));
      acc2.sendMoney(new AccountReference("Annina", "DE1234567895"), new Money("EUR", 100));
      accountRepository.saveAccount(acc2);
      Account acc3 = accountRepository.createAccount("DE1234567892", "Nadja", "EUR", 3000);
      acc3.sendMoney(new AccountReference("Annina", "DE1234567895"), new Money("EUR", 100));
      acc3.sendMoney(new AccountReference("Isabella", "DE1234567894"), new Money("EUR", 100));
      accountRepository.saveAccount(acc3);
    } catch (Exception e) {
      throw new BankingException("Could not init sample data", e);
    }
  }
}
