package com.github.wmarkow.distiller.domain.interactor;

import android.util.Log;

import com.github.wmarkow.distiller.DistillerApplication;
import com.github.wmarkow.distiller.domain.executor.PostExecutionThread;
import com.github.wmarkow.distiller.domain.executor.ThreadExecutor;
import com.github.wmarkow.distiller.domain.model.DistillerDataEntity;
import com.github.wmarkow.distiller.domain.model.DistillerDatabase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/***
 * Reads data from the database.
 * @param <T>
 */
public class ReadDistillerDatabaseDataUseCase<T extends DistillerDataEntity> extends UseCase {
    private final static String TAG = "ReadDistDataUseCase";

    private long lastTimestampInMillis = 0;

    @Inject
    public ReadDistillerDatabaseDataUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
    }

    public void setLastTimestampInMillis(long lastTimestampInMillis) {
        this.lastTimestampInMillis = lastTimestampInMillis;
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
                    List<DistillerDataEntity> data = distillerDatabase.distillerDataDao().loadLatestByTimestamp(lastTimestampInMillis);

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
