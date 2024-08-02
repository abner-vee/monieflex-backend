package com.Java020.MonieFlex;

import com.Java020.MonieFlex.domain.entities.Transaction;
import com.Java020.MonieFlex.domain.enums.EmailTemplateName;
import com.Java020.MonieFlex.domain.enums.TransactionType;
import com.Java020.MonieFlex.repository.TransactionRepository;
import com.Java020.MonieFlex.service.TransactionService;
import com.Java020.MonieFlex.service.impl.EmailService;
import com.Java020.MonieFlex.service.impl.TransactionServiceImpl;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TransactionServiceImplTest {

    private EmailService emailService;
    private TransactionService transactionService;

    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        emailService = Mockito.mock(EmailService.class);
        transactionRepository = Mockito.mock(TransactionRepository.class);  // Initialize transactionRepository first
        transactionService = new TransactionServiceImpl(emailService, transactionRepository);
    }

    @Test
    void testProcessDebitTransaction() throws MessagingException {
        String username = "testuser";
        String email = "test@example.com";
        String transactionDetails = "debit transaction";
        String transactionType = "debit";
        String amount = "1000";
        String date = "2023-01-01";

        transactionService.processTransaction(username, email, transactionDetails, transactionType, amount, date);

        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);

        verify(emailService, times(1)).sendEmail(
                eq(email),
                eq(username),
                eq(EmailTemplateName.DEBIT_ACCOUNT),
                eq("Your account has been debited"),
                captor.capture()
        );

        assertEquals(amount, captor.getValue().get("amount"));
        assertEquals(date, captor.getValue().get("date"));
    }

    @Test
    void testProcessCreditTransaction() throws MessagingException {
        String username = "testuser";
        String email = "test@example.com";
        String transactionDetails = "credit transaction";
        String transactionType = "credit";
        String amount = "1500";
        String date = "2023-01-02";

        transactionService.processTransaction(username, email, transactionDetails, transactionType, amount, date);

        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);

        verify(emailService, times(1)).sendEmail(
                eq(email),
                eq(username),
                eq(EmailTemplateName.CREDIT_ACCOUNT),
                eq("Your account has been credited"),
                captor.capture()
        );

        assertEquals(amount, captor.getValue().get("amount"));
        assertEquals(date, captor.getValue().get("date"));
    }

    @Test
    void testProcessAccountStatementTransaction() throws MessagingException {
        String username = "testuser";
        String email = "test@example.com";
        String transactionDetails = "account statement";
        String transactionType = "statement";

        transactionService.processTransaction(username, email, transactionDetails, transactionType, null, null);

        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);

        verify(emailService, times(1)).sendEmail(
                eq(email),
                eq(username),
                eq(EmailTemplateName.ACCOUNT_STATEMENT),
                eq("Your account statement"),
                captor.capture()
        );

        assertEquals("account statement", captor.getValue().get("statement"));
    }

    @Test
    public void testGetAllTransactions() {
        Transaction transaction1 = new Transaction();
        transaction1.setId(1L);
        transaction1.setAmount(BigDecimal.valueOf(1000));

        Transaction transaction2 = new Transaction();
        transaction2.setId(2L);
        transaction2.setAmount(BigDecimal.valueOf(2000));

        when(transactionRepository.findAll()).thenReturn(Arrays.asList(transaction1, transaction2));

        List<Transaction> transactions = transactionService.getAllTransactions();

        assertEquals(2, transactions.size());
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    public void testGetIncomingTransactions() {
        String accountNumber = "1234567890";
        Transaction transaction1 = new Transaction();
        transaction1.setId(1L);
        transaction1.setTransactionType(TransactionType.TRANSFER);
        transaction1.setCreditedBankAccountNumber(accountNumber);
        transaction1.setAmount(BigDecimal.valueOf(1000));

        when(transactionRepository.findByCreditedBankAccountNumber(accountNumber)).thenReturn(Arrays.asList(transaction1));

        List<Transaction> transactions = transactionService.getIncomingTransactions(accountNumber);

        assertEquals(1, transactions.size());
        assertEquals(TransactionType.TRANSFER, transactions.get(0).getTransactionType());
        verify(transactionRepository, times(1)).findByCreditedBankAccountNumber(accountNumber);
    }

    @Test
    public void testGetOutgoingTransactions() {
        String accountNumber = "1234567890";
        Transaction transaction1 = new Transaction();
        transaction1.setId(1L);
        transaction1.setTransactionType(TransactionType.TRANSFER);
        transaction1.setDebitAccountNumber(accountNumber);
        transaction1.setAmount(BigDecimal.valueOf(1000));

        when(transactionRepository.findByDebitAccountNumber(accountNumber)).thenReturn(Arrays.asList(transaction1));

        List<Transaction> transactions = transactionService.getOutgoingTransactions(accountNumber);

        assertEquals(1, transactions.size());
        assertEquals(TransactionType.TRANSFER, transactions.get(0).getTransactionType());
        verify(transactionRepository, times(1)).findByDebitAccountNumber(accountNumber);
    }
}
