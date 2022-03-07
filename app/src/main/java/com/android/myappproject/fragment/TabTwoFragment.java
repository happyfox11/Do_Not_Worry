package com.android.myappproject.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.android.myappproject.R;
import com.android.myappproject.activity.LevelMainActivity;
import com.android.myappproject.activity.ManageItemListActivity;
import com.android.myappproject.db.Database;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TabTwoFragment extends Fragment
{
    private ArrayList<String> itemList;
    private Button btn_complete;
    private Button btn_new2;
    private int i = 0;
    private TimerFragment timerFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_tab_two, container, false);

        timerFragment = new TimerFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_timerfragment, timerFragment).commit();

        btn_new2 = v.findViewById(R.id.btn_new2);
        btn_complete = v.findViewById(R.id.btn_complete);

        LinearLayout layout = v.findViewById(R.id.layout_list);

        Bundle bundle = getArguments();
        itemList= bundle.getStringArrayList("itemList");
        Log.i("frag2", ":"+itemList);

        if(itemList.size() > 0 ){
            for(i=0 ;i<itemList.size(); i++){
                LinearLayout row = new LinearLayout(getContext());
                row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                row.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout l1 = new LinearLayout(getContext());
                l1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                l1.setOrientation(LinearLayout.HORIZONTAL);
                l1.setBackgroundColor(Color.WHITE);

                LinearLayout l2 = new LinearLayout(getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 8f);
                l2.setGravity(Gravity.RIGHT);
                l2.setLayoutParams(params);
                l2.setOrientation(LinearLayout.HORIZONTAL);


                TextView tv = new TextView(getContext());
                tv.setText(" ★ "+itemList.get(i));

                CheckBox cb = new CheckBox(getContext());
                String tag = "cb"+String.valueOf(i);
                cb.setTag(tag);
                cb.setGravity(Gravity.RIGHT);

                l1.addView(tv);
                l2.addView(cb);

                row.addView(l1);
                row.addView(l2);

                layout.addView(row);
            }
        }else{
            TextView textView = new TextView(getContext());
            textView.setText("체크 리스트 항목이 선택되지 않았습니다. 뒤로 되돌아가서 체크 리스트를 생성해주세요.");
            layout.addView(textView);

            btn_complete.setVisibility(View.GONE);
            btn_new2.setVisibility(View.VISIBLE);
        }

        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean flag = true;

                for(int s=0; s<i; s++){
                    String tag = "cb"+s;
                    CheckBox cb = v.findViewWithTag(tag);
                    Log.i("findViewWith Tag", "s="+s+", i="+i+", cb="+cb);

                    if(cb.isChecked() == false){
                        flag = false;
                        Toast.makeText(getContext(),"체크되지 않은 항목이 존재합니다.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                if(flag){
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

            }
        });

        btn_new2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ManageItemListActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });


        return v;

        //return inflater.inflate(R.layout.fragment_tab_two, container, false);
    }
}