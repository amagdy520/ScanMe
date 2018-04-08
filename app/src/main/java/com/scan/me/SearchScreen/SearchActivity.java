package com.scan.me.SearchScreen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scan.me.EnteryScreen.EntryActivity;
import com.scan.me.R;
import com.scan.me.SearchProfile.SearchUserActivity;
import com.scan.me.UserProfile.Setting.SettingActivity;
import com.squareup.picasso.Picasso;

public class SearchActivity extends AppCompatActivity {

    EditText mSearchEdit;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    String UID;

    CardView mCard;



    ImageView mSearchImage;
    TextView mNameSearch, mUserSearch, mEmailSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        mSearchEdit = (EditText) findViewById(R.id.search_user);


        mCard = (CardView) findViewById(R.id.cardView);
        mSearchImage = (ImageView) findViewById(R.id._searchImage);
        mNameSearch = (TextView) findViewById(R.id._searchName);
        mUserSearch = (TextView) findViewById(R.id._searchUsername);
        mEmailSearch = (TextView) findViewById(R.id._searchEmail);

        mSearchEdit.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    UID = mSearchEdit.getText().toString().toLowerCase().replace(".","&");
                    mDatabase.child("Users").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            boolean check = dataSnapshot.child(UID).exists();
                            if(check){
                                String name = (String) dataSnapshot.child(UID).child("Name").getValue();
                                String user = (String) dataSnapshot.child(UID).child("UserName").getValue();
                                String email = (String) dataSnapshot.child(UID).child("Email").getValue();
                                String image = (String) dataSnapshot.child(UID).child("ProfilePicture").getValue();
                                mCard.setVisibility(View.VISIBLE);
                                mUserSearch.setText(user);
                                mEmailSearch.setText(email);
                                if(name.equals("null")){
                                    mNameSearch.setText("No name Yet");
                                }else{
                                    mNameSearch.setText(name);
                                }
                                if (image.equals("null")){
                                    mSearchImage.setImageResource(R.drawable.account);
                                }else{
                                    Picasso.with(SearchActivity.this).load(image).into(mSearchImage);
                                }
                            }else{
                                Toast.makeText(SearchActivity.this,"User Is Not Found",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    return true;
                }
                return false;
            }
        });

        mCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edSearch = (EditText) findViewById(R.id.search_user);
                // move data to another activity
                String message =edSearch.getText().toString();
                Intent intent = new Intent(SearchActivity.this, SearchUserActivity.class);
                intent.putExtra("message", message);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SearchActivity.this,EntryActivity.class);
        startActivity(intent);
        finish();
    }
}
