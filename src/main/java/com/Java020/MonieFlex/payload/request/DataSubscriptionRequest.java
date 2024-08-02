package com.Java020.MonieFlex.payload.request;

import com.Java020.MonieFlex.domain.enums.BillType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataSubscriptionRequest {
    BillType type;
    String data;
    Integer amount;
    String phone;
    String narration;
}
