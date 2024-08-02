package com.Java020.MonieFlex.payload.request;

import com.Java020.MonieFlex.domain.entities.Customer;
import com.Java020.MonieFlex.domain.enums.TransactionStatus;
import com.Java020.MonieFlex.domain.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionRequest {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private TransactionType transactionType;
    private TransactionStatus transactionStatus;
    private String debitAccountNumber;
    private String debitBankName;
    private String creditedBankCode;
    private String creditedBankName;
    private String creditedAccountName;
    private String creditedBankAccountNumber;
    private String billerCode;
    private String reference;
    private BigDecimal amount;
    private String narration;
    private CustomerRequest customer;

}
