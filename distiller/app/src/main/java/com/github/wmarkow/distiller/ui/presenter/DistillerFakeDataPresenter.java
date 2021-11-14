package com.github.wmarkow.distiller.ui.presenter;

import com.github.wmarkow.distiller.domain.model.DistillerDataEntity;

import java.util.ArrayList;
import java.util.List;

public class DistillerFakeDataPresenter extends DistillerDataPresenter {

    public DistillerFakeDataPresenter() {
        super();
    }

    @Override
    public void readDistillerData() {
        DistillerDataEntity dd = new DistillerDataEntity();
        dd.coldWaterTemp = (Math.random() * 1) + 15f;
        dd.hotWaterTemp = (Math.random() * 1) + 76f;
        dd.boilerTemp = (Math.random() * 0.2) + 91.5f;
        dd.headerTemp = (Math.random() * 0.2) + 80.8f;
        dd.waterRpm = (Math.random() * 50) + 1800f;

        List<DistillerDataEntity> list = new ArrayList<>();
        list.add(dd);

        getDistillerDataView().showNewDistillerData(list);
    }
}
