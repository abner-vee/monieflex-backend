package com.Java020.MonieFlex.payload.request;

import com.Java020.MonieFlex.domain.enums.BillType;
import com.Java020.MonieFlex.domain.enums.BillerType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AirtimeRequest {
    BillType network;
    Integer amount;
    @JsonProperty("phone_number")
    @Size(min = 11, max= 11, message = "Phone number must be more than 11")
    String phoneNumber;
    String narration;
    @JsonProperty("beneficiary_name")
    String beneficiaryName;
}
