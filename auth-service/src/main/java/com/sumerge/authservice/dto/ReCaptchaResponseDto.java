package com.sumerge.authservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;


@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)

public class ReCaptchaResponseDto {
    @JsonProperty("success")
    private boolean success;

    @JsonProperty("challenge_ts")
    private String challengeTs;

    @JsonProperty("hostname")
    private String hostname;
}
