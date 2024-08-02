package com.Java020.MonieFlex.service.impl;

import com.Java020.MonieFlex.Utils.UserUtils;
import com.Java020.MonieFlex.domain.entities.Transaction;
import com.Java020.MonieFlex.domain.enums.TransactionStatus;
import com.Java020.MonieFlex.domain.enums.TransactionType;
import com.Java020.MonieFlex.infrastructure.exception.CustomerNotFoundException;
import com.Java020.MonieFlex.infrastructure.exception.InsufficientBalanceException;
import com.Java020.MonieFlex.infrastructure.exception.InvalidPin;
import com.Java020.MonieFlex.payload.request.AirtimeRequest;
import com.Java020.MonieFlex.payload.response.ApiResponse;
import com.Java020.MonieFlex.repository.CustomerRepository;
import com.Java020.MonieFlex.repository.TransactionRepository;
import com.Java020.MonieFlex.service.AirtimeService;
import com.Java020.MonieFlex.service.ExternalServices.VTPassServiceImpl;
import com.Java020.MonieFlex.service.ExternalServices.VtPassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AirtimeServiceImpl implements AirtimeService {
    private final VTPassServiceImpl vtPassService;
    private final CustomerRepository customerRepository;
    private final UserUtils userUtil;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;

    public ApiResponse<String> buyAirtime(AirtimeRequest airtimeRequest, String pin) {
        String email = userUtil.getLoginUser();
        var user = customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException("User not found"));

        if (!passwordEncoder.matches(pin, user.getTransactionPin())){
            throw new InvalidPin("Enter correct pin!");
        }

        if(userUtil.isBalanceSufficient(BigDecimal.valueOf(airtimeRequest.getAmount()))) {
            System.out.println(vtPassService.generateRequestId());
            Transaction transaction = new Transaction();
            transaction.setTransactionStatus(TransactionStatus.PENDING);
            transaction.setAmount(BigDecimal.valueOf(airtimeRequest.getAmount()));
            transaction.setBillType(airtimeRequest.getNetwork());
            transaction.setTransactionType(TransactionType.AIRTIME);
            transaction.setCustomer(user);
            transaction.setBeneficiaryName(airtimeRequest.getBeneficiaryName());
            transaction.setReference(vtPassService.generateRequestId());
            transaction.setNarration(airtimeRequest.getNarration());
            transactionRepository.save(transaction);

            userUtil.updateAccountBalance(BigDecimal.valueOf(airtimeRequest.getAmount()), true);

            var vtResponse = vtPassService.buyAirtime(airtimeRequest, transaction);
            transactionRepository.save(vtResponse);

            ApiResponse<String> response = new ApiResponse<>();
            if(vtResponse.getTransactionStatus() == TransactionStatus.FAILED) {
                userUtil.updateAccountBalance(BigDecimal.valueOf(airtimeRequest.getAmount()), false);
                response.setStatus("Bad request");
                response.setStatusCode(HttpStatus.BAD_REQUEST);
                response.setData(vtResponse.getTransactionStatus().toString());
                response.setMessage("Couldn't complete transaction");
            } else {
                response.setStatus("success");
                response.setStatusCode(HttpStatus.OK);
                response.setData(vtResponse.getTransactionStatus().toString());
                response.setMessage("Transaction completed");
            }
            return response;
        } else {
            throw new InsufficientBalanceException("Insufficient balance");
        }
    }
}
