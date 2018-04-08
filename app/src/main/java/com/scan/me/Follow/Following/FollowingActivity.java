package com.scan.me.Follow.Following;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.scan.me.EnteryScreen.EntryActivity;
import com.scan.me.Follow.FollowAdapter;
import com.scan.me.R;
import com.squareup.picasso.Picasso;

public class FollowingActivity extends AppCompatActivity {

    String UID;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    RecyclerView mFollowList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower);
        mAuth = FirebaseAuth.getInstance();
        UID = mAuth.getCurrentUser().getEmail().replace(".","&");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(UID).child("Following");
        mDatabase.keepSynced(true);


        mFollowList = (RecyclerView) findViewById(R.id.followerList);
        mFollowList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mFollowList.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<FollowAdapter, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter
                <FollowAdapter, FollowingActivity.BlogViewHolder>(
                FollowAdapter.class,
                R.layout.follow_row,
                FollowingActivity.BlogViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(final FollowingActivity.BlogViewHolder viewHolder, final FollowAdapter model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setUsername(model.getUsername());
                viewHolder.setImage(getApplicationContext(), model.getImage());
            }
        };
        mFollowList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setName(String _eName){
            TextView mName = (TextView) mView.findViewById(R.id._fName);
            mName.setText(_eName);
        }

        public void setUsername(String _eUsername){
            TextView mName = (TextView) mView.findViewById(R.id._fUsername);
            mName.setText(_eUsername);
        }

        public void setImage(Context ctx, final String image){
            ImageView image_post = (ImageView) mView.findViewById(R.id._fProfile);
            Picasso.with(ctx).load(image).into(image_post);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FollowingActivity.this, EntryActivity.class);
        startActivity(intent);
        finish();
    }
}
