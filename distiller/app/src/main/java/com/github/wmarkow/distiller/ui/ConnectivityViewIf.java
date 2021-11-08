package com.github.wmarkow.distiller.ui;

import android.content.Context;

import androidx.annotation.NonNull;

import com.github.wmarkow.distiller.domain.model.DistillerData;

public interface ConnectivityViewIf extends MVPViewIf {
    Context getContext();
    void requestPermissions( final @NonNull String[] permissions);
    void showDistillerConnected();
    void showDistillerDisconnected();
    void showDistillerConnectionInProgress();
    void showDistillerIndicatorDisabled();
    void showDistillerIndicatorEnabled();
}
