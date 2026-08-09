package com.example.invoiceservice.http;

import com.example.invoiceservice.http.get.HttpGetRequestWrapper;
import com.example.invoiceservice.http.post.HttpPostRequestWrapper;
import org.springframework.stereotype.Component;

@Component
public class HttpRequestMethodResolver {
    private final HttpGetRequestWrapper getWrapper;
    private final HttpPostRequestWrapper postWrapper;

    public HttpRequestMethodResolver(HttpGetRequestWrapper getWrapper, HttpPostRequestWrapper postWrapper) {
        this.getWrapper = getWrapper;
        this.postWrapper = postWrapper;
    }

    public HttpRequestWrapperInterface resolve(HttpRequestMethod httpRequestMethod) {
        return switch (httpRequestMethod) {
            case GET -> getWrapper;
            case POST -> postWrapper;
            default -> throw new IllegalArgumentException("Unsupported method: " + httpRequestMethod);
        };
    }
}
