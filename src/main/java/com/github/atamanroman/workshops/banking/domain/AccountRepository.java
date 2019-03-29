package com.github.atamanroman.workshops.banking.domain;

import com.github.atamanroman.workshops.banking.infrastructure.Params;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountRepository {

  private static Logger log = LoggerFactory.getLogger(AccountRepository.class);

  public List<Account> getAccounts(EntityManager em) {
    return em.createQuery("FROM Account", Account.class).getResultList();
  }

  public Optional<Account> getAccountByIban(String iban, EntityManager em) {
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
      String iban, String owner, String currency, int saldo, EntityManager em
  ) {
    Params.notNull(iban, "iban");
    Params.notNull(owner, "owner");
    Params.notNull(currency, "currency");

    var account = new Account(iban, saldo, currency, owner);
    em.persist(account);
    return account;
  }

  public Account saveAccount(Account account, EntityManager em) {
    Params.notNull(account, "account");
    account.getTransactions().forEach(em::merge);
    return em.merge(account);
  }
}
