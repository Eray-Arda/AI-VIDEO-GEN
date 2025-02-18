package com.example.AI.Video.Generation.model;

public class Device {

    private String deviceId;
    private String oneSignalToken;


    public Device() { }


    public Device(String deviceId, String oneSignalToken) {
        this.deviceId = deviceId;
        this.oneSignalToken = oneSignalToken;
    }

    //Getter and Setter
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getOneSignalToken() {
        return oneSignalToken;
    }

    public void setOneSignalToken(String oneSignalToken) {
        this.oneSignalToken = oneSignalToken;
    }
}