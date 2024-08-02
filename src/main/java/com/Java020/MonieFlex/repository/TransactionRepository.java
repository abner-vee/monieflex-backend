package com.Java020.MonieFlex.repository;

import com.Java020.MonieFlex.domain.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCreditedBankAccountNumber(String creditedBankAccountNumber);
    List<Transaction> findByDebitAccountNumber(String debitedBankAccountNumber);
}