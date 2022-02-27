package com.android.myappproject.fragment;

import static com.android.myappproject.activity.LoginActivity.RESULT_CODE_LOGOUT;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.myappproject.R;
import com.android.myappproject.activity.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;


public class BarFragment extends Fragment
{
    private Activity activity;
    private ImageButton btn_back;
    private FirebaseAuth firebaseAuth;
    private Button btn_logout;
    private TextView tv_user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_bar, container, false);

        activity = getActivity();
        firebaseAuth = firebaseAuth.getInstance();

        btn_back= v.findViewById(R.id.btn_back);
        tv_user = v.findViewById(R.id.tv_user);
        btn_logout = v.findViewById(R.id.btn_logout);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });

        String user_email = firebaseAuth.getCurrentUser().getEmail();
        String name = user_email.split("@")[0];
        tv_user.setText(name);


        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
//테스트 : LevelMainActivity에서 로그아웃(프래그먼트) --> MainActivity로 돌아감
// LoginActivity에서 호출했으니까 여기로 다시 가고싶음
//                Intent intent = new Intent();
//                activity.setResult(RESULT_CODE_LOGOUT, intent);
//                activity.finish();

                 Intent intent = new Intent(getContext(), LoginActivity.class);
                 startActivity(intent);
                 activity.finish();
            }
        });

        return v;
    }



}