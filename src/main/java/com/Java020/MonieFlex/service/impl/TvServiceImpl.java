package com.Java020.MonieFlex.service.impl;

import com.Java020.MonieFlex.payload.request.VtPass.TvSubscriptionQueryContent;
import com.Java020.MonieFlex.payload.request.VtPass.VerifySmartCard;
import com.Java020.MonieFlex.payload.response.ApiResponse;
import com.Java020.MonieFlex.service.ExternalServices.VtPassService;
import com.Java020.MonieFlex.service.TvService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TvServiceImpl implements TvService {
    private final VtPassService vtPassService;


    @Override
    public ApiResponse<TvSubscriptionQueryContent> queryTvAccount(VerifySmartCard smartCard) throws JsonProcessingException {
        return vtPassService.queryTVAccount(smartCard);
    }
}
