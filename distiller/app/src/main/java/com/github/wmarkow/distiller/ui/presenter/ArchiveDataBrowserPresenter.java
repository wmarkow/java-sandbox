package com.github.wmarkow.distiller.ui.presenter;

import com.github.wmarkow.distiller.di.PerFragment;
import com.github.wmarkow.distiller.domain.interactor.DefaultSubscriber;
import com.github.wmarkow.distiller.domain.interactor.ReadArchiveDatabaseDataUseCase;
import com.github.wmarkow.distiller.domain.interactor.ReadDistillerDatabaseDataUseCase;
import com.github.wmarkow.distiller.domain.model.DistillerDataEntity;
import com.github.wmarkow.distiller.ui.DistillerDataViewIf;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

@PerFragment
public class ArchiveDataBrowserPresenter implements Presenter {
    private final static String TAG = "DistDataPresenter";

    private final static int AUTOREFRESH_PERIOD_SECONDS = 2;

    private DistillerDataViewIf distillerDataView;
    private ReadArchiveDatabaseDataUseCase readArchiveDatabaseDataUseCase;

    @Inject
    public ArchiveDataBrowserPresenter(ReadArchiveDatabaseDataUseCase readArchiveDatabaseDataUseCase) {
        this.readArchiveDatabaseDataUseCase = readArchiveDatabaseDataUseCase;
    }

    public void setView(DistillerDataViewIf distillerViewIf) {
        this.distillerDataView = distillerViewIf;

        // data comes every 2 seconds
        distillerViewIf.setXRangeResolutionSeconds(AUTOREFRESH_PERIOD_SECONDS);
        // default chart span is 120 seconds
        distillerViewIf.setXRangeVisibleSpanSeconds(120);
    }

    public void readDistillerData(ZonedDateTime date) {
        readArchiveDatabaseDataUseCase.setDate(date);
        readArchiveDatabaseDataUseCase.execute(new ReadDataSubscriber());
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
            distillerDataView.showNewDistillerData(data);
        }
    }
}
