package de.adorsys.banking.domain;

import de.adorsys.banking.infrastructure.Params;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class BankingService {

  private AccountRepository accountRepository;

  public BankingService(AccountRepository accountRepository) {
    this.accountRepository = Params.notNull(accountRepository, "accountRepository");
  }

  public List<Account> readAccounts() {
    return accountRepository.getAccounts();
    }

  public Optional<Account> readAccount(String iban) {
    Params.notNull(iban, "iban");
    return accountRepository.getAccountByIban(iban);
  }
}
