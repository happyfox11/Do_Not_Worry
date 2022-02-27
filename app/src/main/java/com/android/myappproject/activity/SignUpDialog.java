package com.android.myappproject.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.myappproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class SignUpDialog extends Dialog
{
	private Activity activity;
	private EditText ev_signup_email, ev_signup_pass;
	private Button btn_signup_confirm, btn_signup_cancel;

	private FirebaseAuth firebaseAuth;

	public SignUpDialog(@NonNull Context context)
	{
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		this.activity = (Activity) context;//부모 액티비티를 받아온다
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		try{

			setContentView(R.layout.dialog_signup);
			init();
			setting();
			addListener();

		}catch(Exception ex){
			Log.e(this.getClass().getName(), ex.getMessage(), ex);
		}
	}

	private void init(){
		firebaseAuth = FirebaseAuth.getInstance();

		ev_signup_email = findViewById(R.id.ev_signup_email);
		ev_signup_pass = findViewById(R.id.ev_signup_pass);
		btn_signup_confirm = findViewById(R.id.btn_signup_confirm);
		btn_signup_cancel = findViewById(R.id.btn_signup_cancel);
	}

	private void setting(){

	}

	private void addListener(){
		btn_signup_confirm.setOnClickListener(listener_signup_confirm);
		btn_signup_cancel.setOnClickListener(listener_signup_cancel);
	}

	private View.OnClickListener listener_signup_confirm = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			String email = ev_signup_email.getText().toString();
			String passwd= ev_signup_pass.getText().toString();
			createAccount(email, passwd);
		}
	};

	private View.OnClickListener listener_signup_cancel = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			dismiss();
		}
	};

	private void createAccount(String email, String passwd){
		if(email.isEmpty() || passwd.isEmpty()){
			Toast.makeText(activity, "email or password: invalid", Toast.LENGTH_SHORT).show();
		}else{
			firebaseAuth.createUserWithEmailAndPassword(email, passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>()
			{
				@Override
				public void onComplete(@NonNull Task<AuthResult> task)
				{
					if(task.isSuccessful()){
						Log.i(activity.getClass().getName(), "register : successful");
						Toast.makeText(activity, "register : successful", Toast.LENGTH_SHORT).show();

						dismiss();
					}else{
						try{
							throw task.getException();
						}
						catch(FirebaseAuthWeakPasswordException fpEx){
							Toast.makeText(activity, "weak password", Toast.LENGTH_SHORT).show();
						}
						catch(FirebaseAuthUserCollisionException facEx){
							Toast.makeText(activity, "user already exists", Toast.LENGTH_SHORT).show();
						}
						catch(Exception ex){
							Toast.makeText(activity, "register : fail", Toast.LENGTH_SHORT).show();
						}

						Log.w(activity.getClass().getName(), "register : fail", task.getException());

					}
				}
			});
		}
	}
}
