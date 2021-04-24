package com.github.wmarkow.distiller.domain.model;

public class DeviceInfo {
    private String address;
    private boolean connected = false;

    public DeviceInfo(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
