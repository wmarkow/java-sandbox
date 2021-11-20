package com.github.wmarkow.distiller.domain.interactor;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import com.github.wmarkow.distiller.domain.executor.PostExecutionThread;
import com.github.wmarkow.distiller.domain.executor.ThreadExecutor;
import com.github.wmarkow.distiller.domain.model.DistillerDataEntity;
import com.github.wmarkow.distiller.domain.service.BluetoothGattCharacteristicReadCalback;
import com.github.wmarkow.distiller.domain.service.DistillerConnectionService;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/***
 * Prepare a fake data and return them as they were from the database.
 * @param <T>
 */
public class ReadDistillerFakeDatabaseDataUseCase<T extends DistillerDataEntity> extends UseCase {
    private final static String TAG = "ReadDistDataUseCase";


    @Inject
    public ReadDistillerFakeDatabaseDataUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
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
                    Log.i(TAG, String.format("Fetching fake database distiller data ..."));

                    DistillerDataEntity dd = new DistillerDataEntity();
                    dd.coldWaterTemp = (Math.random() * 1) + 15f;
                    dd.hotWaterTemp = (Math.random() * 1) + 76f;
                    dd.boilerTemp = (Math.random() * 0.2) + 91.5f;
                    dd.headerTemp = (Math.random() * 0.2) + 80.8f;
                    dd.waterRpm = (Math.random() * 50) + 1800f;
                    // uncomment below to simulate null temperatures
                    //dd.coldWaterTemp = null;
                    //dd.hotWaterTemp = null;
                    //dd.headerTemp = null;
                    //dd.boilerTemp = null;

                    List<DistillerDataEntity> list = new ArrayList<DistillerDataEntity>();
                    list.add(dd);
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                    subscriber.onError(e);
                }
            }
        });
    }
}
