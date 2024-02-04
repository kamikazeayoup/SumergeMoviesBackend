package com.sumerge.movieservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "validateToken-service" , url = "http://localhost:8080/api")
public interface ValidationTokenServiceClient {


    @GetMapping("/validate")
    boolean CheckToken(@RequestParam("token") String token);
}
