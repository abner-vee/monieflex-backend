package com.Java020.MonieFlex.payload.request.FLW;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FLWVerifyAccountRequest {
    @JsonProperty("account_number")
    String accountNumber;
    @JsonProperty("account_bank")
    String bankAccount;
}
