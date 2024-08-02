package com.Java020.MonieFlex.service;

import com.Java020.MonieFlex.domain.enums.BillType;
import com.Java020.MonieFlex.payload.request.DataSubscriptionRequest;
import com.Java020.MonieFlex.payload.response.ApiResponse;
import com.Java020.MonieFlex.payload.response.vtPass.VtPassDataVariation;

import java.util.List;

public interface DataService {
    ApiResponse<List<VtPassDataVariation>> viewDataVariations(BillType code);
    ApiResponse<String> buyData(DataSubscriptionRequest dataSubscriptionRequest, String pin);
}
