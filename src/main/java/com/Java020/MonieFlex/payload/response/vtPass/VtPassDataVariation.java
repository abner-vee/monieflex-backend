package com.Java020.MonieFlex.payload.response.vtPass;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VtPassDataVariation {
    @JsonProperty("variation_code")
    private String code;
    @JsonProperty("name")
    private String name;
    @JsonProperty("variation_amount")
    private String amount;
}
