package com.Java020.MonieFlex.payload.response.vtPass;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VtPassDataSubscriptionContent {
    private VtPassDataSubscriptionTransaction transaction;
}
