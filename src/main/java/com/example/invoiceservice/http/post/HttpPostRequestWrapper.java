package com.example.invoiceservice.http.post;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.Optional;

@Component
@Slf4j
public class HttpPostRequestWrapper implements HttpPostRequestInterface {
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public HttpPostRequestWrapper(RestClient restClient, ObjectMapper objectMapper) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    public <R,T> Optional<R> makeRequest(String url, T requestBody, Class<R> responseType) {
        URI targetUri = URI.create(url);

        try {
            String jsonRequestBody = objectMapper.writeValueAsString(requestBody);
            
            return Optional.ofNullable(
                    restClient.post()
                            .uri(targetUri)
                            .header("Content-Type", "application/json")
                            .header("Accept", "application/json")
                            .body(jsonRequestBody)
                            .exchange(
                                    (request, response) -> {
                                        String body = response.bodyTo(String.class);
                                        log.info("=== HTTP POST RESPONSE ===");
                                        log.info("URL: {}", url);
                                        log.info("Status Code: {}", response.getStatusCode().value());
                                        log.info("Headers: {}", response.getHeaders());
                                        log.info("Request Body: {}", requestBody);
                                        log.info("Response Body: {}", body);
                                        log.info("=====================\n");

                                        if (response.getStatusCode().is2xxSuccessful()) {
                                            return objectMapper.readValue(body, responseType);
                                        } return null;
                                    }
                            )
            );
        } catch (Exception e) {
            log.error("Error during POST request", e);
            return Optional.empty();
        }
    }
}
