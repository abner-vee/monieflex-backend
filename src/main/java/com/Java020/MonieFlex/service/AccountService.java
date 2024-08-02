package com.Java020.MonieFlex.service;


import com.Java020.MonieFlex.payload.request.FLW.FLWVerifyAccountRequest;
import com.Java020.MonieFlex.payload.request.LocalTransferRequest;
import com.Java020.MonieFlex.payload.request.TransferRequest;
import com.Java020.MonieFlex.payload.response.ApiResponse;
import com.Java020.MonieFlex.payload.response.FLW.AllBanksData;
import com.Java020.MonieFlex.payload.response.FLW.VerifyAccountResponse;

import java.util.List;

public interface AccountService {
    VerifyAccountResponse verifyBankAccount(FLWVerifyAccountRequest verifyAccountRequest);
    List<AllBanksData> getAllBanks();
    ApiResponse<String> transferToBank(TransferRequest transferRequest);
    ApiResponse<?> localTransfer(LocalTransferRequest localTransferRequest);
    ApiResponse<?> accountBalance();

    ApiResponse<?> verifyPin(String pin);
}
