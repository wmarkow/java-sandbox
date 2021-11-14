package com.github.wmarkow.distiller.ui;

import android.content.Context;

import androidx.annotation.NonNull;

public interface ConnectivityViewIf extends MVPViewIf {
    Context getContext();
    void requestPermissions( final @NonNull String[] permissions);
    void showDistillerConnected();
    void showDistillerDisconnected();
    void showDistillerConnecting();
    void showDistillerIndicatorDisabled();
    void showDistillerSwitchChecked(boolean checked);
}
