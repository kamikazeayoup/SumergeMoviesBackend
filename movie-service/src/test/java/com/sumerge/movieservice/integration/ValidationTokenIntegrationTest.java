package com.sumerge.movieservice.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.AfterEach;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@WireMockTest
public class ValidationTokenIntegrationTest {

    private static WireMockServer wireMockServer;


    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void testFetchDataFromValidationTokenReturnsTrue() throws IOException {

        wireMockServer.stubFor(get(urlEqualTo("/api/validate?token=dummytoken"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("true")));

        URL url = new URL("http://localhost:8080/api/validate?token=dummytoken");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        assertThat(connection.getResponseCode()).isEqualTo(200);
        assertThat(new BufferedReader(new InputStreamReader(connection.getInputStream()))
                .lines().collect(Collectors.joining())).isEqualTo("true");
    }

}
