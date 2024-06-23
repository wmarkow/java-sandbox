// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights
// reserved. Use of this source code is governed by a BSD-style license that
// can be found in the LICENSE file.

package com.github.wmarkow.radiosonde.tracker.gui;

import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.handler.CefRequestHandlerAdapter;
import org.cef.handler.CefResourceRequestHandler;
import org.cef.misc.BoolRef;
import org.cef.network.CefRequest;

public class CustomCefRequestHandlerAdapter extends CefRequestHandlerAdapter
{

    @Override
    public CefResourceRequestHandler getResourceRequestHandler( CefBrowser browser, CefFrame frame,
        CefRequest request, boolean isNavigation, boolean isDownload, String requestInitiator,
        BoolRef disableDefaultHandling )
    {
        return new CustomCefResourceRequestHandlerAdapter();
    }
}
