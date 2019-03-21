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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        getUserOldInfo();
    }

    private void getUserOldInfo() {
        mDataBaseRef.child("Users").child(mCurrentUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.hasChild("name") && dataSnapshot.hasChild("image")) {
                    Toast.makeText(SettingActivity.this, "with image !", Toast.LENGTH_SHORT).show();


                    String userName = dataSnapshot.child("name").getValue().toString();
                    String userStatus = dataSnapshot.child("status").getValue().toString();
                    String prfileImgUrl = dataSnapshot.child("image").getValue().toString();
                    setProfileValues(userName, userStatus, prfileImgUrl);

                } else if (dataSnapshot.exists() && dataSnapshot.hasChild("name")) {
                    String userName = dataSnapshot.child("name").getValue().toString();
                    String userStatus = dataSnapshot.child("status").getValue().toString();
                    setProfileValues(userName, userStatus);

                } else {
                    Toast.makeText(SettingActivity.this, "Please you set ur fucken username and status", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setProfileValues(String userName, String userStatus, String prfileImgUrl) {
        mUsername.setText(userName);
        mUserSates.setText(userStatus);

    }

    private void setProfileValues(String userName, String userStatus) {
        mUsername.setText(userName);
        mUserSates.setText(userStatus);

    }

    private void updateUserInfo() {
        String username = mUsername.getText().toString();
        String userstatus = mUserSates.getText().toString();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please update your Name! ", Toast.LENGTH_LONG).show();

        } else if (TextUtils.isEmpty(userstatus)) {
            Toast.makeText(this, "Please update your Status! ", Toast.LENGTH_LONG).show();

        } else {
            HashMap<String, String> profileMap = new HashMap<>();
            profileMap.put("uid", mCurrentUid);
            profileMap.put("name", username);
            profileMap.put("status", userstatus);
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
