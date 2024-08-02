package com.Java020.MonieFlex.payload.response.FLW;

import com.Java020.MonieFlex.payload.response.TransferResponse;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FLWTransferResponse {
    private String status;
    private String message;
    private AllBanksData data;
}
