package com.Java020.MonieFlex.payload.request;

import com.Java020.MonieFlex.domain.enums.BillerType;
import com.Java020.MonieFlex.domain.enums.ElectricityType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerifyMeterRequest {
    ElectricityType product;
    BillerType disco;
    String meter;
}
