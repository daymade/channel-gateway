package tech.typeof.backend.gateway.channel.infrastructure.transport.protocol.http;

import tech.typeof.backend.gateway.channel.infrastructure.transport.protocol.Transport;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class HttpTransport implements Transport {
    private HttpClient httpClient;

    public HttpTransport() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public HttpTransport(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public String sendRequest(Map<String, Object> config) throws RuntimeException {
        // Extract configuration parameters
        var host = (String) config.get("host");
        var port = (int) config.get("port");
        var url = (String) config.get("url");
        var verb = (String) config.get("verb");
        var headers = (Map<String, String>) config.get("headers");
        var queryParams = (Map<String, String>) config.get("queryParams");
        var body = (String) config.get("body");

        // Build the full URI with query parameters
        URI uri = null;
        try {
            uri = new URI("http", null, host, port, url, buildQueryString(queryParams), null);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // Create the request
        var requestBuilder = HttpRequest.newBuilder()
                .uri(uri)
                .method(verb.toUpperCase(), HttpRequest.BodyPublishers.ofString(body != null ? body : ""));

        // Add headers
        if (headers != null) {
            headers.forEach(requestBuilder::header);
        }

        var request = requestBuilder.build();

        // Send the request and get the response
        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return response.body();
    }

    private String buildQueryString(Map<String, String> queryParams) {
        if (queryParams == null || queryParams.isEmpty()) {
            return null;
        }

        var queryString = new StringBuilder();

        queryParams.forEach((key, value) -> {
            if (!queryString.isEmpty()) {
                queryString.append("&");
            }
            queryString.append(key).append("=").append(value);
        });

        return queryString.toString();
    }
}
