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
    private final static UUID WATER_INPUT_TEMP_CHARACTERISTIC_UUID = UUID.fromString("71c6e9dc-3ddb-4d7e-ab3e-83678d439e97");
    private final static UUID WATER_OUTPUT_TEMP_CHARACTERISTIC_UUID = UUID.fromString("8129dfa3-8f14-45c3-851c-b17c15086e6d");
    private final static UUID WATER_FLOW_CHARACTERISTIC_UUID = UUID.fromString("8c0cb23c-3d34-43b0-92cf-e7eba4220616");
    private final static UUID HEAD_TEMP_CHARACTERISTIC_UUID = UUID.fromString("5f5fae58-da57-4b24-8a5c-260272e2e066");
    private final static UUID KEG_TEMP_CHARACTERISTIC_UUID = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a8");

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

        BluetoothGattCharacteristic gattCharacteristic = gattService.getCharacteristic(WATER_INPUT_TEMP_CHARACTERISTIC_UUID);
        boolean result = gatt.readCharacteristic(gattCharacteristic);
        if(!result) {
            return false;
        }
        gatt.read
       /* gattCharacteristic = gattService.getCharacteristic(WATER_OUTPUT_TEMP_CHARACTERISTIC_UUID);
        result = gatt.readCharacteristic(gattCharacteristic);
        if(!result) {
            return false;
        }

        gattCharacteristic = gattService.getCharacteristic(WATER_FLOW_CHARACTERISTIC_UUID);
        result = gatt.readCharacteristic(gattCharacteristic);
        if(!result) {
            return false;
        }

        gattCharacteristic = gattService.getCharacteristic(HEAD_TEMP_CHARACTERISTIC_UUID);
        result = gatt.readCharacteristic(gattCharacteristic);
        if(!result) {
            return false;
        }

        gattCharacteristic = gattService.getCharacteristic(KEG_TEMP_CHARACTERISTIC_UUID);
        result = gatt.readCharacteristic(gattCharacteristic);
        if(!result) {
            return false;
        }*/

        return true;
    }
}
