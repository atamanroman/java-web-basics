package de.adorsys.banking.domain;

import de.adorsys.banking.infrastructure.Params;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRepository {

  private static Logger log = LoggerFactory.getLogger(AccountRepository.class);

  @PersistenceContext
  EntityManager em;

  public List<Account> getAccounts() {
    return em.createQuery("FROM Account", Account.class).getResultList();
  }

  public Optional<Account> getAccountByIban(String iban) {
    Params.notNull(iban, "iban");

    TypedQuery<Account> query = em.createQuery(
        "FROM Account a WHERE a.iban = :iban", Account.class
    );
    query.setParameter("iban", iban);
    List<Account> resultList = query.getResultList();
    assert resultList.size() <= 1;
    return resultList.stream().findFirst();
  }

  public Account createAccount(
      String iban, String owner, String currency, int saldo
  ) {
    Params.notNull(iban, "iban");
    Params.notNull(owner, "owner");
    Params.notNull(currency, "currency");

    Account account = new Account(iban, saldo, currency, owner);
    em.persist(account);
    return account;
  }

  public Account saveAccount(Account account) {
    Params.notNull(account, "account");
    account.getTransactions().forEach(em::merge);
    return em.merge(account);
  }
}
