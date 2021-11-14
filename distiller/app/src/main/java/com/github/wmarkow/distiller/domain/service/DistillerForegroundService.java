package com.github.wmarkow.distiller.domain.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.github.wmarkow.distiller.DistillerApplication;
import com.github.wmarkow.distiller.R;
import com.github.wmarkow.distiller.domain.model.DistillerDataEntity;
import com.github.wmarkow.distiller.domain.model.DistillerDatabase;
import com.github.wmarkow.distiller.ui.MainActivity;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import rx.Subscriber;

/***
 * A service that works in the foreground. Its purpose are:
 * <ul>
 *     <li>manage connectivity to the distiller device</li>
 *     <li>periodically fetch data from the distiller</li>
 * </ul>
 */
public class DistillerForegroundService extends Service {
    private final static String TAG = "DistForegService";
    public static final String CHANNEL_ID = "DistillerForegroundServiceChannel";
    public static final int NOTIFICATION_ID = 1500;
    private final static long AUTOREFRESH_PERIOD_MILLIS = 2000;

    // Binder given to clients
    private IBinder binder = new LocalBinder();
    private NotificationChannel notificationChannel;
    private NotificationCompat.Builder notificationBuilder;
    private DistillerConnectivityService distillerConnectivityService;
    private State state;
    private Timer timer = null;
    private boolean inDestroy = false;
    private Set<DistillerForegroundServiceSubscriber> subscribers = new HashSet<>();


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Distiller Foreground Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent);
        startForeground(NOTIFICATION_ID, notificationBuilder.build());

        distillerConnectivityService = new DistillerConnectivityService();
        distillerConnectivityService.subscribe(new ForegroundDistillerConnectivityServiceSubscriber());
        timer = new Timer();
        state = null;
        processStateMachine(State.BLUETOOTH_SCANNING);

        return START_NOT_STICKY;
    }

    public void addSubscriber(DistillerForegroundServiceSubscriber subscriber) {
        this.subscribers.add(subscriber);

        subscriber.stateChanged(null, state);
    }

    public void removeSubscriber(DistillerForegroundServiceSubscriber subscriber) {
        this.subscribers.remove(subscriber);
    }

    @Override
    public void onDestroy() {
        inDestroy = true;
        super.onDestroy();
        timer.cancel();
        timer.purge();
        timer = null;
        binder = null;
        notificationBuilder = null;
        distillerConnectivityService.stopDistillerDiscovery();
        distillerConnectivityService.disconnectAll();
        distillerConnectivityService = null;
        state = null;
        subscribers.clear();
        subscribers = null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Manages connectivity and fetches data",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }

    private void processStateMachine(State newState) {
        if(inDestroy) {
            return;
        }

        switch(newState) {
            case NOT_CONNECTED_IDLE:
            {
                // wait 10 seconds and scan again
                notificationBuilder.setContentText("Waiting 10 seconds...");
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
                timer.schedule(new TimerTask(){
                    @Override
                    public void run() {
                        processStateMachine(State.BLUETOOTH_SCANNING);
                    }
                }, 10000);
            };break;
            case BLUETOOTH_SCANNING: {
                notificationBuilder.setContentText("Bluetooth scanning...");
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());

                final BluetoothManager bluetoothManager =
                        (BluetoothManager) this.getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE);
                BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();

                distillerConnectivityService.startDistillerDiscovery(bluetoothAdapter);
            };break;
            case CONNECTING_DISTILLER: {
                notificationBuilder.setContentText("Connecting to distiller...");
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
            };break;
            case CONNECTED: {
                notificationBuilder.setContentText("Connected.");
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        readDistillerData();
                    }
                }, AUTOREFRESH_PERIOD_MILLIS, AUTOREFRESH_PERIOD_MILLIS);
            };break;
        }

        State oldState = state;
        state = newState;
        for(DistillerForegroundServiceSubscriber subscriber : subscribers) {
            subscriber.stateChanged(oldState, newState);
        }
    }

    public class LocalBinder extends Binder {
        public DistillerForegroundService getService() {
            return DistillerForegroundService.this;
        }
    }

    private void readDistillerData() {
        if(distillerConnectivityService == null) {
            return;
        }

        DistillerConnectionService dcs = distillerConnectivityService.getConnectedDistillerConnectionService();
        if(dcs == null) {
            return;
        }

        // Real data read
        //dcs.readDistillerData(new DefaultDistillerDataServiceSubscriber());

        // Fake data read
        DistillerDataEntity dd = new DistillerDataEntity();
        dd.coldWaterTemp = (Math.random() * 1) + 15f;
        dd.hotWaterTemp = (Math.random() * 1) + 76f;
        dd.boilerTemp = (Math.random() * 0.2) + 91.5f;
        dd.headerTemp = (Math.random() * 0.2) + 80.8f;
        dd.waterRpm = (Math.random() * 50) + 1800f;

        new DefaultDistillerDataServiceSubscriber().onNext(dd);
    }

    public enum State {
        NOT_CONNECTED_IDLE, // it waits 10 seconds
        BLUETOOTH_SCANNING, // it scans bluetooth for 10 seconds
        CONNECTING_DISTILLER, // it connects to distiller devices
        CONNECTED;
    }

    private class ForegroundDistillerConnectivityServiceSubscriber implements DistillerConnectivityServiceSubscriber {

        @Override
        public void onDeviceDiscoveryStarted() {
            Log.i(TAG, "onDeviceDiscoveryStarted");
        }

        @Override
        public void onDeviceDiscoveryCompleted() {
            Log.i(TAG, "onDeviceDiscoveryCompleted");
            if(distillerConnectivityService.getScanResults().size() == 0) {
                processStateMachine(State.NOT_CONNECTED_IDLE);
            }
        }

        @Override
        public void onDeviceDiscovered(String deviceAddress) {
            Log.i(TAG, "onDeviceDiscovered");

            distillerConnectivityService.connect(deviceAddress);
            processStateMachine(State.CONNECTING_DISTILLER);
        }

        @Override
        public void onError(Throwable e) {
            Log.i(TAG, "onError");
        }

        @Override
        public void onDeviceConnected(String deviceAddress) {
            Log.i(TAG, "onDeviceConnected");

            processStateMachine(State.CONNECTED);
        }

        @Override
        public void onDeviceDisconnected(String deviceAddress) {
            Log.i(TAG, "onDeviceDisconnected");

            processStateMachine(State.NOT_CONNECTED_IDLE);
        }
    }

    private class DefaultDistillerDataServiceSubscriber extends Subscriber<DistillerDataEntity> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(DistillerDataEntity distillerData) {
            // store distiller data in the database
            DistillerDatabase distillerDatabase = DistillerApplication.getDistillerApplication().getDistillerDatabase();
            distillerDatabase.distillerDataDao().insert(distillerData);

            Log.d(TAG, String.format("onNext() UTC millis = %s", distillerData.utcTimestampMillis));
            Log.d(TAG, String.format("onNext() systemUpTime = %s", distillerData.deviceUpTimeMillis));
            Log.d(TAG, String.format("onNext() called with coldWaterTemp = %s", distillerData.coldWaterTemp));
            Log.d(TAG, String.format("onNext() called with hotWaterTemp = %s", distillerData.hotWaterTemp));
            Log.d(TAG, String.format("onNext() called with waterRpm = %s", distillerData.waterRpm));
            Log.d(TAG, String.format("onNext() called with headerTemp = %s", distillerData.headerTemp));
            Log.d(TAG, String.format("onNext() called with boilerTemp = %s", distillerData.boilerTemp));
        }
    }
}
