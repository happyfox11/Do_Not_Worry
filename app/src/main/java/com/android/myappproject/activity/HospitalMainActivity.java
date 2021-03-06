package com.android.myappproject.activity;





import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;


import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.android.myappproject.R;
import com.android.myappproject.adapter.CustomAdapter;
import com.android.myappproject.api.ApiExamSearch;
import com.android.myappproject.api.HospitalSearchApi;
import com.android.myappproject.fragment.BarFragment;
import com.android.myappproject.vo.CustomHospitalVo;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class HospitalMainActivity extends AppCompatActivity {


    private Fragment barFragment;

    private Activity activity;
    private Button btn_region;
    private EditText et_region;

    private ListView lv_custom_item;

    private CustomAdapter customAdapter;

    public static ArrayList<CustomHospitalVo> memberList;
    private  Thread thread;

    // 2022.03.05 ADD hmwoo 병원 조회 수정 작업 START
    private Disposable backgroundtask;

    private ProgressDialog progress;

    private List<CustomHospitalVo> hospitalList;
    // 2022.03.05 ADD hmwoo 병원 조회 수정 작업 END


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try {
            setContentView(R.layout.activity_hospital_main);

            init();
            setting();
            addListener();

        } catch (Exception ex) {

        }
    }

    private void init() {
        barFragment = new BarFragment();

        activity = this;

        lv_custom_item = findViewById(R.id.lv_custom_item);

        memberList = new ArrayList<>();

        btn_region = findViewById(R.id.btn_region);
        et_region = findViewById(R.id.et_region);

        //customAdapter = new CustomAdapter(activity, memberList);

        // 2022.03.05 MOD hmwoo 병원 조회 수정 작업 START
        hospitalList = new ArrayList<>();

        customAdapter = new CustomAdapter(activity, hospitalList);
        // 2022.03.05 MOD hmwoo 병원 조회 수정 작업 END
    }

    private void setting() {
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_fragment, barFragment).commit();

        lv_custom_item.setAdapter(customAdapter);

    }

    private void addListener() {
        btn_region.setOnClickListener(listener_region);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //thread.interrupt();
    }

    private View.OnClickListener listener_region = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //            thread = new Thread() {
            //                public void run() {
            //                    ApiExamSearch api = new ApiExamSearch();
            //                    api.search(String.valueOf(et_region.getText()));
            //                }
            //            };
            //            thread.start();
            //
            //            for(int i=0;i<memberList.size();i++){
            //                customAdapter.addItem(memberList.get(i));
            //            }
            //
            //            memberList.clear();

            // 2022.03.05 MOD hmwoo 병원 조회 수정 작업 START
            asycHospitalData();
        }
    };

    // 2022.03.05 ADD hmwoo 병원 조회 수정 작업 START
    private void asycHospitalData()
    {
        disableUI();

        progress = new ProgressDialog(activity);
        progress.setMessage("병원 데이터 조회 중입니다...");
        progress.show();

        backgroundtask = Observable.fromCallable(new Callable<Boolean>()
        {
            @Override
            public Boolean call() throws Exception
            {
                boolean result = false;

                try
                {
                    HospitalSearchApi hospitalSearchApi = new HospitalSearchApi();
                    hospitalSearchApi.getSearchData(hospitalList, String.valueOf(et_region.getText()));

                    result = true;
                }
                catch (Exception ex)
                {
                    Log.e(activity.getClass().getName(), ex.getMessage());
                }

                return result;
            }
        })
                                   .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Object>()
                {
                    @Override
                    public void accept(Object o) throws Exception
                    {
                        boolean result = (boolean) o;

                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                if(result)
                                {
                                    customAdapter.notifyDataSetChanged();
                                }
                                else
                                {
                                    Toast.makeText(activity, "병원 조회에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                }

                                enableUI();
                            }
                        });

                        backgroundtask.dispose();

                        progress.dismiss();
                    }
                });
    }

    private void disableUI()
    {
        activity.getWindow().getDecorView().setEnabled(false);
    }

    private void enableUI()
    {
        activity.getWindow().getDecorView().setEnabled(true);
    }
    // 2022.03.05 ADD hmwoo 병원 조회 수정 작업 END
}

