package de.adorsys.banking.domain;

import de.adorsys.banking.infrastructure.Params;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class AccountRepositoryImpl implements AccountRepositoryCustom {

  @PersistenceContext
  EntityManager em;

  @Override
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

}
