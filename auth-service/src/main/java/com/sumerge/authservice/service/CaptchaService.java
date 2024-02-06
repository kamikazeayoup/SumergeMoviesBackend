package com.sumerge.authservice.service;

import com.sumerge.authservice.dto.ReCaptchaResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j

@Service
@PropertySource("classpath:application.properties")

public class CaptchaService{
    @Value("${captcha.secret}")
     String secret;
    @Value("${captcha.url}")
     String url;
    private final RestTemplate restTemplate;
    public CaptchaService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public boolean verify(String tokenResponse){
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("secret", secret);
        formData.add("response", tokenResponse);
        ReCaptchaResponseDto recaptchaResponse = null;
        try {
            recaptchaResponse = this.restTemplate.postForObject(url, formData, ReCaptchaResponseDto.class);
        }catch(RestClientException e){
            System.out.print(e.getMessage());
        }
        if(recaptchaResponse.isSuccess()){
            return true;
        }else {
            return false;
        }
    }

}
