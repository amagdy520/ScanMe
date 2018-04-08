package com.scan.me.SingleEvent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scan.me.EnteryScreen.EntryActivity;
import com.scan.me.R;
import com.scan.me.SubmittionScreen.Submittion;
import com.squareup.picasso.Picasso;

import java.util.Arrays;


public class SingleEvent extends AppCompatActivity {

    ImageView mSingle;
    private String post = null;
    private DatabaseReference mDatabase;
    TextView mName,mEmail,mDate,mAddress,mDetails,mUsername;
    String mField;
    Button mButton;
    private String[] mFieldChoose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_event);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        post = getIntent().getExtras().getString("key");
        mField = post;
        mFieldChoose = post.split("#");
        mSingle = (ImageView) findViewById(R.id._eImage);
        mName = (TextView) findViewById(R.id._eName);
        mEmail = (TextView) findViewById(R.id.publisher_email);
        mDate = (TextView) findViewById(R.id._eDate);
        mAddress = (TextView) findViewById(R.id._eAddress);
        mDetails = (TextView) findViewById(R.id._eDetails);
        mUsername = (TextView) findViewById(R.id._UserName);
        mDatabase.child("Users").child(mFieldChoose[0]).child("Posts").child(mFieldChoose[1]).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String image = (String) dataSnapshot.child("image").getValue();
                Picasso.with(SingleEvent.this).load(image).into(mSingle);
                String name = (String) dataSnapshot.child("name").getValue();
                String email = (String) dataSnapshot.child("uid").getValue();
                String date = (String) dataSnapshot.child("date").getValue();
                String address = (String) dataSnapshot.child("address").getValue();
                String username = (String) dataSnapshot.child("username").getValue();
                String detail = (String) dataSnapshot.child("details").getValue();
                mField += (String) dataSnapshot.child("fields").getValue();
                mName.setText(name);
                mDate.setText(date);
                mAddress.setText(address);
                mUsername.setText(username);
                mDetails.setText(detail);
                mEmail.setText(email.replace("@","."));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        mButton = (Button) findViewById(R.id.submit_event);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent submit = new Intent(SingleEvent.this, Submittion.class);
                submit.putExtra("field",mField);
                startActivity(submit);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
}
