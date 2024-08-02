package com.Java020.MonieFlex.infrastructure.controller;

import com.Java020.MonieFlex.payload.request.FLW.FLWVerifyAccountRequest;
import com.Java020.MonieFlex.payload.request.LocalTransferRequest;
import com.Java020.MonieFlex.payload.request.TransferRequest;
import com.Java020.MonieFlex.payload.response.ApiResponse;
import com.Java020.MonieFlex.payload.response.FLW.AllBanksData;
import com.Java020.MonieFlex.payload.response.FLW.VerifyAccountResponse;
import com.Java020.MonieFlex.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/account/")
public class AccountController {

    private final AccountService accountService;
    @PostMapping("verify")
    public ResponseEntity<?> verifyBankAccount(@RequestBody FLWVerifyAccountRequest verifyAccountRequest){
        VerifyAccountResponse response = accountService.verifyBankAccount(verifyAccountRequest);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("verify-pin")
    public ResponseEntity<?> verifyPin(@RequestParam String pin){
        ApiResponse<?> response = accountService.verifyPin(pin);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("get_all_banks")
    public ResponseEntity<?> getAllBanks(){
        List<AllBanksData> responses = accountService.getAllBanks();

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @PostMapping("transfer_to_bank")
    public ResponseEntity<?> transferToBank(@RequestBody TransferRequest transferRequest, @AuthenticationPrincipal UserDetails user){
        var response = accountService.transferToBank(transferRequest);
        System.out.println(user.getUsername());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("transfer_to_monieflex")
    public ResponseEntity<?> localTransfer(@RequestBody LocalTransferRequest localTransferRequest, @AuthenticationPrincipal UserDetails user){
        var response = accountService.localTransfer(localTransferRequest);
        System.out.println(user.getUsername());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("account-balance")
    public ResponseEntity<?> accountBalance(){
        ApiResponse response = accountService.accountBalance();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
