package com.android.myappproject.activity;

import static com.android.myappproject.service.LocalMusicService.FLAG_MUSIC_STOP;
import static com.android.myappproject.service.LocalMusicService.mediaPlayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.myappproject.R;
import com.android.myappproject.service.LocalMusicService;

import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity {

    private Activity activity;

    private DrawerLayout layout_navi;

    private Toolbar tb_navi;

    private NavigationView nv_navi;

    private Button btn_music_control;
    private Boolean check = false;
    private Intent musicIntent;

    public static SeekBar sb_music;
    public static TextView tv_current_music;
    public static TextView tv_end_music;

    public static int current = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_main);

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            init();
            setting();
            addListener();

        } catch (Exception ex) {

        }
    }

    private void init() {
        activity = this;
        layout_navi = findViewById(R.id.layout_navi);
        tb_navi = findViewById(R.id.tb_navi);
        nv_navi = findViewById(R.id.nv_navi);

        btn_music_control = findViewById(R.id.btn_music_control);
        sb_music = findViewById(R.id.sb_music);
        tv_current_music = findViewById(R.id.tv_current_music);
        tv_end_music = findViewById(R.id.tv_end_music);
    }

    private void setting() {

        setSupportActionBar(tb_navi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_navi_menu);

    }

    private void addListener() {

        nv_navi.setNavigationItemSelectedListener(listener_navi_menu_click);
        btn_music_control.setOnClickListener(listener_music_control);

    }

    private View.OnClickListener listener_music_control = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(check == false){
                check = true;
                btn_music_control.setText("♬ 중지");
                checkLocalMusicIntent();
                //musicIntent.putExtra("continue",sb_music.getProgress());
                current = sb_music.getProgress();
                startService(musicIntent);
            }else{
                check = false;
                btn_music_control.setText("♬ 재생");
                checkLocalMusicIntent();
                LocalMusicService.intent.putExtra(FLAG_MUSIC_STOP, true);
                stopService(musicIntent);
            }
        }
    };

    private void checkLocalMusicIntent()
    {
        if(LocalMusicService.intent == null)
        {
            musicIntent = new Intent(LocalMusicService.ACTION_NAME);
            musicIntent.setPackage(LocalMusicService.PACKAGE_NAME);
        }
        else
        {
            musicIntent = LocalMusicService.intent;
        }
    }



    private final NavigationView.OnNavigationItemSelectedListener listener_navi_menu_click = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (item.getItemId() == R.id.icon_relief) {
                Toast.makeText(activity, "Relief Menu Click!", Toast.LENGTH_SHORT).show();
                layout_navi.closeDrawer(GravityCompat.START);

                Intent intent = new Intent(activity, LevelMainActivity.class);
                startActivity(intent);

                return true;
            } else if (item.getItemId() == R.id.icon_info) {
                Toast.makeText(activity, "Info Menu Click!", Toast.LENGTH_SHORT).show();
                layout_navi.closeDrawer(GravityCompat.START);

                Intent intent = new Intent(activity, InfoMainActivity.class);
                startActivity(intent);

                return true;
            } else if (item.getItemId() == R.id.icon_tel) {
                Toast.makeText(activity, "Tel Menu Click!", Toast.LENGTH_SHORT).show();
                layout_navi.closeDrawer(GravityCompat.START);

                Intent intent = new Intent(activity, TelMainActivity.class);
                startActivity(intent);

                return true;
            }

            return false;
        }
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            layout_navi.openDrawer(GravityCompat.START);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}