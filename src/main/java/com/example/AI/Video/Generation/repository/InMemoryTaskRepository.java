package com.example.AI.Video.Generation.repository;

import com.example.AI.Video.Generation.model.Task;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryTaskRepository {

    private final Map<String, Task> taskMap = new HashMap<>();

    public Task save(Task task) {
        taskMap.put(task.getTaskId(), task);
        return task;
    }

    public Optional<Task> findById(String taskId) {
        return Optional.ofNullable(taskMap.get(taskId));
    }
    
    public Collection<Task> findAll() {
        return taskMap.values();
    }
    
    public Optional<Task> findByIdAndDeviceId(String taskId, String deviceId) {
        Task task = taskMap.get(taskId);
        if (task != null && task.getDeviceId().equals(deviceId)) {
            return Optional.of(task);
        }
        return Optional.empty();
    }

}