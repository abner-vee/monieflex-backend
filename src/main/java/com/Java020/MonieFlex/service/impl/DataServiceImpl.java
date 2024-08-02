package com.Java020.MonieFlex.service.impl;

import com.Java020.MonieFlex.Utils.UserUtils;
import com.Java020.MonieFlex.domain.entities.Transaction;
import com.Java020.MonieFlex.domain.enums.BillType;
import com.Java020.MonieFlex.domain.enums.TransactionStatus;
import com.Java020.MonieFlex.domain.enums.TransactionType;
import com.Java020.MonieFlex.infrastructure.exception.CustomerNotFoundException;
import com.Java020.MonieFlex.infrastructure.exception.InsufficientBalanceException;
import com.Java020.MonieFlex.infrastructure.exception.InvalidPin;
import com.Java020.MonieFlex.infrastructure.exception.VtpassException;
import com.Java020.MonieFlex.payload.request.DataSubscriptionRequest;
import com.Java020.MonieFlex.payload.response.ApiResponse;
import com.Java020.MonieFlex.payload.response.vtPass.VtPassDataVariation;
import com.Java020.MonieFlex.repository.CustomerRepository;
import com.Java020.MonieFlex.repository.TransactionRepository;
import com.Java020.MonieFlex.service.DataService;
import com.Java020.MonieFlex.service.ExternalServices.VTPassServiceImpl;
import com.Java020.MonieFlex.service.ExternalServices.VtPassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataServiceImpl implements DataService {
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    private final UserUtils userUtil;
    private final VTPassServiceImpl vtPassServiceimpl;
    private final VtPassService vtPassService;
    private final PasswordEncoder passwordEncoder;


    public ApiResponse<List<VtPassDataVariation>> viewDataVariations(BillType code) {
        return vtPassService.getDataVariations(code.getType());
    }

    public ApiResponse<String> buyData(DataSubscriptionRequest dataSubscriptionRequest, String pin) {
        String email = userUtil.getLoginUser();
        var user = customerRepository.findByEmail(email).orElseThrow(
                () -> new CustomerNotFoundException("User not found")
        );

        if (!passwordEncoder.matches(pin, user.getTransactionPin())){
            throw new InvalidPin("Enter correct pin!");
        }

        if(userUtil.isBalanceSufficient(BigDecimal.valueOf(dataSubscriptionRequest.getAmount()))) {
            Transaction transaction = new Transaction();
            transaction.setTransactionStatus(TransactionStatus.PENDING);
            transaction.setNarration(dataSubscriptionRequest.getNarration());
            transaction.setCustomer(user);
            transaction.setReference(vtPassServiceimpl.generateRequestId());
            transaction.setAmount(BigDecimal.valueOf(dataSubscriptionRequest.getAmount()));
            transaction.setTransactionType(TransactionType.DATA);
            transaction.setBillType(dataSubscriptionRequest.getType());
            transactionRepository.save(transaction);

            var vtResponse = vtPassService.dataSubscription(dataSubscriptionRequest, transaction);
            ApiResponse<String> response = new ApiResponse<>();
            if(vtResponse.getTransactionStatus() == TransactionStatus.FAILED) {
                userUtil.updateAccountBalance(BigDecimal.valueOf(dataSubscriptionRequest.getAmount()), false);
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
