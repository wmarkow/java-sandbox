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
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/***
 * Reads the data directly from the device.
 * @param <T>
 */
public class ReadDistillerDeviceDataUseCase<T extends DistillerDataEntity> extends ForegroundServiceUseCase {
    private final static String TAG = "ReadDistDevDataUseCase";

    private final static UUID DISTILLER_SERVICE_UUID = UUID.fromString("4fafc201-1fb5-459e-8fcc-c5c9c331914b");
    private final static UUID DISTILLER_CHARACTERISTIC_UUID = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a8");

    private DistillerConnectionService distillerConnectionService;
    private DefaultBluetoothGattCharacteristicReadCallback bluetoothGattCharacteristicReadCallback;
    private CountDownLatch countDownLatch;
    private DistillerDataEntity distillerData;

    @Inject
    public ReadDistillerDeviceDataUseCase() {
        bluetoothGattCharacteristicReadCallback = new DefaultBluetoothGattCharacteristicReadCallback();
    }

    public void setDistillerConnectionService(DistillerConnectionService distillerConnectionService) {
        if(this.distillerConnectionService == null || (this.distillerConnectionService != distillerConnectionService)) {
            distillerConnectionService.addBluetoothGattCharacteristicReadCallback(bluetoothGattCharacteristicReadCallback);
        }

        this.distillerConnectionService = distillerConnectionService;
    }

    @Override
    public void destroy() {
        distillerConnectionService.removeBluetoothGattCharacteristicReadCallback(bluetoothGattCharacteristicReadCallback);

        distillerConnectionService = null;
        bluetoothGattCharacteristicReadCallback = null;
    }

    @Override
    protected Observable<DistillerDataEntity> buildUseCaseObservable() {
        return Observable.create(new Observable.OnSubscribe<DistillerDataEntity>() {
            @Override
            public void call(Subscriber<? super DistillerDataEntity> subscriber) {

                try {
                    Log.i(TAG, String.format("Fetching distiller data ..."));

                    countDownLatch = new CountDownLatch(1);

                    boolean result = initiateDataRead();
                    if(!result) {
                        Exception ex = new Exception("boolean ReadDistillerDataUseCase::initiateDataRead() returned false. Data will not be fetched.");
                        subscriber.onError(new Exception());
                        subscriber.onCompleted();

                        return;
                    }
                    // wait for the data to be fetched
                    countDownLatch.await(1, TimeUnit.SECONDS);
                    if(distillerData == null) {
                        Exception ex = new Exception("Timeout (1s) ocurred while fetching distiller data.");
                        subscriber.onError(new Exception());
                    } else {
                        subscriber.onNext(distillerData);
                    }

                    subscriber.onCompleted();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                    subscriber.onError(e);
                }
            }
        });
    }

    private boolean initiateDataRead() {
        distillerData = null;
        if (distillerConnectionService.isConnected() == false) {
            final String deviceAddress = distillerConnectionService.getDeviceAddress();
            Log.e(TAG, String.format("Not connected to GATT server of %s device. Maybe you forgot to call connect()?", deviceAddress));

            return false;
        }

        BluetoothGatt gatt = distillerConnectionService.getBluetoothGatt();
        BluetoothGattService gattService = gatt.getService(DISTILLER_SERVICE_UUID);

        BluetoothGattCharacteristic distillerCharacteristic = gattService.getCharacteristic(DISTILLER_CHARACTERISTIC_UUID);
        return gatt.readCharacteristic(distillerCharacteristic);
    }

    private class DefaultBluetoothGattCharacteristicReadCallback implements BluetoothGattCharacteristicReadCalback {

        @Override
        public void onCharacteristicRead(BluetoothGattCharacteristic characteristic, int status) {
            byte[] bytes = characteristic.getValue();
            if(bytes.length != 24) {
                // wrong data length or no data available
                Log.w(TAG, String.format("Wrong data length or no data available for characteristic %s", characteristic.getUuid()));
                return;
            }

            distillerData = new DistillerDataEntity();
            distillerData.deviceUpTimeMillis = ByteBuffer.wrap(bytes, 0, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
            distillerData.coldWaterTemp = getRealTempOrNull(ByteBuffer.wrap(bytes, 4, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat());
            distillerData.hotWaterTemp = getRealTempOrNull(ByteBuffer.wrap(bytes, 8, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat());
            distillerData.waterRpm = ByteBuffer.wrap(bytes, 12, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
            distillerData.headerTemp = getRealTempOrNull(ByteBuffer.wrap(bytes, 16, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat());
            distillerData.boilerTemp = getRealTempOrNull(ByteBuffer.wrap(bytes, 20, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat());

            Log.d(TAG, String.format("onCharacteristicRead() %s called with systemUpTime = %s", characteristic.getUuid(), distillerData.deviceUpTimeMillis));
            Log.d(TAG, String.format("onCharacteristicRead() %s called with coldWaterTemp = %s", characteristic.getUuid(), distillerData.coldWaterTemp));
            Log.d(TAG, String.format("onCharacteristicRead() %s called with hotWaterTemp = %s", characteristic.getUuid(), distillerData.hotWaterTemp));
            Log.d(TAG, String.format("onCharacteristicRead() %s called with waterRpm = %s", characteristic.getUuid(), distillerData.waterRpm));
            Log.d(TAG, String.format("onCharacteristicRead() %s called with headerTemp = %s", characteristic.getUuid(), distillerData.headerTemp));
            Log.d(TAG, String.format("onCharacteristicRead() %s called with boilerTemp = %s", characteristic.getUuid(), distillerData.boilerTemp));

            countDownLatch.countDown();
        }

        private Double getRealTempOrNull(float temp) {
            // -273.0 send from the device means that the temperature is unknown (i.e. thermometer not connected)
            if(temp <= -273.0) {
                return null;
            }

            return (double)temp;
        }
    }
}
