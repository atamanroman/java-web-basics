package com.github.atamanroman.workshops.banking.infrastructure;

import com.github.atamanroman.workshops.banking.domain.BankingException;
import com.github.atamanroman.workshops.banking.domain.Transaction;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Transactional {

  static EntityManagerFactory emf; // set by WebApplication
  private static final String EMF_MUST_BE_SET = "EntityManagerFactory must be set from outside before doing actual work";
  private static Logger log = LoggerFactory.getLogger(Transaction.class);

  private Transactional() {
  }

  public static <T> T run(ReturnWithEm<T> code) {
    assertEmfPresent();
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    tx.begin();
    try {
      T result = code.run(em);
      tx.commit();
      return result;
    } catch (Exception e) {
      tx.rollback();
      throw new TransactionRolledBackException("Transaction rolled back", e);
    }
  }

  public static void run(VoidWithEm code) {
    assertEmfPresent();
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    tx.begin();
    try {
      code.run(em);
      tx.commit();
    } catch (Exception e) {
      tx.rollback();
      throw new TransactionRolledBackException("Transaction rolled back", e);
    } finally {
      em.close();
    }
  }

  private static void assertEmfPresent() {
    if (emf == null) {
      throw new IllegalStateException(EMF_MUST_BE_SET);
    }
  }

  public static class TransactionRolledBackException extends BankingException {

    public TransactionRolledBackException() {
    }

    public TransactionRolledBackException(String message) {
      super(message);
    }

    public TransactionRolledBackException(String message, Throwable cause) {
      super(message, cause);
    }

    public TransactionRolledBackException(Throwable cause) {
      super(cause);
    }

    public TransactionRolledBackException(String message, Throwable cause,
        boolean enableSuppression,
        boolean writableStackTrace) {
      super(message, cause, enableSuppression, writableStackTrace);
    }
  }

  @FunctionalInterface
  public interface ReturnWithEm<R> {

    R run(EntityManager em) throws Exception;
  }

  @FunctionalInterface
  public interface VoidWithEm {

    void run(EntityManager em) throws Exception;
  }
}
