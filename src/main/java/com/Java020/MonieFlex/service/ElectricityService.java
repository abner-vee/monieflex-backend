package com.Java020.MonieFlex.service;

import com.Java020.MonieFlex.payload.request.ElectricityRequest;
import com.Java020.MonieFlex.payload.request.VerifyMeterRequest;
import com.Java020.MonieFlex.payload.response.ApiResponse;
import com.Java020.MonieFlex.payload.response.vtPass.VtPassVerifyMeterContent;

public interface ElectricityService {
    ApiResponse<?> payElectricityBill(ElectricityRequest electricityRequest, String pin);
}
