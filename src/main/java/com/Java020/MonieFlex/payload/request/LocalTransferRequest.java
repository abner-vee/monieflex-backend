package com.Java020.MonieFlex.payload.request;

import com.Java020.MonieFlex.domain.entities.Account;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocalTransferRequest {

    @NotBlank(message = "Account number cannot be empty")
    private String accountNumber;

    @Min(value = 1, message = "Amount should not be less than 1")
    @NotBlank(message = "Amount cannot be empty")
    private BigDecimal amount;

    private String receiverName;

    private String narration;
}
