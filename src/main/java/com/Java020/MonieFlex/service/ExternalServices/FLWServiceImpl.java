package com.Java020.MonieFlex.service.ExternalServices;

import com.Java020.MonieFlex.Utils.FlutterwaveUtils;
import com.Java020.MonieFlex.domain.entities.Transaction;
import com.Java020.MonieFlex.domain.enums.TransactionStatus;
import com.Java020.MonieFlex.payload.request.FLW.FLWTransferRequest;
import com.Java020.MonieFlex.payload.request.FLW.FLWVerifyAccountRequest;
import com.Java020.MonieFlex.payload.request.TransferRequest;
import com.Java020.MonieFlex.payload.response.FLW.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FLWServiceImpl implements FLWService{
    @Value("${monieFlex.flutterwave.secret-key}")
    private String FLW_SECRET_KEY;
    private final RestClient restClient;

    public HttpHeaders getFLWHeader(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + FLW_SECRET_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }
    @Override
    public VerifyAccountResponse verifyBankAccount(FLWVerifyAccountRequest verifyAccountRequest) {
        try {
            ResponseEntity<FLWVerifyAccountResponse> response = restClient
                    .post()
                    .uri(FlutterwaveUtils.VERIFY_BANK_ACCOUNT)  // Specific URI for this request
                    .headers(headers -> headers.addAll(getFLWHeader()))
                    .body(verifyAccountRequest)
                    .retrieve()
                    .toEntity(FLWVerifyAccountResponse.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                FLWVerifyAccountResponse body = response.getBody();
                if ("success".equalsIgnoreCase(body.getStatus())) {
                    return body.getData();
                }
            }
            throw new RuntimeException("Error in processing request");

        } catch (Exception ex) {
            throw new RuntimeException("Invalid Account. Please check your details");

        }
    }

    @Override
    @SneakyThrows
    public List<AllBanksData> getAllBanks() {

        ResponseEntity<FLWAllBanksResponse> responseEntity = restClient.get()
                .uri(FlutterwaveUtils.GET_ALL_BANKS)
                .headers(headers -> headers.addAll(getFLWHeader()))
                .retrieve()
                .toEntity(FLWAllBanksResponse.class);
               // .block();  // Blocking until the response is available

        if (responseEntity != null && responseEntity.getStatusCode().is2xxSuccessful()) {
            FLWAllBanksResponse response = responseEntity.getBody();


            if (response != null && response.getStatus().equalsIgnoreCase("success")) {
                List<AllBanksData> allBanksData = response.getData();
                if (ObjectUtils.isNotEmpty(allBanksData)) {
                    return allBanksData;
                }
            }
        }
        throw new RuntimeException("Unable to process this request at this moment");
    }

    @Override
    public Transaction bankTransfer(TransferRequest transfer, String reference) {

            FLWTransferRequest body = new FLWTransferRequest();
            body.setBankCode(transfer.getBankCode());
            body.setAccountNumber(transfer.getAccountNumber());
            body.setAmount(transfer.getAmount());
            body.setCurrency("NGN");
            body.setNarration(transfer.getNarration());
            body.setReference(reference);

        ResponseEntity<FLWTransferResponse> responseEntity = restClient.post()
                .uri(FlutterwaveUtils.BANK_TRANSFER)
                .headers(headers -> headers.addAll(getFLWHeader()))
                .body(body)
                .retrieve()
                .toEntity(FLWTransferResponse.class);


        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            var responseBody = responseEntity.getBody();
            if (Objects.requireNonNull(responseBody).getStatus().equalsIgnoreCase("success")) {
                var data = responseBody.getData();
                if (ObjectUtils.isNotEmpty(data)) {
                    return getTransaction(reference, TransactionStatus.SUCCESSFUL);
                } else {
                    return getTransaction(reference, TransactionStatus.PENDING);
                }
            } else {
                return getTransaction(reference, TransactionStatus.FAILED);
            }
        } else {
            return getTransaction(reference, TransactionStatus.FAILED);
        }
    }

    private Transaction getTransaction(String reference, TransactionStatus status) {
        Transaction transaction = new Transaction();
        transaction.setReference(reference);
        transaction.setTransactionStatus(status);
        return transaction;
    }


}
