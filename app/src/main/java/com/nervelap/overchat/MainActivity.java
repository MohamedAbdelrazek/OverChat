package com.nervelap.overchat;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;


public class MainActivity extends AppCompatActivity {
    Toolbar mToolbar;
    ViewPager mViewPager;
    TabLayout mTabLayout;
    TapsAdapter mTapsAdapter;

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

    }
}
