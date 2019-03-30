package de.adorsys.banking.infrastructure;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.adorsys.banking.domain.Account;
import de.adorsys.banking.domain.BankingService;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private BankingService bankingService;

  @Test
  public void getAccountList() throws Exception {
    Account acc = new Account("DE1234567890", 1000, "EUR", "Roman");

    given(bankingService.readAccounts()).willReturn(Lists.newArrayList(acc));
    mockMvc.perform(get("/accounts")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[:1].owner").value("Roman"))
        .andExpect(jsonPath("$", hasSize(1)));
  }

  @Test
  public void getAccount() {
  }
}