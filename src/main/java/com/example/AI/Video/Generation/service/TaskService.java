package com.example.AI.Video.Generation.service;

import com.example.AI.Video.Generation.model.Device;
import com.example.AI.Video.Generation.model.Task;
import com.example.AI.Video.Generation.repository.InMemoryTaskRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
public class TaskService {

    private final InMemoryTaskRepository repository;
    private final OneSignalService oneSignalService;
    private final DeviceService deviceService;

    public TaskService(InMemoryTaskRepository repository, OneSignalService oneSignalService, DeviceService deviceService) {
        this.repository = repository;
        this.oneSignalService = oneSignalService;
        this.deviceService = deviceService;
    }

    //Not Used
    public Task createTask(String deviceId) {
        String taskId = UUID.randomUUID().toString();
        Task task = new Task(taskId, deviceId, "pending");
        return repository.save(task);
    }

    public Task createTaskFromRunwayId(String deviceId, String runwayTaskId) {
        Task task = new Task(runwayTaskId, deviceId, "pending");
        return repository.save(task);
    }


    public Task updateTaskStatus(String taskId, String status, String outputUrl) {
        Task task = repository.findById(taskId).orElse(null);
        if (task != null) {
            task.setStatus(status);
            task.setOutputUrl(outputUrl);
            repository.save(task);

            //If Completed
            if ("SUCCEEDED".equalsIgnoreCase(status) || "TASK_STATUS_SUCCEED".equalsIgnoreCase(status)) {
                Device device = deviceService.getDevice(task.getDeviceId());
                if (device != null) {
                    String deviceToken = device.getOneSignalToken();
                    oneSignalService.sendPushNotification(deviceToken, "İçerik hazir!");
                }
            }
        }
        return task;
    }


    public Task getTask(String taskId) {
        return repository.findById(taskId).orElse(null);
    }

    public Task getTaskByIdAndDeviceId(String taskId, String deviceId){
        return repository.findByIdAndDeviceId(taskId, deviceId).orElse(null);
    }

    public Collection<Task> getAllTasks() {
        return repository.findAll();
    }
}