package com.saggafarsyad.helloapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * This is a service to say hello using toast :)
 */
public class HelloService extends Service
{
    // Foreground status
    private int mState;
    public static final int STATE_START = 1;
    public static final int STATE_STOP = 0;

    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public HelloService getService() {
            return HelloService.this;
        }
    }

    public HelloService() {}

    public void sayHello() {
        Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_SHORT).show();
    }

    public int getState() {
        return mState;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mState = STATE_STOP;

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // Stop foreground if still running
        if (mState == STATE_START) {
            stop();
        }

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);

        // @todo: Return last state
        mState = STATE_START;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // Allow ReBinding
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
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("HelloApp")
                .setContentText("The Service is running foreground")
                .setSmallIcon(R.drawable.ic_service_notif)
                .setContentIntent(pendingIntent);

       return mBuilder.build();
    }
}
