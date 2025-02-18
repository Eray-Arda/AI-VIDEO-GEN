package com.example.AI.Video.Generation.service.ai;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class GetImgAiImageToImageService {

    @Value("${getimg.api.key}")
    private String getimgApiKey;

    private final String endpoint = "https://api.getimg.ai/v1/stable-diffusion-xl/image-to-image";
    
    private final RestTemplate restTemplate;
    
    public GetImgAiImageToImageService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
 
    public Map<String, Object> createImageEdit(MultipartFile image, String prompt)
            throws IOException {
                
        String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
        
        Map<String, Object> payload = new HashMap<>();
        payload.put("model", "stable-diffusion-xl-v1-0");
        payload.put("prompt", prompt);
        payload.put("image", base64Image);

        //Temp Unavaliable
        //payload.put("response_format", "url");


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("accept", "application/json");
        headers.set("Authorization", "Bearer " + getimgApiKey);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(payload, headers);
        
        ResponseEntity<Map> response = restTemplate.postForEntity(endpoint, requestEntity, Map.class);
        return response.getBody();
    }
}