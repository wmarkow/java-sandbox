package com.github.wmarkow.distiller.domain.interactor;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import com.github.wmarkow.distiller.domain.executor.PostExecutionThread;
import com.github.wmarkow.distiller.domain.executor.ThreadExecutor;
import com.github.wmarkow.distiller.domain.model.DistillerData;
import com.github.wmarkow.distiller.domain.service.DistillerConnectionService;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import dagger.Provides;
import rx.Observable;
import rx.Subscriber;

public class ReadDistillerDataUseCase<DistillerData> extends UseCase {
    private final static String TAG = "ReadDistDataUseCase";

    private final static UUID DISTILLER_SERVICE_UUID = UUID.fromString("4fafc201-1fb5-459e-8fcc-c5c9c331914b");
    private final static UUID DISTILLER_CHARACTERISTIC_UUID = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a8");

    private DistillerConnectionService distillerConnectionService;

    @Inject
    public ReadDistillerDataUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
    }

    public void setDistillerConnectionService(DistillerConnectionService distillerConnectionService) {
        this.distillerConnectionService = distillerConnectionService;
    }

    @Override
    public void destroy() {

    }

    @Override
    protected Observable<DistillerData> buildUseCaseObservable() {
        return Observable.create(new Observable.OnSubscribe<DistillerData>() {
            @Override
            public void call(Subscriber<? super DistillerData> subscriber) {

                try {
                    Log.i(TAG, String.format("Fetching distiller data ..."));

                    boolean result = initiateDataRead();
                    Log.i(TAG, String.format("Fetching distiller data ended with %s...", result));

                    //subscriber.onNext(notes);

                    subscriber.onCompleted();

                    return;

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                    subscriber.onError(e);
                }
            }
        });
    }

    private boolean initiateDataRead() {
        if (distillerConnectionService.isConnected() == false) {
            final String deviceAddress = distillerConnectionService.getDistillerDeviceInfo().getAddress();
            Log.e(TAG, String.format("Not connected to GATT server of %s device. Maybe you forgot to call connect()?", deviceAddress));

            return false;
        }

        BluetoothGatt gatt = distillerConnectionService.getBluetoothGatt();
        BluetoothGattService gattService = gatt.getService(DISTILLER_SERVICE_UUID);

        BluetoothGattCharacteristic distillerCharacteristic = gattService.getCharacteristic(DISTILLER_CHARACTERISTIC_UUID);
        return gatt.readCharacteristic(distillerCharacteristic);
    }
}
