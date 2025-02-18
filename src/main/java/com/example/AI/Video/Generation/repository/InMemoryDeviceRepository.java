package com.example.AI.Video.Generation.repository;

import com.example.AI.Video.Generation.model.Device;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Repository
public class InMemoryDeviceRepository {

    private final Map<String, Device> deviceMap = new HashMap<>();

    public Device save(Device device) {
        deviceMap.put(device.getDeviceId(), device);
        return device;
    }

    public Optional<Device> findById(String deviceId) {
        return Optional.ofNullable(deviceMap.get(deviceId));
    }
    
}
