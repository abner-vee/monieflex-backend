package com.Java020.MonieFlex.service.impl;

import com.Java020.MonieFlex.domain.entities.Transaction;
import com.Java020.MonieFlex.domain.enums.EmailTemplateName;
import com.Java020.MonieFlex.payload.request.CustomerRequest;
import com.Java020.MonieFlex.payload.request.TransactionRequest;
import com.Java020.MonieFlex.repository.TransactionRepository;
import com.Java020.MonieFlex.service.TransactionService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    private final EmailService emailService;
    private final TransactionRepository transactionRepository;

    @Override
    public void processTransaction(String username, String email, String transactionDetails, String transactionType, String amount, String date) {
        switch (transactionType.toLowerCase()) {
            case "debit":
                sendDebitEmail(username, email, amount, date);
                break;
            case "credit":
                sendCreditEmail(username, email, amount, date);
                break;
            case "statement":
                sendAccountStatementEmail(username, email, transactionDetails);
                break;
            default:
                throw new IllegalArgumentException("Unknown transaction type: " + transactionType);
        }
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public List<Transaction> getIncomingTransactions(String accountNumber) {
        return transactionRepository.findByCreditedBankAccountNumber(accountNumber);
    }

    @Override
    public List<Transaction> getOutgoingTransactions(String accountNumber) {
        return transactionRepository.findByDebitAccountNumber(accountNumber);
    }

    public TransactionRequest toDTO(Transaction transaction) {
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setId(transaction.getId());
        transactionRequest.setCreatedAt(transaction.getCreatedAt());
        transactionRequest.setUpdatedAt(transaction.getUpdatedAt());
        transactionRequest.setTransactionType(transaction.getTransactionType());
        transactionRequest.setTransactionStatus(transaction.getTransactionStatus());
        transactionRequest.setCreditedBankName(transaction.getCreditedBankName());
        transactionRequest.setCreditedAccountName(transaction.getCreditedAccountName());
        transactionRequest.setCreditedBankAccountNumber(transaction.getCreditedBankAccountNumber());
        transactionRequest.setReference(transaction.getReference());
        transactionRequest.setAmount(transaction.getAmount());
        transactionRequest.setNarration(transaction.getNarration());

        CustomerRequest customerRequest = getRequest(transaction);

        transactionRequest.setCustomer(customerRequest);

        return transactionRequest;
    }

    private static CustomerRequest getRequest(Transaction transaction) {
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setId(transaction.getCustomer().getId());
        customerRequest.setFirstName(transaction.getCustomer().getFirstName());
        customerRequest.setMiddleName(transaction.getCustomer().getMiddleName());
        customerRequest.setLastName(transaction.getCustomer().getLastName());
        customerRequest.setPhoneNumber(transaction.getCustomer().getPhoneNumber());
        customerRequest.setAddress(transaction.getCustomer().getAddress());
        customerRequest.setEmail(transaction.getCustomer().getEmail());
        return customerRequest;
    }


    private void sendDebitEmail(String username, String email, String amount, String date) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("amount", amount);
        properties.put("date", date);

        try {
            emailService.sendEmail(
                    email,
                    username,
                    EmailTemplateName.DEBIT_ACCOUNT,
                    "Your account has been debited",
                    properties
            );
        } catch (MessagingException e) {
            log.error("Failed to send debit email to {}: {}", email, e.getMessage());
        }
    }

    private void sendCreditEmail(String username, String email, String amount, String date) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("amount", amount);
        properties.put("date", date);

        try {
            emailService.sendEmail(
                    email,
                    username,
                    EmailTemplateName.CREDIT_ACCOUNT,
                    "Your account has been credited",
                    properties
            );
        } catch (MessagingException e) {
            log.error("Failed to send credit email to {}: {}", email, e.getMessage());
        }
    }

    private void sendAccountStatementEmail(String username, String email, String statementDetails) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("statement", statementDetails);

        try {
            emailService.sendEmail(
                    email,
                    username,
                    EmailTemplateName.ACCOUNT_STATEMENT,
                    "Your account statement",
                    properties
            );
        } catch (MessagingException e) {
            log.error("Failed to send account statement email to {}: {}", email, e.getMessage());
        }
    }
}
