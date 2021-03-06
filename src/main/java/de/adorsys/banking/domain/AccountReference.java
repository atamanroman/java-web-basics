package de.adorsys.banking.domain;

import de.adorsys.banking.infrastructure.Params;
import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class AccountReference {

  private String owner;
  private String iban;

  private AccountReference() {
  }

  public AccountReference(String owner, String iban) {
    this.owner = Params.notNull(owner, "owner");
    this.iban = Params.notNull(iban, "iban");
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

  @Override
  public String toString() {
    return "AccountReference{" + iban + " (" + owner + ")" + '}';
  }
}
