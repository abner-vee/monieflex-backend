package com.Java020.MonieFlex.payload.response.vtPass;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class VtPassTvSubscriptionQueryResponse {
    @JsonProperty("code")
    private String statusCode;
    @JsonProperty("content")
    private Object content;
}
