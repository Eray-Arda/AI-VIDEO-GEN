package com.example.AI.Video.Generation.controller;

import com.example.AI.Video.Generation.service.OneSignalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private OneSignalService oneSignalService;

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(
            @RequestParam String deviceToken,
            @RequestParam String message) {
        String response = oneSignalService.sendPushNotification(deviceToken, message);
        return ResponseEntity.ok(response);
    }
}