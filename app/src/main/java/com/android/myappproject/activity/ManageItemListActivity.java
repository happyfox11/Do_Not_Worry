package com.android.myappproject.activity;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


import com.android.myappproject.R;
import com.android.myappproject.fragment.BarFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ManageItemListActivity extends AppCompatActivity
{
    private Activity activity;

    //private ImageButton btn_back;

    private EditText et_array_item;

    private Button btn_array_item_add, btn_array_item_del, btn_clear;

    private ListView lv_array;

    private ArrayAdapter arrayAdapter;

    private List<String> itemList = new ArrayList<String>(Arrays.asList("드라이기","가스레인지", "전기장판", "현관문", "과제 제출"));
    private ArrayList<String> sendList;
    private Button btn_make_list;

    private Fragment barFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            setContentView(R.layout.activity_manage_item_list);

            init();

            setting();

            addListener();
        }
        catch (Exception ex)
        {
            //LogService.error(this, ex.getMessage(), ex);
        }
    }

    private void init()
    {
        activity = this;

        et_array_item = findViewById(R.id.et_array_item);

        //btn_back = findViewById(R.id.btn_array_back);

        btn_array_item_add = findViewById(R.id.btn_array_item_add);
        btn_clear = findViewById(R.id.btn_clear);


        btn_array_item_del = findViewById(R.id.btn_array_item_del);

        lv_array = findViewById(R.id.lv_array);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, itemList);

        btn_make_list = findViewById(R.id.btn_make_list);

        sendList = new ArrayList<String>();

        barFragment = new BarFragment();
    }

    private void setting()
    {
        lv_array.setAdapter(arrayAdapter);

        lv_array.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_fragment, barFragment).commit();
    }

    private void addListener()
    {
        //btn_back.setOnClickListener(listener_back);

        btn_array_item_add.setOnClickListener(listener_item_add);
        btn_clear.setOnClickListener(listener_clear);

        btn_array_item_del.setOnClickListener(listener_item_del);

        lv_array.setOnItemClickListener(listener_item_click);
        btn_make_list.setOnClickListener(listener_make_list);

    }

    private AdapterView.OnItemClickListener listener_item_click = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            String item = itemList.get(position);
            Toast.makeText(activity, position + " : " + item, Toast.LENGTH_SHORT).show();
        }
    };

/*    private View.OnClickListener listener_back = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            finish();
        }
    };*/

    private View.OnClickListener listener_clear = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            et_array_item.setText("");
        }
    };

    private View.OnClickListener listener_item_add = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            String item = et_array_item.getText().toString();

            if(item.equals(""))
            {
                Toast.makeText(activity, "추가할 아이템 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
            }

            itemList.add(item);
            et_array_item.setText("");
            arrayAdapter.notifyDataSetChanged();
        }
    };


    private View.OnClickListener listener_item_del = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            SparseBooleanArray checkedItems = lv_array.getCheckedItemPositions();
            int count = arrayAdapter.getCount() ;

            for (int i = count-1; i >= 0; i--) {
                if (checkedItems.get(i)) {
                    itemList.remove(i) ;
                }
            }

            lv_array.clearChoices() ;

            arrayAdapter.notifyDataSetChanged();
        }
    };

    private View.OnClickListener listener_make_list = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SparseBooleanArray checkedItems = lv_array.getCheckedItemPositions();
            int count = arrayAdapter.getCount() ;

            for (int i = count-1; i >= 0; i--) {
                if (checkedItems.get(i)) {
                    sendList.add(itemList.get(i));
                }
            }

            Intent intent = new Intent(activity, SelectLevelActivity.class);
            intent.putStringArrayListExtra("itemList", sendList);
            startActivity(intent);

            sendList.clear();

            finish();
        }
    };
}