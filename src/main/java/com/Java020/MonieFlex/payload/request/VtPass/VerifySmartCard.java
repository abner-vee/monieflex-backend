package com.Java020.MonieFlex.payload.request.VtPass;

import com.Java020.MonieFlex.domain.enums.BillType;
import lombok.Data;

@Data
public class VerifySmartCard {
    private String card;
    private BillType type;
}
