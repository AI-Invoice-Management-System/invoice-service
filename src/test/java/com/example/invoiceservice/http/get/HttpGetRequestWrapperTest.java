package com.example.invoiceservice.http.get;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestHeadersSpec.ExchangeFunction;
import org.springframework.web.client.RestClient.RequestHeadersSpec.ConvertibleClientHttpResponse;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HttpGetRequestWrapperTest {

    @Mock
    private RestClient restClient;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RestClient.RequestHeadersUriSpec getSpec;

    @Mock
    private RestClient.RequestHeadersSpec headersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    private HttpGetRequestWrapper httpGetRequestWrapper;

    @BeforeEach
    void setUp() {
        httpGetRequestWrapper = new HttpGetRequestWrapper(restClient, objectMapper);
    }

    @Test
    void makeRequest_Success() throws Exception {
        String url = "http://example.com";
        String responseBody = "{\"key\":\"value\"}";
        TestResponse testResponse = new TestResponse("value");

        when(restClient.get()).thenReturn(getSpec);
        when(getSpec.uri(any(URI.class))).thenReturn(headersSpec);
        when(headersSpec.header(anyString(), anyString())).thenReturn(headersSpec);
        
        // Mocking exchange
        when(headersSpec.exchange(any())).thenAnswer(invocation -> {
            ExchangeFunction function = invocation.getArgument(0);
            
            ConvertibleClientHttpResponse response = mock(ConvertibleClientHttpResponse.class);
            when(response.getStatusCode()).thenReturn(HttpStatusCode.valueOf(200));
            when(response.bodyTo(String.class)).thenReturn(responseBody);
            
            return function.exchange(null, response);
        });

        when(objectMapper.readValue(responseBody, TestResponse.class)).thenReturn(testResponse);

        Optional<TestResponse> result = httpGetRequestWrapper.makeRequest(url, TestResponse.class);

        assertTrue(result.isPresent());
        assertEquals("value", result.get().key());
        verify(restClient).get();
    }

    @Test
    void makeRequest_Failure_Non2xx() {
        String url = "http://example.com";

        when(restClient.get()).thenReturn(getSpec);
        when(getSpec.uri(any(URI.class))).thenReturn(headersSpec);
        when(headersSpec.header(anyString(), anyString())).thenReturn(headersSpec);

        when(headersSpec.exchange(any())).thenAnswer(invocation -> {
            ExchangeFunction function = invocation.getArgument(0);
            
            RestClient.RequestHeadersSpec.ConvertibleClientHttpResponse response = mock(RestClient.RequestHeadersSpec.ConvertibleClientHttpResponse.class);
            when(response.getStatusCode()).thenReturn(HttpStatusCode.valueOf(404));
            when(response.bodyTo(String.class)).thenReturn("Not Found");
            return function.exchange(null, response);
        });

        Optional<TestResponse> result = httpGetRequestWrapper.makeRequest(url, TestResponse.class);

        assertFalse(result.isPresent());
    }

    @Test
    void makeRequest_Exception() {
        String url = "http://example.com";

        when(restClient.get()).thenThrow(new RuntimeException("Connection error"));

        Optional<TestResponse> result = httpGetRequestWrapper.makeRequest(url, TestResponse.class);

        assertFalse(result.isPresent());
        verify(restClient).get();
    }

    private record TestResponse(String key) {}
}
