package com.github.wmarkow.distiller.domain.service;

public interface DistillerConnectionServiceSubscriber {
    public void onDistillerConnectivityChanged(String deviceAddress, boolean isConnected);
}
