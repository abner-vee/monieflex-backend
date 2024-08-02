package com.Java020.MonieFlex.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ElectricityType {

    PRE_PAID("prepaid"),
    POST_PAID("postpaid");

    private final String product_type;
}
