package com.Java020.MonieFlex.payload.response.FLW;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FLWVerifyAccountResponse {
    private String status;
    private String message;
    private VerifyAccountResponse data;
}
