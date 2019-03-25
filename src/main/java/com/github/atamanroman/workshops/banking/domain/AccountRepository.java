package com.github.atamanroman.workshops.banking.domain;

import com.github.atamanroman.workshops.banking.infrastructure.Params;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountRepository {

  private static Logger log = LoggerFactory.getLogger(AccountRepository.class);

  private EntityManagerFactory emf;

  public AccountRepository(EntityManagerFactory emf) {
    this.emf = Params.notNull(emf, "emf");
  }

  public List<Account> getAccounts() {
    EntityManager em = emf.createEntityManager();
    return em.createQuery("FROM Account", Account.class).getResultList();
  }

  public Optional<Account> getAccountByIban(String iban) {
    Params.notNull(iban, "iban");

    EntityManager em = emf.createEntityManager();
    TypedQuery<Account> query = em
        .createQuery("FROM Account a WHERE a.iban = :iban", Account.class);
    query.setParameter("iban", iban);
    List<Account> resultList = query.getResultList();
    assert resultList.size() <= 1;
    return resultList.stream().findFirst();
  }

  public Account createAccount(String iban, String owner, String currency, int saldo) {
    Params.notNull(iban, "iban");
    Params.notNull(owner, "owner");
    Params.notNull(currency, "currency");

    var account = new Account(iban, saldo, currency, owner);
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    tx.begin();
    try {
      em.persist(account);
    } catch (Exception e) {
      tx.rollback();
      throw e;
    }
    tx.commit();
    return account;
  }

  public Account saveAccount(Account account) {
    Params.notNull(account, "account");

    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    tx.begin();
    try {
      account.getTransactions().forEach(em::merge);
      Account result = em.merge(account);
      tx.commit();
      return result;
    } catch (Exception e) {
      tx.rollback();
      throw e;
    }
  }
}
