package com.Java020.MonieFlex.Utils;

import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class AccountUtils {

    public String generateAccountNumber(String email) {
        String appSymbol = "M";
        int randomNumber = Math.abs(ThreadLocalRandom.current().nextInt(1000000000, 2000000000));
        String combinedString = "0" + Math.abs(appSymbol.hashCode()) + Math.abs(email.hashCode()) + String.valueOf(randomNumber);
        return combinedString.substring(0, 10);
    }
}
