package com.Java020.MonieFlex.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BillerType {

    IKEDC("ikeja-electric"),
    EKEDC("eko-electric"),
    KEDCO("kano-electric"),
    PHED("portharcourt-electric"),
    JED("jos-electric"),
    IBEDC("ibadan-electric"),
    KAEDCO("kaduna-electric"),
    AEDC("abuja-electric"),
    EEDC("enugu-electric"),
    BEDC("benin-electric"),
    ABA("aba-electric");


    private final String biller_type;

}

