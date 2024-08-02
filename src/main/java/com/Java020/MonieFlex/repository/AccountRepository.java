package com.Java020.MonieFlex.repository;

import com.Java020.MonieFlex.domain.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByCustomer_EmailIgnoreCase(@NonNull String email);
    Optional<Account> findByAccountNumber( String accountNumber);
}
