package de.adorsys.banking.infrastructure;

public class Params {

  private Params() {
  }

  public static <T> T notNull(T param, String paramName) {
    if (param == null) {
      throw new IllegalArgumentException(paramName + "must not be null");
    }
    return param;
  }
}
