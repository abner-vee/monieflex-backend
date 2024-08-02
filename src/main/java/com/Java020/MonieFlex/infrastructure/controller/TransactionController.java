package com.Java020.MonieFlex.infrastructure.controller;

import com.Java020.MonieFlex.domain.entities.Transaction;
import com.Java020.MonieFlex.payload.request.TransactionRequest;
import com.Java020.MonieFlex.service.TransactionService;
import com.Java020.MonieFlex.service.impl.TransactionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;


    private final TransactionServiceImpl transactionImpl;

    @GetMapping("/all-transactions")
    public List<TransactionRequest> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return transactions.stream()
                .map(transactionImpl::toDTO)
                .collect(Collectors.toList());
    }
    @GetMapping("/incoming-funds")
    public List<TransactionRequest> getIncomingTransactions(String accountNumber) {
        List<Transaction> transactions = transactionService.getIncomingTransactions(accountNumber);
        return transactions.stream()
                .map(transactionImpl::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("outgoing-funds")
    public List<TransactionRequest> getOutgoingTransactions(String accountNumber) {
        List<Transaction> transactions = transactionService.getOutgoingTransactions(accountNumber);
        return transactions.stream()
                .map(transactionImpl::toDTO)
                .collect(Collectors.toList());
    }
}

