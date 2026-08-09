package com.example.invoiceservice.http.post;

import com.example.invoiceservice.http.HttpRequestWrapperInterface;

import java.util.Optional;

public interface HttpPostRequestInterface extends HttpRequestWrapperInterface {
    <R,T> Optional<R> makeRequest(String url, T requestBody, Class<R> responseType);
}
