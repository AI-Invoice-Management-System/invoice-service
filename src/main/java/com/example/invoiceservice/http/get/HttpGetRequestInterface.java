package com.example.invoiceservice.http.get;

import com.example.invoiceservice.http.HttpRequestWrapperInterface;

import java.util.Optional;

public interface HttpGetRequestInterface extends HttpRequestWrapperInterface {
    <R> Optional<R> makeRequest(String url, Class<R> responseType);
}
