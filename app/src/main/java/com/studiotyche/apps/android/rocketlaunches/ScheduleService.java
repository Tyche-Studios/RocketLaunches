package com.studiotyche.apps.android.rocketlaunches;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Calendar;

public class ScheduleService extends Service {

    public ScheduleService() {
    }

    public class ServiceBinder extends Binder {
        ScheduleService getService() {
            return ScheduleService.this;
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IBinder mBinder = new ServiceBinder();

    public void setAlarm(Calendar c, boolean setTime) {
        new AlarmTask(this, c, setTime).run();
    }
}
