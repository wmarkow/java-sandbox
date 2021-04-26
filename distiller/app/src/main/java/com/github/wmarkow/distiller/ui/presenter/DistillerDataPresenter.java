package com.github.wmarkow.distiller.ui.presenter;

import com.github.wmarkow.distiller.domain.service.DistillerConnectionService;
import com.github.wmarkow.distiller.domain.service.DistillerConnectivityService;
import com.github.wmarkow.distiller.ui.ConnectivityViewIf;
import com.github.wmarkow.distiller.ui.DistillerDataViewIf;

import javax.inject.Inject;

public class DistillerDataPresenter implements Presenter {
    private final static String TAG = "DistDataPresenter";

    private DistillerDataViewIf distillerDataView;

    private DistillerConnectivityService distillerConnectivityService;

    @Inject
    public DistillerDataPresenter(DistillerConnectivityService distillerConnectivityService) {
        this.distillerConnectivityService = distillerConnectivityService;
    }

    public void setView(DistillerDataViewIf distillerViewIf) {
        this.distillerDataView = distillerViewIf;
    }

    public void readDistillerData() {
        DistillerConnectionService dcs = distillerConnectivityService.getConnectedDistillerConnectionService();
        if(dcs == null) {
            return;
        }

        dcs.readDistillerData();
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }
}
