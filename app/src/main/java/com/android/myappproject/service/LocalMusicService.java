package com.android.myappproject.service;

import static com.android.myappproject.activity.MainActivity.sb_music;
import static com.android.myappproject.activity.MainActivity.tv_current_music;
import static com.android.myappproject.activity.MainActivity.tv_end_music;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.SeekBar;

import com.android.myappproject.R;
import com.android.myappproject.receiver.MusicAlarmReceiver;

import java.util.Calendar;

public class LocalMusicService extends Service
{
    public static Intent intent;

    public static MediaPlayer mediaPlayer = null;

    public static final String ACTION_NAME = "com.android.service.MUSIC";

    public static final String PACKAGE_NAME = "com.android.myappproject";

    public static final String FLAG_MUSIC_STOP = "MUSIC_EXIT_FLAG";

    private int cnt=0;

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        if(mediaPlayer == null)
        {
            //미디어플레이어로 음악을 실행해줄 미디어 플레이어를 생성
            mediaPlayer = MediaPlayer.create(this, R.raw.change);

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        this.intent = intent;

        if(mediaPlayer != null && mediaPlayer.isPlaying() == false)
        {
           // mediaPlayer.start(); //생성해준 미디어 플레이어를 실행해준다

            sb_music.setMax(mediaPlayer.getDuration());
            //tv_end_music.setText(String.valueOf(mediaPlayer.getDuration()/1000));
            tv_end_music.setText(String.valueOf(mediaPlayer.getDuration()/(1000*60))+":"+String.valueOf((mediaPlayer.getDuration()/1000)%60));


            sb_music.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(fromUser)
                        mediaPlayer.seekTo(progress);
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) { }
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) { }
            });


            mediaPlayer.setLooping(true);
            mediaPlayer.start();

            new Thread(new Runnable(){
                @Override
                public void run() {
                    while(mediaPlayer.isPlaying()){
                        try{
                            Thread.sleep(1000);
                            cnt++;
                        } catch(Exception e){
                            e.printStackTrace();
                        }

                        //tv_current_music.setText(String.valueOf(mediaPlayer.getCurrentPosition()/1000));
                        tv_current_music.setText(String.valueOf(mediaPlayer.getCurrentPosition()/(1000*60))+":"+String.valueOf((mediaPlayer.getCurrentPosition()/1000)%60));
                        sb_music.setProgress(mediaPlayer.getCurrentPosition());
                    }
                }
            }).start();
        }

        // START_STICKY : 서비스가 강제종료되었을 경우 서비스를 재시작 하도록 설정
        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        /*
         * 백그라운드에서 실행되는 앱은 알아서 일정시간 후에 죽여버린다.
         * 그래서 foreground notification을 사용해서 앱이 실행중이라는 것을 알려주든지
         * 특수한 방법을 사용해야 한다.
         * */
        //음악 중지 버튼을 누르지 않았는데, 앱에서 빠져나왔다는 이유만으로 음악이 중지된 경우(백그라운드에서 실행중)의
        //문제를 해결해주기 위한 방법이다. 즉, 음악 중지 버튼을 누른 경우에만 실제로 음악을 중지시켜주는 것이다.
        //플래그 값이 true인 경우에만

        if(intent.getBooleanExtra(FLAG_MUSIC_STOP, false))
        {//default 값이 false이니까 음악 중지를 누르지 않았다면 아래의 코드가 실행되지 않을 것이다.
            if(mediaPlayer != null)
            {
                if(mediaPlayer.isPlaying())
                {
                    mediaPlayer.stop();
                }
                mediaPlayer.release();
                mediaPlayer = null;
                /*
                onDestroy함수가 실행된다는 것은 localMusicService 객체가 소멸된다는 의미이다.
                localMusicService 안에 mediaPlayer가 속해있으므로, 같이 사라질 것임
                하지만, 뭐 잘 안되면 mediaplayer가 제대로 종료되지 않고 localMusicService가 먼저 사라질 수도 있음
                그러면 mediaplayer가 남아있을 수도 있으므로
                static으로 선언을 해서  localMusicService 객체가 소멸되더라도 한동안은 남아있을수 있게(?)한다.
                얘가 종료안되고 있으면 안드로이드에서 알아서 이상하게 생각하고 종료해준다.
                그래서 종료되기 전에 재실행 해줘야 한다(?????????????????)
                */
            }
        }
        else
        {//onDestroy 실행됨 --> 내가 음악 중지 버튼을 누른 것이 아닌데 onDestroy가 실행된 경우니까..?
            // 브로드캐스트를 이용해서
            final Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.SECOND, 1);//현재 시간에 1초를 더해준다
            //1초만에 가비지 컬렉션에서 mediaPlayer를 수거하지는 않을테니까...?

            Intent intent = new Intent(this, MusicAlarmReceiver.class);
            PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
        }
    }
}


