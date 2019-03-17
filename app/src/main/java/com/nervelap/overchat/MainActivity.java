package com.nervelap.overchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;


public class MainActivity extends AppCompatActivity {
    Toolbar mToolbar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar=findViewById(R.id.main_activity_app_bar_id);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Over Chat");
    }
}
