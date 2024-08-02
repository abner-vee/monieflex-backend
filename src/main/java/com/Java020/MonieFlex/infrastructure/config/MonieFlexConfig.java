package com.Java020.MonieFlex.infrastructure.config;

import com.Java020.MonieFlex.Utils.FlutterwaveUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MonieFlexConfig {

    @Value("${flutterwave.base.url}")
    private String flutterwaveurl;
    @Value("${vtpass.base.url}")
    private String vtpassurl;
    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(flutterwaveurl)
                .build();
    }

    @Bean
    public RestClient vtpassbaseurl() {
        return RestClient.builder()
                .baseUrl(vtpassurl)
                .build();
    }
}
