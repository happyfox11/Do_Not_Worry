package com.android.myappproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.myappproject.R;
import com.android.myappproject.activity.LevelMainActivity;
import com.android.myappproject.activity.ManageItemListActivity;

import java.util.ArrayList;

public class TabOneFragment extends Fragment
{
    private ArrayList<String> itemList;
    private Button btn_complete;
    private Button btn_new;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_tab_one, container, false);

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