// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights
// reserved. Use of this source code is governed by a BSD-style license that
// can be found in the LICENSE file.

package com.github.wmarkow.radiosonde.tracker.integration.windy.cef;

import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.handler.CefRequestHandlerAdapter;
import org.cef.handler.CefResourceRequestHandler;
import org.cef.misc.BoolRef;
import org.cef.network.CefRequest;

import com.github.wmarkow.radiosonde.tracker.domain.WindDataDistributionListener;

public class CustomCefRequestHandlerAdapter extends CefRequestHandlerAdapter
{
    private WindDataDistributionListener listener;

    public CustomCefRequestHandlerAdapter( WindDataDistributionListener listener )
    {
        this.listener = listener;
    }

    @Override
    public CefResourceRequestHandler getResourceRequestHandler( CefBrowser browser, CefFrame frame,
        CefRequest request, boolean isNavigation, boolean isDownload, String requestInitiator,
        BoolRef disableDefaultHandling )
    {
        return new CustomCefResourceRequestHandlerAdapter(listener);
    }
}
