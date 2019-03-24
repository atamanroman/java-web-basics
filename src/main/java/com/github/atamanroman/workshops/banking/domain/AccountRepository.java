package com.github.atamanroman.workshops.banking.domain;

import com.github.atamanroman.workshops.banking.infrastructure.Params;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountRepository {

  private static Logger log = LoggerFactory.getLogger(AccountRepository.class);

  private DataSource ds;

  public AccountRepository(DataSource ds) {
    this.ds = Params.notNull(ds, "DataSource");
  }

  public List<Account> getAccounts() {
    try (
        var con = ds.getConnection();
        var ps = con.prepareStatement(
            "SELECT * FROM accounts as a LEFT JOIN transactions as t ON a.iban = t.account"
        );
        var rs = ps.executeQuery()
    ) {
      return accountsWithTransactionsFromRS(rs);
    } catch (SQLException e) {
      throw new BankingException("Could not get accounts", e);
    }
  }

  public Optional<Account> getAccountByIban(String iban) {
    Params.notNull(iban, "iban");

    try (
        var con = ds.getConnection();
        var ps = con.prepareStatement(
            "SELECT * FROM accounts as a LEFT JOIN transactions as t"
                + " ON a.iban = t.account WHERE a.iban = ?"
        )
    ) {
      ps.setString(1, iban);
      try (ResultSet rs = ps.executeQuery()) {
        var results = accountsWithTransactionsFromRS(rs);
        assert results.size() <= 1;
        var result = results.stream().findFirst();
        log.info("Found account={} for iban={}", result, iban);
        return result;
      }
    } catch (SQLException e) {
      throw new BankingException("Could find account with iban=" + iban, e);
    }
  }

  public Account createAccount(String iban, String owner, String currency, int saldo) {
    Params.notNull(iban, "iban");
    Params.notNull(owner, "owner");
    Params.notNull(currency, "currency");

    var account = new Account(iban, saldo, currency, owner);
    try (
        var con = ds.getConnection();
        var ps = con.prepareStatement(
            "INSERT INTO accounts(owner, iban, currency, saldo) VALUES(?, ?, ?, ?)"
        )) {
      ps.setString(1, owner);
      ps.setString(2, iban);
      ps.setString(3, currency);
      ps.setInt(4, saldo);
      int inserts = ps.executeUpdate();
      assert inserts == 1;
      log.info("Created account={}", account);
      return account;
    } catch (SQLException e) {
      throw new BankingException("Could not create account=" + account, e);
    }
  }

  public Account saveAccount(Account account) {
    Params.notNull(account, "account");
    try (Connection con = ds.getConnection();
        PreparedStatement ps = con.prepareStatement(
            "UPDATE accounts SET owner=?, currency=?, saldo=? WHERE iban=?"
        )) {
      ps.setString(1, account.getOwner());
      ps.setString(2, account.getCurrency());
      ps.setInt(3, account.getSaldo());
      ps.setString(4, account.getIban());
      try {
        con.setAutoCommit(false);
        int inserts = ps.executeUpdate();
        assert inserts == 1;
        mergeTransactions(account, con);
        log.info("Saved account={}", account);
        return account;
      } catch (Exception e) {
        con.rollback();
        throw e;
      } finally {
        con.setAutoCommit(true);
      }
    } catch (Exception e) {
      throw new BankingException("Could not create account=" + account, e);
    }
  }

  private void mergeTransactions(Account account, Connection con) throws SQLException {
    for (var tx : account.getTransactions()) {
      mergeTransaction(tx, con);
    }
  }

  private void mergeTransaction(Transaction tx, Connection con) throws SQLException {
    try (PreparedStatement ps = con.prepareStatement(
        "MERGE INTO transactions("
            + "id, debtor_name, debtor_iban, creditor_name, creditor_iban, booking_date, amount, currency, account"
            + ") KEY(id) VALUES("
            + "?, ?, ?, ?, ?, ?, ?, ?, ?"
            + ")"
    )) {
      ps.setObject(1, tx.getId());
      ps.setString(2, tx.getDebtor().getOwner());
      ps.setString(3, tx.getDebtor().getIban());
      ps.setString(4, tx.getCreditor().getOwner());
      ps.setString(5, tx.getCreditor().getIban());
      ps.setObject(6, tx.getBookingDate());
      ps.setInt(7, tx.getMoney().getAmount());
      ps.setString(8, tx.getMoney().getCurrency());
      ps.setString(9, tx.getAccount());
      int inserts = ps.executeUpdate();
      assert inserts == 1;
    }
  }

  private Account accountFromRS(ResultSet resultSet) throws SQLException {
    if (resultSet.getString("accounts.iban") == null) {
      return null;
    }
    return new Account(
        resultSet.getString("accounts.iban"), resultSet.getInt("accounts.saldo"),
        resultSet.getString("accounts.currency"), resultSet.getString("accounts.owner")
    );
  }

  private Transaction transactionFromRS(ResultSet resultSet) throws SQLException {
    if (resultSet.getObject("transactions.id", UUID.class) == null) {
      return null;
    }
    var debtor = new AccountReference(
        resultSet.getString("transactions.debtor_name"),
        resultSet.getString("transactions.debtor_iban")
    );
    var creditor = new AccountReference(
        resultSet.getString("transactions.creditor_name"),
        resultSet.getString("transactions.creditor_iban")
    );
    return new Transaction(
        resultSet.getObject("transactions.id", UUID.class),
        resultSet.getString("transactions.account"),
        debtor, creditor,
        new Money(
            resultSet.getString("transactions.currency"),
            resultSet.getInt("transactions.amount")
        ),
        resultSet.getObject("transactions.booking_date", LocalDate.class)
    );
  }

  private List<Account> accountsWithTransactionsFromRS(ResultSet rs) throws SQLException {
    var accounts = new ArrayList<Account>();
    Account curr = null;
    var currTxs = new ArrayList<Transaction>();
    while (rs.next()) {
      var acc = accountFromRS(rs);
      if (acc == null) {
        throw new IllegalStateException("Account missing in this ResultSet");
      }

      var currTx = transactionFromRS(rs);
      if (currTx != null) {
        currTxs.add(currTx);
      }

      if (!Objects.equals(curr, acc) || rs.isLast()) {
        if (curr != null) { // skip on first account
          addAccountToResult(curr, currTxs, accounts);
          currTxs = new ArrayList<>();
        }
        curr = acc;
      }
    }
    return accounts;
  }

  private void addAccountToResult(Account curr, List<Transaction> currTxs, List<Account> accounts) {
    var currWithTxs = new Account(
        curr.getIban(), curr.getSaldo(), curr.getCurrency(), curr.getOwner(),
        new ArrayList<>(currTxs)
    );
    accounts.add(currWithTxs);
  }
}
