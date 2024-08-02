package com.Java020.MonieFlex.payload.request;

import com.Java020.MonieFlex.domain.enums.AccountType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequest {

    private String accountNumber;
    private AccountType accountType;
    private BigDecimal accountBalance;
}
