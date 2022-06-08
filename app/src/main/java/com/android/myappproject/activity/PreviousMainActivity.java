package com.android.myappproject.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.myappproject.R;
import com.android.myappproject.db.Database;
import com.android.myappproject.fragment.BarFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PreviousMainActivity extends AppCompatActivity {

    private Activity activity;
    private Button btn_select;
    private CalendarView cv;
    private BarFragment barFragment;
    private Map<String,String> result;
    private LinearLayout lv_layout;
    private LinearLayout lv_title;
    public static String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            setContentView(R.layout.activity_prev_main);

            init();
            setting();
            addListener();

        }catch (Exception ex){

        }
    }

    private void init(){
        activity = this;
        barFragment = new BarFragment();
        btn_select = findViewById(R.id.btn_select);
        cv = findViewById(R.id.cv);
        lv_layout = findViewById(R.id.lv_layout);
        lv_title = findViewById(R.id.lv_title);
        result = new HashMap<>();
    }

    private void setting(){
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_fragment, barFragment).commit();
        selectQuery();
    }

    private void addListener(){
        btn_select.setOnClickListener(listener_select);
        cv.setOnDateChangeListener(listener_change_date);
    }

    private CalendarView.OnDateChangeListener listener_change_date = new CalendarView.OnDateChangeListener()
    {
        @Override
        public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
        {
            btn_select.setText(year+"/"+(month+1)+"/"+dayOfMonth+" 정보 조회");
            String selected_month =  String.valueOf(month +1);
            String selected_dayOfMonth = String.valueOf(dayOfMonth);

            if(month+1 < 10){
                selected_month = "0"+selected_month;
            }
            if(dayOfMonth < 10){
                selected_dayOfMonth = "0"+selected_dayOfMonth;
            }
            selectedDate = String.valueOf(year+"/"+selected_month+"/"+selected_dayOfMonth);
            btn_select.setText(selectedDate + " 정보 조회");
        }
    };

    private View.OnClickListener listener_select = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
             selectQuery();

            lv_layout.removeAllViews();
             if(result.size() > 0)
                 lv_title.setVisibility(View.VISIBLE);
             else
                 lv_title.setVisibility(View.GONE);


            for(Map.Entry<String,String> entry : result.entrySet()) {
                Log.i("adfadfaf",entry.getKey()+" "+ entry.getValue());

                String s_time = entry.getKey();
                String s_checklist = entry.getValue();

                LinearLayout row = new LinearLayout(activity);
                row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                row.setOrientation(LinearLayout.HORIZONTAL);

                //
                LinearLayout col1 = new LinearLayout(activity);
                col1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 7f));
                col1.setOrientation(LinearLayout.HORIZONTAL);

                TextView time = new TextView(activity);
                time.setText(s_time);
                time.setTextColor(Color.RED);
                time.setTextSize((int)getResources().getDimension(R.dimen.frag_one_tv_size));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(25,0,0,0);  // 왼쪽, 위, 오른쪽, 아래 순서입니다.
                time.setLayoutParams(params);

                //
                LinearLayout col2 = new LinearLayout(activity);
                col2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 3f));
                col2.setOrientation(LinearLayout.HORIZONTAL);

                TextView checklist = new TextView(activity);
                checklist.setText(s_checklist);
                checklist.setTextColor(Color.BLUE);
                checklist.setTextSize((int)getResources().getDimension(R.dimen.frag_one_tv_size));

                col1.addView(time);
                col2.addView(checklist);

                row.addView(col1);
                row.addView(col2);

                View v1 = new View(activity);
                v1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.view_height)));
                v1.setBackgroundColor(Color.rgb(115, 122, 222));

                lv_layout.setBackgroundColor(Color.rgb(220,220,220));
                lv_layout.addView(row);
                lv_layout.addView(v1);
            }
        }
    };

    private void selectQuery(){
        //lv_layout.removeAllViews();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String name = "";
        if(user != null){
            String user_email = user.getEmail();
            name = user_email.split("@")[0];
        }else{
            name = "user";
        }

        Log.i("username", name+","+selectedDate);

        Database db = new Database(activity);

        result = db.executeQuery(name, selectedDate);
        //result = db.executeQuery();
    }
}