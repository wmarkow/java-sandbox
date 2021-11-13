package com.github.wmarkow.distiller.domain.service;

public interface DistillerForegroundServiceSubscriber {

    public void stateChanged(DistillerForegroundService.State oldState, DistillerForegroundService.State newState);
}
