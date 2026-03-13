package com.cbn.admincbn.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import com.cbn.admincbn.entity.BankAccount;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    
    Optional<BankAccount> findByBankaccName(String name);
    
    @Transactional
    void deleteByBankaccName(String name);
}