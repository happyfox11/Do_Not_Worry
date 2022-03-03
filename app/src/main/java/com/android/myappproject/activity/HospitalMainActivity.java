package com.android.myappproject.activity;





import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;



import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


import com.android.myappproject.R;
import com.android.myappproject.adapter.CustomAdapter;
import com.android.myappproject.api.ApiExamSearch;
import com.android.myappproject.fragment.BarFragment;
import com.android.myappproject.vo.CustomHospitalVo;


import java.util.ArrayList;


public class HospitalMainActivity extends AppCompatActivity {


    private Fragment barFragment;

    private Activity activity;
    private Button btn_region;
    private EditText et_region;

    private ListView lv_custom_item;

    private CustomAdapter customAdapter;

    public static ArrayList<CustomHospitalVo> memberList;
    private  Thread thread;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try {
            setContentView(R.layout.activity_hospital_main);

            init();
            setting();
            addListener();

        } catch (Exception ex) {

        }
    }

    private void init() {
        barFragment = new BarFragment();

        activity = this;

        lv_custom_item = findViewById(R.id.lv_custom_item);

        memberList = new ArrayList<>();

        btn_region = findViewById(R.id.btn_region);
        et_region = findViewById(R.id.et_region);

        customAdapter = new CustomAdapter(activity, memberList);
    }

    private void setting() {
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_fragment, barFragment).commit();

        lv_custom_item.setAdapter(customAdapter);

    }

    private void addListener() {
        btn_region.setOnClickListener(listener_region);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //thread.interrupt();
    }

    private View.OnClickListener listener_region = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            thread = new Thread() {
                public void run() {
                    ApiExamSearch api = new ApiExamSearch();
                    api.search(String.valueOf(et_region.getText()));
                }
            };
            thread.start();

            for(int i=0;i<memberList.size();i++){
                customAdapter.addItem(memberList.get(i));
            }

            memberList.clear();
        }
    };
}

