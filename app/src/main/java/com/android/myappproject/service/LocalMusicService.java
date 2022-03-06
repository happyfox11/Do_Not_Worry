package com.android.myappproject.service;

import static com.android.myappproject.activity.MainActivity.current;
import static com.android.myappproject.activity.MainActivity.sb_music;
import static com.android.myappproject.activity.MainActivity.tv_current_music;
import static com.android.myappproject.activity.MainActivity.tv_end_music;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.myappproject.R;
import com.android.myappproject.activity.MainActivity;
import com.android.myappproject.receiver.MusicAlarmReceiver;

import java.util.Calendar;
import java.util.TimerTask;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LocalMusicService extends Service
{
    public static Intent intent;

    public static MediaPlayer mediaPlayer = null;

    public static final String ACTION_NAME = "com.android.service.MUSIC";

    public static final String PACKAGE_NAME = "com.android.myappproject";

    public static final String FLAG_MUSIC_STOP = "MUSIC_EXIT_FLAG";

    private Thread setTimeThread;
    private Disposable backgroundtask;


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

        if(mediaPlayer != null && mediaPlayer.isPlaying() == false)//음악이 재생중이지 않다면
        {
            mediaPlayer.setLooping(true);
            mediaPlayer.seekTo(current);
            mediaPlayer.start(); //생성해준 미디어 플레이어를 실행해준다


            sb_music.setMax(mediaPlayer.getDuration());
            //tv_end_music.setText(String.valueOf(mediaPlayer.getDuration()/1000));
            tv_end_music.setText(String.valueOf(mediaPlayer.getDuration() / (1000 * 60)) + ":" + String.valueOf((mediaPlayer.getDuration() / 1000) % 60));

            /*sb_music.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser)
                        mediaPlayer.seekTo(progress);

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });*/


            //mediaPlayer.setLooping(true);
            //mediaPlayer.start();

            /*setTimeThread =  new Thread(new Runnable(){
                @Override
                public void run() {
                    while(mediaPlayer.isPlaying()){
                        try{
                            Thread.sleep(1000);
                        } catch(InterruptedException iex){
                            //interrupt를 통해 종료시킨 경우 이 예외에 걸려서 종료된다.
                            Log.i("스레드", "Thread 종료");
                            break;
                        } catch(Exception e){
                            e.printStackTrace();
                        }
                        int cp = mediaPlayer.getCurrentPosition();

                        //tv_current_music.setText(String.valueOf(mediaPlayer.getCurrentPosition()/1000));
                        tv_current_music.setText(String.valueOf(cp/(1000*60))+":"+String.valueOf((cp/1000)%60));
                        sb_music.setProgress(cp);
                    }
                }
            });
            setTimeThread.start();*/


            backgroundtask = Observable.fromCallable(new Callable<Object>() {//--> 메인메소드가 아닌 스레드에서 동작

                @Override
                public Object call() throws Exception {
                    String data = "";

                    //2. doInBackground : 메인스레드가 아닌 백그라운드 작업
                    try {
                        while (mediaPlayer.isPlaying()) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException iex) {
                                //interrupt를 통해 종료시킨 경우 이 예외에 걸려서 종료된다.
                                Log.i("스레드", "Thread 종료");
                                break;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            int cp = mediaPlayer.getCurrentPosition();

                            //tv_current_music.setText(String.valueOf(mediaPlayer.getCurrentPosition()/1000));

                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    tv_current_music.setText(String.valueOf(cp / (1000 * 60)) + ":" + String.valueOf((cp / 1000) % 60));
                                    sb_music.setProgress(cp);
                                }
                            });

                        }

                    } catch (Exception ex) {
                    }

                    return data;//null이면 오류난다//아래의 Object o 값으로 변환된다
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Object>() {
                @Override
                public void accept(Object o) throws Exception {
                    //3. onPostExecute : 백그라운드 작업 후 처리작업


                    backgroundtask.dispose();

                }
            });
        }



        // START_STICKY : 서비스가 강제종료되었을 경우 서비스를 재시작 하도록 설정
        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();


        if (intent.getBooleanExtra(FLAG_MUSIC_STOP, false)) {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.release();
                //setTimeThread.interrupt();
                mediaPlayer = null;

            }
        } else {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.SECOND, 1);

            Intent intent = new Intent(this, MusicAlarmReceiver.class);
            PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
        }
    }
}


