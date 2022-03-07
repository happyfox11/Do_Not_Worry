package com.android.myappproject.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.android.myappproject.R;
import com.android.myappproject.activity.LevelMainActivity;
import com.android.myappproject.activity.ManageItemListActivity;
import com.android.myappproject.db.Database;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


public class TabThreeFragment extends Fragment
{
    private ArrayList<String> itemList;
    private Button btn_complete;
    private Button btn_new3;
    private Button btn_camera;
    private TimerFragment timerFragment;

    int tag= 0;
    private Boolean[] flag;

    private final int REQUEST_WRITE_EXTERNAL_STORAGE = 1005;

    private Uri photoUri;

    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultCallback<ActivityResult> cameraResultCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_three, container, false);

        timerFragment = new TimerFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_timerfragment, timerFragment).commit();

        LinearLayout layout = v.findViewById(R.id.layout_list);

        btn_new3 = v.findViewById(R.id.btn_new3);
        btn_complete = v.findViewById(R.id.btn_complete);

        Bundle bundle = getArguments();
        itemList = bundle.getStringArrayList("itemList");
        Log.i("frag1", ":" + (itemList.size()));

        flag = new Boolean[itemList.size()];
        Arrays.fill(flag,false);

        if (itemList.size() > 0) {
            for (int i = 0; i < itemList.size(); i++) {
                LinearLayout set = new LinearLayout(getContext());
                set.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                set.setOrientation(LinearLayout.VERTICAL);

                LinearLayout r1 = new LinearLayout(getContext());
                r1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                r1.setOrientation(LinearLayout.HORIZONTAL);
                r1.setBackgroundColor(Color.WHITE);

                LinearLayout r2 = new LinearLayout(getContext());
                r2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 400));
                r2.setOrientation(LinearLayout.HORIZONTAL);
                //r2.setBackgroundColor(Color.GREEN);

                LinearLayout l1 = new LinearLayout(getContext());
                l1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 4f));
                l1.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout l2 = new LinearLayout(getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,6f);
                l2.setGravity(Gravity.RIGHT);
                l2.setLayoutParams(params);
                l2.setOrientation(LinearLayout.HORIZONTAL);


                TextView tv = new TextView(getContext());
                tv.setText(" ★ "+itemList.get(i));

                ImageView img_photo = new ImageView(getContext());
                img_photo.setTag("img_photo"+i);

                btn_camera = new Button(getContext());
                btn_camera.setTag(i);
                Log.i("photo", "Button New tag:"+btn_camera.getTag());

                btn_camera.setText("사진 찍기");
                btn_camera.setBackgroundColor(Color.rgb(40,67,8));
                btn_camera.setTextColor(Color.WHITE);


                l1.addView(tv);
                l2.addView(btn_camera);

                r1.addView(l1);
                r1.addView(l2);
                r2.addView(img_photo);

                set.addView(r1);
                set.addView(r2);

                layout.addView(set);

                Log.i("photo", "Button find tag: btn_camera0"+v.findViewWithTag(0));
                Log.i("photo", "Button find tag: btn_camera1"+v.findViewWithTag(1));

                v.findViewWithTag(btn_camera.getTag()).setOnClickListener(listener_call_camera);
                //btn_camera.setOnClickListener(listener_call_camera);
                ActivityResultCallback<ActivityResult> cameraResultCallback = new ActivityResultCallback<ActivityResult>()
                {
                    @Override
                    public void onActivityResult(ActivityResult result)
                    {
                        //if(result.getResultCode() == RESULT_OK)
                        //img_photo.setImageURI(photoUri);
                        ImageView imageView = v.findViewWithTag("img_photo"+tag);
                        imageView.setImageURI(photoUri);
                        Log.i("photo", "setImage tag:"+tag);
                        Log.i("imageView.getWidth()", String.valueOf(imageView.getWidth()));


                        /*if(imageView.getWidth() > 0){
                            flag[tag] = true;
                        }*/
                    }
                };
                cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), cameraResultCallback);

            }
        } else {
            TextView textView = new TextView(getContext());
            textView.setText("체크 리스트 항목이 선택되지 않았습니다. 뒤로 되돌아가서 체크 리스트를 생성해주세요.");
            layout.addView(textView);

            btn_complete.setVisibility(View.GONE);
            btn_new3.setVisibility(View.VISIBLE);
        }


        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean next = true;

/*                for(int f= 0; f<itemList.size(); f++){
                    Log.i("flag", flag[f]+","+f);
                    if(flag[f] == false){
                        next = false;
                       break;
                    }
                }*/

                for(int f=0 ;f<=tag; f++){
                    ImageView imageView = v.findViewWithTag("img_photo"+tag);

                    if(imageView.getWidth() <= 0){
                        next = false;
                        break;
                    }
                }

                if(next == true){
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
                }else{
                    Toast.makeText(getContext(),"확인되지 않은 항목이 존재합니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_new3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ManageItemListActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });


        return v;
    }


    private final View.OnClickListener listener_call_camera = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {

            tag = (int) view.getTag();
            Log.i("photo", "Button Click tag:"+tag);

            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
                if(
                        getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ){
                    String[] permissions = {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    };
                    requestPermissions(permissions, REQUEST_WRITE_EXTERNAL_STORAGE);
                }else{
                    loadCameraImage();
                }
            }else{
                loadCameraImage();
            }

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0){
            if(requestCode == REQUEST_WRITE_EXTERNAL_STORAGE){
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    loadCameraImage();
                }else{
                    requestUserPermission("외부 저장소 저장");
                }
            }
        }
    }

    private void loadCameraImage(){
        try{
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String tmpFileName = timestamp;

            File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File photoFile = File.createTempFile(tmpFileName, ".jpg", storageDir);

            // LogService.debug(activity, "저장 파일 위치 : "+photoFile.getAbsolutePath());
            Log.i("photo", "저장 파일 위치 : "+photoFile.getAbsolutePath());

            photoUri = FileProvider.getUriForFile(getContext(), "com.android.myappproject",photoFile);
            Log.i("photo", "Url 저장 형태 : "+photoUri);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            cameraLauncher.launch(intent);

        }catch(Exception ex){
            //LogService.error(activity, ex.getMessage(), ex);
            Log.e("photo", ex.getMessage());
        }

    }

    private void requestUserPermission(String perm)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(perm);
        builder.setMessage("작업 처리를 위해 권한이 필요합니다. 권한 설정을 위해 이동하시겠습니까?");

        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent appDetail = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:"+"com.android.myappproject"));
                appDetail.addCategory(Intent.CATEGORY_DEFAULT);
                appDetail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(appDetail);
            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

    }
}