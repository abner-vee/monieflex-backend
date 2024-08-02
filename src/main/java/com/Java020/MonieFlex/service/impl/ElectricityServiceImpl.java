package com.Java020.MonieFlex.service.impl;

import com.Java020.MonieFlex.Utils.AccountUtils;
import com.Java020.MonieFlex.Utils.UserUtils;
import com.Java020.MonieFlex.domain.entities.Account;
import com.Java020.MonieFlex.domain.entities.Customer;
import com.Java020.MonieFlex.domain.entities.Transaction;
import com.Java020.MonieFlex.domain.enums.EmailTemplateName;
import com.Java020.MonieFlex.domain.enums.TransactionStatus;
import com.Java020.MonieFlex.domain.enums.TransactionType;
import com.Java020.MonieFlex.infrastructure.exception.InsufficientBalanceException;
import com.Java020.MonieFlex.infrastructure.exception.InvalidPin;
import com.Java020.MonieFlex.payload.request.ElectricityRequest;
import com.Java020.MonieFlex.payload.request.TransferRequest;
import com.Java020.MonieFlex.payload.request.VerifyMeterRequest;
import com.Java020.MonieFlex.payload.response.ApiResponse;
import com.Java020.MonieFlex.payload.response.vtPass.VtPassVerifyMeterContent;
import com.Java020.MonieFlex.repository.CustomerRepository;
import com.Java020.MonieFlex.repository.TransactionRepository;
import com.Java020.MonieFlex.service.ElectricityService;
import com.Java020.MonieFlex.service.ExternalServices.VTPassServiceImpl;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class ElectricityServiceImpl implements ElectricityService {

    private final UserUtils userUtils;
    private final CustomerRepository customerRepository;
    private final VTPassServiceImpl vtPassService;
    private final TransactionRepository transactionRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public ApiResponse<?> payElectricityBill(ElectricityRequest electricityRequest, String pin){
        String email = UserUtils.getLoginUser();
        Customer customer = customerRepository.findByEmail(email).get();

        if (!passwordEncoder.matches(pin, customer.getTransactionPin())){
            throw new InvalidPin("Enter correct pin!");
        }

        if (userUtils.isBalanceSufficient(BigDecimal.valueOf(electricityRequest.getAmount()))){
            Transaction transaction = Transaction.builder()
                    .amount(BigDecimal.valueOf(electricityRequest.getAmount()))
                    .transactionStatus(TransactionStatus.PENDING)
                    .narration(electricityRequest.getNarration())
                    .meterNumber(electricityRequest.getMeterNumber())
                    .transactionType(TransactionType.ELECTRICITY)
                    .reference(vtPassService.generateRequestId())
                    .billerType(electricityRequest.getBillerType())
                    .customer(customer)
                    .build();

            transactionRepository.save(transaction);

            var response = vtPassService.electricitySubscription(electricityRequest, transaction);
            if (response.getTransactionStatus() == TransactionStatus.SUCCESSFUL){
                transaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
                transactionRepository.save(transaction);
                userUtils.updateAccountBalance(BigDecimal.valueOf(electricityRequest.getAmount()), true);
                try{
                    emailService.sendEmail(
                            email,
                            customer.fullName(),
                            EmailTemplateName.ELECTRICITY_ALERT,
                            "Payment for Electricity bill SUCCESSFUL",
                            electricityMail(electricityRequest)
                    );
                } catch (MessagingException e) {
                    throw new RuntimeException("Error Sending Email");
                }
                return ApiResponse.builder()
                        .status("success")
                        .message("Payment successful")
                        .data(transaction)
                        .statusCode(HttpStatus.OK)
                        .build();
            } else {
                transaction.setTransactionStatus(TransactionStatus.FAILED);
                transactionRepository.save(transaction);
                userUtils.updateAccountBalance(BigDecimal.valueOf(electricityRequest.getAmount()), false);
                return ApiResponse.builder()
                        .status("failed")
                        .message("Payment failed")
                        .data(transaction)
                        .statusCode(HttpStatus.BAD_REQUEST)
                        .build();
            }

        } else {
            throw  new InsufficientBalanceException("Insufficient balance");
        }
    }

    private static Map<String, Object> electricityMail(ElectricityRequest electricityRequest ){
        Map<String, Object> properties = new HashMap<>();
        properties.put("amount", electricityRequest.getAmount());
        properties.put("meter_number", electricityRequest.getMeterNumber());
        properties.put("date", LocalDate.now().toString());
        return properties;
    }
}
