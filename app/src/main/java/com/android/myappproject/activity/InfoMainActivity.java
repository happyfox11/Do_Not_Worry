package com.android.myappproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.myappproject.R;
import com.android.myappproject.adapter.InfoPagerAdapter;
import com.android.myappproject.fragment.Info1_Fragment;
import com.android.myappproject.fragment.Info2_Fragment;
import com.android.myappproject.fragment.Info3_Fragment;
import com.android.myappproject.fragment.Info4_Fragment;
import com.android.myappproject.fragment.Info5_Fragment;
import com.android.myappproject.fragment.Info6_Fragment;

public class InfoMainActivity extends AppCompatActivity {

    private ViewPager pager;
    private InfoPagerAdapter infoPagerAdapter;

    private Info1_Fragment info1_Fragment;
    private Info2_Fragment info2_Fragment;
    private Info3_Fragment info3_Fragment;
    private Info4_Fragment info4_Fragment;
    private Info5_Fragment info5_Fragment;
    private Info6_Fragment info6_Fragment;

    private Button btn_prev_info;
    private Button btn_next_info;

    private ProgressBar pg_bar;
    private int value=100/6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try{
            setContentView(R.layout.activity_info_main);

            init();
            setting();
            addListener();

        }catch(Exception ex){

        }
    }

    private void init(){
        pager = findViewById(R.id.pager);
        infoPagerAdapter = new InfoPagerAdapter(getSupportFragmentManager());

        info1_Fragment = new Info1_Fragment();
        info2_Fragment = new Info2_Fragment();
        info3_Fragment = new Info3_Fragment();
        info4_Fragment = new Info4_Fragment();
        info5_Fragment = new Info5_Fragment();
        info6_Fragment = new Info6_Fragment();

        btn_prev_info= findViewById(R.id.btn_prev_info);
        btn_next_info=findViewById(R.id.btn_next_info);

        pg_bar = findViewById(R.id.pg_bar);
    }

    private void setting(){
        pager.setOffscreenPageLimit(3);

        infoPagerAdapter.addItem(info1_Fragment);
        infoPagerAdapter.addItem(info2_Fragment);
        infoPagerAdapter.addItem(info3_Fragment);
        infoPagerAdapter.addItem(info4_Fragment);
        infoPagerAdapter.addItem(info5_Fragment);
        infoPagerAdapter.addItem(info6_Fragment);

        pager.setAdapter(infoPagerAdapter);
    }


    private void addListener(){
        btn_prev_info.setOnClickListener(listener_prev);
        btn_next_info.setOnClickListener(listener_next);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                //순환 뷰 페이저를 만들고 싶음
//                if (state == ViewPager.SCROLL_STATE_IDLE) {
//                    if (pager.getCurrentItem() == 0) {
//                        pager.setCurrentItem(infoPagerAdapter.getCount(), false);
//
//                    } else if (pager.getCurrentItem() == 5) {
//                        pager.setCurrentItem(-1, false);
//                    }
//                }

                setProgressBar();
            }

        });
    }

    private View.OnClickListener listener_prev = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(pager.getCurrentItem() == 0){//첫번째 페이지
                pager.setCurrentItem(infoPagerAdapter.getCount()-1);
            }else{
                pager.setCurrentItem(pager.getCurrentItem()-1);
            }
        }
    };

    private View.OnClickListener listener_next = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(pager.getCurrentItem()+1 == infoPagerAdapter.getCount()){//마지막 페이지
                pager.setCurrentItem(0);
            }else{
                pager.setCurrentItem(pager.getCurrentItem()+1);
            }
        }
    };

    private void setProgressBar(){
        pg_bar.setProgress((pager.getCurrentItem()+1)*100/6);
    }




}