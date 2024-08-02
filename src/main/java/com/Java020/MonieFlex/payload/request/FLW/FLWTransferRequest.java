package com.Java020.MonieFlex.payload.request.FLW;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FLWTransferRequest {
    @JsonProperty("account_bank")
    String bankCode;

    @JsonProperty("account_number")
    String accountNumber;
    @JsonProperty("amount")
    int amount;
    @JsonProperty("currency")
    String currency;
    @JsonProperty("narration")
    String narration;
    @JsonProperty("reference")
    String reference;

}
