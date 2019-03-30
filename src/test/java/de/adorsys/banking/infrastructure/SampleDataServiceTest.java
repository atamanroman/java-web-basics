package de.adorsys.banking.infrastructure;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;

import de.adorsys.banking.domain.BankingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleDataServiceTest {

  @Autowired
  private BankingService bankingService;

  @Test
  public void accountSizeTest() {
    assertNotNull(bankingService);

    assertThat(bankingService.readAccounts(), hasSize(3));
  }
}