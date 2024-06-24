package com.github.wmarkow.radiosonde.tracker.gui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.cef.callback.CefAuthCallback;
import org.cef.callback.CefURLRequestClient;
import org.cef.network.CefRequest;
import org.cef.network.CefURLRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomCefURLRequestClient implements CefURLRequestClient
{
    private final static Logger LOGGER = LoggerFactory.getLogger( CustomCefURLRequestClient.class );

    private long nativeRef_ = 0;
    private ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
    private byte[] receivedBody = null;
    private volatile boolean requestCompleted = false;

    public void send( CefRequest request )
    {
        // It is good enough just to create the request ;it will be executed automatically.
        CefURLRequest.create( request, this );
    }

    @Override
    public long getNativeRef( String identifer )
    {
        return nativeRef_;
    }

    @Override
    public void setNativeRef( String identifer, long nativeRef )
    {
        nativeRef_ = nativeRef;
    }

    @Override
    public boolean getAuthCredentials( boolean isProxy, String host, int port, String realm, String scheme,
        CefAuthCallback callback )
    {
        LOGGER.info( String.format( "getAuthCredentials() called." ) );

        return false;
    }

    @Override
    public void onDownloadData( CefURLRequest request, byte[] data, int dataLength )
    {
        LOGGER.info( String.format( "onDownloadData() called. Received %s bytes.", dataLength ) );

        // TODO Copy byte stream and store it somewhere
        byteStream.write( data, 0, dataLength );
    }

    @Override
    public void onDownloadProgress( CefURLRequest request, int current, int total )
    {
        LOGGER.info( String.format( "onDownloadProgress() called." ) );
    }

    @Override
    public void onRequestComplete( CefURLRequest request )
    {
        requestCompleted = true;
        byte[] responseBody = getResponseBody();
        String bodyAsString = new String( responseBody, StandardCharsets.US_ASCII );

        LOGGER.debug( String.format( "onRequestComplete() called. Received %s bytes. Body as string is %s",
            byteStream.size(), bodyAsString ) );

        // byte[] decodedBytes = Base64.getDecoder().decode( responseBody );
        // String decodedString = new String( decodedBytes );
        // System.out.println( String.format( "Received body as string is %s", decodedString ) );
    }

    @Override
    public void onUploadProgress( CefURLRequest request, int current, int total )
    {
        LOGGER.info( String.format( "onUploadProgress() called." ) );
    }

    public boolean isRequestCompleted()
    {
        return requestCompleted;
    }

    public byte[] getResponseBody()
    {
        if(requestCompleted == false)
        {
            return null;
        }
        
        if( receivedBody == null )
        {
            try
            {
                byteStream.flush();
            }
            catch( IOException e )
            {

                LOGGER.error( e.getMessage(), e );
            }
            receivedBody = byteStream.toByteArray();
            try
            {
                byteStream.close();
            }
            catch( IOException e )
            {
                LOGGER.error( e.getMessage(), e );
            }
        }

        return receivedBody;
    }
}
