package com.Java020.MonieFlex.service;

import com.Java020.MonieFlex.payload.request.VtPass.TvSubscriptionQueryContent;
import com.Java020.MonieFlex.payload.request.VtPass.VerifySmartCard;
import com.Java020.MonieFlex.payload.response.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface TvService {
    public ApiResponse<TvSubscriptionQueryContent> queryTvAccount(VerifySmartCard smartCard) throws JsonProcessingException;
}
