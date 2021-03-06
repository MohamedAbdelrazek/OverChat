package com.nervelap.overchat;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private EditText mEmailAddress, mPassword;
    private Button mRegister;
    private TextView mAlreadyHaveAccount;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDataBaseRef;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        FirebaseApp.initializeApp(this);
        mDataBaseRef = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mEmailAddress = findViewById(R.id.registe_email_id);
        mPassword = findViewById(R.id.register_password_id);
        mRegister = findViewById(R.id.register_btn_id);
        mProgressDialog = new ProgressDialog(this);

        mAlreadyHaveAccount = findViewById(R.id.new_account_txt_id);
        mAlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent LoginIntentActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(LoginIntentActivity);
            }
        });


        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount(mEmailAddress.getText().toString(), mPassword.getText().toString());
            }
        });


    }

    private void CreateNewAccount(String email, String password) {

        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter Email... ", Toast.LENGTH_SHORT).show();
        } else {

            mProgressDialog.setTitle("Creating New Account");
            mProgressDialog.setMessage("Please wait, while creating new account for you ! ");
            mProgressDialog.setCanceledOnTouchOutside(true);
            mProgressDialog.show();
            mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        String currentUser = mFirebaseAuth.getCurrentUser().getUid();
                        mDataBaseRef.child("Users").child(currentUser).setValue("registered");
                        Toast.makeText(RegisterActivity.this, "Account Created successfully ! ", Toast.LENGTH_SHORT).show();
                        sendUserToMainActivity();
                        mProgressDialog.dismiss();
                    } else {
                        Toast.makeText(RegisterActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
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
