package com.example.AI.Video.Generation.controller;

import com.example.AI.Video.Generation.model.Device;
import com.example.AI.Video.Generation.model.Task;
import com.example.AI.Video.Generation.service.TaskService;
import com.example.AI.Video.Generation.service.ai.NovitaAiService;
import com.example.AI.Video.Generation.repository.InMemoryDeviceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/novita")
public class NovitaAiController {

    private final NovitaAiService novitaAiService;
    private final TaskService taskService;
    private final InMemoryDeviceRepository inMemoryDeviceRepository;

    public NovitaAiController(NovitaAiService novitaAiService, TaskService taskService, InMemoryDeviceRepository inMemoryDeviceRepository) {
        this.novitaAiService = novitaAiService;
        this.taskService = taskService;
        this.inMemoryDeviceRepository = inMemoryDeviceRepository;
    }
    
 
    @PostMapping("/textToVideo")
    public ResponseEntity<String> generateVideoFromText(@RequestParam("deviceId") String deviceId, @RequestParam("prompt") String prompt) {
        
        Optional<Device> deviceResponse = inMemoryDeviceRepository.findById(deviceId);
        if(!deviceResponse.isPresent()){
            return ResponseEntity.status(404).body("No such device.");
        }
        if (prompt.trim().isEmpty()) {
            prompt = "Generate a video";
        }
        String novitaTaskId = novitaAiService.generateTxt2Video(prompt);
        if (novitaTaskId != null) {
            Task newTask = taskService.createTaskFromRunwayId(deviceId, novitaTaskId);
            return ResponseEntity.ok(newTask.getTaskId());
        } else {
            return ResponseEntity.status(500).body("Text-to-video generation failed.");
        }
    }
    

    @GetMapping("/status")
    public ResponseEntity<Task> getTaskStatus(@RequestParam("taskId") String taskId) {
        Map<String, Object> statusResponse = novitaAiService.checkTaskStatus(taskId);
        if (statusResponse == null) {
            return ResponseEntity.status(500).body(null);
        }
        Map<String, Object> taskInfo = (Map<String, Object>) statusResponse.get("task");
        String newStatus = taskInfo != null ? (String) taskInfo.get("status") : null;
        String outputUrl = null;
        if (statusResponse.containsKey("videos")) {
            Object videosObj = statusResponse.get("videos");
            if (videosObj instanceof java.util.List) {
                java.util.List<?> videos = (java.util.List<?>) videosObj;
                if (!videos.isEmpty()) {
                    Map<String, Object> video = (Map<String, Object>) videos.get(0);
                    outputUrl = (String) video.get("video_url");
                }
            }
        }
        Task updatedTask = taskService.updateTaskStatus(taskId, newStatus, outputUrl);
        return ResponseEntity.ok(updatedTask);
    }
}