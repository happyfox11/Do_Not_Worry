package com.android.myappproject.fragment;

import static java.lang.Thread.sleep;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.android.myappproject.R;

import java.util.Timer;
import java.util.TimerTask;


public class TimerFragment extends Fragment
{
    private Button btn_timer;
    private TextView tv_time;
    private HandlerTask handlerTask;
    private Timer timer = new Timer();
    private int count = 0;

    private static final int NOTIFICATION_ID = 75;
    private NotificationManager notificationManager;

    public class HandlerTask extends TimerTask{

        private TextView tv_time;

        private int count;
        private boolean checkRun = false;//태스크가 현재 실행중인지 체크하기 위한 플래그

        private boolean hasStarted(){
            return checkRun;
        }
        public int getCount(){
            return count;
        }

        private HandlerTask(TextView tv_handler_time, int count){
            this.tv_time = tv_handler_time;
            this.count = count;
        }

        @Override
        public void run() {
            checkRun = true;
            count++;

            tv_time.post(new Runnable() {
                @Override
                public void run() {
                    int min = count/60;
                    int sec = count%60;

                    String sm = String.valueOf(min);
                    String ss = String.valueOf(sec);

                    if(min<10) sm = "0"+sm;
                    if(sec<10) ss = "0"+ss;

                    if(count == 180){
                        NotificationCompat.Builder builder = getDefaultBuilder();
                        notificationManager = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
                        notificationManager.notify(NOTIFICATION_ID, builder.build());
                        //Log.i("timer", "getActivity(): "+getActivity());
                    }
                    if(count > 180){
                        tv_time.setTextColor(Color.RED);
                    }
                    tv_time.setText(sm+":"+ss);


                }
            });


        }

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
        builder.setSmallIcon(R.mipmap.ic_main_launcher);
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

        tv_time = v.findViewById(R.id.tv_checklist);


        handlerTask = new HandlerTask(tv_time, count);

        btn_timer = v.findViewById(R.id.btn_timer);
        btn_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(handlerTask.hasStarted() == false){
                    timer.schedule(handlerTask,0,1000);
                    btn_timer.setText("타이머 정지");
                }
                else {
                    handlerTask.cancel();
                    count = handlerTask.getCount();
                    handlerTask = new HandlerTask(tv_time, count);
                    btn_timer.setText("타이머 시작");
                }
            }
        });


        return v;

        //return inflater.inflate(R.layout.fragment_timer, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(handlerTask.hasStarted() == true)
            handlerTask.cancel();
    }

}