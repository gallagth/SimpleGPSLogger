package com.gallagth.simplegpslogger.utils;

import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

/**
 * Created by Thomas on 12/08/2014.
 */
public class ServiceHelper {

    public static void startUpdates(Messenger service) throws RemoteException {
        startUpdates(service, 10000, 0);
    }

    public static void startUpdates(Messenger service, long minTime, float minDistance) throws RemoteException {
        Message msg = new Message();
        msg.what = LocationRecorder.START_UPDATES;
        Bundle data = new Bundle();
        data.putLong(LocationRecorder.MIN_TIME_KEY, minTime);
        data.putFloat(LocationRecorder.MIN_DISTANCE_KEY, minDistance);
        service.send(msg);
    }

    public static void stopUpdates(Messenger service) throws RemoteException {
        //TODO check if recording
        Message msg = new Message();
        msg.what = LocationRecorder.STOP_UPDATES;
        service.send(msg);
    }

    public static void stopRecording(Messenger service) throws RemoteException {
        Message msg = new Message();
        msg.what = LocationRecorder.STOP_RECORDING;
        service.send(msg);
    }

    public static void startRecording(Messenger service) throws RemoteException {
        Message msg = new Message();
        msg.what = LocationRecorder.START_RECORDING;
        service.send(msg);
    }
}
