//******************************************************************
//                                                                 
//  FoxHuntingSerialPortListener.java                                               
//  Copyright 2023 PSI AG. All rights reserved.              
//  PSI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms
//                                                                 
// ******************************************************************

package com.github.wmarkow.fox.hunting.serial;

public interface FoxHuntingSerialPortListener
{
    void onRssiNoSignalMeassure( int rssi );

    void onRssiPreambleSignalMeassure( int rssi );
}
