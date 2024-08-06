package com.Java020.MonieFlex.service.ExternalServices;

import com.Java020.MonieFlex.Utils.VtpassEndpoints;
import com.Java020.MonieFlex.Utils.VtpassUtils;
import com.Java020.MonieFlex.domain.entities.Transaction;
import com.Java020.MonieFlex.domain.enums.TransactionStatus;
import com.Java020.MonieFlex.infrastructure.exception.VtpassException;
import com.Java020.MonieFlex.payload.request.AirtimeRequest;
import com.Java020.MonieFlex.payload.request.DataSubscriptionRequest;
import com.Java020.MonieFlex.payload.request.ElectricityRequest;
import com.Java020.MonieFlex.payload.request.VerifyMeterRequest;
import com.Java020.MonieFlex.payload.request.VtPass.*;
import com.Java020.MonieFlex.payload.response.ApiResponse;
import com.Java020.MonieFlex.payload.response.vtPass.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VTPassServiceImpl implements VtPassService{
    @Value("${monieFlex.vtpass.public-key}")
    private String VTP_PUBLIC_KEY;
    @Value("${monieFlex.vtpass.secret-key}")
    private String VTP_SECRET_KEY;
    @Value("${monieFlex.vtpass.api-key}")
    private String VTP_API_KEY;

    private final RestClient vtpassbaseurl;

    public String generateRequestId() {
        StringBuilder result = new StringBuilder();
        ZoneId gmtPlus1Zone = ZoneId.of("GMT+1");
        LocalDateTime gmtPlus1DateTime = LocalDateTime.now(gmtPlus1Zone);
        String date = gmtPlus1DateTime.format(DateTimeFormatter.ISO_DATE);
        result.append(date.replaceAll("-", ""));
        result.append(String.format("%02d", gmtPlus1DateTime.getHour()));
        result.append(String.format("%02d", gmtPlus1DateTime.getMinute()));
        result.append(UUID.randomUUID().toString(), 0, 10);
        return result.toString();
    }

    public HttpHeaders vtPassPostHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", VTP_API_KEY);
        headers.set("secret-key", VTP_SECRET_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public HttpHeaders vtPassGetHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("api_key", VTP_API_KEY);
        headers.set("secret_key", VTP_PUBLIC_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @SneakyThrows
    public Transaction electricitySubscription(ElectricityRequest electricityRequest, Transaction transaction) {
        VtPassElectricityRequest vtElectricity = VtPassElectricityRequest.builder()
                .requestId(generateRequestId())
                .serviceID(electricityRequest.getBillerType().getBiller_type())
                .billersCode(electricityRequest.getMeterNumber())
                .variationCode(electricityRequest.getProductType().getProduct_type())
                .amount(electricityRequest.getAmount())
                .phone(electricityRequest.getPhone())
                .narration(electricityRequest.getNarration())
                .build();

        ResponseEntity<VtPassElectricityResponse> responseEntity = vtpassbaseurl
                .post()
                .uri(VtpassUtils.PURCHASE_PRODUCT)
                .headers(headers -> headers.addAll(vtPassPostHeader()))
                .body(vtElectricity)
                .retrieve()
                .toEntity(VtPassElectricityResponse.class);


        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            VtPassElectricityResponse responseBody = responseEntity.getBody();
            if (responseBody != null && responseBody.getResponseDescription().toLowerCase().contains("success")) {
                String reference = responseBody.getToken() != null ? responseBody.getToken() : responseBody.getExchangeReference();
                transaction.setNarration(electricityRequest.getNarration());
                transaction.setReference(responseBody.getRequestId());
                transaction.setProviderReference(reference);
                transaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
            } else {
                transaction.setTransactionStatus(TransactionStatus.FAILED);
            }
        } else {
            transaction.setTransactionStatus(TransactionStatus.FAILED);
        }
        return transaction;
    }

    @Override
    public ApiResponse<TvSubscriptionQueryContent> queryTVAccount(VerifySmartCard body) throws JsonProcessingException {
        VtPassVerifySmartCardRequest request = new VtPassVerifySmartCardRequest(body.getCard(), body.getType().getType());

        var response = vtpassbaseurl
                .post()
                .uri(VtpassEndpoints.VERIFY_NUMBER)
                .headers(headers -> headers.addAll(vtPassPostHeader()))
                .body(request)
                .retrieve()
                .toEntity(VtPassTvSubscriptionQueryResponse.class);

        if (Objects.requireNonNull(response.getBody()).getStatusCode().equalsIgnoreCase("000")) {
            if (ObjectUtils.isNotEmpty(response.getBody().getContent())) {
                ObjectMapper mapper = new ObjectMapper();
                var content = mapper.readValue(
                        mapper.writeValueAsString(response.getBody().getContent()),
                        TvSubscriptionQueryContent.class
                );
                return new ApiResponse<>(content, "Account found");
            }
        } else if (Objects.requireNonNull(response.getBody()).getContent() instanceof String) {
            throw new VtpassException(response.getBody().getContent().toString());
        }
        throw new VtpassException("Request failed");

    }

    public Transaction buyAirtime(AirtimeRequest airtime, Transaction transaction) {
        VtPassAirtimeRequest vtAirtime = VtPassAirtimeRequest.builder()
                .requestId(generateRequestId())
                .serviceID(airtime.getNetwork().getType())
                .amount(airtime.getAmount())
                .phone(airtime.getPhoneNumber())
                .build();

        var response = vtpassbaseurl
                .post()
                .uri(VtpassUtils.PURCHASE_PRODUCT)
                .headers(headers -> headers.addAll(vtPassPostHeader()))
                .body(vtAirtime)
                .retrieve()
                .toEntity(VtPassAirtimeResponse.class);


        System.out.println("::::::: " + response);
        System.out.println(response.getBody());

        if(Objects.requireNonNull(response.getBody()).responseDescription.toLowerCase().contains("success")) {
            var data = response.getBody();
            transaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
            transaction.setProviderReference(data.getTransactionId());
            transaction.setUpdatedAt(LocalDateTime.now());
        } else {
            transaction.setTransactionStatus(TransactionStatus.FAILED);
        }
        return transaction;
    }
    public ApiResponse<List<VtPassDataVariation>> getDataVariations(String code) {
        var response = vtpassbaseurl
                .get()
                .uri(VtpassUtils.VARIATION_URL(code))
                .headers(headers -> headers.addAll(vtPassGetHeader()))
                .retrieve()
                .toEntity(VtPassDataVariationResponse.class);

        if(response.getStatusCode().is2xxSuccessful()){
            if(Objects.requireNonNull(response.getBody()).getDescription() != null) {
                if (response.getBody().getDescription().equalsIgnoreCase("000")) {
                    if (ObjectUtils.isNotEmpty(response.getBody().getContent().getVariations())) {
                        return new ApiResponse<>(
                                response.getBody().getContent().getVariations(),
                                "Request successfully processed");
                    }
                }
            }
        }
        throw new VtpassException("Request failed");
    }

    @SneakyThrows
    public Transaction dataSubscription(DataSubscriptionRequest dataSubscriptionRequest, Transaction transaction) {
        VtPassDataSubscriptionRequest vtData = VtPassDataSubscriptionRequest.builder()
                .requestId(generateRequestId())
                .serviceID(dataSubscriptionRequest.getType().getType())
                .billersCode(dataSubscriptionRequest.getPhone())
                .variationCode(dataSubscriptionRequest.getData())
                .phone(dataSubscriptionRequest.getPhone())
                .amount(dataSubscriptionRequest.getAmount())
                .build();

        var response = vtpassbaseurl
                .post()
                .uri(VtpassUtils.PURCHASE_PRODUCT)
                .headers(headers -> headers.addAll(vtPassPostHeader()))
                .body(vtData)
                .retrieve()
                .toEntity(VtPassAirtimeResponse.class);

        if(Objects.requireNonNull(response.getBody()).responseDescription.toLowerCase().contains("success")) {
            var data = response.getBody();
            transaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
            transaction.setProviderReference(data.getTransactionId());
            transaction.setUpdatedAt(LocalDateTime.now());
        } else {
            transaction.setTransactionStatus(TransactionStatus.FAILED);
        }
        return transaction;
    }
}
