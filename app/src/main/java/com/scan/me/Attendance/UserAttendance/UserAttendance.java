package com.scan.me.Attendance.UserAttendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scan.me.Attendance.Attendance;
import com.scan.me.Attendance.DateAdapter;
import com.scan.me.EnteryScreen.EntryActivity;
import com.scan.me.R;

public class UserAttendance extends AppCompatActivity {

    String date, UID;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    RecyclerView mDateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_attendance);
        date = getIntent().getExtras().getString("date");
        mAuth = FirebaseAuth.getInstance();
        UID = mAuth.getCurrentUser().getEmail().replace(".", "&");
        Toast.makeText(this,date,Toast.LENGTH_SHORT).show();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(UID).child("Submission").child(date).child("users");
        mDatabase.keepSynced(true);


        mDateList = (RecyclerView) findViewById(R.id.users);
        mDateList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mDateList.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<UserAttendanceAdapter, UserAttendance.BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter
                <UserAttendanceAdapter, UserAttendance.BlogViewHolder>(
                UserAttendanceAdapter.class,
                R.layout.user_attend_row,
                UserAttendance.BlogViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(final UserAttendance.BlogViewHolder viewHolder, final UserAttendanceAdapter model, int position) {
                viewHolder.setName(model.getName());
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

        public void setName(String _eName) {
            TextView mName = (TextView) mView.findViewById(R.id._name);
            mName.setText(_eName);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UserAttendance.this, Attendance.class);
        startActivity(intent);
        finish();
    }
}
