package com.github.wmarkow.fox.hunting.serial;

public interface FoxHuntingSerialPortListener
{
    void onRssiNoSignalMeassure( int rssi );

    void onRssiPreambleSignalMeassure( int rssi );

    void onSerialPortDisconnected();
}
