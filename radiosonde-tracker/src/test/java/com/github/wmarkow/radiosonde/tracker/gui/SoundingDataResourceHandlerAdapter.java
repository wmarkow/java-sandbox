package com.github.wmarkow.radiosonde.tracker.gui;

import org.cef.callback.CefCallback;
import org.cef.handler.CefResourceHandlerAdapter;
import org.cef.misc.IntRef;
import org.cef.misc.StringRef;
import org.cef.network.CefRequest;
import org.cef.network.CefResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoundingDataResourceHandlerAdapter extends CefResourceHandlerAdapter
{
    private final static Logger LOGGER = LoggerFactory.getLogger( SoundingDataResourceHandlerAdapter.class );

    private CustomCefURLRequestClient requestClient = null;

    @Override
    public boolean processRequest( CefRequest request, CefCallback callback )
    {
        LOGGER.info( String.format( "processRequest() called." ) );
        // TODO: here I need to process request by my own
        // read whole data from the server and then provide it in readResponse method
        // Use CustomCefURLRequestClient (?)
        requestClient = new CustomCefURLRequestClient();
        requestClient.send( request );

        callback.Continue();
        return true;
    }

    @Override
    public void getResponseHeaders( CefResponse response, IntRef responseLength, StringRef redirectUrl )
    {
        LOGGER.info( String.format( "getResponseHeaders() called." ) );

        // TODO: copy response headers from CustomCefURLRequestClient ?
        // wrapped.getResponseHeaders( response, responseLength, redirectUrl );
    }

    @Override
    public boolean readResponse( byte[] dataOut, int bytesToRead, IntRef bytesRead, CefCallback callback )
    {
        LOGGER.info( String.format( "readResponse() called." ) );

//        while( true )
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
        // TODO: copy response from CustomCefURLRequestClient ?
        // TODO: log the response to file
        // return wrapped.readResponse( dataOut, bytesToRead, bytesRead, callback );
        // byte[] responseBody = requestClient.getResponseBody();
        // String s = new String( responseBody, StandardCharsets.US_ASCII );
        // System.out.println( String.format( "Received body as string is %s", s ) );
        return true;
    }

    @Override
    public void cancel()
    {
        LOGGER.info( String.format( "cancel() called." ) );
    }
}
