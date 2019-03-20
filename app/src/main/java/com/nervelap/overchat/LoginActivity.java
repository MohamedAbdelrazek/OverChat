package com.nervelap.overchat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText mEmailAddress, mPassword;
    private Button mLogin, mPhoneLogin;
    private TextView mCreateNewAccount, mForgetPassword;
    private FirebaseAuth mFirebaseAuth;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mEmailAddress = findViewById(R.id.email_id);
        mPassword = findViewById(R.id.password_id);
        mLogin = findViewById(R.id.submit_btn_id);
        mPhoneLogin = findViewById(R.id.login_by_phone_btn_id);
        mCreateNewAccount = findViewById(R.id.new_account_txt_id);
        mForgetPassword = findViewById(R.id._login_by_phone_txt_id);
        mProgressDialog = new ProgressDialog(this);
        mCreateNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntentActivity = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(registerIntentActivity);
            }
        });
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(mEmailAddress.getText().toString(), mPassword.getText().toString());
            }
        });
    }

    private void login(String email, String password) {
        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter Email... ", Toast.LENGTH_SHORT).show();
        } else {
            mProgressDialog.setTitle("Sign In");
            mProgressDialog.setMessage("Please wait....");
            mProgressDialog.setCanceledOnTouchOutside(true);
            mProgressDialog.show();
            mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        mProgressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Successfully logged in !", Toast.LENGTH_SHORT).show();
                        sendUserToMainActivity();
                    } else {
                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                    }
                }
            });

        }
    }

    private void sendUserToMainActivity() {
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

}