package com.Java020.MonieFlex.service.ExternalServices;

import com.Java020.MonieFlex.domain.entities.Transaction;
import com.Java020.MonieFlex.payload.request.FLW.FLWVerifyAccountRequest;
import com.Java020.MonieFlex.payload.request.TransferRequest;
import com.Java020.MonieFlex.payload.response.FLW.AllBanksData;
import com.Java020.MonieFlex.payload.response.FLW.VerifyAccountResponse;

import java.util.List;

public interface FLWService {

    VerifyAccountResponse verifyBankAccount(FLWVerifyAccountRequest verifyAccountRequest);
    List<AllBanksData> getAllBanks();

    Transaction bankTransfer(TransferRequest transferRequest, String reference);
}
