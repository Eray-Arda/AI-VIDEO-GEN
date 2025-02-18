package com.example.AI.Video.Generation.controller;

import com.example.AI.Video.Generation.model.Task;
import com.example.AI.Video.Generation.service.TaskService;
import com.example.AI.Video.Generation.service.ai.RunwayMl;
import java.util.Collection;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final RunwayMl runwayMl;

    public TaskController(TaskService taskService, RunwayMl runwayMl) {
        this.taskService = taskService;
        this.runwayMl = runwayMl;

    }

    // POST /api/tasks/create?deviceId=12345

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestParam String deviceId) {
        Task task = taskService.createTask(deviceId);
        return ResponseEntity.ok(task);
    }

    //GET /api/tasks/status?taskId=TASK_ID&deviceID=DEVICE_ID
    @GetMapping("/status")
    public Task getTaskStatus(@RequestParam String taskId, @RequestParam String deviceId) {
        return taskService.getTaskByIdAndDeviceId(taskId, deviceId);
    }

    // GET /api/tasks/all
        @GetMapping("/all")
    public ResponseEntity<Collection<Task>> getAllTasks() {
        Collection<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }
}