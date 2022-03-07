package com.android.myappproject.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.myappproject.R;
import com.android.myappproject.activity.LevelMainActivity;
import com.android.myappproject.activity.ManageItemListActivity;
import com.android.myappproject.db.Database;
import com.android.myappproject.db.DatabaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TabOneFragment extends Fragment
{
    private ArrayList<String> itemList;
    private Button btn_complete;
    private Button btn_new;
    private TimerFragment timerFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_tab_one, container, false);
        timerFragment = new TimerFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_timerfragment, timerFragment).commit();
        LinearLayout layout = v.findViewById(R.id.layout_list);

        btn_new = v.findViewById(R.id.btn_new);
        btn_complete = v.findViewById(R.id.btn_complete);

        Bundle bundle = getArguments();
        itemList= bundle.getStringArrayList("itemList");
        Log.i("frag1", ":"+(itemList.size()));

        if(itemList.size() > 0 ){
            for(int i=0 ;i<itemList.size(); i++){
                TextView textView = new TextView(getContext());
                textView.setText(" ★ "+itemList.get(i));
                layout.addView(textView);
            }
        }else{
            TextView textView = new TextView(getContext());
            textView.setText("체크 리스트 항목이 선택되지 않았습니다. 뒤로 되돌아가서 체크 리스트를 생성해주세요.");
            layout.addView(textView);

            btn_complete.setVisibility(View.GONE);
            btn_new.setVisibility(View.VISIBLE);
        }



        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
                SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
                Date now = new Date();
                String date = sdf1.format(now);
                String time = sdf2.format(now);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String name = "";
                if(user != null){
                    String user_email = user.getEmail();
                    name = user_email.split("@")[0];
                }else{
                    name = "user";
                }

                Log.i("username", String.valueOf(name));

                Database db = new Database(getActivity());
                db.insertRecord(name, date, time, itemList.toString());


                Intent intent = new Intent(getContext(), LevelMainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ManageItemListActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });


        return v;
        //return inflater.inflate(R.layout.fragment_tab_one, container, false);
    }
}