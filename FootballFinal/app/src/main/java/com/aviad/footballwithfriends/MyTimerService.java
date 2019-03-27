package com.aviad.footballwithfriends;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;

import com.aviad.footballwithfriends.MainActivity;
import com.aviad.footballwithfriends.R;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MyTimerService extends Service {

    public final static String ACTION_START = "start";
    public final static String ACTION_STOP = "stop";
    public final static String ACTION_PAUSE = "pause";
    public final static String ACTION_TIMER_CHANGED = "timeChanged";
    private static final String CHANNEL_ID_TIMER = "timerChannel";
    private static final int NOTIFICATION_ID_TIMER = 1;


    ScheduledExecutorService timerThread;
    private int counter = 0;
    boolean isRunning = false;


    //Help other devs
    public static Intent getStartIntent(Context context){
        Intent intent = new Intent(context, MyTimerService.class);
        intent.setAction(MyTimerService.ACTION_START);
        return intent;
    }

    public static Intent getStopIntent(Context context) {
        Intent intent = new Intent(context, MyTimerService.class);
        intent.setAction(MyTimerService.ACTION_STOP);
        return intent;
    }

    public static Intent getPauseIntent(Context context){
        Intent intent = new Intent(context,MyTimerService.class);
        intent.setAction(MyTimerService.ACTION_PAUSE);
        return intent;
    }

    //when activity calls startService-> onStartComnand is invoked

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction() == null)return START_NOT_STICKY;
        switch (intent.getAction()) {
            case ACTION_START:
                start();
                return START_NOT_STICKY;
            case ACTION_STOP:
                stop(); // clear
                return START_NOT_STICKY;
            case ACTION_PAUSE:
                pause();
                return START_NOT_STICKY;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void pause() {



    }

    private void stop() {
        super.onDestroy();
        System.out.println("On Destroy");
        timerThread.shutdownNow();
        isRunning = false;
        counter = 0;
        Intent timeUpdateIntent = new Intent(ACTION_TIMER_CHANGED);
        timeUpdateIntent.putExtra("time",counter);
        LocalBroadcastManager.getInstance(this).sendBroadcast(timeUpdateIntent);
    }

    private void start() {
        if (isRunning){
            return;
        }
        System.out.println("start command!");
        isRunning = true;
        timerThread = Executors.newSingleThreadScheduledExecutor();
        timerThread.scheduleAtFixedRate(()->{
            counter++;
            Intent timeUpdateIntent = new Intent(ACTION_TIMER_CHANGED);
            timeUpdateIntent.putExtra("time",counter);
            LocalBroadcastManager.getInstance(this).sendBroadcast(timeUpdateIntent);
        },0, 1, TimeUnit.SECONDS);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
