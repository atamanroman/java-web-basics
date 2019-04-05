package de.adorsys.banking.domain;

public class OverdraftNotAllowedException extends BankingException {

  public OverdraftNotAllowedException() {
  }

  public OverdraftNotAllowedException(String message) {
    super(message);
  }

  public OverdraftNotAllowedException(String message, Throwable cause) {
    super(message, cause);
  }

  public OverdraftNotAllowedException(Throwable cause) {
    super(cause);
  }

  public OverdraftNotAllowedException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
