package de.adorsys.banking.domain;

public interface AccountRepositoryCustom {

  Account createAccount(
      String iban, String owner, String currency, int saldo
  );
}
