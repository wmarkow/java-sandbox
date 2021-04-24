package com.github.wmarkow.distiller.domain.interactor;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;

import com.github.wmarkow.distiller.domain.executor.PostExecutionThread;
import com.github.wmarkow.distiller.domain.executor.ThreadExecutor;
import com.github.wmarkow.distiller.domain.model.BleScanResult;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

public class DeviceDiscoveryUseCase extends UseCase {
    private final static String TAG = "DeviceDiscoveryUseCase";
    private static final long SCAN_PERIOD = 10000;

    private Handler handler = new Handler();
    private ScanCallback scanCallback;
    private BluetoothAdapter bluetoothAdapter;
    private DefaultSubscriber<BleScanResult> subscriber;

    @Inject
    public DeviceDiscoveryUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
    }

    public DeviceDiscoveryUseCase()
    {
        super();
    }

    public void execute(BluetoothAdapter bluetoothAdapter, DefaultSubscriber<BleScanResult> subscriber)
    {
        Log.i(TAG, String.format("Start distiller device discovery", SCAN_PERIOD));

        this.bluetoothAdapter = bluetoothAdapter;
        this.subscriber = subscriber;
        subscriber.onStart();
        scanCallback = new TrainScanCallback();
        bluetoothAdapter.getBluetoothLeScanner().startScan(scanCallback);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, String.format("Stop distiller device discovery after %s ms", SCAN_PERIOD));
                stopScan();
            }
        }, SCAN_PERIOD);
    }

    public void stopScan()
    {
        handler.removeCallbacksAndMessages(null);
        bluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
        subscriber.onCompleted();
        scanCallback = null;
        subscriber = null;
    }

    public void execute(Subscriber useCaseSubscriber)
    {
        throw new UnsupportedOperationException("Call execute(BluetoothAdapter bluetoothAdapter, DefaultSubscriber<TrainScanResult> subscriber) instead.");
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return null;
    }

    @Override
    public void destroy() {
        handler = null;
        bluetoothAdapter = null;
        subscriber = null;
        scanCallback = null;
    }

    private class TrainScanCallback extends ScanCallback
    {
        private volatile boolean firstDistillerFound = false;

        @Override
        public synchronized void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);

            // need to use additional flag to prevent this method being called again after the first scan result is received.
            // BluetoothLeScanner.stopScan(ScanCallback callback) doesn't happen immediately and often a second scan result
            // is also reported.
            if(firstDistillerFound)
            {
                return;
            }

            Log.i(TAG, String.format("callbackType = %s, result = %s", callbackType, result));

            SparseArray<byte[]> manufacturerData =  result.getScanRecord().getManufacturerSpecificData();

            // manufacturer data contains "wmarkow"
            if(!manufacturerData.toString().startsWith("{28023="))
            {
                // this is not a distiller device
                // ignore this device
                return;
            }

            firstDistillerFound = true;

            String deviceAddress = result.getDevice().getAddress();
            BleScanResult trainScanResult = new BleScanResult(deviceAddress);
            subscriber.onNext(trainScanResult);
        }

        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);

            Log.i(TAG, String.format("onBatchScanResults()"));

            for(ScanResult scanResult : results)
            {
                BleScanResult trainScanResult = new BleScanResult(scanResult.getDevice().getAddress());
                subscriber.onNext(trainScanResult);
            }
        }

        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);

            Log.i(TAG, String.format("onScanFailed(%s)", errorCode));

            subscriber.onError(new Exception(String.format("Scan failed with error code %s", errorCode)));
        }
    }
}
