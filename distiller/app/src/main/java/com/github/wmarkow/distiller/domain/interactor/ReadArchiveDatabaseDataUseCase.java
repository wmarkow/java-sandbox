package com.github.wmarkow.distiller.domain.interactor;

import android.util.Log;

import com.github.wmarkow.distiller.DistillerApplication;
import com.github.wmarkow.distiller.domain.executor.PostExecutionThread;
import com.github.wmarkow.distiller.domain.executor.ThreadExecutor;
import com.github.wmarkow.distiller.domain.model.DistillerDataEntity;
import com.github.wmarkow.distiller.domain.model.DistillerDatabase;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/***
 * Reads data from the database.
 * @param <T>
 */
public class ReadArchiveDatabaseDataUseCase<T extends DistillerDataEntity> extends UseCase {
    private final static String TAG = "ReadArchiveDatabaseDataUseCase";

    private long startTimestampInMillis;
    private long endTimestampInMillis;


    @Inject
    public ReadArchiveDatabaseDataUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
    }

    public void setDate(ZonedDateTime zonedDateTime) {
        ZonedDateTime midnight = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC")).withHour(0).withMinute(0).withSecond(0);

        startTimestampInMillis = midnight.toInstant().toEpochMilli();
        endTimestampInMillis = midnight.plusDays(1).toInstant().toEpochMilli();
    }

    @Override
    public void destroy() {

    }

    @Override
    protected Observable<List<DistillerDataEntity>> buildUseCaseObservable() {
        return Observable.create(new Observable.OnSubscribe<List<DistillerDataEntity>>() {
            @Override
            public void call(Subscriber<? super List<DistillerDataEntity>> subscriber) {

                try {
                    Log.i(TAG, String.format("Fetching database distiller data ..."));

                    DistillerDatabase distillerDatabase = DistillerApplication.getDistillerApplication().getDistillerDatabase();

                    List<DistillerDataEntity> data = distillerDatabase.distillerDataDao().loadByTimestampRange(startTimestampInMillis, endTimestampInMillis);

                    subscriber.onNext(data);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                    subscriber.onError(e);
                }
            }
        });
    }
}
