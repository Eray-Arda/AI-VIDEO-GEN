package com.example.AI.Video.Generation.controller;

import com.example.AI.Video.Generation.model.Device;
import com.example.AI.Video.Generation.model.Task;
import com.example.AI.Video.Generation.service.TaskService;
import com.example.AI.Video.Generation.service.ai.RunwayMl;
import com.example.AI.Video.Generation.repository.InMemoryDeviceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/runway")
public class RunwayController {
    
    private final RunwayMl runwayMl;
    private final TaskService taskService;
    private final InMemoryDeviceRepository inMemoryDeviceRepository;

    public RunwayController(RunwayMl runwayMl, TaskService taskService, InMemoryDeviceRepository inMemoryDeviceRepository) {
        this.runwayMl = runwayMl;
        this.taskService = taskService;
        this.inMemoryDeviceRepository = inMemoryDeviceRepository;
    }


    // POST /api/runway/generate/textAndImage
    @PostMapping("/textAndImage")
    public ResponseEntity<String> generateVideo(@RequestParam("deviceId") String deviceId, @RequestParam("image") MultipartFile image, @RequestParam("prompt") String prompt) {
        Optional<Device> deviceResponse = inMemoryDeviceRepository.findById(deviceId);
        if(!deviceResponse.isPresent()){
            return ResponseEntity.status(404).body("No such device.");
        }
        try {
            if (prompt.trim().isEmpty()) {
                prompt = "Generate a video";
            }
            String runwayTaskId = runwayMl.generateVidFromImageAndText(image, prompt);

            if (runwayTaskId != null) {
                Task newTask = taskService.createTaskFromRunwayId(deviceId, runwayTaskId);
                return ResponseEntity.ok(newTask.getTaskId());
            } else {
                return ResponseEntity.status(500).body("Video generation failed.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error reading file: " + e.getMessage());
        }
    }
 
    // GET /api/runway/status?taskId=TASK_ID
    @GetMapping("/status")
    public ResponseEntity<?> getTaskStatus(@RequestParam("taskId") String taskId) {
        Map<String, Object> statusResponse = runwayMl.checkTaskStatus(taskId);
        if (statusResponse == null) {
            return ResponseEntity.status(500).body("Failed to retrieve task status.");
        }
        
        String newStatus = (String) statusResponse.get("status");
        
        String outputUrl = null;
        Object outputObj = statusResponse.get("output");
        if (outputObj instanceof java.util.List) {
            java.util.List<?> outputs = (java.util.List<?>) outputObj;
            if (!outputs.isEmpty()) {
                outputUrl = outputs.get(0).toString();
            }
        }
        
        Task updatedTask = taskService.updateTaskStatus(taskId, newStatus, outputUrl);
        return ResponseEntity.ok(updatedTask);
    }
}