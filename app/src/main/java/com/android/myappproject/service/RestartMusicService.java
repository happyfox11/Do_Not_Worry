package com.android.myappproject.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.android.myappproject.R;
import com.android.myappproject.activity.MainActivity;


public class RestartMusicService extends Service
{
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        String ChannelID = "default";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, ChannelID);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(null);
        builder.setContentText(null);
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            notificationManager.createNotificationChannel(new NotificationChannel(ChannelID, "리스타트 채널", NotificationManager.IMPORTANCE_NONE));
        }

        Notification notification = builder.build();
        startForeground(9, notification);//푸시알림을 발생시킴(오레오버전 이상부터는 백그라운드 앱 실행시 알림을 해줘야한다)

        Intent in = new Intent(this, com.android.myappproject.service.LocalMusicService.class);
        startService(in);

        stopForeground(true);//푸시알림을 바로 중지시켜버림-->푸시알림이 발생되었다고 인식함
        stopSelf();//결론은 RestartMusicService가 중지되고, 위의 LocalMusicService가 실행되는 것이다.
        //오레오버전 이상 부터는 바로 LocalMusicService로 갈수가 없어서 RestartMusicService를 이용해서 우회해서 가는듯?

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
