package com.github.atamanroman.workshops.banking;

import java.util.Objects;

public class AccountReference {

  private String owner;
  private String iban;

  public AccountReference(String owner, String iban) {
    if (owner == null) {
      throw new IllegalArgumentException("owner must not be null");
    }
    if (iban == null) {
      throw new IllegalArgumentException("owner must not be null");
    }

    this.owner = owner;
    this.iban = iban;
  }

  public String getOwner() {
    return owner;
  }

  public String getIban() {
    return iban;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AccountReference that = (AccountReference) o;
    return iban.equals(that.iban);
  }

  @Override
  public int hashCode() {
    return Objects.hash(iban);
  }
}
