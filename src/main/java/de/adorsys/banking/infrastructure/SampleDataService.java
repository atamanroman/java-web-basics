package de.adorsys.banking.infrastructure;

import de.adorsys.banking.domain.Account;
import de.adorsys.banking.domain.AccountReference;
import de.adorsys.banking.domain.AccountRepository;
import de.adorsys.banking.domain.BankingException;
import de.adorsys.banking.domain.Money;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SampleDataService {

  @Autowired
  private SampleDataService self;
  private static Logger log = LoggerFactory.getLogger(SampleDataService.class);

  private AccountRepository accountRepository;

  public SampleDataService(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  public void create() {
    log.info("Insert sample data");
    try {
      Account acc1 = accountRepository.createAccount("DE1234567890", "Roman", "EUR", 1000);
      acc1.sendMoney(new AccountReference("Basti", "DE1234567893"), new Money("EUR", 100));
      acc1.sendMoney(new AccountReference("Isabella", "DE1234567894"), new Money("EUR", 100));
      accountRepository.save(acc1);
      Account acc2 = accountRepository.createAccount("DE1234567891", "Andi", "EUR", 2000);
      acc2.sendMoney(new AccountReference("Basti", "DE1234567893"), new Money("EUR", 50));
      acc2.sendMoney(new AccountReference("Annina", "DE1234567895"), new Money("EUR", 100));
      accountRepository.save(acc2);
      Account acc3 = accountRepository.createAccount("DE1234567892", "Nadja", "EUR", 3000);
      acc3.sendMoney(new AccountReference("Annina", "DE1234567895"), new Money("EUR", 100));
      acc3.sendMoney(new AccountReference("Isabella", "DE1234567894"), new Money("EUR", 100));
      accountRepository.save(acc3);
    } catch (Exception e) {
      throw new BankingException("Could not init sample data", e);
    }
  }

  @PostConstruct
  public void init() {
    self.create();
  }
}

