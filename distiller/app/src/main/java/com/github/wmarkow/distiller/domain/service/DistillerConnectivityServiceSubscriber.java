package com.github.wmarkow.distiller.domain.service;

import com.github.wmarkow.distiller.domain.model.DistillerData;

public interface DistillerConnectivityServiceSubscriber {

    public void onDeviceDiscoveryStarted();

    public void onDeviceDiscoveryCompleted();

    public void onDeviceDiscovered(String deviceAddress);

    public void onError(Throwable e);

    public void onDeviceConnected(String deviceAddress);

    public void onDeviceDisconnected(String deviceAddress);
}
