package com.Java020.MonieFlex.service.ExternalServices;

import com.Java020.MonieFlex.domain.entities.Transaction;
import com.Java020.MonieFlex.payload.request.AirtimeRequest;
import com.Java020.MonieFlex.payload.request.DataSubscriptionRequest;
import com.Java020.MonieFlex.payload.request.ElectricityRequest;
import com.Java020.MonieFlex.payload.request.VtPass.TvSubscriptionQueryContent;
import com.Java020.MonieFlex.payload.request.VtPass.VerifySmartCard;
import com.Java020.MonieFlex.payload.response.ApiResponse;
import com.Java020.MonieFlex.payload.response.vtPass.VtPassDataVariation;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface VtPassService {

    Transaction electricitySubscription(ElectricityRequest electricityRequest, Transaction transaction);
    public ApiResponse<TvSubscriptionQueryContent> queryTVAccount(VerifySmartCard body) throws JsonProcessingException;
    Transaction buyAirtime(AirtimeRequest airtime, Transaction transaction);
    ApiResponse<List<VtPassDataVariation>> getDataVariations(String code);
    Transaction dataSubscription(DataSubscriptionRequest dataSubscriptionRequest, Transaction transaction);
}
