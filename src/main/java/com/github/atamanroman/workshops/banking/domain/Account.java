package com.github.atamanroman.workshops.banking.domain;

import com.github.atamanroman.workshops.banking.infrastructure.Params;
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

  public Account(
      String iban,
      int saldo,
      String currency,
      String owner,
      List<Transaction> transactions
  ) {
    this.iban = Params.notNull(iban, "iban");
    this.saldo = Params.notNull(saldo, "saldo");
    this.currency = Params.notNull(currency, "currency");
    this.owner = Params.notNull(owner, "owner");
    this.transactions = Params.notNull(transactions, "transactions");
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
    Params.notNull(creditor, "creditor");
    Params.notNull(amount, "amount");
    if (!Objects.equals(amount.getCurrency(), currency)) {
      throw new ConversionNeededException();
    }
    if (amount.getAmount() > this.saldo) {
      throw new OverdraftNotAllowedException();
    }

    saldo -= amount.getAmount();
    var newTx = new Transaction(iban, toReference(), creditor, amount, LocalDate.now());
    transactions.add(newTx);
    return newTx;
  }

  AccountReference toReference() {
    return new AccountReference(owner, iban);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Account account = (Account) o;
    return iban.equals(account.iban);
  }

  @Override
  public int hashCode() {
    return Objects.hash(iban);
  }

  @Override
  public String toString() {
    return "Account{" +
        "iban='" + iban + '\'' +
        ", owner='" + owner + '\'' +
        ", transactions=" + transactions +
        ", saldo=" + saldo +
        ", currency='" + currency + '\'' +
        '}';
  }
}
