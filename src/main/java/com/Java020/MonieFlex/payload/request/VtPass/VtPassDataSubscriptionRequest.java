package com.Java020.MonieFlex.payload.request.VtPass;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VtPassDataSubscriptionRequest {
    @JsonProperty("request_id")
    String requestId;
    String serviceID;
    String billersCode;
    @JsonProperty("variation_code")
    String variationCode;
    Integer amount;
    String phone;
}
