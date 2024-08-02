package com.Java020.MonieFlex.payload.response.vtPass;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VtPassDataSubscriptionTransaction {
    private String status;
    @JsonProperty("product_name")
    private String productName;
    @JsonProperty("unique_element")
    private String uniqueElement;
    private String phone;
    private String name;
    private Integer amount;
    private String transactionId;
}
