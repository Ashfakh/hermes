package com.ashfakh.hermes.client;

import org.apache.http.*;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.*;

import org.apache.http.client.HttpClient;
import org.apache.http.HttpHeaders;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Service
public class HttpLocalClient {

    public String sendOverHTTP(String uri, Map<String, String> headers, String jsonBody) {
        try {
            HttpRequest.Builder request = HttpRequest.newBuilder()
                    .uri(new URI(uri));
            headers.forEach(request::header);
            HttpRequest finalRequest = request
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            java.net.http.HttpClient http = java.net.http.HttpClient.newHttpClient();
            HttpResponse<String> response = http.send(finalRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            return response.body();

        } catch (URISyntaxException | IOException |
                InterruptedException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String sendJsonOverHTTPWIthBearerToken(String uri, String bearerToken, String jsonBody, Map<String, String> additionalHeaders) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", String.format("Bearer %s", bearerToken));
        headers.put("Content-Type", "application/json");
        headers.putAll(additionalHeaders);
        return sendOverHTTP(uri, headers, jsonBody);
    }

    public String sendFileOverHTTPWIthBearerToken(String url, String bearerToken, ContentBody fileBody,  Map<String, String> additionalParams) {

        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", bearerToken));

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            additionalParams.forEach(builder::addTextBody);
            builder.addPart("file", fileBody);

            HttpEntity multipart = builder.build();
            httpPost.setEntity(multipart);

            org.apache.http.HttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            return EntityUtils.toString(responseEntity);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


}
