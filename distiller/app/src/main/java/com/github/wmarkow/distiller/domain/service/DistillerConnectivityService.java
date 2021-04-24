package com.github.wmarkow.distiller.domain.service;

import android.bluetooth.BluetoothAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/***
 * Used do keep the connectivity between the application and Lego hubs devices.
 */
public class DistillerConnectivityService {

    //private Set<LegoHubScanResult> foundHubs = new HashSet<>();
    //private LegoHubDiscoveryUseCase legoHubDiscoveryUseCase = null;
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
      /*  if (legoHubDiscoveryUseCase != null) {
            return false;
        }
        legoHubDiscoveryUseCase = new LegoHubDiscoveryUseCase();
        legoHubDiscoveryUseCase.execute(bluetoothAdapter, new LegoHubDiscoveryUseCaseSubscriber());
*/
        return true;
    }

    public boolean isConnected()
    {
        return false;
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

    /*private final class LegoHubDiscoveryUseCaseSubscriber extends DefaultSubscriber<LegoHubScanResult> {
        @Override
        public void onStart() {
            for(ConnectivityServiceSubscriber subscriber : subscribers)
            {
                subscriber.onHubDiscoveryStarted();
            }
        }

        @Override
        public void onCompleted() {
            for(ConnectivityServiceSubscriber subscriber : subscribers)
            {
                subscriber.onHubDiscoveryCompleted();
            }

            legoHubDiscoveryUseCase.destroy();
            legoHubDiscoveryUseCase = null;
        }

        @Override
        public void onError(Throwable e) {
            for(ConnectivityServiceSubscriber subscriber : subscribers)
            {
                subscriber.onError(e);
            }
        }

        @Override
        public void onNext(LegoHubScanResult hubScanResult) {
            foundHubs.add(hubScanResult);
            createTrainConnectionServiceIfNeeded(hubScanResult.getAddress());

            for(ConnectivityServiceSubscriber subscriber : subscribers)
            {
                HubInfo hubInfo = new HubInfo(hubScanResult.getAddress());
                subscriber.onHubDiscovered(hubInfo);
            }
        }
    }

    private class DefaultLegoHubConnectionSubscriber implements LegoHubConnectionSubscriber {

        @Override
        public void onHubConnectivityChanged(String hubAddress, boolean isConnected) {
            for(ConnectivityServiceSubscriber subscriber : subscribers)
            {
                subscriber.onHubConnectivityChanged(hubAddress, isConnected);
            }
        }
    }*/
}
