package com.example.invoiceservice.http;

import com.example.invoiceservice.http.get.HttpGetRequestWrapper;
import com.example.invoiceservice.http.post.HttpPostRequestWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HttpRequestMethodResolverTest {

    @Mock
    private HttpGetRequestWrapper getWrapper;

    @Mock
    private HttpPostRequestWrapper postWrapper;

    private HttpRequestMethodResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new HttpRequestMethodResolver(getWrapper, postWrapper);
    }

    @Test
    void resolve_Get() {
        HttpRequestWrapperInterface result = resolver.resolve(HttpRequestMethod.GET);
        assertEquals(getWrapper, result);
    }

    @Test
    void resolve_Post() {
        HttpRequestWrapperInterface result = resolver.resolve(HttpRequestMethod.POST);
        assertEquals(postWrapper, result);
    }

    @Test
    void resolve_Unsupported() {
        assertThrows(IllegalArgumentException.class, () -> resolver.resolve(HttpRequestMethod.PUT));
    }
}
