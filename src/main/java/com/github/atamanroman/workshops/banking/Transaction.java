package com.github.atamanroman.workshops.banking;

import java.time.LocalDate;

public class Transaction {

  private AccountReference debtor;
  private AccountReference creditor;
  private Money money;
  private LocalDate bookingDate;

  public Transaction(AccountReference debtor, AccountReference creditor, Money money,
      LocalDate bookingDate) {
    if (debtor == null) {
      throw new IllegalArgumentException("debtor must not be null");
    }
    if (creditor == null) {
      throw new IllegalArgumentException("creditor must not be null");
    }
    if (money == null) {
      throw new IllegalArgumentException("money must not be null");
    }
    if (bookingDate == null) {
      throw new IllegalArgumentException("bookingDate must not be null");
    }

    this.debtor = debtor;
    this.creditor = creditor;
    this.money = money;
    this.bookingDate = bookingDate;
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

}
