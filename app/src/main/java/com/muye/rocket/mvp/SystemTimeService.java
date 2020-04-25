package com.muye.rocket.mvp;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.muye.rocket.mvp.home.contract.SystemTimeContract;
import com.muye.rocket.mvp.home.presenter.SystemTimePresenter;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SystemTimeService extends Service implements SystemTimeContract.View {
    public static final String INTENT_FILTER_SYSTEM_TIME = "com.muye.rocket.systemTime";

    SystemTimeContract.Presenter mSystemTimePresenter;
    TimeTimerTask mTimerTask;
    Timer mTimer;
    private long mSystemTime;

    @Override
    public void onCreate() {
        super.onCreate();
        fetchSystemTime();
        silentForegroundNotification();
    }

    private void fetchSystemTime() {
        if (mSystemTimePresenter == null) {
            mSystemTimePresenter = new SystemTimePresenter(this);
        }
        mSystemTimePresenter.fetchSystemTime();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void bindSystemTime(long time) {
        long count = Math.abs(mSystemTime - time);
        if(!isAppOnForeground()){
            mSystemTime = time;
        }else {
            if (count >= 4) {
                mSystemTime = time;
            }
        }
        countDownTime();
    }

    @Override
    public void bindTimeTask(long time) {

    }

    @Override
    public void onError(int code, String message) {

    }

    @Override
    public void showLoadDialog(String message) {

    }

    @Override
    public void hideLoadDialog() {

    }

    private void refreshSystemTime() {
        mSystemTime += 1;
        if (isAppOnForeground()) {
            if (mSystemTime % 10 == 0) {
                fetchSystemTime();
            }
        } else if (mSystemTime % 6 == 0) {
            fetchSystemTime();
        }
    }

    private long getSystemTime() {
        return mSystemTime;
    }

    private void initTimer() {
        if (mTimer == null) {
            mTimer = new Timer();
        }

        if (mTimerTask == null) {
            mTimerTask = new TimeTimerTask(this);
        }
    }

    private void countDownTime() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        initTimer();
        mTimer.schedule(mTimerTask, 1000, 1000);
    }

    static class TimeTimerTask extends TimerTask {
        WeakReference<SystemTimeService> weakReference;

        public TimeTimerTask(SystemTimeService systemTimeService) {
            weakReference = new WeakReference<>(systemTimeService);
        }

        @Override
        public void run() {
            if (weakReference != null && weakReference.get() != null) {
                weakReference.get().refreshSystemTime();
                long systemTime = weakReference.get().getSystemTime();
                Intent intent = new Intent(INTENT_FILTER_SYSTEM_TIME);
                intent.putExtra("systemTime", systemTime);
                weakReference.get().sendBroadcast(intent);
            }
        }
    }


    private void silentForegroundNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel silentChannel = new NotificationChannel("40", "App Service", NotificationManager.IMPORTANCE_HIGH);
            silentChannel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);//设置在锁屏界面上显示这条通知
            silentChannel.setSound(null, null);
            silentChannel.enableVibration(false);
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            //数字是随便写的“40”，
            nm.createNotificationChannel(silentChannel);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "40");
            //其中的2，是也随便写的，正式项目也是随便写
            startForeground(2, builder.build());
        }
    }

    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();
        /**
         * 获取Android设备中所有正在运行的App
         */
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onDestroy() {
        if (mTimerTask != null) {
            mTimerTask.cancel();
        }
        if (mTimer != null) {
            mTimer.cancel();
        }
        super.onDestroy();
    }
}
