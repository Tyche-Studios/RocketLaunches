package com.studiotyche.apps.android.rocketlaunches;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class NotifyService extends Service {
    SharedPreference sharedPreference = null;

    public NotifyService() {
        sharedPreference = new SharedPreference();
    }

    public class ServiceBinder extends Binder {
        NotifyService getService() {
            return NotifyService.this;
        }
    }

    private static final int NOTIFICATION = 123;
    public static final String INTENT_NOTIFY = "com.studiotyche.apps.android.rocketlaunches.INTENT_NOTIFY";
    private NotificationManager mNM;

    public void onCreate() {
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getBooleanExtra(INTENT_NOTIFY, false))
            showNotification();

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IBinder mBinder = new ServiceBinder();

    private void showNotification() {
        CharSequence title = sharedPreference.getRockets(getApplicationContext()).get(0).getName();
        CharSequence text = sharedPreference.getRockets(getApplicationContext()).get(0).getLocalDate();

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_rocket_launch)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setContentIntent(contentIntent);

        mNM.notify(NOTIFICATION, notificationBuilder.build());
    }
}
