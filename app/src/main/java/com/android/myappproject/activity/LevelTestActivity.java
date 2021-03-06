package com.android.myappproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
import com.android.myappproject.fragment.BarFragment;

import java.util.ArrayList;

public class LevelTestActivity extends AppCompatActivity {

    private Activity activity;
    private TextView tv1, tv2, tv3, tv4, tv5, tv6;
    private CheckBox cb1, cb2, cb3 ,cb4, cb5, cb6;
    private Button btn_level_recommend;
    //private ImageButton btn_test_back;

    private Fragment barFragment;

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
        //btn_test_back = findViewById(R.id.btn_test_back);

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

        barFragment = new BarFragment();
    }

    private void setting(){
        add_tv_contents();
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_fragment, barFragment).commit();
    }

    private void addListener(){
        btn_level_recommend.setOnClickListener(listener_level_recommend);
        //btn_test_back.setOnClickListener(listener_back);
    }

    private void add_tv_contents(){

        tv1.setText("?????? ??? ????????????, ????????? ?????????, ????????? ???????????? ?????? ????????? ???????????? ?????? ??????.");
        tv2.setText("???????????? ????????? ???????????????, ???????????? ????????? ???????????? ????????? ????????? ??????.");
        tv3.setText("??? ??? ????????? ???????????? ????????? ????????? ??????, (3??? ??????) ??????????????? ????????????.");
        tv4.setText("??????????????? ?????????????????? ???????????? ?????? ??? ????????? ????????? ???????????? ?????? ??????.");
        tv5.setText("????????? ???????????? ????????? ????????????, ????????? ????????? ????????? ??????.");
        tv6.setText("???????????? ???????????? ?????????, ?????? ????????? ???????????? ??????.");

    }

/*    private View.OnClickListener listener_back = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };*/

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
            finish();
        }
    };

}