package com.android.myappproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.myappproject.R;
import com.android.myappproject.fragment.BarFragment;

public class ReportLevelActivity extends AppCompatActivity {
    private Activity activity;
    //private ImageButton btn_report_back;
    private Button btn_lv_num;
    private Button btn_back_home;

    private Fragment barFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try{
            setContentView(R.layout.activity_report_level);

            init();
            setting();
            addListener();

        }catch(Exception ex){

        }
    }

    private void init(){
        activity = this;
        //btn_report_back = findViewById(R.id.btn_back);
        btn_lv_num = findViewById(R.id.btn_lv_num);
        btn_back_home = findViewById(R.id.btn_select);

        barFragment = new BarFragment();
    }

    private void setting(){
        Intent intent = getIntent();
        int lv_num = intent.getExtras().getInt("lv_num");
        btn_lv_num.setText(String.valueOf(lv_num));
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_fragment, barFragment).commit();
    }

    private void addListener(){
        //btn_report_back.setOnClickListener(listener_report_back);
        btn_back_home.setOnClickListener(listener_back_home);
    }

/*    private View.OnClickListener listener_report_back = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };*/

    private View.OnClickListener listener_back_home = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, LevelMainActivity.class);
            startActivity(intent);
            finish();
        }
    };

}