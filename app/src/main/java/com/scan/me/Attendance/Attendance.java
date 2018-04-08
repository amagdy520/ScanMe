package com.scan.me.Attendance;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scan.me.Attendance.UserAttendance.UserAttendance;
import com.scan.me.EnteryScreen.EntryActivity;
import com.scan.me.Follow.FollowAdapter;
import com.scan.me.Follow.Follower.FollowerActivity;
import com.scan.me.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;

public class Attendance extends AppCompatActivity {


    String UID;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    RecyclerView mDateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        mAuth = FirebaseAuth.getInstance();
        UID = mAuth.getCurrentUser().getEmail().replace(".","&");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(UID).child("Submission");
        mDatabase.keepSynced(true);


        mDateList = (RecyclerView) findViewById(R.id.mDate);
        mDateList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mDateList.setLayoutManager(layoutManager);

    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<DateAdapter, Attendance.BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter
                <DateAdapter, Attendance.BlogViewHolder>(
                DateAdapter.class,
                R.layout.attend_row,
                Attendance.BlogViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(final Attendance.BlogViewHolder viewHolder, final DateAdapter model, int position) {
                final String post = getRef(position).getKey();
                viewHolder.setDate(model.getDate());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        Intent intent = new Intent(Attendance.this, UserAttendance.class);
                        intent.putExtra("date",post);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        };
        mDateList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setDate(String _eName){
            TextView mName = (TextView) mView.findViewById(R.id._date);
            mName.setText(_eName);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Attendance.this, EntryActivity.class);
        startActivity(intent);
        finish();
    }
}
