package com.android.myappproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.android.myappproject.R;
import com.android.myappproject.fragment.BarFragment;

import java.io.Serializable;
import java.util.ArrayList;

public class SelectLevelActivity extends AppCompatActivity {

    private Activity activity;
    private ImageButton btn_back;
    private Button btn_lv1;
    private Button btn_lv2;
    private Button btn_lv3;

    private ArrayList<String> sendList;

    private Fragment barFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try{
            setContentView(R.layout.activity_select_level);

            init();
            setting();
            addListener();

        }catch(Exception ex){

        }
    }

    private void init(){
        activity = this;
        //btn_back = findViewById(R.id.btn_test_back);
        btn_lv1 = findViewById(R.id.btn_back_home);
        btn_lv2 = findViewById(R.id.btn_lv2);
        btn_lv3 = findViewById(R.id.btn_lv3);

        sendList = new ArrayList<String>();

        barFragment = new BarFragment();
    }

    private void setting() {
        receiveItemList();
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_fragment, barFragment).commit();
    }



    private void addListener(){
        //btn_back.setOnClickListener(listener_back);
        btn_lv1.setOnClickListener(listener_lv1);
        btn_lv2.setOnClickListener(listener_lv2);
        btn_lv3.setOnClickListener(listener_lv3);
    }

    private void receiveItemList(){
        Intent intent = getIntent();
        ArrayList<String> data = (ArrayList<String>) intent.getSerializableExtra("itemList");
        for(int i = 0; i<data.size(); i++) {
            sendList.add(data.get(i));
        }
    }

/*    private View.OnClickListener listener_back = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };*/

    private View.OnClickListener listener_lv1 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, LevelTabActivity.class);
            intent.putExtra("level", 1);
            intent.putStringArrayListExtra("itemList",sendList);
            startActivity(intent);
        }
    };

    private View.OnClickListener listener_lv2 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, LevelTabActivity.class);
            intent.putExtra("level", 2);
            intent.putStringArrayListExtra("itemList",sendList);
            startActivity(intent);
        }
    };

    private View.OnClickListener listener_lv3 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, LevelTabActivity.class);
            intent.putExtra("level", 3);
            intent.putStringArrayListExtra("itemList",sendList);
            startActivity(intent);
        }
    };
}