package com.Java020.MonieFlex.payload.response.vtPass;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VtPassDataSubscriptionResponse {
    private String code;
    private VtPassDataVariationContent content;
    @JsonProperty("response_description")
    private String responseDescription;
    private String requestId;
    private String amount;
    @JsonProperty("purchased_code")
    private String purchasedCode;
    private String exchangeReference;
    private String token;
}
