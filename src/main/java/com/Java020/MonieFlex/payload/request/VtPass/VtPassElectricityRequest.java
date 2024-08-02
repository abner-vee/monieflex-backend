package com.Java020.MonieFlex.payload.request.VtPass;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VtPassElectricityRequest {
    @JsonProperty("request_id")
    String requestId;
    String serviceID;
    String billersCode;
    @JsonProperty("variation_code")
    String variationCode;
    Integer amount;
    String phone;
    String narration;
}
