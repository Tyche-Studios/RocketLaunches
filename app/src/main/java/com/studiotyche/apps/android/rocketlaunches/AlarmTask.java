package com.studiotyche.apps.android.rocketlaunches;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by edwn112 on 07-10-2015.
 */
public class AlarmTask implements Runnable {

    private final Calendar date;
    private final AlarmManager am;
    private final Context context;

    public boolean setTime;

    public AlarmTask(Context context, Calendar date, boolean setTime) {
        this.context = context;
        this.am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.date = date;
        this.setTime = setTime;
    }

    public void run() {
        if (setTime) {
            Intent intent = new Intent(context, NotifyService.class);
            intent.putExtra(NotifyService.INTENT_NOTIFY, true);
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

            am.set(AlarmManager.RTC, date.getTimeInMillis(), pendingIntent);
        } else {
            Intent intent = new Intent(context, NotifyService.class);
            intent.putExtra(NotifyService.INTENT_NOTIFY, true);
            PendingIntent sender = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            am.cancel(sender);
        }
    }
}
