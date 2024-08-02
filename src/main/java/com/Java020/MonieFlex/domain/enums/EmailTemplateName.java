package com.Java020.MonieFlex.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailTemplateName {
    ACTIVATE_ACCOUNT("activate_account"),
    UNUSUAL_ACTIVITY("unusual_activity"),
    DEBIT_ACCOUNT("debit_account"),
    CREDIT_ACCOUNT("credit_account"),
    ACCOUNT_STATEMENT("account_statement"),
    RESET_FORGOTTEN_PASSWORD("reset_forgotten_password"),
    PASSWORD_RESET_SUCCESS("Reset Password Successful"),
    ELECTRICITY_ALERT("electricity_alert");
    private final String name;
}
