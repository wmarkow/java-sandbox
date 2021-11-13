package com.github.wmarkow.distiller.ui.presenter;

import com.github.wmarkow.distiller.domain.model.DistillerData;
import com.github.wmarkow.distiller.domain.service.DistillerConnectionService;
import com.github.wmarkow.distiller.domain.service.DistillerConnectivityService;

public class DistillerFakeDataPresenter extends DistillerDataPresenter {

    public DistillerFakeDataPresenter() {
        super(null);
    }

    @Override
    public void readDistillerData() {
        DistillerData dd = new DistillerData();
        dd.deviceUpTime = System.currentTimeMillis();
        dd.coldWaterTemp = (float) (Math.random() * 1) + 15f;
        dd.hotWaterTemp = (float) (Math.random() * 1) + 76f;
        dd.boilerTemp = (float) (Math.random() * 0.2) + 91.5f;
        dd.headerTemp = (float) (Math.random() * 0.2) + 80.8f;
        dd.waterRpm = (float)(Math.random() * 50) + 1800f;

        getDistillerDataView().showDistillerData(dd);
    }
}
