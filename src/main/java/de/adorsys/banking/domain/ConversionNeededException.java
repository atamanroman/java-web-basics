package de.adorsys.banking.domain;

public class ConversionNeededException extends BankingException {

  public ConversionNeededException() {
  }

  public ConversionNeededException(String message) {
    super(message);
  }

  public ConversionNeededException(String message, Throwable cause) {
    super(message, cause);
  }

  public ConversionNeededException(Throwable cause) {
    super(cause);
  }

  public ConversionNeededException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
