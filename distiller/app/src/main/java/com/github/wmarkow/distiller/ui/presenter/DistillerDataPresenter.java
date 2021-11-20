package com.github.wmarkow.distiller.ui.presenter;

import com.github.wmarkow.distiller.DistillerApplication;
import com.github.wmarkow.distiller.di.PerFragment;
import com.github.wmarkow.distiller.domain.interactor.DefaultSubscriber;
import com.github.wmarkow.distiller.domain.interactor.ReadDistillerDatabaseDataUseCase;
import com.github.wmarkow.distiller.domain.interactor.ReadDistillerFakeDatabaseDataUseCase;
import com.github.wmarkow.distiller.domain.model.DistillerDataEntity;
import com.github.wmarkow.distiller.domain.model.DistillerDatabase;
import com.github.wmarkow.distiller.ui.DistillerDataViewIf;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

@PerFragment
public class DistillerDataPresenter implements Presenter {
    private final static String TAG = "DistDataPresenter";

    private final static int AUTOREFRESH_PERIOD_SECONDS = 2;

    private DistillerDataViewIf distillerDataView;
    private Timer timer = null;
    private long lastTimestampInMillis = 0;

    //private ReadDistillerFakeDatabaseDataUseCase readDataUseCase;
    private ReadDistillerDatabaseDataUseCase readDataUseCase;

    @Inject
    public DistillerDataPresenter(ReadDistillerDatabaseDataUseCase readDataUseCase) {
        this.readDataUseCase = readDataUseCase;
        // calculate it as 4 hours before now, in UTC of course
        lastTimestampInMillis = ZonedDateTime.now(ZoneId.of("UTC")).minusHours(4).toInstant().toEpochMilli();
    }

    public void setView(DistillerDataViewIf distillerViewIf) {
        this.distillerDataView = distillerViewIf;

        // data comes every 2 seconds
        distillerViewIf.setXRangeResolutionSeconds(AUTOREFRESH_PERIOD_SECONDS);
        // default chart span is 120 seconds
        distillerViewIf.setXRangeVisibleSpanSeconds(120);
    }

    public void readDistillerData() {
        readDataUseCase.setLastTimestampInMillis(lastTimestampInMillis);
        readDataUseCase.execute(new ReadDataSubscriber());

        // reads from the database
        //DistillerDatabase distillerDatabase = DistillerApplication.getDistillerApplication().getDistillerDatabase();
        //List<DistillerDataEntity> data = distillerDatabase.distillerDataDao().loadLatestByTimestamp(lastTimestampInMillis);
    }

    @Override
    public void resume() {
        // enable autorefresh
        timer = new Timer();
        timer.schedule(new AutorefreshTimerTask(), 0, 1000 * AUTOREFRESH_PERIOD_SECONDS);
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

    private class ReadDataSubscriber extends DefaultSubscriber<List<DistillerDataEntity>> {
        @Override
        public void onStart() {
            // do nothing by default
        }

        @Override
        public void onCompleted() {
            // no-op by default.
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(List<DistillerDataEntity> data) {
            if(data.size() == 0) {
                return;
            }

            lastTimestampInMillis = data.get(data.size() - 1).utcTimestampMillis;
            distillerDataView.showNewDistillerData(data);
        }
    }
}
