package com.scan.me.EnteryScreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scan.me.Attendance.Attendance;
import com.scan.me.CreateScreen.CreateActivity;
import com.scan.me.Follow.Follower.FollowerActivity;
import com.scan.me.Follow.Following.FollowingActivity;
import com.scan.me.R;
import com.scan.me.ScanToSendData.SubmitData;
import com.scan.me.SearchScreen.SearchActivity;
import com.scan.me.ShowQrCode.DialogCode;
import com.scan.me.StartScreen.StartScreen;
import com.scan.me.UserProfile.Setting.SettingActivity;
import com.scan.me.UserProfile.UserProfile;
import com.squareup.picasso.Picasso;

import static java.lang.System.exit;


public class EntryActivity extends Activity {


    ImageView mProfile;
    LinearLayout  mScanSend, mProfileFragment, mCode, mCreate, mSetting, mLogOut, mSearch, mFollower, mFollowing, mAttend;
    TextView mUserName;
    String UID, US, STR;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup_activity);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        UID = mAuth.getCurrentUser().getEmail().toString().replace(".","&");
        STR = UID;
        mSearch = (LinearLayout) findViewById(R.id.search_fragment);
        mProfile = (ImageView) findViewById(R.id.profile_image_drawer);
        mUserName= (TextView) findViewById(R.id.username_drawer);
        mDatabase.child("Users").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String us = (String) dataSnapshot.child("UserName").getValue();
                String image = (String) dataSnapshot.child("ProfilePicture").getValue();
                if(!image.equals("null")){
                    Picasso.with(EntryActivity.this).load(image).into(mProfile);
                }
                mUserName.setText(us);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mScanSend = (LinearLayout) findViewById(R.id.sendData_fragment);
        mProfileFragment = (LinearLayout) findViewById(R.id.profile_fragment);
        mCode = (LinearLayout) findViewById(R.id.myCode_fragment);
        mCreate = (LinearLayout) findViewById(R.id.create_fragment);
        mSetting = (LinearLayout) findViewById(R.id.setting_fragment);
        mLogOut = (LinearLayout) findViewById(R.id.logout_fragment);
        mAttend = (LinearLayout) findViewById(R.id.attend_fragment);
        mFollower = (LinearLayout) findViewById(R.id.follower_fragment);
        mFollowing = (LinearLayout) findViewById(R.id.following_fragment);

        mDatabase.child("Users").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                US = (String) dataSnapshot.child("UserName").getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EntryActivity.this,SearchActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(EntryActivity.this,StartScreen.class);
                startActivity(intent);
                finish();
            }
        });
        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EntryActivity.this,CreateActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mProfileFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EntryActivity.this,UserProfile.class);
                startActivity(intent);
                finish();
            }
        });
        mSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EntryActivity.this,SettingActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mScanSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent single = new Intent(EntryActivity.this, SubmitData.class);
                startActivity(single);
                finish();
            }
        });
        mCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogCode alert = new DialogCode();
                alert.showDialog(EntryActivity.this);
            }
        });

        mFollower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent single = new Intent(EntryActivity.this, FollowerActivity.class);
                startActivity(single);
                finish();
            }
        });

        mFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent single = new Intent(EntryActivity.this, FollowingActivity.class);
                startActivity(single);
                finish();
            }
        });
        mAttend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent single = new Intent(EntryActivity.this, Attendance.class);
                startActivity(single);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exit(0);
    }
}
