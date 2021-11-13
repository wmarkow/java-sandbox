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
import com.github.wmarkow.distiller.ui.ConnectivityViewIf;
import com.github.wmarkow.distiller.domain.service.DistillerConnectivityService;

import java.util.List;

import javax.inject.Inject;

public class ConnectivityPresenter implements Presenter {
    private final static String TAG = "ConnectivityPresenter";
    private final static String SERVICE_CLASS_NAME = DistillerForegroundService.class.getName();

    private ServiceConnection serviceConnection;

    private ConnectivityViewIf connectivityViewIf;

    private DistillerForegroundService distillerForegroundService = null;

    private DefaultConnectivityServiceSubscriber defaultConnectivityServiceSubscriber;

    @Inject
    public ConnectivityPresenter(DistillerConnectivityService distillerConnectivityService) {
        defaultConnectivityServiceSubscriber = new DefaultConnectivityServiceSubscriber();

        serviceConnection = new ForegroundServiceConnection();
    }

    public void enableForegroundService(boolean enabled) {

        if(!checkRequiredPermissions()) {
            connectivityViewIf.showDistillerSwitchChecked(false);
            connectivityViewIf.showDistillerIndicatorDisabled();

            return;
        }

        Context context = DistillerApplication.getDistillerApplication().getApplicationContext();

        if(enabled) {
            Intent serviceIntent = new Intent(context, DistillerForegroundService.class);
            serviceIntent.putExtra("inputExtra", "Manages connectivity and fetches data");
            ContextCompat.startForegroundService(context, serviceIntent);

            connectivityViewIf.showDistillerIndicatorEnabled();

            bindToForegroundService();
        } else {
            unbindFromForegroundService();

            Intent serviceIntent = new Intent(context, DistillerForegroundService.class);
            context.stopService(serviceIntent);

            connectivityViewIf.showDistillerIndicatorDisabled();
        }
    }

    public void setView(ConnectivityViewIf connectivityViewIf) {
        this.connectivityViewIf = connectivityViewIf;
    }

    @Override
    public void resume() {
        connectivityViewIf.showDistillerSwitchChecked(isServiceRunning(SERVICE_CLASS_NAME));

        bindToForegroundService();

//        if(distillerForegroundService.isDistillerConnected()) {
//            connectivityViewIf.showDistillerConnected();
//
//            return;
//        }

        connectivityViewIf.showDistillerDisconnected();
    }

    @Override
    public void pause() {
        unbindFromForegroundService();
    }

    @Override
    public void destroy() {
        unbindFromForegroundService( );
    }

    private class DefaultConnectivityServiceSubscriber implements DistillerConnectivityServiceSubscriber {

        @Override
        public void onDeviceDiscoveryStarted() {
            connectivityViewIf.showDistillerConnectionInProgress();
        }

        @Override
        public void onDeviceDiscoveryCompleted() {
//            if (distillerConnectivityService.getScanResults().size() == 0) {
//                // means no device has been found
//                connectivityViewIf.showDistillerDisconnected();
//                Toast.makeText(connectivityViewIf.getContext(), "No distiller device found!", Toast.LENGTH_LONG).show();
//
//                return;
//            }

//            if(distillerConnectivityService.isConnected()) {
//                connectivityViewIf.showDistillerConnected();
//            }
        }

        @Override
        public void onDeviceDiscovered(String deviceAddress) {
            // connect to that device automatically
//            distillerConnectivityService.connect(deviceAddress);
        }

        @Override
        public void onError(Throwable e) {
            connectivityViewIf.showDistillerDisconnected();
            Toast.makeText(connectivityViewIf.getContext(), "Error during distiller device discovery!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onDeviceConnected(String deviceAddress) {
            // This event comes directly from android.bluetooth.BluetoothGatt
            // Notification must be dispatched in UI thread in order to update widgets correctly.
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    connectivityViewIf.showDistillerConnected();
                }
            });
        }

        @Override
        public void onDeviceDisconnected(String deviceAddress) {
            // This event comes directly from android.bluetooth.BluetoothGatt
            // Notification must be dispatched in UI thread in order to update widgets correctly.
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    connectivityViewIf.showDistillerDisconnected();
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
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            // in the Internet they write that this method is almost never called
            Log.i(TAG, "Unbounded from DistillerForegroundService");

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
            distillerForegroundService = null;
        }

        Log.i(TAG, "Unbounded from DistillerForegroundService");
    }
}
