package com.github.wmarkow.radiosonde.tracker.gui;

import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.handler.CefResourceRequestHandlerAdapter;
import org.cef.network.CefRequest;

public class CustomCefResourceRequestHandlerAdapter extends CefResourceRequestHandlerAdapter
{

    @Override
    public boolean onBeforeResourceLoad(CefBrowser browser, CefFrame frame, CefRequest request) {
        
        System.out.println(request.toString());
        
        return false;
    }
}
