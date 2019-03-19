package com.github.atamanroman.workshops.banking.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Account {

  private String iban;
  private String owner;
  private List<Transaction> transactions;
  private int saldo;
  private String currency;

  public Account(String iban, int saldo, String currency, String owner) {
    this(iban, saldo, currency, owner, new ArrayList<>());
  }

  public Account(String iban, int saldo, String currency, String owner,
      List<Transaction> transactions) {
    if (iban == null) {
      throw new IllegalArgumentException("iban must not be null");
    }
    if (currency == null) {
      throw new IllegalArgumentException("currency must not be null");
    }
    if (owner == null) {
      throw new IllegalArgumentException("owner must not be null");
    }
    if (transactions == null) {
      throw new IllegalArgumentException("transactions must not be null");
    }

    this.iban = iban;
    this.saldo = saldo;
    this.currency = currency;
    this.owner = owner;
    this.transactions = transactions;
  }

  public String getIban() {
    return iban;
  }

  public List<Transaction> getTransactions() {
    return new ArrayList<>(transactions);
  }

  public String getOwner() {
    return owner;
  }

  public int getSaldo() {
    return saldo;
  }

  public String getCurrency() {
    return currency;
  }

  public Transaction sendMoney(AccountReference creditor, Money amount) {
    if (creditor == null) {
      throw new IllegalArgumentException("creditor must not be null");
    }
    if (amount == null) {
      throw new IllegalArgumentException("amount must not be null");
    }
    if (!Objects.equals(amount.getCurrency(), currency)) {
      throw new ConversionNeededException();
    }
    if (amount.getAmount() > this.saldo) {
      throw new OverdraftNotAllowedException();
    }

    saldo -= amount.getAmount();
    var newTx = new Transaction(toReference(), creditor, amount, LocalDate.now());
    transactions.add(newTx);
    return newTx;
  }

  AccountReference toReference() {
    return new AccountReference(owner, iban);
  }

}
