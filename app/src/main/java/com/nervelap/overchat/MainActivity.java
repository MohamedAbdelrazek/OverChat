package com.nervelap.overchat;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TapsAdapter mTapsAdapter;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mCurrentUser;

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
        //  FirebaseApp.initializeApp(this);
        //    mFirebaseAuth = FirebaseAuth.getInstance();
        //mCurrentUser = mFirebaseAuth.getCurrentUser();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mCurrentUser == null) {

            sendUserToLoginActivity();

        }
    }

    private void sendUserToLoginActivity() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }
}
