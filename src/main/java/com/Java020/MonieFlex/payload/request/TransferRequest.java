package com.Java020.MonieFlex.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequest {
    String bankCode;
    String accountNumber;
    String receiverName;
    int amount;
    String currency;
    String narration;
}
