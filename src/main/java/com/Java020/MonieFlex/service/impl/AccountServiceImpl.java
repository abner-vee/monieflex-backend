package com.Java020.MonieFlex.service.impl;

import com.Java020.MonieFlex.Utils.UserUtils;
import com.Java020.MonieFlex.domain.entities.Account;
import com.Java020.MonieFlex.domain.entities.Customer;
import com.Java020.MonieFlex.domain.entities.Transaction;
import com.Java020.MonieFlex.domain.enums.EmailTemplateName;
import com.Java020.MonieFlex.domain.enums.TransactionStatus;
import com.Java020.MonieFlex.domain.enums.TransactionType;
import com.Java020.MonieFlex.infrastructure.exception.InsufficientBalanceException;
import com.Java020.MonieFlex.infrastructure.exception.InvalidPin;
import com.Java020.MonieFlex.payload.request.FLW.FLWVerifyAccountRequest;
import com.Java020.MonieFlex.payload.request.LocalTransferRequest;
import com.Java020.MonieFlex.payload.request.TransferRequest;
import com.Java020.MonieFlex.payload.response.ApiResponse;
import com.Java020.MonieFlex.payload.response.FLW.AllBanksData;
import com.Java020.MonieFlex.payload.response.FLW.VerifyAccountResponse;
import com.Java020.MonieFlex.repository.AccountRepository;
import com.Java020.MonieFlex.repository.CustomerRepository;
import com.Java020.MonieFlex.repository.TransactionRepository;
import com.Java020.MonieFlex.service.AccountService;
import com.Java020.MonieFlex.service.ExternalServices.FLWService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final FLWService flwService;
    private final UserUtils userUtils;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    protected String generateTxRef() {
        return "MONF-" + UUID.randomUUID().toString().substring(0, 6);
    }
    @Override
    public VerifyAccountResponse verifyBankAccount(FLWVerifyAccountRequest verifyAccountRequest) {
        return flwService.verifyBankAccount(verifyAccountRequest);
    }

    @Override
    public List<AllBanksData> getAllBanks() {
        return flwService.getAllBanks();
    }


    @Override
    public ApiResponse<String> transferToBank(TransferRequest transferRequest) {
        String loginUserEmail = userUtils.getLoginUser();

        Customer customer = customerRepository.findByEmail(loginUserEmail).get();
        Account account = accountRepository.findByCustomer_EmailIgnoreCase(loginUserEmail).get();

        if (userUtils.isBalanceSufficient(BigDecimal.valueOf(transferRequest.getAmount()))){

            Transaction transaction = new Transaction();
            transaction.setTransactionStatus(TransactionStatus.PENDING);
            transaction.setAmount(BigDecimal.valueOf(transferRequest.getAmount()));
            transaction.setTransactionType(TransactionType.TRANSFER);
            transaction.setCreditedBankAccountNumber(transferRequest.getAccountNumber());
            transaction.setNarration(transferRequest.getNarration());
            transaction.setReference(generateTxRef());
            transaction.setCustomer(customer);

            transactionRepository.save(transaction);

            var result = flwService.bankTransfer(transferRequest, transaction.getReference());
            if (result.getTransactionStatus() == TransactionStatus.SUCCESSFUL){
                transaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
                transactionRepository.save(transaction);
                userUtils.updateAccountBalance(BigDecimal.valueOf(transferRequest.getAmount()), true);
                try{
                    emailService.sendEmail(
                            loginUserEmail,
                            customer.fullName(),
                            EmailTemplateName.DEBIT_ACCOUNT,
                            "CREDIT TO " + transferRequest.getAccountNumber() + " SUCCESSFUL",
                            debitMail(account, transferRequest)


                    );
                } catch (MessagingException e) {
                    throw new RuntimeException("Error Sending Email");
                }
                return new ApiResponse<>("Transaction successful", String.valueOf(HttpStatus.OK));
            } else{
                transaction.setTransactionStatus(TransactionStatus.FAILED);
                transactionRepository.save(transaction);
                userUtils.updateAccountBalance(BigDecimal.valueOf(transferRequest.getAmount()), false);
                return new ApiResponse<>("Transaction failed", String.valueOf(HttpStatus.BAD_REQUEST));
            }
        }else {
            throw new InsufficientBalanceException("Insufficient Balance");
        }

    }

    @Override
    public ApiResponse<?> localTransfer(LocalTransferRequest localTransferRequest) {
        String loginUserEmail = userUtils.getLoginUser();
        Customer customer = customerRepository.findByEmail(loginUserEmail).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        if (!userUtils.isBalanceSufficient(localTransferRequest.getAmount())){
            return new ApiResponse<>("Insufficient Balance", HttpStatus.BAD_REQUEST);
        }

        Transaction transaction = new Transaction();
        transaction.setCreditedBankAccountNumber(localTransferRequest.getAccountNumber());
        transaction.setNarration(localTransferRequest.getNarration());
        transaction.setAmount(localTransferRequest.getAmount());
        transaction.setReference(generateTxRef());
        transaction.setCreditedAccountName(localTransferRequest.getReceiverName());
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setTransactionStatus(TransactionStatus.PENDING);
        transaction.setCreditedBankName("MonieFlex");
        transaction.setCustomer(customer);
        transactionRepository.save(transaction);

        var account = accountRepository.findByAccountNumber(localTransferRequest.getAccountNumber());
        if (account.isPresent()){
            userUtils.updateAccountBalance(localTransferRequest.getAmount(), true);
            account.get().setAccountBalance(account.get().getAccountBalance().add(localTransferRequest.getAmount()));
            transaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
            transactionRepository.save(transaction);
            return new ApiResponse<>("Transfer Successful", HttpStatus.OK);
        } else {
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            transactionRepository.save(transaction);
            return new ApiResponse<>("Transaction failed", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ApiResponse<?> accountBalance() {
        String loginUserEmail = userUtils.getLoginUser();
        Account account = accountRepository.findByCustomer_EmailIgnoreCase(loginUserEmail).get();
        BigDecimal accountBalance = account.getAccountBalance();
        ApiResponse apiResponse = ApiResponse.builder()
                .status("success")
                .message("Account balance")
                .data(accountBalance)
                .statusCode(HttpStatus.OK)
                .build();

        return apiResponse;
    }

    @Override
    public ApiResponse<?> verifyPin(String pin) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return customerRepository.findByEmail(email)
                .map(customer -> {
                   boolean isValid =  passwordEncoder.matches(pin,customer.getTransactionPin());
                    if(isValid){
                        return ApiResponse.builder()
                                .httpStatus(HttpStatus.OK)
                                .message("Pin verified")
                                .statusCode(HttpStatus.OK)
                                .data(true)
                                .build();
                    }
                    throw new InvalidPin("Incorrect pin entered");
                })
                .orElseThrow(()-> new UsernameNotFoundException("Customer not found"));
    }

    private static Map<String, Object> debitMail(Account account, TransferRequest transferRequest){
        Map<String, Object> properties = new HashMap<>();
        properties.put("amount", transferRequest.getAmount());
        properties.put("accountBalance", account.getAccountBalance());
        properties.put("date", LocalDate.now().toString());
        return properties;
    }

}
