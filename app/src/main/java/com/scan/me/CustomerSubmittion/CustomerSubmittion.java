package com.scan.me.CustomerSubmittion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.scan.me.EnteryScreen.Blog;
import com.scan.me.R;
import com.scan.me.Scan.CaptureActivityPortrait;
import com.scan.me.UserProfile.UserProfile;
import com.squareup.picasso.Picasso;

public class CustomerSubmittion extends AppCompatActivity {

    String postId, UID;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    RecyclerView mAttend;
    TextView counter;
    ImageView mScan;
    Activity activity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_submittion);
        postId = getIntent().getStringExtra("key");
        mAuth = FirebaseAuth.getInstance();
        UID = mAuth.getCurrentUser().getEmail().replace(".","&");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(UID).child("Posts").child(postId).child("Submit");
        mDatabase.keepSynced(true);
        mAttend = (RecyclerView) findViewById(R.id.submittion_customer);
        mAttend.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mAttend.setLayoutManager(layoutManager);
        counter = (TextView) findViewById(R.id.count_submittion);
        mScan = (ImageView) findViewById(R.id.mScanToSee);
        mScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setPrompt("Scan a barcode");
                integrator.setDesiredBarcodeFormats(integrator.ALL_CODE_TYPES);
                integrator.setCameraId(0);
                integrator.setOrientationLocked(false);

                integrator.setCaptureActivity(CaptureActivityPortrait.class);
                integrator.setBeepEnabled(true);
                integrator.initiateScan();
            }
        });
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                counter.setText(String.valueOf(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<SubmitAdapter, CustomerSubmittion.BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter
                <SubmitAdapter, CustomerSubmittion.BlogViewHolder>(
                SubmitAdapter.class,
                R.layout.submittion_row,
                CustomerSubmittion.BlogViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(final CustomerSubmittion.BlogViewHolder viewHolder, final SubmitAdapter model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setUsername(model.getUsername());
                viewHolder.setEmail(model.getEmail());
                viewHolder.setImage(getApplicationContext(), model.getImage());
                if(model.getAttend().equals("No")){
                    viewHolder.setAttend(R.drawable.not_attend_red);
                }else {
                    viewHolder.setAttend(R.drawable.attend_green);
                }
            }
        };
        mAttend.setAdapter(firebaseRecyclerAdapter);
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setName(String _eName){
            TextView mName = (TextView) mView.findViewById(R.id._cName);
            mName.setText(_eName);
        }

        public void setUsername(String _eUsername){
            TextView mName = (TextView) mView.findViewById(R.id._cUserName);
            mName.setText(_eUsername);
        }

        public void setEmail(String _eUsername){
            TextView mName = (TextView) mView.findViewById(R.id._cEmail);
            mName.setText(_eUsername);
        }

        public void setAttend(int image){
            ImageView mAttend = (ImageView) mView.findViewById(R.id.check);
            mAttend.setImageResource(image);
        }

        public void setImage(Context ctx, final String image){
            ImageView image_post = (ImageView) mView.findViewById(R.id._cProfile);
            Picasso.with(ctx).load(image).into(image_post);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "You Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                final String [] x = result.getContents().toString().split("#");
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DatabaseReference databaseReference = mDatabase.child(x[0]);
                        databaseReference.child("attend").setValue("Yes");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
