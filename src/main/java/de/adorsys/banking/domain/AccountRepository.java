package de.adorsys.banking.domain;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends AccountRepositoryCustom, JpaRepository<Account, UUID> {

  Optional<Account> findByIban(String iban);
}
