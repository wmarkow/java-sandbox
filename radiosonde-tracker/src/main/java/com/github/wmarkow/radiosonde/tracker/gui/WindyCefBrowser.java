package com.github.wmarkow.radiosonde.tracker.gui;

import java.awt.Component;

import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.browser.CefBrowser;

import com.github.wmarkow.radiosonde.tracker.integration.windy.cef.CustomCefRequestHandlerAdapter;

public class WindyCefBrowser
{
    private final static String SOUNDING_URL = "https://www.windy.com/sounding/%s/%s";

    private final CefApp cefApp;
    private final CefClient client;
    private final CefBrowser browser;
    private final Component browerUI;

    public WindyCefBrowser( String startURL, boolean useOSR, boolean isTransparent )
    {
        CefSettings settings = new CefSettings();
        settings.windowless_rendering_enabled = useOSR;
        cefApp = CefApp.getInstance( settings );

        client = cefApp.createClient();
        client.addRequestHandler( new CustomCefRequestHandlerAdapter() );

        browser = client.createBrowser( startURL, useOSR, isTransparent );
        browerUI = browser.getUIComponent();
    }

    public Component getBrowserComponent()
    {
        return browerUI;
    }

    public void fireWindDataRequest( double latitude, double longitude )
    {
        browser.loadURL( String.format( SOUNDING_URL, latitude, longitude ) );
    }
}
