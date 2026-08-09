package com.example.invoiceservice.http.get;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.Optional;

@Component
@Slf4j
public class HttpGetRequestWrapper implements HttpGetRequestInterface {
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public HttpGetRequestWrapper(RestClient restClient, ObjectMapper objectMapper) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }
    public <R> Optional<R> makeRequest(String url, Class<R> responseType) {
        try {
            URI targetUri = URI.create(url);
            return Optional.ofNullable(
                    restClient.get()
                            .uri(targetUri)
                            .header("Content-Type", "application/json")
                            .header("Accept", "application/json")
                            .exchange(
                                    (request, response) -> {
                                        String body = response.bodyTo(String.class);
                                        log.info("=== HTTP GET RESPONSE ===");
                                        log.info("URL: {}", url);
                                        log.info("Status Code: {}", response.getStatusCode().value());
                                        log.info("Headers: {}", response.getHeaders());
                                        log.info("Response Body: {}", body);
                                        log.info("=====================\n");
    
                                        if (response.getStatusCode().is2xxSuccessful()) {
                                            return objectMapper.readValue(body, responseType);
                                        } return null;
                                    }
                            )
            );
        }
        catch (Exception e) {
            log.error("Error during GET request", e);
            return Optional.empty();
        }
    }
}
