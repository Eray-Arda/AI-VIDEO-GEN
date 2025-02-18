package com.example.AI.Video.Generation.service.ai;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NovitaAiService {

    @Value("${novitaai.api.key}")
    private String novitaApiKey;

    private final String generateEndpoint = "https://api.novita.ai/v3/async/txt2video";
    
    private final String statusEndpoint = "https://api.novita.ai/v3/async/task-result";

    private final RestTemplate restTemplate;
    
    public NovitaAiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    

    public String generateTxt2Video(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + novitaApiKey);
        
        Map<String, Object> payload = new HashMap<>();
        payload.put("model_name", "darkSushiMixMix_225D_64380.safetensors");
        payload.put("width", 640);
        payload.put("height", 480);
        payload.put("guidance_scale", 7.5);
        payload.put("steps", 20);
        payload.put("seed", -1);

        Map<String, Object> promptObj = new HashMap<>();
        promptObj.put("prompt", prompt);
        promptObj.put("frames", 16);
        payload.put("prompts", new Map[] { promptObj });

        payload.put("negative_prompt", "(worst quality:2), (low quality:2), (normal quality:2), ((monochrome)), ((grayscale)), bad hands");
        
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(generateEndpoint, request, Map.class);
        
        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null && responseBody.containsKey("task_id")) {
            return responseBody.get("task_id").toString();
        }
        return null;
    }
    

    public Map<String, Object> checkTaskStatus(String taskId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + novitaApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        String url = statusEndpoint + "?task_id=" + taskId;
        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
        return response.getBody();
    }
}