package com.gallagth.simplegpslogger.utils;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.NotificationCompat;

import com.gallagth.simplegpslogger.MainActivity;
import com.gallagth.simplegpslogger.R;
import com.gallagth.simplegpslogger.model.GeoPoint;
import com.gallagth.simplegpslogger.model.Run;
import com.gallagth.simplegpslogger.model.StampedPoint;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Thomas on 11/08/2014.
 */
public class LocationRecorder extends Service {

    private static final int NOTIFICATION_ID = 1;

    public static final int START_RECORDING = 2;
    public static final int STOP_RECORDING = 3;
    public static final int START_UPDATES = 4;
    public static final int STOP_UPDATES = 5;
    public static final String MIN_TIME_KEY = "minTime";
    public static final String MIN_DISTANCE_KEY = "minDistance";
    public static final String RUN_NAME_KEY = "runName";

    private Gson mGson;
    private Run mCurrentRun;

    private LocationManager mLocationManager;
    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (isRecording()) {
                StampedPoint point = new StampedPoint(
                        location.getTime(),
                        new GeoPoint(location.getLatitude(), location.getLongitude(), location.getAltitude()));
                mCurrentRun.appendPoint(point);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private boolean isUpdating;
    private boolean isRecording;

    private void startUpdates(long minTime, float minDistance) {
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, mLocationListener);
        isUpdating = true;
    }

    private void stopUpdates() {
        mLocationManager.removeUpdates(mLocationListener);
        isUpdating = false;
    }

    private void startRecording(String runName) {
        if (!isUpdating()) {
            throw new IllegalStateException("Must call start updates before recording");
        }
        //show notification
        showNotification();
        mCurrentRun = new Run(runName, System.currentTimeMillis());
        isRecording = true;
    }

    private void stopRecording() {
        //write the run to a file
        String json = mGson.toJson(mCurrentRun);
        File outFile = new File(this.getExternalFilesDir(null), mCurrentRun.generateFileName());
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
            writer.write(json);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            //TODO careful here we lost the run!
        }
        //hide the notification
        NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(NOTIFICATION_ID);
        isRecording = false;
    }

    private boolean isUpdating() {
        return isUpdating;
    }

    private boolean isRecording() {
        return isRecording;
    }

    private void showNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_drawer)
                .setContentTitle("Recording GPS track")
                .setContentText("5 minutes, 132 points")
                .setAutoCancel(false)
                .setOngoing(true);
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent,PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(resultPendingIntent);
        NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, builder.build());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isUpdating = false;
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mGson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    private final Messenger mMessenger = new Messenger(new IncomingHandler());

    public class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case START_RECORDING:
                    String runName = msg.getData().getString(RUN_NAME_KEY);
                    startRecording(runName);
                    break;
                case STOP_RECORDING:
                    stopRecording();
                    break;
                case START_UPDATES:
                    long minTime = msg.getData().getLong(MIN_TIME_KEY, 10000);
                    float minDistance = msg.getData().getFloat(MIN_DISTANCE_KEY, 0);
                    startUpdates(minTime, minDistance);
                    break;
                case STOP_UPDATES:
                    stopUpdates();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
}
