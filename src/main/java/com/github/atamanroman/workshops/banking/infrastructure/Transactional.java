package com.github.atamanroman.workshops.banking.infrastructure;

import com.github.atamanroman.workshops.banking.domain.Transaction;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Transactional {

  public static Logger log = LoggerFactory.getLogger(Transaction.class);

  private Transactional() {
  }

  public static <T> T doInTransaction(EntityManagerFactory emf, ReturnWithEm<T> code) {
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    tx.begin();
    try {
      T result = code.run(em);
      tx.commit();
      return result;
    } catch (Exception e) {
      tx.rollback();
      throw new TransactionRolledBackException(e);
    }
  }

  public static void doInTransaction(EntityManagerFactory emf, VoidWithEm code) {
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

  public static class TransactionRolledBackException extends RuntimeException {

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
