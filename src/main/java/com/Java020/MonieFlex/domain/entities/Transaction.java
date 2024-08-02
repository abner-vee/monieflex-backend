package com.Java020.MonieFlex.domain.entities;

import com.Java020.MonieFlex.domain.enums.BillType;
import com.Java020.MonieFlex.domain.enums.BillerType;
import com.Java020.MonieFlex.domain.enums.TransactionStatus;
import com.Java020.MonieFlex.domain.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_tbl")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transaction extends BaseClass {
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    private String debitAccountNumber;
    private String debitBankName;
    private String creditedBankCode;
    private String creditedBankName;
    private String creditedAccountName;
    private String creditedBankAccountNumber;
    private String billerCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "biller_type")
    private BillerType billerType;
    private BillType billType;

    private String billerName;
    private String itemCode;
    private String labelName;
    private String beneficiaryName;
    private String networkType;
    private String phoneNumber;
    private String meterNumber;
    private String meterAccountName;
    private String smartCardNumber;
    @Column(name = "provider_reference")
    private String providerReference;
    private String reference;
    private BigDecimal amount;
    private String narration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    @JsonManagedReference
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @JsonManagedReference
    private Customer customer;

    @OneToOne
    @JoinColumn(name = "beneficiary_id")
    @JsonManagedReference
    private Beneficiary beneficiary;
}
