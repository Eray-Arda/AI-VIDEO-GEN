package com.example.AI.Video.Generation.service.ai;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class RunwayMl {

    @Value("${runwayml.api.key}")
    private String runwayApiKey;
    
    private final String runwayEndpoint = "https://api.dev.runwayml.com/v1/image_to_video";
    
    private final RestTemplate restTemplate;

    public RunwayMl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    

    public String generateVidFromImageAndText(MultipartFile image, String prompt) 
            throws IOException{

        String base64Image = "data:image/jpg;base64," + Base64.getEncoder().encodeToString(image.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + runwayApiKey);
        headers.set("X-Runway-Version", "2024-11-06");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("promptText", prompt);
        requestBody.put("promptImage", base64Image);
        requestBody.put("duration", 5);
        requestBody.put("model", "gen3a_turbo");


        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(runwayEndpoint, request, Map.class);
        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null && responseBody.containsKey("id")) {
            return responseBody.get("id").toString();
        }
        return null;
    }
    
    //Check Status
    public Map<String, Object> checkTaskStatus(String taskId) {
        String statusEndpoint = "https://api.dev.runwayml.com/v1/tasks/" + taskId;
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + runwayApiKey);
        headers.set("X-Runway-Version", "2024-11-06");

        HttpEntity<?> request = new HttpEntity<>(headers);
        
        ResponseEntity<Map> response = restTemplate.exchange(statusEndpoint, HttpMethod.GET, request, Map.class);
        
        return response.getBody();
    }
}