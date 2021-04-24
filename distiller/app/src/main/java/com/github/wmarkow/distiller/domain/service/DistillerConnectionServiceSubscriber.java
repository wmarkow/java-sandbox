package com.github.wmarkow.distiller.domain.service;

import com.github.wmarkow.distiller.domain.model.DeviceInfo;

public interface DistillerConnectionServiceSubscriber {
    public void onDistillerConnectivityChanged(DeviceInfo deviceInfo, boolean isConnected);
}
