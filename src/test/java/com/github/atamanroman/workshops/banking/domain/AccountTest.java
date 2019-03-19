package com.github.atamanroman.workshops.banking.domain;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

public class AccountTest {

  private Account src;

  @Before
  public void setup() {
    src = new Account("DE1234567890", 1000, "EUR", "Roman");
  }

  @Test
  public void isProperlyCreated() {
    assertEquals(1000, src.getSaldo());
    assertEquals("DE1234567890", src.getIban());
    assertEquals("Roman", src.getOwner());
    assertEquals("EUR", src.getCurrency());
    assertEquals(0, src.getTransactions().size());
  }

  @Test
  public void toReferenceKeepsValues() {
    var accountReference = src.toReference();
    assertEquals(src.getIban(), accountReference.getIban());
    assertEquals(src.getOwner(), accountReference.getOwner());
  }

  @Test
  public void overdraftNotAllowed() {
    try {
      src.sendMoney(new AccountReference("Basti", "DE1234567891"), new Money("EUR", 1001));
      fail("OverdraftNotAllowedException now thrown");
    } catch (OverdraftNotAllowedException e) {
      // OK
    }
  }

  @Test
  public void transactionWithSufficientFundsGoesThrough() {
    var tx = src.sendMoney(
        new AccountReference("Basti", "DE1234567891"),
        new Money("EUR", 100)
    );
    assertEquals(LocalDate.now(), tx.getBookingDate());
    assertEquals(tx.getMoney(), new Money("EUR", 100));
    assertEquals(900, src.getSaldo());
  }

  @Test
  public void noMoneyConversionAllowed() {
    try {
      src.sendMoney(
          new AccountReference("Basti", "DE1234567891"),
          new Money("USD", 100)
      );
      fail("OverdraftNotAllowedException now thrown");
    } catch (ConversionNeededException e) {
      // OK
    }
  }
}