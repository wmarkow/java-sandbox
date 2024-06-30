package com.github.wmarkow.radiosonde.tracker.integration.windy.cef;

import java.awt.geom.Point2D;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.cef.callback.CefAuthCallback;
import org.cef.callback.CefURLRequestClient;
import org.cef.network.CefRequest;
import org.cef.network.CefURLRequest;
import org.geotools.geometry.DirectPosition2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wmarkow.radiosonde.tracker.domain.WindDataDistribution;
import com.github.wmarkow.radiosonde.tracker.domain.WindDataDistributionListener;
import com.github.wmarkow.radiosonde.tracker.integration.windy.WindDataDistributionProvider;

public class SoundingCefURLRequestClient implements CefURLRequestClient
{
    private final static Logger LOGGER = LoggerFactory.getLogger( SoundingCefURLRequestClient.class );

    private long nativeRef_ = 0;
    private ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
    private byte[] receivedBody = null;
    private Point2D requestLocation;
    private WindDataDistributionListener listener;

    public SoundingCefURLRequestClient( WindDataDistributionListener listener )
    {
        this.listener = listener;
    }

    public void send( CefRequest request )
    {
        String path =
            request.getURL().substring( CustomCefResourceRequestHandlerAdapter.WINDY_URL_BASE.length() );
        requestLocation = deriveLatLonFromEncodedPath( path );

        LOGGER.info( String.format(
            "send() called: Location(lat,lon)=(%s, %s). Browser URL %s. Sounding data request found: %s",
            requestLocation.getY(), requestLocation.getX(), request.getURL(), request.toString() ) );

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
        byte[] responseBody = getResponseBody();
        String bodyAsString = new String( responseBody, StandardCharsets.US_ASCII );

        LOGGER.debug( String.format( "onRequestComplete() called. Received %s bytes. Body as string is %s",
            byteStream.size(), bodyAsString ) );

        byte[] decodedBytes = Base64.getDecoder().decode( responseBody );
        String soundingJson = new String( decodedBytes );
        LOGGER.debug( String.format( "onRequestComplete() called. Sounding json is %s", soundingJson ) );

        if( listener != null )
        {
            WindDataDistributionProvider provider = new WindDataDistributionProvider();
            double latitude = requestLocation.getY();
            double longitude = requestLocation.getX();
            WindDataDistribution distribution = provider.parse( soundingJson, latitude, longitude );

            listener.onNewWindDataDistributionAvailable( distribution );
        }
    }

    @Override
    public void onUploadProgress( CefURLRequest request, int current, int total )
    {
        LOGGER.info( String.format( "onUploadProgress() called." ) );
    }

    private byte[] getResponseBody()
    {
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

    private Point2D deriveLatLonFromEncodedPath( String encodedPath )
    {
        // Encoded path is as below:
        // /Zm9yZWNhc3Q/ZWNtd2Y/bWV0ZW9ncmFtL2VjbXdmL3YxLjEvNTIuMDQxLzE3LjQ3OD9zdGVwPTMmcmVmVGltZT0yMDI0LTA2LTI3VDAwOjAwOjAwWiZ0b2tlbj1leUowZVhBaU9pSktWMVFpTENKaGJHY2lPaUpJVXpJMU5pSjkuZXlKbGVIQWlPakUzTVRrMk5EZ3hPVGtzSW1saGRDSTZNVGN4T1RRM05UTTVPU3dpYVc1bUlqcDdJbWx3SWpvaU56Y3VOalV1T0RBdU1USTJJaXdpZFdFaU9pSk5iM3BwYkd4aFhDODFMakFnS0ZkcGJtUnZkM01nVGxRZ01UQXVNRHNnVjJsdU5qUTdJSGcyTkNrZ1FYQndiR1ZYWldKTGFYUmNMelV6Tnk0ek5pQW9TMGhVVFV3c0lHeHBhMlVnUjJWamEyOHBJRU5vY205dFpWd3ZOemd1TUM0ek9UQTBMamN3SUZOaFptRnlhVnd2TlRNM0xqTTJJbjE5LmF0NzdPS0syMXl3NUxQd21rVEZ0Rjh4VGtCMlRoU1JsbnI5UG13ajJzbkEmdG9rZW4yPXBlbmRpbmcmdWlkPWM3YmYwNjE1LTk3ZTQtYjYyZC0zMTUzLTA4ZWE4ZDQ2NGIwYSZzYz0wJnByPTEmdj00Mi4zLjEmcG9jPTEy
        String[] splitsOfEncoded = encodedPath.split( "\\/" );
        String decodedUrlData = new String( Base64.getDecoder().decode( splitsOfEncoded[ 3 ] ) );

        // Decoded data are as below. Latitude and longitude are there.
        // meteogram/ecmwf/v1.1/52.127/17.779?step=3&refTime=2024-06-27T00:00:00Z&token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3MTk2NDgzODcsImluZiI6eyJ1YSI6Ik1vemlsbGFcLzUuMCAoV2luZG93cyBOVCAxMC4wOyBXaW42NDsgeDY0KSBBcHBsZVdlYktpdFwvNTM3LjM2IChLSFRNTCwgbGlrZSBHZWNrbykgQ2hyb21lXC83OC4wLjM5MDQuNzAgU2FmYXJpXC81MzcuMzYiLCJpcCI6Ijc3LjY1LjgwLjEyNiJ9LCJpYXQiOjE3MTk0NzU1ODd9.n0weI8Yl91U2z9VnEKp4b9ZHDs4EhsZYXJ3hi9iD1yU&token2=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJtYWdpYyI6MjQwLCJpYXQiOjE3MTk0NzU1ODgsImV4cCI6MTcxOTY0ODM4OH0.agKbVYgtjOYsueXTvyNJBoFmbXzkQw5dCGcbyH2EkL4&uid=2d8cf18e-a075-da32-2da8-edffb6a559c0&sc=0&pr=0&v=42.3.1&poc=
        int indexOfQuestionMark = decodedUrlData.indexOf( '?' );
        String firstPart = decodedUrlData.substring( 0, indexOfQuestionMark );
        String[] splitsOfDecoded = firstPart.split( "\\/" );
        double latitude = Double.parseDouble( splitsOfDecoded[ 3 ] );
        double longitude = Double.parseDouble( splitsOfDecoded[ 4 ] );

        return new DirectPosition2D( longitude, latitude );
    }
}
