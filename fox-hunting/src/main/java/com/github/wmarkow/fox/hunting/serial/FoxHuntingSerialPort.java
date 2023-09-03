//******************************************************************
//                                                                 
//  FoxHuntingSerialPort.java                                               
//  Copyright 2023 PSI AG. All rights reserved.              
//  PSI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms
//                                                                 
// ******************************************************************

package com.github.wmarkow.fox.hunting.serial;

import java.io.UnsupportedEncodingException;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class FoxHuntingSerialPort
{
    private SerialPort serialPort;
    private String inputDataBuffer = "";
    private FoxHuntingSerialPortListener listener;

    public void setListener( FoxHuntingSerialPortListener listener )
    {
        this.listener = listener;
    }

    public boolean discoverAndConnect()
    {
        for( SerialPort sp : SerialPort.getCommPorts() )
        {
            if( sp.getVendorID() == 6790 )
            {
                serialPort = sp;
                break;
            }
        }

        if( serialPort == null )
        {
            return false;
        }

        serialPort.setBaudRate( 230400 );
        serialPort.addDataListener( new SerialPortDataListener()
        {

            @Override
            public int getListeningEvents()
            {
                return SerialPort.LISTENING_EVENT_DATA_RECEIVED
                    | SerialPort.LISTENING_EVENT_PORT_DISCONNECTED;
            }

            @Override
            public void serialEvent( SerialPortEvent aEvent )
            {
                if( aEvent.getEventType() == SerialPort.LISTENING_EVENT_DATA_RECEIVED )
                {
                    try
                    {
                        String string = new String( aEvent.getReceivedData(), "UTF-8" );
                        inputDataBuffer = inputDataBuffer.concat( string );
                    }
                    catch( UnsupportedEncodingException e )
                    {
                        e.printStackTrace();
                    }

                    // extract line of data
                    int index = -1;
                    while( (index = inputDataBuffer.indexOf( "\r\n" )) > -1 )
                    {
                        if( index > -1 )
                        {
                            String line = inputDataBuffer.substring( 0, index );
                            inputDataBuffer = inputDataBuffer.substring( index + 2 );

                            System.out.println( line );
                            try
                            {
                                if( line.startsWith( "n" ) )
                                {
                                    int rssi = Integer.parseInt( line.substring( 1 ) );
                                    if( listener != null )
                                    {
                                        listener.onRssiNoSignalMeassure( rssi );
                                    }
                                }
                                else if( line.startsWith( "r" ) )
                                {
                                    int rssi = Integer.parseInt( line.substring( 1 ) );
                                    if( listener != null )
                                    {
                                        listener.onRssiPreambleSignalMeassure( rssi );
                                    }
                                }
                            }
                            catch( Exception ex )
                            {
                                ex.printStackTrace();
                            }
                        }
                    };
                }

                if( aEvent.getEventType() == SerialPort.LISTENING_EVENT_PORT_DISCONNECTED )
                {
                    listener.onSerialPortDisconnected();
                }
            }
        } );
        if( serialPort.openPort() )
        {
            System.out.println( "Port opened success." );

            return true;
        }
        else
        {
            System.out.println( "Port opened fail." );
        }
        System.out.println();

        return false;
    }
}
