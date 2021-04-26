package com.github.wmarkow.distiller.domain.service;

import com.github.wmarkow.distiller.domain.model.DistillerData;

public interface DistillerDataServiceSubscriber {
    public void onDistillerDataRead(String deviceAddress, DistillerData distillerData);
}
