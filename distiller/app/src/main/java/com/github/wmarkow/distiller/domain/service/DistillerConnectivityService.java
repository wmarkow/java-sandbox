package com.github.wmarkow.distiller.domain.service;

import android.bluetooth.BluetoothAdapter;
import android.util.Log;

import com.github.wmarkow.distiller.domain.interactor.DefaultSubscriber;
import com.github.wmarkow.distiller.domain.interactor.DeviceDiscoveryUseCase;
import com.github.wmarkow.distiller.domain.model.BleScanResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

/***
 * Used do keep the connectivity between the application and Lego hubs devices.
 */
public class DistillerConnectivityService {

    private Set<BleScanResult> scanResults = new HashSet<>();
    private DeviceDiscoveryUseCase deviceDiscoveryUseCase = null;
    //private Map<String, LegoHubConnectionService> trainConnectionServices = new HashMap<>();
    private List<DistillerConnectivityServiceSubscriber> subscribers = new ArrayList<DistillerConnectivityServiceSubscriber>();
    //private DefaultLegoHubConnectionSubscriber defaultLegoHubConnectionSubscriber = new DefaultLegoHubConnectionSubscriber();

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
        deviceDiscoveryUseCase = new DeviceDiscoveryUseCase();
        deviceDiscoveryUseCase.execute(bluetoothAdapter, new DeviceDiscoveryUseCaseSubscriber());

        return true;
    }

    public boolean isConnected() {
        return false;
    }

    public Set<BleScanResult> getScanResults() {
        return scanResults;
    }

  /*  private synchronized LegoHubConnectionService createTrainConnectionServiceIfNeeded(String deviceAddress) {
        if (trainConnectionServices.containsKey(deviceAddress)) {
            return trainConnectionServices.get(deviceAddress);
        }
        // FIXME: pass application context by dagger
        Context appContext = WeatherApplication.getWeatherApplication().getApplicationContext();
        LegoHubConnectionService service = new LegoHubConnectionService(appContext, deviceAddress);
        service.setLegoHubConnectionSubscriber(defaultLegoHubConnectionSubscriber);
        trainConnectionServices.put(deviceAddress, service);

        return service;
    }*/

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
            //createTrainConnectionServiceIfNeeded(hubScanResult.getAddress());
        }

    /*private class DefaultLegoHubConnectionSubscriber implements LegoHubConnectionSubscriber {

        @Override
        public void onHubConnectivityChanged(String hubAddress, boolean isConnected) {
            for(ConnectivityServiceSubscriber subscriber : subscribers)
            {
                subscriber.onHubConnectivityChanged(hubAddress, isConnected);
            }
        }
    }*/
    }
}
