package com.github.atamanroman.workshops.banking.domain;

import com.github.atamanroman.workshops.banking.infrastructure.Params;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Transaction {

  private UUID id;
  private String account;
  private AccountReference debtor;
  private AccountReference creditor;
  private Money money;
  private LocalDate bookingDate;

  public Transaction(
      String account,
      AccountReference debtor,
      AccountReference creditor,
      Money money,
      LocalDate bookingDate
  ) {
    this(UUID.randomUUID(), account, debtor, creditor, money, bookingDate);
  }

  public Transaction(
      UUID id,
      String account,
      AccountReference debtor,
      AccountReference creditor,
      Money money,
      LocalDate bookingDate
  ) {
    this.id = Params.notNull(id, "id");
    this.account = Params.notNull(account, "account");
    this.debtor = Params.notNull(debtor, "debtor");
    this.creditor = Params.notNull(creditor, "creditor");
    this.money = Params.notNull(money, "money");
    this.bookingDate = Params.notNull(bookingDate, "bookingDate");
  }

  public AccountReference getDebtor() {
    return debtor;
  }

  public AccountReference getCreditor() {
    return creditor;
  }

  public Money getMoney() {
    return money;
  }

  public LocalDate getBookingDate() {
    return bookingDate;
  }

  public UUID getId() {
    return id;
  }

  public String getAccount() {
    return account;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Transaction that = (Transaction) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return String.format(
        "Transaction{%s - %s from %s to %s on %s}",
        id, money, debtor, creditor, bookingDate
    );
  }
}
