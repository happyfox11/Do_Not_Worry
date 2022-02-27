package com.android.myappproject.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.myappproject.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class MainActivity extends AppCompatActivity {

    private Activity activity;
    private Button btn_login;
    private Button btn_register;
    private EditText et_email;
    private EditText et_password;

    private DrawerLayout layout_navi;

    private Toolbar tb_navi;

    private NavigationView nv_navi;


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

        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);

        layout_navi = findViewById(R.id.layout_navi);

        tb_navi = findViewById(R.id.tb_navi);

        nv_navi = findViewById(R.id.nv_navi);

    }

    private void setting() {

        setSupportActionBar(tb_navi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_navi_menu);

    }

    private void addListener() {

        nv_navi.setNavigationItemSelectedListener(listener_navi_menu_click);
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