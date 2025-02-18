package com.example.AI.Video.Generation.controller;


import com.example.AI.Video.Generation.model.Device;
import com.example.AI.Video.Generation.service.DeviceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }


    // POST /api/devices/register?deviceId=12345&oneSignalToken=TOKEN123

    @PostMapping("/register")
    public ResponseEntity<Device> registerOrUpdateDevice(
            @RequestParam String deviceId,
            @RequestParam String oneSignalToken) {
        Device device = deviceService.updateOrCreateDevice(deviceId, oneSignalToken);
        return ResponseEntity.ok(device);
    }
}