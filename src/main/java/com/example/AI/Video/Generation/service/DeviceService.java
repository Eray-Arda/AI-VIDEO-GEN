package com.example.AI.Video.Generation.service;

import com.example.AI.Video.Generation.model.Device;
import com.example.AI.Video.Generation.repository.InMemoryDeviceRepository;
import org.springframework.stereotype.Service;

@Service
public class DeviceService {

    private final InMemoryDeviceRepository repository;

    public DeviceService(InMemoryDeviceRepository repository) {
        this.repository = repository;
    }


    public Device updateOrCreateDevice(String deviceId, String oneSignalToken) {
        Device device = repository.findById(deviceId).orElse(new Device(deviceId, oneSignalToken));
        device.setOneSignalToken(oneSignalToken);
        return repository.save(device);
    }
    
    public Device getDevice(String deviceId) {
        return repository.findById(deviceId).orElse(null);
    }
}