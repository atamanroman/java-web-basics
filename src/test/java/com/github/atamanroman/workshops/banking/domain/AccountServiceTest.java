package com.github.atamanroman.workshops.banking.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.github.atamanroman.workshops.banking.infrastructure.SampleDataService;
import com.github.atamanroman.workshops.banking.infrastructure.Transactional;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

public class AccountServiceTest {

  private EntityManagerFactory emf;
  private AccountRepository accountRepository;
  private BankingService bankingService;

  @Before
  public void setup() {
    this.emf = Persistence
        .createEntityManagerFactory("java-web-basics");
    Whitebox.setInternalState(Transactional.class, "emf", emf); // TODO meh for testing :(

    var flyway = Flyway.configure().dataSource(getDataSource(emf)).load();
    flyway.migrate();

    accountRepository = new AccountRepository();
    bankingService = new BankingService(accountRepository);
  }

  @After
  public void teardown() {
    try {
      var entityManager = emf.createEntityManager();
      entityManager.getTransaction().begin();
      entityManager.createNativeQuery("DROP ALL OBJECTS").executeUpdate();
      entityManager.getTransaction().commit();
    } finally {
      emf.close();
    }
  }

  @Test
  public void noAccountsInEmptyDb() {
    assertEquals(0, bankingService.readAccounts().size());
  }

  @Test
  public void noAccountInEmptyDb() {
    assertFalse(bankingService.readAccount("DE123456789").isPresent());
  }

  @Test
  public void sampleDataCanBeInserted() {
    new SampleDataService(accountRepository).init();
  }

  private static DataSource getDataSource(EntityManagerFactory emf) {
    var connectionAccess = ((SessionFactoryImpl) emf).getJdbcServices()
        .getBootstrapJdbcConnectionAccess();
    var connectionProvider = ((JdbcEnvironmentInitiator.ConnectionProviderJdbcConnectionAccess) connectionAccess)
        .getConnectionProvider();
    return connectionProvider.unwrap(DataSource.class);
  }

}
