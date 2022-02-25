package com.android.myappproject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.myappproject.R;

public class TelMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try{
            setContentView(R.layout.activity_tel_main);

            init();
            setting();
            addListener();

        }catch(Exception ex){

        }
    }

    private void init(){

    }

    private void setting(){

    }

    private void addListener(){

    }
}