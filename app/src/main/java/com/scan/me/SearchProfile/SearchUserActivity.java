package com.scan.me.SearchProfile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scan.me.EnteryScreen.Blog;
import com.scan.me.EnteryScreen.EntryActivity;
import com.scan.me.R;
import com.scan.me.SearchScreen.SearchActivity;
import com.scan.me.ShowQrCode.AlreadySubmit;
import com.scan.me.SingleEvent.SingleEvent;
import com.squareup.picasso.Picasso;

public class SearchUserActivity extends AppCompatActivity {

    String message;
    EditText mSearch;
    private RecyclerView mBlogList;
    String UID,US, NAM,IMG,US2, NAM2,IMG2;
    private DatabaseReference mDatabase, mDatabaseShow;
    private FirebaseAuth mAuth;
    ImageView mProfileImage, back, follow;
    TextView mNameUser, mUserName, mFollower, mFollowing;
    private boolean mProcess = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        mSearch = (EditText) findViewById(R.id.search_user);
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchUserActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });
        // get user account
        Bundle bundle = getIntent().getExtras();
        message = bundle.getString("message");

        mAuth = FirebaseAuth.getInstance();
        UID = message.replace(".","&");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabaseShow = FirebaseDatabase.getInstance().getReference().child("Users").child(UID).child("Posts");
        mDatabaseShow.keepSynced(true);
        mDatabase.keepSynced(true);

        mBlogList = (RecyclerView) findViewById(R.id.posts);
        mBlogList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mBlogList.setLayoutManager(layoutManager);


        follow = (ImageView) findViewById(R.id.follow);
        back = (ImageView) findViewById(R.id.back);
        mProfileImage = (ImageView) findViewById(R.id.profile_picture);
        mNameUser = (TextView) findViewById(R.id.name_profile);
        mUserName = (TextView) findViewById(R.id.name_user);
        mFollower = (TextView) findViewById(R.id.followerNumber);
        mFollowing = (TextView) findViewById(R.id.followingNumber);


        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Users").child(UID).child("Follower").child(mAuth.getCurrentUser().getEmail().toString().replace(".","&")).exists()){
                    follow.setImageResource(R.drawable.ic_check_box_black_24dp);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.child("Users").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                US = (String) dataSnapshot.child("UserName").getValue();
                NAM = (String) dataSnapshot.child("Name").getValue();
                IMG = (String) dataSnapshot.child("ProfilePicture").getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.child("Users").child(mAuth.getCurrentUser().getEmail().replace(".","&")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                US2 = (String) dataSnapshot.child("UserName").getValue();
                NAM2 = (String) dataSnapshot.child("Name").getValue();
                IMG2 = (String) dataSnapshot.child("ProfilePicture").getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("Users").child(UID).child("Follower").child(mAuth.getCurrentUser().getEmail().toString().replace(".","&")).exists()){
                            follow.setImageResource(R.drawable.ic_person_add_black_24dp);
                            DatabaseReference databaseReference = mDatabase.child("Users").child(UID);
                            databaseReference.child("Follower").child(mAuth.getCurrentUser().getEmail().toString().replace(".","&")).removeValue();
                            DatabaseReference databaseReference1 = mDatabase.child("Users").child(mAuth.getCurrentUser().getEmail().toString().replace(".","&")).child("Following");
                            databaseReference1.child(UID).removeValue();
                            return;
                        }else{
                            follow.setImageResource(R.drawable.ic_check_box_black_24dp);
                            mDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    DatabaseReference databaseReference = mDatabase.child("Users").child(UID).child("Follower").child(mAuth.getCurrentUser().getEmail().toString().replace(".","&"));
                                    databaseReference.child("email").setValue(mAuth.getCurrentUser().getEmail());
                                    databaseReference.child("name").setValue(NAM2);
                                    databaseReference.child("username").setValue(US2);
                                    databaseReference.child("image").setValue(IMG2);
                                    DatabaseReference databaseReference1 = mDatabase.child("Users").child(mAuth.getCurrentUser().getEmail().toString().replace(".","&")).child("Following").child(UID);
                                    databaseReference1.child("email").setValue(UID.replace("&","."));
                                    databaseReference1.child("username").setValue(US);
                                    databaseReference1.child("name").setValue(NAM);
                                    databaseReference1.child("image").setValue(IMG);

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        mDatabase.child("Users").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nam = (String) dataSnapshot.child("Name").getValue();
                String us = (String) dataSnapshot.child("UserName").getValue();
                long follower =  dataSnapshot.child("Follower").getChildrenCount();
                long following = dataSnapshot.child("Following").getChildrenCount();
                String image = (String) dataSnapshot.child("ProfilePicture").getValue();
                if(!nam.equals("null")){
                    mNameUser.setText(nam);
                }
                if(!image.equals("null")){
                    Picasso.with(SearchUserActivity.this).load(image).into(mProfileImage);
                }
                mFollower.setText(String.valueOf(follower));
                mFollowing.setText(String.valueOf(following));
                mUserName.setText(us);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SearchUserActivity.this,EntryActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Blog, SearchUserActivity.BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter
                <Blog, SearchUserActivity.BlogViewHolder>(
                Blog.class,
                R.layout.event_row,
                SearchUserActivity.BlogViewHolder.class,
                mDatabaseShow
        ){
            @Override
            protected void populateViewHolder(final SearchUserActivity.BlogViewHolder viewHolder, final Blog model, int position) {
                final String post_key = UID+"#"+getRef(position).getKey();
                final String post = getRef(position).getKey();
                viewHolder.setName(model.getName());
                viewHolder.setUsname(model.getUsname());
                viewHolder.setUserame(model.getUsername());
                viewHolder.setDate(model.getDate());
                viewHolder.setDetails(model.getDetails());
                viewHolder.setAddress(model.getAddress());
                viewHolder.setImage(getApplicationContext(), model.getImage());
                viewHolder.set_like_icon(post);
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                boolean check = dataSnapshot.child("Users").child(UID).child("Posts").child(post).child("Submit").child(mAuth.getCurrentUser().getEmail().toString().replace(".","&")).exists();
                                if (check){
                                    String qr = (String) dataSnapshot.child("Users").child(UID).child("Posts").child(post).child("Submit").child(mAuth.getCurrentUser().getEmail().toString().replace(".","&")).child("Qr").getValue();
                                    Intent single = new Intent(SearchUserActivity.this, AlreadySubmit.class);
                                    single.putExtra("qr",qr);
                                    startActivity(single);
                                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                                }else{
                                    Intent single = new Intent(SearchUserActivity.this, SingleEvent.class);
                                    single.putExtra("key",post_key);
                                    startActivity(single);
                                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });
                viewHolder.mDatabaseLike.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        viewHolder.count.setText((String.valueOf(dataSnapshot.child(post).getChildrenCount()))+" Likes");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                viewHolder.mLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mProcess = true;

                        viewHolder.mDatabaseLike.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(mProcess) {
                                    if (dataSnapshot.child(post).hasChild((mAuth.getCurrentUser().getUid()))) {
                                        viewHolder.mDatabaseLike.child(post).child(mAuth.getCurrentUser().getUid()).removeValue();
                                        mProcess = false;
                                    } else {
                                        viewHolder.mDatabaseLike.child(post).child(mAuth.getCurrentUser().getUid()).setValue(mAuth.getCurrentUser().getEmail());
                                        mProcess = false;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }
        };
        mBlogList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View mView;

        DatabaseReference mDatabaseLike;
        FirebaseAuth mAuth;
        LinearLayout mLike;
        ImageView mDelete, mLikebtn;
        TextView count;
        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mLike = (LinearLayout) mView.findViewById(R.id.like);
            mDelete = (ImageView) mView.findViewById(R.id.deletePost);
            mDelete.setVisibility(View.INVISIBLE);
            mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
            mDatabaseLike.keepSynced(true);
            mAuth = FirebaseAuth.getInstance();
            mLikebtn = (ImageView) mView.findViewById(R.id.likeImage);
            count = (TextView) mView.findViewById(R.id.likeNumber);
        }

        public void set_like_icon(final String post_key){
            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())){
                        mLikebtn.setImageResource(R.drawable.ic_favorite_black_24dp);
                    }else{
                        mLikebtn.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public void setName(String _eName){
            TextView mName = (TextView) mView.findViewById(R.id.eventName);
            mName.setText(_eName);
        }

        public void setUsname(String _eUsername){
            TextView mName = (TextView) mView.findViewById(R.id.nameUserAccount);
            mName.setText(_eUsername);
        }

        public void setUserame(String _eUsername){
            TextView mName = (TextView) mView.findViewById(R.id.publisherID);
            mName.setText(_eUsername);
        }

        public void setDate(String date){
            TextView mDate = (TextView) mView.findViewById(R.id.eventDate);
            mDate.setText(date);
        }

        public void setDetails(String details){
            TextView mDetails = (TextView) mView.findViewById(R.id.eventDesc);
            mDetails.setText(details);
        }

        public void setAddress(String address){
            TextView mAddress = (TextView) mView.findViewById(R.id.eventAddress);
            mAddress.setText(address);
        }

        public void setImage(Context ctx, final String image){
            ImageView image_post = (ImageView) mView.findViewById(R.id.event_pic);
            Picasso.with(ctx).load(image).into(image_post);
        }
    }
}
