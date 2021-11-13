package com.github.wmarkow.distiller.ui.presenter;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.github.wmarkow.distiller.DistillerApplication;
import com.github.wmarkow.distiller.domain.model.DistillerData;
import com.github.wmarkow.distiller.domain.service.DistillerConnectionService;
import com.github.wmarkow.distiller.domain.service.DistillerConnectivityServiceSubscriber;
import com.github.wmarkow.distiller.domain.service.DistillerForegroundService;
import com.github.wmarkow.distiller.domain.service.DistillerForegroundServiceSubscriber;
import com.github.wmarkow.distiller.ui.ConnectivityViewIf;
import com.github.wmarkow.distiller.domain.service.DistillerConnectivityService;

import java.util.List;

import javax.inject.Inject;

public class ConnectivityPresenter implements Presenter {
    private final static String TAG = "ConnectivityPresenter";
    private final static String SERVICE_CLASS_NAME = DistillerForegroundService.class.getName();

    private ConnectivityViewIf connectivityViewIf;

    private DistillerForegroundService distillerForegroundService = null;
    private ServiceConnection serviceConnection;
    private DefaultDistillerForegroundServiceSubscriber subscriber;

    @Inject
    public ConnectivityPresenter() {
        subscriber = new DefaultDistillerForegroundServiceSubscriber();

        serviceConnection = new ForegroundServiceConnection();
    }

    public void enableForegroundService(boolean enabled) {

        if(!checkRequiredPermissions()) {
            connectivityViewIf.showDistillerSwitchChecked(false);

            return;
        }

        Context context = DistillerApplication.getDistillerApplication().getApplicationContext();

        if(enabled) {
            connectivityViewIf.showDistillerDisconnected();
            Intent serviceIntent = new Intent(context, DistillerForegroundService.class);
            serviceIntent.putExtra("inputExtra", "Manages connectivity and fetches data");
            ContextCompat.startForegroundService(context, serviceIntent);

            bindToForegroundService();
        } else {
            connectivityViewIf.showDistillerIndicatorDisabled();
            unbindFromForegroundService();

            Intent serviceIntent = new Intent(context, DistillerForegroundService.class);
            context.stopService(serviceIntent);
        }
    }

    public void setView(ConnectivityViewIf connectivityViewIf) {
        this.connectivityViewIf = connectivityViewIf;
    }

    @Override
    public void resume() {
        boolean serviceRunning = isServiceRunning(SERVICE_CLASS_NAME);
        connectivityViewIf.showDistillerSwitchChecked(serviceRunning);
        if(serviceRunning) {
            connectivityViewIf.showDistillerDisconnected();
        } else {
            connectivityViewIf.showDistillerIndicatorDisabled();
        }

        bindToForegroundService();
    }

    @Override
    public void pause() {
        unbindFromForegroundService();
    }

    @Override
    public void destroy() {
        unbindFromForegroundService( );
    }

    private class DefaultDistillerForegroundServiceSubscriber implements DistillerForegroundServiceSubscriber {

        @Override
        public void stateChanged(DistillerForegroundService.State oldState, DistillerForegroundService.State newState) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    updateIndicatorStatus(newState);
                }
            });
        }
    }

    private class ForegroundServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            Log.i(TAG, "Bounded to DistillerForegroundService");

            // We've bound to LocalService, cast the IBinder and get LocalService instance
            DistillerForegroundService.LocalBinder binder = (DistillerForegroundService.LocalBinder) service;
            distillerForegroundService = binder.getService();

            distillerForegroundService.addSubscriber(subscriber);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            // in the Internet they write that this method is almost never called
            Log.i(TAG, "Unbounded from DistillerForegroundService");

            distillerForegroundService.removeSubscriber(subscriber);
            distillerForegroundService = null;
        }
    };

    private boolean checkRequiredPermissions() {
        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) connectivityViewIf.getContext().getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Toast.makeText(connectivityViewIf.getContext(), "Bluetooth musi być włączony!", Toast.LENGTH_LONG).show();

            return false;
        }

        LocationManager locationManager = (LocationManager) connectivityViewIf.getContext().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(connectivityViewIf.getContext(), "Lokalizacja musi być włączona!", Toast.LENGTH_LONG).show();

            return false;
        }

        // check for Location access permission
        if (ContextCompat.checkSelfPermission(
                connectivityViewIf.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_DENIED) {

            // ask user to grant permission
            connectivityViewIf.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION});

            // check permission again
            if (ContextCompat.checkSelfPermission(
                    connectivityViewIf.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_DENIED) {

                Toast.makeText(connectivityViewIf.getContext(), "Uprawnienia do Lokalizacji muszą być nadane!", Toast.LENGTH_LONG).show();

                return false;
            }
        }

        return true;
    }

    private boolean isServiceRunning(String serviceClassName){
        final ActivityManager activityManager = (ActivityManager) DistillerApplication.getDistillerApplication().getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().equals(serviceClassName)){
                return true;
            }
        }
        return false;
    }

    private void bindToForegroundService( ) {
        if(!isServiceRunning(SERVICE_CLASS_NAME)) {
            Log.i(TAG,"Foreground service not started. No bounding possible.");
            return;
        }

        Context context = DistillerApplication.getDistillerApplication().getApplicationContext();

        Log.i(TAG, "Binding to the foreground service...");

        Intent intent = new Intent(context, DistillerForegroundService.class);
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindFromForegroundService( ) {
        Context context = DistillerApplication.getDistillerApplication().getApplicationContext();

        Log.i(TAG, "Unbinding from the foreground service...");

        if(distillerForegroundService != null) {
            context.unbindService(serviceConnection);
            // Need to set the reference to null because in most cases the onServiceDisconnected will be not called
            distillerForegroundService.removeSubscriber(subscriber);
            distillerForegroundService = null;
        }

        Log.i(TAG, "Unbounded from DistillerForegroundService");
    }

    private void updateIndicatorStatus(DistillerForegroundService.State state) {
        switch(state) {
            case NOT_CONNECTED_IDLE:
                connectivityViewIf.showDistillerDisconnected();
                break;
            case BLUETOOTH_SCANNING:
            case CONNECTING_DISTILLER:
                connectivityViewIf.showDistillerConnectionInProgress();
                break;
            case CONNECTED:
                connectivityViewIf.showDistillerConnected();
        }
    }
}
