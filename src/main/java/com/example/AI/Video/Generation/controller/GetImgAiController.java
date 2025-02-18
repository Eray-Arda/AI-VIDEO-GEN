package com.example.AI.Video.Generation.controller;

import com.example.AI.Video.Generation.model.Device;
import com.example.AI.Video.Generation.service.ai.GetImgAiImageToImageService;
import com.example.AI.Video.Generation.repository.InMemoryDeviceRepository;
import com.example.AI.Video.Generation.service.TaskService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;


import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/getimg")
public class GetImgAiController {
    
    private final GetImgAiImageToImageService getImgService;
    private final TaskService taskService;
    private final InMemoryDeviceRepository inMemoryDeviceRepository;

    public GetImgAiController(GetImgAiImageToImageService getImgService, InMemoryDeviceRepository inMemoryDeviceRepository, TaskService taskService) {
        this.getImgService = getImgService;
        this.inMemoryDeviceRepository = inMemoryDeviceRepository;
        this.taskService = taskService;
    }
    
    @PostMapping("/imageToImage")
    public ResponseEntity<?> createImageEdit(@RequestParam("image") MultipartFile image, @RequestParam("prompt") String prompt, @RequestParam("deviceId") String deviceId) {
        Optional<Device> deviceResponse = inMemoryDeviceRepository.findById(deviceId);
        if(!deviceResponse.isPresent()){
            return ResponseEntity.status(404).body("No such device.");
        }
        try {
            Map<String, Object> response = getImgService.createImageEdit(image, prompt);
            String taskID = UUID.randomUUID().toString();
            taskService.createTaskFromRunwayId(deviceId, taskID);
            if (response != null && response.containsKey("image")) {
                String base64Image = (String) response.get("image");
                // Decode the base64 string into a byte array.
                byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);
                taskService.updateTaskStatus(taskID, "SUCCEEDED", base64Image);
                return new ResponseEntity<>(decodedBytes, headers, HttpStatus.OK);
            } else {
                return ResponseEntity.status(500).body("No image field found in the response.");
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error processing file: " + e.getMessage());
        }
    }
}