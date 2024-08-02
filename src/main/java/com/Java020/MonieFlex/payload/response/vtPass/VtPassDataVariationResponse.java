package com.Java020.MonieFlex.payload.response.vtPass;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VtPassDataVariationResponse {
    @JsonProperty("response_description")
    private String description;
    @JsonProperty("content")
    private VtPassDataVariationContent content;
}
