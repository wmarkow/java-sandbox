package com.github.wmarkow.radiosonde.tracker.gui;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.cef.callback.CefAuthCallback;
import org.cef.callback.CefURLRequestClient;
import org.cef.network.CefRequest;
import org.cef.network.CefURLRequest;

public class CustomCefURLRequestClient implements CefURLRequestClient
{
    private long nativeRef_ = 0;
    private ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
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
        System.out.println( "getAuthCredentials" );
        return false;
    }

    @Override
    public void onDownloadData( CefURLRequest request, byte[] data, int dataLength )
    {
        System.out.println( String.format( "onDownloadData, received %s bytes", dataLength) );

        // TODO Copy byte stream and store it somewhere
        byteStream.write( data, 0, dataLength );
    }

    @Override
    public void onDownloadProgress( CefURLRequest request, int current, int total )
    {
        System.out.println( "onDownloadProgress" );
    }

    @Override
    public void onRequestComplete( CefURLRequest request )
    {
        System.out.println( String.format( "onRequestComplete, received %s bytes", byteStream.size() ) );
        
        // TODO: indicate somewhere that the data downloaded ?
        requestCompleted = true;
        byte[] responseBody = getResponseBody();
        String s = new String( responseBody, StandardCharsets.US_ASCII );
        System.out.println(String.format( "Received body as string is %s", s ));
        byte[] decodedBytes = Base64.getDecoder().decode( responseBody );
        String decodedString = new String(decodedBytes);
        System.out.println(String.format( "Received body as string is %s", decodedString ));
        
        // String updateStr = "onRequestCompleted\n\n";
        // CefResponse response = request.getResponse();
        // boolean isText = response.getHeaderByName("Content-Type").startsWith("text");
        // updateStr += response.toString();
        // updateStatus(updateStr, isText);
    }

    @Override
    public void onUploadProgress( CefURLRequest request, int current, int total )
    {
        System.out.println( "onUploadProgress" );
    }

    public boolean isRequestCompleted()
    {
        return requestCompleted;
    }
    
    public byte[] getResponseBody()
    {
        return byteStream.toByteArray();
    }
}
