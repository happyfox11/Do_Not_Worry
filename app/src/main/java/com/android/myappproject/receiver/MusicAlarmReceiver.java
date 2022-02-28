package com.android.myappproject.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.android.myappproject.service.LocalMusicService;
import com.android.myappproject.service.RestartMusicService;


public class MusicAlarmReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            Intent in = new Intent(context, RestartMusicService.class);
            context.startService(in);
        }
        else
        {
            /*
            * 예전 방식으로, 이렇게 하면 LocalMusicService의 onDestroy()의 else 구문이 실행됨으로써
            * 무한으로 실행되는 방식이었다. 상위 버전에서는 이 부분이 블록되었다.
            * */
            Intent in = new Intent(context, LocalMusicService.class);
            context.startService(in);
        }
    }
}
