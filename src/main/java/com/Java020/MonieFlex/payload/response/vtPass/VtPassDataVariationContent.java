package com.Java020.MonieFlex.payload.response.vtPass;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VtPassDataVariationContent {
    @JsonProperty("serviceName")
    private String serviceName;
    @JsonProperty("serviceId")
    private String serviceId;
    @JsonProperty("varations")
    private List<VtPassDataVariation> variations;
}
