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
public class VtPassAirtimeRequest {
    @JsonProperty("request_id")
    String requestId;
    @JsonProperty("serviceID")
    String serviceID;
    @JsonProperty("amount")
    Integer amount;
    @JsonProperty("phone")
    String phone;
}
