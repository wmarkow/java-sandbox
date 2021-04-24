package com.github.wmarkow.distiller.domain.service;

import com.github.wmarkow.distiller.domain.model.DeviceInfo;

public interface DistillerConnectivityServiceSubscriber {

    public void onDeviceDiscoveryStarted();

    public void onDeviceDiscoveryCompleted();

    public void onDeviceDiscovered(DeviceInfo deviceInfo);

    public void onError(Throwable e);

    public void onDeviceConnected(DeviceInfo deviceInfo);

    public void onDeviceDisconnected(DeviceInfo deviceInfo);
}
