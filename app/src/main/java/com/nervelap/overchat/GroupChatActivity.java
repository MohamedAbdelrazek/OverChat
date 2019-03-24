package com.nervelap.overchat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class GroupChatActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageButton mSendMessageButton;
    private EditText mMessageEditText;
    private TextView mDisplayMessagesTextView;
    private ScrollView mMessagesScrollView;
    private String mGroupName;
    private String mUserName;
    private String mUserID;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mUserRef;
    private DatabaseReference mGroupRef;
    private String mCurrentDate;
    private String mCurrentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mUserID = mFirebaseAuth.getCurrentUser().getUid();
        mGroupName = getIntent().getExtras().get("GroupName").toString();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mGroupRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(mGroupName);
        mToolbar = findViewById(R.id.group_chat_app_bar_id);
        setSupportActionBar(mToolbar);
        mSendMessageButton = findViewById(R.id.send_group_message_btn);
        mMessageEditText = findViewById(R.id.edit_txt_group_message);
        mDisplayMessagesTextView = findViewById(R.id.group_chat_text_view_id);
        mMessagesScrollView = findViewById(R.id.messages_scroll_view);

        getSupportActionBar().setTitle(mGroupName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mUserRef.child(mUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    mUserName = dataSnapshot.child("name").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mSendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageTotheGroup();
            }
        });
    }

    private void sendMessageTotheGroup() {
        String message = mMessageEditText.getText().toString();
        if (TextUtils.isEmpty(message)) {

        } else {

            mCurrentTime = getTime();
            mCurrentDate = getDate();

            Map<String, String> map = new HashMap<>();
            map.put("message", message);
            map.put("username", mUserName);
            map.put("date", mCurrentDate);
            map.put("time", mCurrentTime);
            mGroupRef.push().setValue(map);
            mMessageEditText.setText("");
        }
    }

    private String getDate() {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
        return dateformat.format(c.getTime());
    }

    private String getTime() {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("hh:mm:ss aa");
        return dateformat.format(c.getTime());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
