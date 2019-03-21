package com.nervelap.overchat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {
    private Button mUpdateProfileSetting;
    private EditText mUsername, mUserSates;
    private CircleImageView mProfilePic;
    private String mCurrentUid;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDataBaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDataBaseRef = FirebaseDatabase.getInstance().getReference();
        mCurrentUid = mFirebaseAuth.getCurrentUser().getUid();
        mUsername = findViewById(R.id.user_name_id);
        mUserSates = findViewById(R.id.user_profile_status);
        mUpdateProfileSetting = findViewById(R.id.update_btn_id);
        mProfilePic = findViewById(R.id.profile_image_id);
        mUpdateProfileSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInfo();
            }


        });
    }

    private void updateUserInfo() {
        String username = mUsername.getText().toString();
        String userstates = mUserSates.getText().toString();
        if (TextUtils.isEmpty(username) && TextUtils.isEmpty(userstates)) {
            Toast.makeText(this, "please Write your username and your status ! ", Toast.LENGTH_SHORT).show();

        } else {
            HashMap<String, String> profileMap = new HashMap<>();
            profileMap.put("uid", mCurrentUid);
            profileMap.put("name", username);
            profileMap.put("states", userstates);
            mDataBaseRef.child("Users").child(mCurrentUid).setValue(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(SettingActivity.this, "Profile updated Successfully !", Toast.LENGTH_SHORT).show();
                        sendUserToMainActivity();
                    } else {
                        String message = task.getException().getMessage();

                        Toast.makeText(SettingActivity.this, message, Toast.LENGTH_LONG).show();

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
