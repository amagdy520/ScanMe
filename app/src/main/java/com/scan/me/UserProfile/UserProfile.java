package com.scan.me.UserProfile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.scan.me.CreateScreen.CreateActivity;
import com.scan.me.CustomerSubmittion.CustomerSubmittion;
import com.scan.me.EnteryScreen.Blog;
import com.scan.me.EnteryScreen.EntryActivity;
import com.scan.me.R;
import com.scan.me.SearchProfile.SearchUserActivity;
import com.scan.me.SearchScreen.SearchActivity;
import com.scan.me.SingleEvent.SingleEvent;
import com.scan.me.UserProfile.NewDocument.NewDocument;
import com.scan.me.UserProfile.Setting.SettingActivity;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {

    ImageView mProfileImage;
    TextView mNameUser, mUserName, mFollower, mFollowing;
    String UID, US;
    private DatabaseReference mDatabase, mDatabaseShow;
    private StorageReference mStorage;
    private FirebaseAuth mAuth;
    private RecyclerView mBlogList;
    ImageView mSetting, mNewDocument;
    EditText mSearch;
    private boolean mProcess=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mDefinitions();

        mDatabase.child("Users").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                US = (String) dataSnapshot.child("UserName").getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfile.this,SettingActivity.class);
                startActivity(intent);
            }
        });
        mSearch = (EditText) findViewById(R.id.search_user);
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfile.this,SearchActivity.class);
                startActivity(intent);
            }
        });
        mNewDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfile.this, CreateActivity.class);
                startActivity(intent);
            }
        });
    }
    public void mDefinitions(){
        mAuth = FirebaseAuth.getInstance();
        UID = mAuth.getCurrentUser().getEmail().toString().replace(".","&");
        mStorage = FirebaseStorage.getInstance().getReference();
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
        mProfileImage = (ImageView) findViewById(R.id.profile_picture);
        mNameUser = (TextView) findViewById(R.id.name_profile);
        mUserName = (TextView) findViewById(R.id.name_user);
        mFollower = (TextView) findViewById(R.id.followerNumber);
        mFollowing = (TextView) findViewById(R.id.followingNumber);
        mSetting = (ImageView) findViewById(R.id.settingButton);
        mNewDocument = (ImageView) findViewById(R.id.newDocument);


        mDatabase.child("Users").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nam = (String) dataSnapshot.child("Name").getValue();
                String us = (String) dataSnapshot.child("UserName").getValue();
                long follower = dataSnapshot.child("Follower").getChildrenCount();
                long following = dataSnapshot.child("Following").getChildrenCount();
                String image = (String) dataSnapshot.child("ProfilePicture").getValue();
                if(!nam.equals("null")){
                    mNameUser.setText(nam);
                }
                if(!image.equals("null")){
                    Picasso.with(UserProfile.this).load(image).into(mProfileImage);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UserProfile.this,EntryActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Blog, UserProfile.BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter
                <Blog, UserProfile.BlogViewHolder>(
                Blog.class,
                R.layout.event_row,
                UserProfile.BlogViewHolder.class,
                mDatabaseShow
        ){
            @Override
            protected void populateViewHolder(final UserProfile.BlogViewHolder viewHolder, final Blog model, int position) {
                final String post_key = getRef(position).getKey();
                viewHolder.setName(model.getName());
                viewHolder.setUserame(model.getUsername());
                viewHolder.setUsname(model.getUsname());
                viewHolder.setDate(model.getDate());
                viewHolder.setDetails(model.getDetails());
                viewHolder.setAddress(model.getAddress());
                viewHolder.setImage(getApplicationContext(), model.getImage());
                viewHolder.set_like_icon(post_key);
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent single = new Intent(UserProfile.this, CustomerSubmittion.class);
                        single.putExtra("key",post_key);
                        startActivity(single);
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }
                });
                viewHolder.mDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
                        builder.setMessage("Are you Sure?")
                                .setNegativeButton("Cancel",null)
                                .setPositiveButton("Yes I'm Sure", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        viewHolder.mDatabase.child("Users").child(UID).child("Posts").child(post_key).removeValue();
                                        viewHolder.mDatabaseLike.child(post_key).removeValue();

                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    }
                });
                viewHolder.mDatabaseLike.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        viewHolder.count.setText((String.valueOf(dataSnapshot.child(post_key).getChildrenCount()))+" Likes");
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
                                    if (dataSnapshot.child(post_key).hasChild((mAuth.getCurrentUser().getUid()))) {
                                        viewHolder.mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();
                                        mProcess = false;
                                    } else {
                                        viewHolder.mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue(mAuth.getCurrentUser().getEmail());
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

        DatabaseReference mDatabaseLike, mDatabase;
        FirebaseAuth mAuth;
        LinearLayout mLike;
        ImageView mDelete, mLikebtn;
        TextView count;
        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mLike = (LinearLayout) mView.findViewById(R.id.like);
            mDelete = (ImageView) mView.findViewById(R.id.deletePost);
            mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabaseLike.keepSynced(true);
            mDatabase.keepSynced(true);
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

        public void setUserame(String _eUsername){
            TextView mName = (TextView) mView.findViewById(R.id.publisherID);
            mName.setText(_eUsername);
        }

        public void setUsname(String _eUsername){
            TextView mName = (TextView) mView.findViewById(R.id.nameUserAccount);
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
