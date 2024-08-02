package com.Java020.MonieFlex.payload.response.vtPass;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VtPassElectricityResponse {
    private String code;
    private VtPassElectricityContent content;
    @JsonProperty("response_description")
    private String responseDescription;
    private String requestId;
    private String amount;
    @JsonProperty("purchased_code")
    private String purchasedCode;
    private String exchangeReference;
    private String token;
}
