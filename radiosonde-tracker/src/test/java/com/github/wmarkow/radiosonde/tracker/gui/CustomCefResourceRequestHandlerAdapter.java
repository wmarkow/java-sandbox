package com.github.wmarkow.radiosonde.tracker.gui;

import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.handler.CefResourceHandler;
import org.cef.handler.CefResourceRequestHandlerAdapter;
import org.cef.network.CefRequest;

public class CustomCefResourceRequestHandlerAdapter extends CefResourceRequestHandlerAdapter
{

    @Override
    public boolean onBeforeResourceLoad( CefBrowser browser, CefFrame frame, CefRequest request )
    {
        System.out.println( request.toString() );

        return false;
    }

    @Override
    public CefResourceHandler getResourceHandler( CefBrowser browser, CefFrame frame, CefRequest request )
    {
        CefResourceHandler result = super.getResourceHandler( browser, frame, request );

        if( "GET".equals( request.getMethod() ) )
        {
            final String URL_BASE = "https://node.windy.com";
            if(request.getURL().startsWith( URL_BASE ))
            {
                String path = request.getURL().substring( URL_BASE.length() );
                
                if(path.matches( "([a-z]|[A-Z]|[0-9]|\\/)+" ))
                {
                    System.out.println("sounding data are requested");
                    return new SoundingDataResourceHandlerAdapter();
                }
            }
        }
        return result;
    }
}
