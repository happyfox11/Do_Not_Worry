package com.android.myappproject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.android.myappproject.R;

import java.util.ArrayList;

public class LevelTestActivity extends AppCompatActivity {

    private Activity activity;
    private TextView tv1, tv2, tv3, tv4, tv5, tv6;
    private CheckBox cb1, cb2, cb3 ,cb4, cb5, cb6;
    private Button btn_level_recommend;
    private ImageButton btn_test_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            setContentView(R.layout.activity_level_test);

            init();
            setting();
            addListener();

        }catch(Exception ex){

        }
    }

    private void init(){
        activity = this;
        btn_test_back = findViewById(R.id.btn_test_back);

        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        tv5 = findViewById(R.id.tv5);
        tv6 = findViewById(R.id.tv6);

        cb1 = findViewById(R.id.cb1);
        cb2 =findViewById(R.id.cb2);
        cb3 = findViewById(R.id.cb3);
        cb4 = findViewById(R.id.cb4);
        cb5 = findViewById(R.id.cb5);
        cb6 = findViewById(R.id.cb6);

        btn_level_recommend = findViewById(R.id.btn_level_recommend);
    }

    private void setting(){
        add_tv_contents();

    }

    private void addListener(){
        btn_level_recommend.setOnClickListener(listener_level_recommend);
        btn_test_back.setOnClickListener(listener_back);
    }

    private void add_tv_contents(){

        tv1.setText("문을 잘 잠갔는지, 가스를 껐는지, 수도를 잠갔는지 등에 대해서 걱정해본 적이 있다.");
        tv2.setText("확인하지 않으면 불안하지만, 확실하게 한번만 확인하면 마음이 안심이 된다.");
        tv3.setText("한 번 확인을 했음에도 걱정이 끝나지 않아, (3회 이상) 반복적으로 확인한다.");
        tv4.setText("반복적으로 확인했음에도 불구하고 다시 그 장소로 돌아가 확인해본 적이 있다.");
        tv5.setText("눈으로 확인하는 것으로 부족하고, 확실한 증거가 있어야 한다.");
        tv6.setText("반복적인 확인으로 인해서, 일상 생활에 불편함이 많다.");

    }

    private View.OnClickListener listener_back = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    private View.OnClickListener listener_level_recommend = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            int result = 1;

            if(cb1.isChecked() == true) result = 1;
            if(cb2.isChecked() == true) result = 1;
            if(cb3.isChecked() == true) result = 2;
            if(cb4.isChecked() == true) result = 2;
            if(cb5.isChecked() == true) result = 3;
            if(cb6.isChecked() == true) result = 3;


            Intent intent = new Intent(activity, ReportLevelActivity.class);
            intent.putExtra("lv_num", result);
            startActivity(intent);
        }
    };

}