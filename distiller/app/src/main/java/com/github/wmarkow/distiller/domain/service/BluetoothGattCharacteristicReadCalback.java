package com.github.wmarkow.distiller.domain.service;

import android.bluetooth.BluetoothGattCharacteristic;

public interface BluetoothGattCharacteristicReadCalback {
    public void onCharacteristicRead(BluetoothGattCharacteristic characteristic,
                                     int status);
}
