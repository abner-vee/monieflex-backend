package com.Java020.MonieFlex.service;

import com.Java020.MonieFlex.payload.request.AirtimeRequest;
import com.Java020.MonieFlex.payload.response.ApiResponse;

public interface AirtimeService {

    ApiResponse<String> buyAirtime(AirtimeRequest airtimeRequest, String pin);
}
