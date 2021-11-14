package com.github.wmarkow.distiller.ui.presenter;

import com.github.wmarkow.distiller.DistillerApplication;
import com.github.wmarkow.distiller.domain.model.DistillerDataEntity;
import com.github.wmarkow.distiller.domain.model.DistillerDatabase;
import com.github.wmarkow.distiller.ui.DistillerDataViewIf;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

public class DistillerDataPresenter implements Presenter {
    private final static String TAG = "DistDataPresenter";

    private final static long AUTOREFRESH_PERIOD_MILLIS = 2000;

    private DistillerDataViewIf distillerDataView;
    private Timer timer = null;
    private long lastTimestampInMillis = 0;

    @Inject
    public DistillerDataPresenter() {
        // calculate it as 4 hours before now, in UTC of course
        lastTimestampInMillis = ZonedDateTime.now(ZoneId.of("UTC")).minusHours(4).toInstant().toEpochMilli();
    }

    public void setView(DistillerDataViewIf distillerViewIf) {
        this.distillerDataView = distillerViewIf;
    }

    public void readDistillerData() {
        DistillerDatabase distillerDatabase = DistillerApplication.getDistillerApplication().getDistillerDatabase();

        List<DistillerDataEntity> data = distillerDatabase.distillerDataDao().loadLatestByTimestamp(lastTimestampInMillis);

        if(data.size() == 0) {
            return;
        }

        lastTimestampInMillis = data.get(data.size() - 1).utcTimestampMillis;

        distillerDataView.showNewDistillerData(data);
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

    protected DistillerDataViewIf getDistillerDataView() {
        return this.distillerDataView;
    }

    private class AutorefreshTimerTask extends TimerTask {

        @Override
        public void run() {
            readDistillerData();
        }
    }
}
