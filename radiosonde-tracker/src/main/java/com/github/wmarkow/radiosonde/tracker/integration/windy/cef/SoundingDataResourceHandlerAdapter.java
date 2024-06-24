package com.github.wmarkow.radiosonde.tracker.integration.windy.cef;

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

    private SoundingCefURLRequestClient requestClient = null;

    @Override
    public boolean processRequest( CefRequest request, CefCallback callback )
    {
        LOGGER.info( String.format( "processRequest() called." ) );

        // The request must be handled separately with the help of CustomCefURLRequestClient.
        // https://github.com/chromiumembedded/java-cef/issues/85 says that CefURLRequestClient is used for
        // creating browser independent requests.
        // Side effect: the CustomCefURLRequestClient is executed in his own way outside of the lifecycle of
        // the browser, after the getResponseHeaders()
        // and readResponse() are called, so it is not possible to correctly provide the result to the
        // browser.
        // CustomCefURLRequestClient will be executed anyway but the browser will not render sounding
        // correctly.
        requestClient = new SoundingCefURLRequestClient();
        requestClient.send( request );

        // Cancel the request but our CustomCefURLRequestClient will be executed anyway.
        callback.cancel();
        return false;
    }

    @Override
    public void getResponseHeaders( CefResponse response, IntRef responseLength, StringRef redirectUrl )
    {
        LOGGER.info( String.format( "getResponseHeaders() called." ) );
    }

    @Override
    public boolean readResponse( byte[] dataOut, int bytesToRead, IntRef bytesRead, CefCallback callback )
    {
        LOGGER.info( String.format( "readResponse() called." ) );

        return false;
    }

    @Override
    public void cancel()
    {
        LOGGER.info( String.format( "cancel() called." ) );
    }
}
