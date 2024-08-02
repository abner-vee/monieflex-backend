package com.Java020.MonieFlex.infrastructure.controller;

import com.Java020.MonieFlex.domain.enums.BillType;
import com.Java020.MonieFlex.payload.request.AirtimeRequest;
import com.Java020.MonieFlex.payload.request.DataSubscriptionRequest;
import com.Java020.MonieFlex.payload.request.ElectricityRequest;
import com.Java020.MonieFlex.payload.request.VerifyMeterRequest;
import com.Java020.MonieFlex.payload.request.VtPass.TvSubscriptionQueryContent;
import com.Java020.MonieFlex.payload.request.VtPass.VerifySmartCard;
import com.Java020.MonieFlex.payload.response.ApiResponse;
import com.Java020.MonieFlex.payload.response.vtPass.VtPassDataVariation;
import com.Java020.MonieFlex.service.AirtimeService;
import com.Java020.MonieFlex.service.DataService;
import com.Java020.MonieFlex.service.ElectricityService;
import com.Java020.MonieFlex.service.TvService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/payment")
public class PaymentController {
    private final TvService tvService;
    private final ElectricityService electricityService;
    private final AirtimeService airtimeService;
    private final DataService dataService;
    @PostMapping("electricity")
    public ResponseEntity<ApiResponse<?>> buyElectricity(@RequestBody ElectricityRequest electricityRequest, @RequestParam String pin){
        var response = electricityService.payElectricityBill(electricityRequest, pin);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/query-tv-account")
    public ResponseEntity<ApiResponse<TvSubscriptionQueryContent>> queryTvAccount(
            @RequestBody VerifySmartCard smartCard
    ) throws JsonProcessingException {
        var response = tvService.queryTvAccount(smartCard);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping("airtime")
    public ResponseEntity<ApiResponse<?>> buyAirtime(@RequestBody AirtimeRequest airtimeRequest, @RequestParam String pin){
        var response = airtimeService.buyAirtime(airtimeRequest, pin);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/data-variations")
    public ResponseEntity<ApiResponse<List<VtPassDataVariation>>> fetchDataVariation(@RequestParam BillType code) {
        var response = dataService.viewDataVariations(code);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    @PostMapping("/data-purchase")
    public ResponseEntity<ApiResponse<?>> buyData(@RequestBody DataSubscriptionRequest dataSubscriptionRequest, @RequestParam String pin){
        var response = dataService.buyData(dataSubscriptionRequest, pin);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
