package com.github.wmarkow.distiller.domain.service;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.github.wmarkow.distiller.DistillerApplication;
import com.github.wmarkow.distiller.di.components.ApplicationComponent;
import com.github.wmarkow.distiller.di.components.DaggerDistillerConnectionComponent;
import com.github.wmarkow.distiller.di.components.DistillerConnectionComponent;
import com.github.wmarkow.distiller.di.modules.UseCasesModule;
import com.github.wmarkow.distiller.domain.interactor.ReadDistillerDataUseCase;
import com.github.wmarkow.distiller.domain.model.DistillerData;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/***
 * Manages a bluetooth connection to a specific Distiller.
 */
public class DistillerConnectionService {
    private final static String TAG = "DistillerConnervice";

    private String deviceAddress;
    private BluetoothGatt bluetoothGatt = null;
    private boolean servicesDiscovered = false;
    private DistillerConnectionServiceSubscriber distillerConnectionServiceSubscriber;
    private List<BluetoothGattCharacteristicReadCalback> bluetoothGattCharacteristicReadCallbacks = new ArrayList<>();

    private Context applicationContext;
    private BluetoothManager bluetoothManager;

    @Inject
    ReadDistillerDataUseCase readDistillerDataUseCase;

    public DistillerConnectionService(Context applicationContext, String deviceAddress) {
        this.applicationContext = applicationContext;
        this.deviceAddress = deviceAddress;

        bluetoothManager =
                (BluetoothManager) applicationContext.getSystemService(Context.BLUETOOTH_SERVICE);

        final ApplicationComponent applicationComponent = DistillerApplication.getDistillerApplication().getApplicationComponent();
        DistillerConnectionComponent distillerConnectionComponent = DaggerDistillerConnectionComponent.builder()
                .applicationComponent(applicationComponent)
                .useCasesModule(new UseCasesModule())
                .build();
        distillerConnectionComponent.inject(this);
    }

    public void setDistillerConnectionServiceSubscriber(DistillerConnectionServiceSubscriber distillerConnectionServiceSubscriber) {
        this.distillerConnectionServiceSubscriber = distillerConnectionServiceSubscriber;
    }

    public void addBluetoothGattCharacteristicReadCallback(BluetoothGattCharacteristicReadCalback callback) {
        bluetoothGattCharacteristicReadCallbacks.add(callback);
    }

    public void removeBluetoothGattCharacteristicReadCallback(BluetoothGattCharacteristicReadCalback callback) {
        this.bluetoothGattCharacteristicReadCallbacks.remove(callback);
    }

    public void connect() {
        Log.i(TAG, String.format("connect() called on device %s", deviceAddress));

        if (bluetoothGatt != null) {
            bluetoothGatt.connect();
        }

        BluetoothDevice device = bluetoothManager.getAdapter().getRemoteDevice(deviceAddress);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            connectGatt0(device);

            return;
        }

        // for older Android than 6 use this method to connect
        bluetoothGatt = device.connectGatt(applicationContext, false, new MyBluetoothGattCallback());
    }

    public void disconnect() {
        if(bluetoothGatt != null) {
            bluetoothGatt.disconnect();
            bluetoothGatt = null;
            servicesDiscovered = false;
        }
    }

    public synchronized void readDistillerData(Subscriber<DistillerData> subscriber) {
        readDistillerDataUseCase.setDistillerConnectionService(this);

        readDistillerDataUseCase.execute(subscriber);
    }

    public BluetoothGatt getBluetoothGatt() {
        return bluetoothGatt;
    }

    /***
     * This method makes connection working with Legoino hub emulation, but this requires Android API 23 (Android 6)
     * @param device
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void connectGatt0(BluetoothDevice device) {
        bluetoothGatt = device.connectGatt(applicationContext, false, new MyBluetoothGattCallback(), BluetoothDevice.TRANSPORT_LE);
    }

    public boolean isConnected() {
        if (bluetoothGatt == null) {
            return false;
        }

        BluetoothDevice device = bluetoothManager.getAdapter().getRemoteDevice(deviceAddress);
        int connectionState = bluetoothManager.getConnectionState(device, BluetoothProfile.GATT);
        if (connectionState == BluetoothProfile.STATE_CONNECTED) {
            return servicesDiscovered;
        }

        servicesDiscovered = false;
        return false;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) {
            return;
        }

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData =
                    new HashMap<String, String>();
            String gatServiceUuid = gattService.getUuid().toString();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattService.getCharacteristics()) {
                String gattCharacteristicUuid = gattCharacteristic.getUuid().toString();

                Log.i(TAG, String.format("GATT Service / Characteristic is %s/%s", gatServiceUuid, gattCharacteristicUuid));
            }
        }
    }

    private class MyBluetoothGattCallback extends BluetoothGattCallback {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.i(TAG, String.format("onConnectionStateChange() called with status = %s and newState = %s", status, newState));
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i(TAG, String.format("Connected to GATT server on device %s.", deviceAddress));

                // Attempts to discover services after successful connection.
                gatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.i(TAG, String.format("Disconnected from GATT server on device %s with status %s", deviceAddress, status));
                servicesDiscovered = false;
                if (distillerConnectionServiceSubscriber != null) {
                    distillerConnectionServiceSubscriber.onDistillerConnectivityChanged(deviceAddress, false);
                }
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.i(TAG, String.format("onServicesDiscovered() called"));

            // display uuids of gat services
            displayGattServices(gatt.getServices());
            // set some speed to the motor on channel A

            // services must be discovered in order to send data to HUB
            servicesDiscovered = true;
            if (distillerConnectionServiceSubscriber != null) {
                distillerConnectionServiceSubscriber.onDistillerConnectivityChanged(deviceAddress, true);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            for(BluetoothGattCharacteristicReadCalback listener: bluetoothGattCharacteristicReadCallbacks) {
                listener.onCharacteristicRead(characteristic, status);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            Log.d(TAG, String.format("onCharacteristicChanged() called"));
        }
    }
}
