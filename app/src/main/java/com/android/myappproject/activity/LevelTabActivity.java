package com.android.myappproject.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.myappproject.R;
import com.android.myappproject.adapter.TabAdapter;
import com.android.myappproject.fragment.BarFragment;
import com.android.myappproject.fragment.TabOneFragment;
import com.android.myappproject.fragment.TabThreeFragment;
import com.android.myappproject.fragment.TabTwoFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class LevelTabActivity extends AppCompatActivity {

    private Activity activity;

    private TabLayout tl_tab;

    public ViewPager2 vp_tab;

    private TabAdapter tabAdapter;

    private ImageButton btn_back;

    private List<Fragment> fragmentList;

    private TabOneFragment fragment_tab1;

    private TabTwoFragment fragment_tab2;
    private TabThreeFragment fragment_tab3;

    private ArrayList<String> sendList;

    private Fragment barFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            setContentView(R.layout.activity_level_tab);

            init();

            setting();

            addListener();
        }
        catch(Exception ex)
        {
            //LogService.error(this, ex.getMessage(), ex);
        }
    }

    private void init()
    {
        activity = this;

        //btn_back = findViewById(R.id.btn_tab_back);

        tl_tab = findViewById(R.id.tl_tab);

        vp_tab = findViewById(R.id.vp_tab);

        fragmentList = new ArrayList<>();

        fragment_tab1 = new TabOneFragment();

        fragment_tab2 = new TabTwoFragment();
        fragment_tab3 = new TabThreeFragment();

        tabAdapter = new TabAdapter(this, fragmentList);

        sendList = new ArrayList<String>();

        barFragment = new BarFragment();
    }

    private void setting()
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_fragment, barFragment).commit();

        receiveItemList();

        fragmentList.add(fragment_tab1);

        fragmentList.add(fragment_tab2);
        fragmentList.add(fragment_tab3);

        vp_tab.setAdapter(tabAdapter);

        tl_tab.addTab(tl_tab.newTab());

        tl_tab.addTab(tl_tab.newTab());
        tl_tab.addTab(tl_tab.newTab());

        new TabLayoutMediator(tl_tab, vp_tab, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position)
            {
                TextView textView = new TextView(activity);

                Bundle bundle = new Bundle();
                bundle.putStringArrayList("itemList", sendList);

                if(position == 0)
                {
                    textView.setText("Level 1");
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextColor(Color.WHITE);

                    fragment_tab1.setArguments(bundle);
                }
                else if(position == 1)
                {
                    textView.setText("Level 2");
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextColor(Color.WHITE);

                    fragment_tab2.setArguments(bundle);
                }else if(position == 2)
                {
                    textView.setText("Level 3");
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextColor(Color.WHITE);

                    fragment_tab3.setArguments(bundle);
                }

                tab.setCustomView(textView);
            }
        }).attach();

        Intent intent = getIntent();
        int level = intent.getExtras().getInt("level")-1;

        vp_tab.setCurrentItem(level);
    }

    private void addListener()
    {
        btn_back.setOnClickListener(listener_back);

        tl_tab.addOnTabSelectedListener(listener_tab_click);
    }

    private TabLayout.OnTabSelectedListener listener_tab_click = new TabLayout.OnTabSelectedListener()
    {
        @Override
        public void onTabSelected(TabLayout.Tab tab)
        {
            //LogService.info(activity, ((TextView)tab.getCustomView()).getText().toString() + " 선택");
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab)
        {
            //LogService.info(activity, ((TextView)tab.getCustomView()).getText().toString() + " 비선택");
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab)
        {
            //LogService.info(activity, ((TextView)tab.getCustomView()).getText().toString() + " 재선택");
        }
    };

    private View.OnClickListener listener_back = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            finish();
        }
    };

    private void receiveItemList(){
        Intent intent = getIntent();
        ArrayList<String> data = (ArrayList<String>) intent.getSerializableExtra("itemList");
        for(int i = 0; i<data.size(); i++) {
            sendList.add(data.get(i));
        }
    }

}