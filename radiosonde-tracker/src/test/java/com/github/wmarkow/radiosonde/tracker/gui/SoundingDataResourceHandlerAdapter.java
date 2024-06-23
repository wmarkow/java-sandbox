package com.github.wmarkow.radiosonde.tracker.gui;

import java.nio.charset.StandardCharsets;

import org.cef.callback.CefCallback;
import org.cef.handler.CefResourceHandlerAdapter;
import org.cef.misc.IntRef;
import org.cef.misc.StringRef;
import org.cef.network.CefRequest;
import org.cef.network.CefResponse;

public class SoundingDataResourceHandlerAdapter extends CefResourceHandlerAdapter
{
    private CustomCefURLRequestClient requestClient = null;

    @Override
    public boolean processRequest( CefRequest request, CefCallback callback )
    {
        System.out.println( "processRequest" );
        // TODO: here I need to process request by my own
        // read whole data from the server and then provide it in readResponse method
        // Use CustomCefURLRequestClient (?)
        requestClient = new CustomCefURLRequestClient();
        requestClient.send( request );

//        while( requestClient.isRequestCompleted() == false )
//        {
//            try
//            {
//                Thread.sleep( 100 );
//            }
//            catch( InterruptedException e )
//            {
//                e.printStackTrace();
//            }
//        }

        callback.Continue();
        return true;
    }

    @Override
    public void getResponseHeaders( CefResponse response, IntRef responseLength, StringRef redirectUrl )
    {
        System.out.println( "getResponseHeaders" );
        // TODO: copy response headers from CustomCefURLRequestClient ?
        // wrapped.getResponseHeaders( response, responseLength, redirectUrl );
    }

    @Override
    public boolean readResponse( byte[] dataOut, int bytesToRead, IntRef bytesRead, CefCallback callback )
    {
        System.out.println( "readResponse" );
        // TODO: copy response from CustomCefURLRequestClient ?
        // TODO: log the response to file
        // return wrapped.readResponse( dataOut, bytesToRead, bytesRead, callback );
        byte[] responseBody = requestClient.getResponseBody();
        String s = new String( responseBody, StandardCharsets.US_ASCII );
        System.out.println(String.format( "Received body as string is %s", s ));
        return true;
    }

    @Override
    public void cancel()
    {
        System.out.println( "cancel" );
    }
}
