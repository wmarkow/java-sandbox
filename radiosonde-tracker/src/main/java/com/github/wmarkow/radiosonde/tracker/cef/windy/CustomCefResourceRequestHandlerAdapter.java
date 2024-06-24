package com.github.wmarkow.radiosonde.tracker.cef.windy;

import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.handler.CefResourceHandler;
import org.cef.handler.CefResourceRequestHandlerAdapter;
import org.cef.network.CefRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomCefResourceRequestHandlerAdapter extends CefResourceRequestHandlerAdapter
{
    private final static Logger LOGGER =
        LoggerFactory.getLogger( CustomCefResourceRequestHandlerAdapter.class );

    @Override
    public boolean onBeforeResourceLoad( CefBrowser browser, CefFrame frame, CefRequest request )
    {
        LOGGER.trace( String.format( "onBeforeResourceLoad() called. Request is %s", request.toString() ) );

        return false;
    }

    @Override
    public CefResourceHandler getResourceHandler( CefBrowser browser, CefFrame frame, CefRequest request )
    {
        CefResourceHandler result = super.getResourceHandler( browser, frame, request );

        if( "GET".equals( request.getMethod() ) )
        {
            final String URL_BASE = "https://node.windy.com";
            if( request.getURL().startsWith( URL_BASE ) )
            {
                String path = request.getURL().substring( URL_BASE.length() );

                if( path.matches( "([a-z]|[A-Z]|[0-9]|\\/)+" ) )
                {
                    LOGGER
                        .info( String.format( "getResourceHandler() called. Sounding data request found: %s",
                            request.toString() ) );

                    return new SoundingDataResourceHandlerAdapter();
                }
            }
        }
        return result;
    }
}
