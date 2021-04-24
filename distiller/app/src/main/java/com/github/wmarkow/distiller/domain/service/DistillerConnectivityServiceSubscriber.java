package com.github.wmarkow.distiller.domain.service;

import com.github.wmarkow.distiller.domain.model.DeviceInfo;

public interface DistillerConnectivityServiceSubscriber {

    public void onDeviceDiscoveryStarted();

    public void onDeviceDiscoveryCompleted(DeviceInfo deviceInfo);

    public void onError(Throwable e);

    public void onDeviceConnected(DeviceInfo deviceInfo);
}
