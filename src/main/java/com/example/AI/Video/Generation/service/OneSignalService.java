package com.example.AI.Video.Generation.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class OneSignalService {

    @Value("${onesignal.app-id}")
    private String appId;

    @Value("${onesignal.rest-api-key}")
    private String restApiKey;

    private final String apiUrl = "https://api.onesignal.com/notifications?c=push";

    private final RestTemplate restTemplate;

    public OneSignalService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String sendPushNotification(String deviceToken, String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("accept", "application/json");
        headers.set("Authorization", "Key " + restApiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("app_id", appId);

        Map<String, String> contents = new HashMap<>();
        contents.put("en", message);
        requestBody.put("contents", contents);

        Map<String, String> headings = new HashMap<>();
        headings.put("en", "Notification");
        requestBody.put("headings", headings);

        requestBody.put("include_player_ids", Collections.singletonList(deviceToken));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);

        return response.getBody();
    }
}