package com.example.AI.Video.Generation.model;

import java.time.LocalDateTime;

public class Task {

    private String taskId;
    private String deviceId;
    private String status;
    private String outputUrl;
    private LocalDateTime createdAt;

    public Task() { }

    public Task(String taskId, String deviceId, String status) {
        this.taskId = taskId;
        this.deviceId = deviceId;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOutputUrl() {
        return outputUrl;
    }

    public void setOutputUrl(String outputUrl) {
        this.outputUrl = outputUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}