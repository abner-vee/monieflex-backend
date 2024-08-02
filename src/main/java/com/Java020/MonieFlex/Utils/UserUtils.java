package com.Java020.MonieFlex.Utils;

import com.Java020.MonieFlex.repository.AccountRepository;
import com.Java020.MonieFlex.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
@RequiredArgsConstructor
public class UserUtils {

    private final AccountRepository accountRepository;

    public static String getLoginUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails){
            return ((UserDetails)principal).getUsername();
        } else{
            return principal.toString();
        }
    }

    public boolean isBalanceSufficient(BigDecimal amount) {
        var loggedInUser = UserUtils.getLoginUser();
        var account = accountRepository.findByCustomer_EmailIgnoreCase(loggedInUser);
        return account.isPresent() &&
                account.get().getAccountBalance().compareTo(amount) > 0;
    }

    public void updateAccountBalance(BigDecimal amount, boolean isDebit) {
        var loggedInUser = UserUtils.getLoginUser();
        var account = accountRepository.findByCustomer_EmailIgnoreCase(loggedInUser);
        if(account.isPresent()) {
            var accountValue = account.get();
            if(isDebit) {
                accountValue.setAccountBalance(accountValue.getAccountBalance().subtract(amount));
            } else {
                accountValue.setAccountBalance(accountValue.getAccountBalance().add(amount));
            }
            accountRepository.save(accountValue);
        }
    }

}
