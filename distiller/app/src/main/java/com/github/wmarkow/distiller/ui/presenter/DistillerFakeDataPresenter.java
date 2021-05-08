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
        dd.systemUpTime = System.currentTimeMillis();
        dd.coldWaterTemp = (float) (Math.random() * 2) + 15f;
        dd.hotWaterTemp = (float) (Math.random() * 2) + 70f;
        dd.boilerTemp = (float) (Math.random() * 2) + 91f;
        dd.headerTemp = (float) (Math.random() * 0.5) + 78f;
        dd.waterRpm = (float)(Math.random() * 50) + 1800f;

        getDistillerDataView().showDistillerData(dd);
    }
}
