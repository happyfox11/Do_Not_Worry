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
    public static final int RESULT_CODE_LOGOUT = 1002;

    private FirebaseAuth firebaseAuth;

    private Activity activity;
    private Button btn_login;
    private Button btn_register;
    private EditText et_email;
    private EditText et_password;

    private DrawerLayout layout_navi;

    private Toolbar tb_navi;

    private NavigationView nv_navi;

    private ActivityResultLauncher<Intent> resultLauncher;


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
        firebaseAuth = FirebaseAuth.getInstance();
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

        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), activityResultCallback);

    }

    private void addListener() {
        btn_login.setOnClickListener(listener_login);
        btn_register.setOnClickListener(listener_register);

        nv_navi.setNavigationItemSelectedListener(listener_navi_menu_click);
    }

    private final View.OnClickListener listener_login = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String email = et_email.getText().toString();
            String passwd = et_password.getText().toString();

            signIn(email, passwd);
        }
    };


    private final View.OnClickListener listener_register = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SignUpDialog signUpDialog = new SignUpDialog(activity);

            signUpDialog.show();
        }
    };


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

    private ActivityResultCallback<ActivityResult> activityResultCallback = new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            int resultCode = result.getResultCode();

            if (resultCode == RESULT_CODE_LOGOUT) {
                Toast.makeText(activity, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void signIn(String email, String passwd) {
        if (email.isEmpty() || passwd.isEmpty()) {
            Toast.makeText(activity, "이메일 또는 패스워드가 유효하지 않습니다.", Toast.LENGTH_SHORT).show();
        } else {
            authEmailAndPasswd(email, passwd);
        }
    }

    private void authEmailAndPasswd(String email, String passwd) {
        firebaseAuth.signInWithEmailAndPassword(email, passwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i(activity.getClass().getName(), "로그인에 성공하였습니다.");

                    Intent intent = new Intent(activity, LevelMainActivity.class);

                    resultLauncher.launch(intent);
                } else {
                    Log.w(activity.getClass().getName(), "로그인에 실패하였습니다.", task.getException());

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException fauEx) {
                        Log.d(activity.getClass().getName(), "존재하지 않는 유저로 로그인하였습니다.");
                        Toast.makeText(activity, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    } catch (Exception ex) {
                        Toast.makeText(activity, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


}