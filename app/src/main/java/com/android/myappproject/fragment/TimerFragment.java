package com.android.myappproject.fragment;

import static com.android.myappproject.activity.MainActivity.sb_music;
import static com.android.myappproject.activity.MainActivity.tv_current_music;
import static java.lang.Thread.sleep;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.android.myappproject.R;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class TimerFragment extends Fragment
{
    private Button btn_timer;
    private TextView tv_time;
    private int count;
    private Boolean flag = false;
    private ImageView iv_shake_clock;

    private static final int NOTIFICATION_ID = 75;
    private NotificationManager notificationManager;

    public Disposable backgroundtask;

    private void asyncTimer(){
        backgroundtask = Observable.fromCallable(new Callable<Object>() {//--> 메인메소드가 아닌 스레드에서 동작

            @Override
            public Object call() throws Exception {
                String data = "";
                if(flag == false){
                    backgroundtask.dispose();
                    Thread.currentThread().interrupt();
                }
                //2. doInBackground : 메인스레드가 아닌 백그라운드 작업
                try {

                    while(flag == true){
                        if(flag == false)
                            break;
                        try {
                            Thread.sleep(1000);
                            count++;

                            Log.i("카운트", count+"");

                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (count > 180)
                                        tv_time.setTextColor(Color.RED);

                                    int min = count / 60;
                                    int sec = count % 60;

                                    String sm = String.valueOf(min);
                                    String ss = String.valueOf(sec);

                                    if (min < 10) sm = "0" + sm;
                                    if (sec < 10) ss = "0" + ss;

                                    tv_time.setText(sm + ":" + ss);
                                    //tv_time.setText(String.valueOf(count));
                                }
                            });

                            if (count == 180) {
                                NotificationCompat.Builder builder = getDefaultBuilder();
                                notificationManager = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
                                notificationManager.notify(NOTIFICATION_ID, builder.build());
                                //Log.i("timer", "getActivity(): "+getActivity());
                            }



                        }catch (Exception ex){ }
                    }

                } catch (Exception ex) { }

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

    private NotificationCompat.Builder getDefaultBuilder()
    {
        String channelID = "timeout_notification_channel";

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            if(notificationManager != null && notificationManager.getNotificationChannel(channelID) == null)
            {
                NotificationChannel notificationChannel = new NotificationChannel
                        (
                                channelID,
                                "Timeout Notification Channel",
                                NotificationManager.IMPORTANCE_HIGH
                        );

                notificationChannel.setDescription("시간 초과 알림 채널");

                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        NotificationCompat.Builder builder =  new NotificationCompat.Builder(getContext(), channelID);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("목표 시간 초과");
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentText("3분이 초과되었습니다. 다음에는 성공해봐요!");
        builder.setAutoCancel(false);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);

        return builder;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_timer, container, false);

        count = 0;

        tv_time = v.findViewById(R.id.tv_checklist);

        btn_timer = v.findViewById(R.id.btn_timer);
        btn_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(flag == false){//작동중이지 않다면(시작->정지)
                    btn_timer.setText("타이머 정지");
                    flag = true;
                    Log.i("플래그", flag+"시작클릭");
                    asyncTimer();
                }
                else {//작동중이지 않다면
                    btn_timer.setText("타이머 시작");
                    flag = false;
                    Log.i("플래그", flag+"중지클릭");
                    backgroundtask.dispose();
                }

            }
        });

        Animation shake = AnimationUtils.loadAnimation(getContext(),R.anim.shake2);
        iv_shake_clock = v.findViewById(R.id.iv_shake_clock);
        iv_shake_clock.startAnimation(shake);


        return v;

        //return inflater.inflate(R.layout.fragment_timer, container, false);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("호출","onDestroy");

        flag = false;
        Log.i("호출","onStop");
        if (backgroundtask != null && backgroundtask.isDisposed() == false) {
            backgroundtask.dispose();
        }
    }

}