package com.Java020.MonieFlex.payload.request.VtPass;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VtPassVerifyMeterRequest {
    String type;
    String billersCode;
    String serviceID;
}
