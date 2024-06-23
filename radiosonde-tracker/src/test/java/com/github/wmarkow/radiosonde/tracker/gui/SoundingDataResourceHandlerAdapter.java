package com.github.wmarkow.radiosonde.tracker.gui;

import org.cef.callback.CefCallback;
import org.cef.handler.CefResourceHandler;
import org.cef.handler.CefResourceHandlerAdapter;
import org.cef.misc.IntRef;
import org.cef.misc.StringRef;
import org.cef.network.CefRequest;
import org.cef.network.CefResponse;

public class SoundingDataResourceHandlerAdapter extends CefResourceHandlerAdapter
{
    private CefResourceHandler wrapped;
    private int startPos = 0;

    public SoundingDataResourceHandlerAdapter( CefResourceHandler wrapped )
    {
        this.wrapped = wrapped;
    }

    @Override
    public boolean processRequest( CefRequest request, CefCallback callback )
    {
        callback.Continue();
        return true;
//        return wrapped.processRequest( request, callback );
    }

    @Override
    public void getResponseHeaders( CefResponse response, IntRef responseLength, StringRef redirectUrl )
    {
        wrapped.getResponseHeaders( response, responseLength, redirectUrl );
    }

    @Override
    public boolean readResponse( byte[] dataOut, int bytesToRead, IntRef bytesRead, CefCallback callback )
    {
        return wrapped.readResponse( dataOut, bytesToRead, bytesRead, callback );
    }

    @Override
    public void cancel()
    {
        startPos = 0;
//        wrapped.cancel();
    }
}
