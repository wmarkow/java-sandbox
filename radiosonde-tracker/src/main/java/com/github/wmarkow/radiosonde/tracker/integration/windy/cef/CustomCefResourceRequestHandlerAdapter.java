package com.github.wmarkow.radiosonde.tracker.integration.windy.cef;

import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.handler.CefResourceHandler;
import org.cef.handler.CefResourceRequestHandlerAdapter;
import org.cef.network.CefRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wmarkow.radiosonde.tracker.domain.WindDataDistributionListener;

public class CustomCefResourceRequestHandlerAdapter extends CefResourceRequestHandlerAdapter
{
    private final static Logger LOGGER =
        LoggerFactory.getLogger( CustomCefResourceRequestHandlerAdapter.class );

    private WindDataDistributionListener listener;

    public CustomCefResourceRequestHandlerAdapter( WindDataDistributionListener listener )
    {
        this.listener = listener;
    }

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
                    LOGGER.info( String.format(
                        "getResourceHandler() called. Browser URL %s. Sounding data request found: %s",
                        browser.getURL(), request.toString() ) );
                    return new SoundingDataResourceHandlerAdapter( listener );
                }
            }
        }
        return result;
    }
}
