package com.Java020.MonieFlex.payload.request;

import com.Java020.MonieFlex.domain.enums.BillerType;
import com.Java020.MonieFlex.domain.enums.ElectricityType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ElectricityRequest {
    @JsonProperty("product_type")
    ElectricityType productType;
    @JsonProperty("meter_number")
    String meterNumber;
    BillerType billerType;
    Integer amount;
    String phone;
    String narration;
}
