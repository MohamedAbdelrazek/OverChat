package com.nervelap.overchat;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TapsAdapter mTapsAdapter;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDataBaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = findViewById(R.id.main_activity_app_bar_id);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Over Chat");
        mViewPager = findViewById(R.id.main_tabs_pager);
        mTabLayout = findViewById(R.id.main_tab_id);
        mTapsAdapter = new TapsAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mTapsAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        FirebaseApp.initializeApp(this);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mCurrentUser = mFirebaseAuth.getCurrentUser();
        mDataBaseRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mCurrentUser == null) {
            sendUserToLoginActivity();
        } else
            verifyUserInfo();
    }

    //the main purpose of that Fun is to make sure that the user set his/her name and not empty ...
    private void verifyUserInfo() {
        String currentUserId = mFirebaseAuth.getCurrentUser().getUid();
        mDataBaseRef.child("Users").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("name").exists()) {
                    Toast.makeText(MainActivity.this, "Welcome !", Toast.LENGTH_SHORT).show();
                } else {
                    sendUserToSettingActivity();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });


    }

    private void sendUserToLoginActivity() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    private void sendUserToSettingActivity() {
        Intent settingIntent = new Intent(getApplicationContext(), SettingActivity.class);
        settingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(settingIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.menu_find_friend_id) {

        } else if (item.getItemId() == R.id.menu_log_out_id) {
            mFirebaseAuth.signOut();
            sendUserToLoginActivity();

        } else if (item.getItemId() == R.id.menu_settings_id) {

            startActivity(new Intent(getApplicationContext(), SettingActivity.class));

        } else if (item.getItemId() == R.id.menu_create_group_id) {

            requstNewGroup();

        }
        return true;
    }

    private void requstNewGroup() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.AlertDialog);
        alertDialog.setTitle("Enter Group Name :");
        final EditText groupName = new EditText(this);
        groupName.setHint("e.g IT Jobs");
        alertDialog.setView(groupName);
        alertDialog.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String gName = groupName.getText().toString();
                if (TextUtils.isEmpty(gName)) {
                    Toast.makeText(MainActivity.this, "Please Enter the Group Name !", Toast.LENGTH_SHORT).show();
                } else {

                    createNewGroup(gName);
                }
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });


        alertDialog.show();
    }

    private void createNewGroup(String gName) {

        mDataBaseRef.child("Groups").child(gName).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Group Created successfully !", Toast.LENGTH_SHORT).show();
                } else {
                }

            }
        });
    }


}