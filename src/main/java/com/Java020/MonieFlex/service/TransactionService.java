package com.Java020.MonieFlex.service;

import com.Java020.MonieFlex.domain.entities.Transaction;

import java.util.List;

public interface TransactionService {
    void processTransaction(String username, String email, String transactionDetails, String transactionType, String amount, String date);
    List<Transaction> getAllTransactions();
    List<Transaction> getIncomingTransactions(String accountNumber);
    List<Transaction> getOutgoingTransactions(String accountNumber);
}
