package com.studiotyche.apps.android.rocketlaunches;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.util.Calendar;

/**
 * Created by edwn112 on 07-10-2015.
 */
public class ScheduleClient {

    private ScheduleService mBoundService;
    private Context mContext;
    private boolean mIsBound;

    public ScheduleClient(Context context) {
        mContext = context;
    }

    public void doBindService() {
        mContext.bindService(new Intent(mContext, ScheduleService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBoundService = ((ScheduleService.ServiceBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBoundService = null;
        }
    };

    public void setAlarmForNotification(Calendar c, boolean setTime) {
        if (mBoundService != null)
            mBoundService.setAlarm(c, setTime);
    }

    public void doUnbindService() {
        if (mIsBound) {
            mContext.unbindService(mConnection);
            mIsBound = false;
        }
    }
}
