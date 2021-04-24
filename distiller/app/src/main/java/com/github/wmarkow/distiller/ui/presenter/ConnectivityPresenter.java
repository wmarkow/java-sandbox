package com.github.wmarkow.distiller.ui.presenter;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.github.wmarkow.distiller.domain.model.DeviceInfo;
import com.github.wmarkow.distiller.domain.service.DistillerConnectivityServiceSubscriber;
import com.github.wmarkow.distiller.ui.ConnectivityViewIf;
import com.github.wmarkow.distiller.domain.service.DistillerConnectivityService;

import javax.inject.Inject;

public class ConnectivityPresenter implements Presenter {
    private final static String TAG = "ConnectivityPresenter";

    private ConnectivityViewIf connectivityViewIf;

    private DistillerConnectivityService distillerConnectivityService;
    private DefaultConnectivityServiceSubscriber defaultConnectivityServiceSubscriber;

    @Inject
    public ConnectivityPresenter(DistillerConnectivityService distillerConnectivityService) {
        this.distillerConnectivityService = distillerConnectivityService;
        defaultConnectivityServiceSubscriber = new DefaultConnectivityServiceSubscriber();

        this.distillerConnectivityService.subscribe(defaultConnectivityServiceSubscriber);
    }

    public void connectToDistiller() {
        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) connectivityViewIf.getContext().getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Toast.makeText(connectivityViewIf.getContext(), "Bluetooth musi być włączony!", Toast.LENGTH_LONG).show();

            connectivityViewIf.showDistillerDisconnected();

            return;
        }

        LocationManager locationManager = (LocationManager) connectivityViewIf.getContext().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(connectivityViewIf.getContext(), "Lokalizacja musi być włączona!", Toast.LENGTH_LONG).show();

            connectivityViewIf.showDistillerDisconnected();

            return;
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

                connectivityViewIf.showDistillerDisconnected();

                return;
            }
        }

        distillerConnectivityService.startDistillerDiscovery(bluetoothAdapter);
    }

    public void setView(ConnectivityViewIf connectivityViewIf) {
        this.connectivityViewIf = connectivityViewIf;
    }

    @Override
    public void resume() {
        if(distillerConnectivityService.isConnected()) {
            connectivityViewIf.showDistillerConnected();

            return;
        }

        connectivityViewIf.showDistillerDisconnected();
    }

    @Override
    public void pause() {
        //trainDiscoveryUseCase.unsubscribe();
    }

    @Override
    public void destroy() {
        distillerConnectivityService.unsubscribe(defaultConnectivityServiceSubscriber);
    }

    private class DefaultConnectivityServiceSubscriber implements DistillerConnectivityServiceSubscriber {

        @Override
        public void onDeviceDiscoveryStarted() {
            connectivityViewIf.showDistillerConnectionInProgress();
        }

        @Override
        public void onDeviceDiscoveryCompleted() {
            if (distillerConnectivityService.getScanResults().size() == 0) {
                // means no device has been found
                connectivityViewIf.showDistillerDisconnected();
                Toast.makeText(connectivityViewIf.getContext(), "No distiller device found!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onDeviceDiscovered(DeviceInfo deviceInfo) {
            // connect to that device automatically
            distillerConnectivityService.connect(deviceInfo.getAddress());
        }

        @Override
        public void onError(Throwable e) {
            connectivityViewIf.showDistillerDisconnected();
            Toast.makeText(connectivityViewIf.getContext(), "Error during distiller device discovery!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onDeviceConnected(DeviceInfo deviceInfo) {
            connectivityViewIf.showDistillerConnected();
        }

        @Override
        public void onDeviceDisconnected(DeviceInfo deviceInfo) {
            connectivityViewIf.showDistillerDisconnected();
        }
    }
}
