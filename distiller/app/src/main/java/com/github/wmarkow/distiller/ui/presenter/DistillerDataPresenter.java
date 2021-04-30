package com.github.wmarkow.distiller.ui.presenter;

import com.github.wmarkow.distiller.domain.model.DistillerData;
import com.github.wmarkow.distiller.domain.service.DistillerConnectionService;
import com.github.wmarkow.distiller.domain.service.DistillerConnectivityService;
import com.github.wmarkow.distiller.ui.DistillerDataViewIf;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import rx.Subscriber;

public class DistillerDataPresenter implements Presenter {
    private final static String TAG = "DistDataPresenter";

    private final static long AUTOREFRESH_PERIOD_MILLIS = 2000;

    private DistillerDataViewIf distillerDataView;

    private DistillerConnectivityService distillerConnectivityService;
    private Timer timer = null;

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

        dcs.readDistillerData(new DefaultDistillerDataServiceSubscriber());
    }

    @Override
    public void resume() {
        // enable autorefresh
        timer = new Timer();
        timer.schedule(new AutorefreshTimerTask(), 0, AUTOREFRESH_PERIOD_MILLIS);
    }

    @Override
    public void pause() {
        // disable autorefresh
        timer.cancel();
        timer.purge();
        timer = null;
    }

    @Override
    public void destroy() {

    }

    private class DefaultDistillerDataServiceSubscriber extends Subscriber<DistillerData> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(DistillerData distillerData) {
            distillerDataView.showDistillerData(distillerData);
        }
    }

    private class AutorefreshTimerTask extends TimerTask {

        @Override
        public void run() {
            readDistillerData();
        }
    }
}
