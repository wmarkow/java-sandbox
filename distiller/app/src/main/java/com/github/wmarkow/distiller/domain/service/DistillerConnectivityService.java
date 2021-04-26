package com.github.wmarkow.distiller.domain.service;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.util.Log;

import com.github.wmarkow.distiller.DistillerApplication;
import com.github.wmarkow.distiller.domain.interactor.DefaultSubscriber;
import com.github.wmarkow.distiller.domain.interactor.DeviceDiscoveryUseCase;
import com.github.wmarkow.distiller.domain.model.BleScanResult;
import com.github.wmarkow.distiller.domain.model.DeviceInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

/***
 * Used do keep the connectivity between the application and Lego hubs devices.
 */
public class DistillerConnectivityService {

    private Set<BleScanResult> scanResults = new HashSet<>();
    private Map<String, DistillerConnectionService> distillerConnectionServices = new HashMap<>();
    private DeviceDiscoveryUseCase deviceDiscoveryUseCase = null;

    private List<DistillerConnectivityServiceSubscriber> subscribers = new ArrayList<DistillerConnectivityServiceSubscriber>();
    private DefaultDistillerConnectionServiceSubscriber distillerConnectionServiceSubscriber = new DefaultDistillerConnectionServiceSubscriber();

    @Inject
    public DistillerConnectivityService() {
        Log.i("DistConnServ", "Constructor");
    }

    public void subscribe(DistillerConnectivityServiceSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void unsubscribe(DistillerConnectivityServiceSubscriber subscriber) {
        subscribers.remove(subscriber);
    }

    public synchronized boolean startDistillerDiscovery(BluetoothAdapter bluetoothAdapter) {
        if (deviceDiscoveryUseCase != null) {
            return false;
        }

        // all scan results should be cleared
        scanResults.clear();

        // all not connected devices should be removed
        for(String deviceAddress : distillerConnectionServices.keySet())
        {
            DistillerConnectionService dcs = distillerConnectionServices.get(deviceAddress);
            if(dcs == null) {
                distillerConnectionServices.remove(deviceAddress);
                continue;
            }

            if(!dcs.isConnected()) {
                distillerConnectionServices.remove(deviceAddress);
                continue;
            }
        }

        deviceDiscoveryUseCase = new DeviceDiscoveryUseCase();
        deviceDiscoveryUseCase.execute(bluetoothAdapter, new DeviceDiscoveryUseCaseSubscriber());

        return true;
    }

    public void connect(String deviceAddress) {
        getDistillerConnectionService(deviceAddress).connect();
    }

    public boolean isConnected() {
        // if at least one device is connected then return true
        for(DistillerConnectionService dcs : distillerConnectionServices.values()) {
            if(dcs.isConnected()) {
                return true;
            }
        }

        return false;
    }

    public DistillerConnectionService getConnectedDistillerConnectionService() {
        for(DistillerConnectionService dcs : distillerConnectionServices.values()) {
            if(dcs.isConnected()) {
                return dcs;
            }
        }

        return null;
    }

    public Set<BleScanResult> getScanResults() {
        return scanResults;
    }

    private synchronized DistillerConnectionService getDistillerConnectionService(String deviceAddress) {
        return createDistillerConnectionServiceIfNeeded(deviceAddress);
    }

    private synchronized DistillerConnectionService createDistillerConnectionServiceIfNeeded(String deviceAddress) {
        if (distillerConnectionServices.containsKey(deviceAddress)) {
            return distillerConnectionServices.get(deviceAddress);
        }
        // FIXME: pass application context by dagger
        Context appContext = DistillerApplication.getDistillerApplication().getApplicationContext();
        DistillerConnectionService service = new DistillerConnectionService(appContext, deviceAddress);
        service.setDistillerConnectionServiceSubscriber(distillerConnectionServiceSubscriber);
        distillerConnectionServices.put(deviceAddress, service);

        return service;
    }

    private final class DeviceDiscoveryUseCaseSubscriber extends DefaultSubscriber<BleScanResult> {
        @Override
        public void onStart() {
            for (DistillerConnectivityServiceSubscriber subscriber : subscribers) {
                subscriber.onDeviceDiscoveryStarted();
            }
        }

        @Override
        public void onCompleted() {
            for (DistillerConnectivityServiceSubscriber subscriber : subscribers) {
                subscriber.onDeviceDiscoveryCompleted();
            }

            deviceDiscoveryUseCase.destroy();
            deviceDiscoveryUseCase = null;
        }

        @Override
        public void onError(Throwable e) {
            for (DistillerConnectivityServiceSubscriber subscriber : subscribers) {
                subscriber.onError(e);
            }
        }

        @Override
        public void onNext(BleScanResult bleScanResult) {
            scanResults.add(bleScanResult);

            deviceDiscoveryUseCase.stopScan();
            DistillerConnectionService distillerConnectionService = createDistillerConnectionServiceIfNeeded(bleScanResult.getAddress());

            for(DistillerConnectivityServiceSubscriber subscriber : subscribers)
            {
                DeviceInfo deviceInfo = distillerConnectionService.getDistillerDeviceInfo();
                subscriber.onDeviceDiscovered(deviceInfo);
            }
        }
    }

    private class DefaultDistillerConnectionServiceSubscriber implements DistillerConnectionServiceSubscriber {

        @Override
        public void onDistillerConnectivityChanged(DeviceInfo deviceInfo, boolean isConnected) {
            for(DistillerConnectivityServiceSubscriber subscriber : subscribers)
            {
                if(isConnected) {
                    subscriber.onDeviceConnected(deviceInfo);
                } else {
                    subscriber.onDeviceDisconnected(deviceInfo);
                }
            }
        }
    }
}
