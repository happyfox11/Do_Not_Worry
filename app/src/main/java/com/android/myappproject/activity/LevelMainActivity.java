package com.android.myappproject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.android.myappproject.R;

public class LevelMainActivity extends AppCompatActivity {

    private Activity activity;
    private ImageButton btn_level_main_back;
    private Button btn_prev_db;
    private Button btn_select_level;
    private Button btn_level_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try{
            setContentView(R.layout.activity_level_main);

            init();
            setting();
            addListener();

        }catch(Exception ex){

        }
    }

    private void init(){
        activity = this;
        btn_level_main_back = findViewById(R.id.btn_report_back);
        btn_prev_db = findViewById(R.id.btn_back_home);
        btn_select_level = findViewById(R.id.btn_lv2);
        btn_level_test = findViewById(R.id.btn_lv3);
    }

    private void setting(){

    }

    private void addListener(){
        btn_level_main_back.setOnClickListener(listener_back);
        btn_prev_db.setOnClickListener(listener_prev_db);
        btn_select_level.setOnClickListener(listener_select_level);
        btn_level_test.setOnClickListener(listener_level_test);
    }

    private View.OnClickListener listener_back = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    private View.OnClickListener listener_prev_db = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    private View.OnClickListener listener_select_level = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, ManageItemListActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener listener_level_test = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, LevelTestActivity.class);
            startActivity(intent);
        }
    };


}