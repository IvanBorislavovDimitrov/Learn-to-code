package com.code.to.learn.web.client;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ResilientHttpClient implements HttpClient {

    private final HttpClient httpClient = HttpClientBuilder.create().build();
    private final ResilientExecutor resilientExecutor = new ResilientExecutor();

    @Override
    public HttpParams getParams() {
        return httpClient.getParams();
    }

    @Override
    public ClientConnectionManager getConnectionManager() {
        return httpClient.getConnectionManager();
    }

    @Override
    public HttpResponse execute(HttpUriRequest request) throws IOException, ClientProtocolException {
        return resilientExecutor.executeWithRetry(() -> httpClient.execute(request));
    }

    @Override
    public HttpResponse execute(HttpUriRequest request, HttpContext context) throws IOException, ClientProtocolException {
        return resilientExecutor.executeWithRetry(() -> httpClient.execute(request, context));
    }

    @Override
    public HttpResponse execute(HttpHost target, HttpRequest request) throws IOException, ClientProtocolException {
        return resilientExecutor.executeWithRetry(() -> httpClient.execute(target, request));
    }

    @Override
    public HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context) throws IOException, ClientProtocolException {
        return resilientExecutor.executeWithRetry(() -> httpClient.execute(target, request, context));
    }

    @Override
    public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
        return resilientExecutor.executeWithRetry(() -> httpClient.execute(request, responseHandler));
    }

    @Override
    public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException, ClientProtocolException {
        return resilientExecutor.executeWithRetry(() -> httpClient.execute(request, responseHandler));
    }

    @Override
    public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
        return resilientExecutor.executeWithRetry(() -> httpClient.execute(target, request, responseHandler));
    }

    @Override
    public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException, ClientProtocolException {
        return resilientExecutor.executeWithRetry(() -> httpClient.execute(target, request, responseHandler, context));
    }
}
