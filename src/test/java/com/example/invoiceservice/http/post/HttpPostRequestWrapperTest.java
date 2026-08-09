package com.example.invoiceservice.http.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestHeadersSpec.ConvertibleClientHttpResponse;
import org.springframework.web.client.RestClient.RequestHeadersSpec.ExchangeFunction;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HttpPostRequestWrapperTest {

    @Mock
    private RestClient restClient;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RestClient.RequestBodyUriSpec postSpec;

    @Mock
    private RestClient.RequestBodySpec bodySpec;

    private HttpPostRequestWrapper httpPostRequestWrapper;

    @BeforeEach
    void setUp() {
        httpPostRequestWrapper = new HttpPostRequestWrapper(restClient, objectMapper);
    }

    @Test
    void makeRequest_Success() throws Exception {
        String url = "http://example.com";
        TestRequest requestObj = new TestRequest("input");
        String requestJson = "{\"input\":\"input\"}";
        String responseBody = "{\"output\":\"output\"}";
        TestResponse responseObj = new TestResponse("output");

        when(objectMapper.writeValueAsString(requestObj)).thenReturn(requestJson);
        when(restClient.post()).thenReturn(postSpec);
        when(postSpec.uri(any(URI.class))).thenReturn(bodySpec);
        when(bodySpec.header(anyString(), anyString())).thenReturn(bodySpec);
        when(bodySpec.body(anyString())).thenReturn(bodySpec);

        when(bodySpec.exchange(any())).thenAnswer(invocation -> {
            ExchangeFunction function = invocation.getArgument(0);
            ConvertibleClientHttpResponse response = mock(ConvertibleClientHttpResponse.class);
            when(response.getStatusCode()).thenReturn(HttpStatusCode.valueOf(200));
            when(response.bodyTo(String.class)).thenReturn(responseBody);
            return function.exchange(null, response);
        });

        when(objectMapper.readValue(responseBody, TestResponse.class)).thenReturn(responseObj);

        Optional<TestResponse> result = httpPostRequestWrapper.makeRequest(url, requestObj, TestResponse.class);

        assertTrue(result.isPresent());
        assertEquals("output", result.get().output());
        verify(restClient).post();
    }

    @Test
    void makeRequest_SerializationFailure() throws Exception {
        String url = "http://example.com";
        TestRequest requestObj = new TestRequest("input");

        when(objectMapper.writeValueAsString(requestObj)).thenThrow(new RuntimeException("JSON error"));

        Optional<TestResponse> result = httpPostRequestWrapper.makeRequest(url, requestObj, TestResponse.class);

        assertFalse(result.isPresent());
    }

    @Test
    void makeRequest_Non2xx() throws Exception {
        String url = "http://example.com";
        TestRequest requestObj = new TestRequest("input");

        when(objectMapper.writeValueAsString(requestObj)).thenReturn("{}");
        when(restClient.post()).thenReturn(postSpec);
        when(postSpec.uri(any(URI.class))).thenReturn(bodySpec);
        when(bodySpec.header(anyString(), anyString())).thenReturn(bodySpec);
        when(bodySpec.body(anyString())).thenReturn(bodySpec);

        when(bodySpec.exchange(any())).thenAnswer(invocation -> {
            ExchangeFunction function = invocation.getArgument(0);
            ConvertibleClientHttpResponse response = mock(ConvertibleClientHttpResponse.class);
            when(response.getStatusCode()).thenReturn(HttpStatusCode.valueOf(500));
            when(response.bodyTo(String.class)).thenReturn("Internal Server Error");
            return function.exchange(null, response);
        });

        Optional<TestResponse> result = httpPostRequestWrapper.makeRequest(url, requestObj, TestResponse.class);

        assertFalse(result.isPresent());
    }

    private record TestRequest(String input) {}
    private record TestResponse(String output) {}
}
