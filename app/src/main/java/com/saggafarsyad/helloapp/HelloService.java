package com.saggafarsyad.helloapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * This is a service to say hello using toast :)
 */
public class HelloService extends Service
{
    public static final int STATE_START = 1;
    public static final int STATE_STOP = 0;
    private final String LOG_TAG = "HelloService";
    private final IBinder mBinder = new LocalBinder();
    // Foreground status
    private int mState;

    public HelloService() {}

    public void sayHello() {
        Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_SHORT).show();
    }

    public int getState() {
        return mState;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(LOG_TAG, "onStartCommand");
        mState = STATE_STOP;

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(LOG_TAG, "onDestroy");

        // Stop foreground if still running
        if (mState == STATE_START) {
            Log.i(LOG_TAG, "Stopping Service...");
            stop();
        }

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(LOG_TAG, "onBind");
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.i(LOG_TAG, "onRebind");
        Log.i(LOG_TAG, "State : " + mState);
        super.onRebind(intent);

        // @todo: Return last state
//        mState = STATE_START;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(LOG_TAG, "onUnbind");
        Log.i(LOG_TAG, "State : " + mState);

        // Return true to allow reBinding
        return true;
    }

    public void start() {
        // Set state to STATE_START
        mState = STATE_START;

        // Start foreground
        startForeground(1, buildNotification());
    }

    public void stop() {
        // Set state to STATE_STOP
        mState = STATE_STOP;

        // Stop foreground and remove from notification
        stopForeground(true);
    }

    private Notification buildNotification() {
        // build notification
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_NO_CREATE
        );

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("HelloApp")
                .setContentText("The Service is running foreground")
                .setSmallIcon(R.drawable.ic_service_notif)
                .setContentIntent(pendingIntent);

       return mBuilder.build();
    }

    public class LocalBinder extends Binder {
        public HelloService getService() {
            return HelloService.this;
        }
    }
}
