package com.scan.me.UserProfile.Setting;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.scan.me.EnteryScreen.EntryActivity;
import com.scan.me.R;
import com.scan.me.UserProfile.NewDocument.NewDocument;
import com.scan.me.UserProfile.UserProfile;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {

    ImageView mBack;
    TextView mName, mUser, mEmail, mGender;
    LinearLayout mModifyName, mModifyProfile, mModifyGender, mModifyPassword;
    ImageView mProfile;
    String UID;
    private DatabaseReference mDatabase, mDatabaseShow;
    private StorageReference mStorage;
    private FirebaseAuth mAuth;
    private final int GALLERY_REQUEST=1;
    private Uri mImageUri = null;
    ImageButton mSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mStorage = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        UID = mAuth.getCurrentUser().getEmail().toString().replace(".","&");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(UID);
        mDatabase.keepSynced(true);
        mModifyName = (LinearLayout) findViewById(R.id.modifyName);
        mModifyProfile = (LinearLayout) findViewById(R.id.modifyPicture);
        mModifyGender = (LinearLayout) findViewById(R.id.modifyGender);
        mModifyPassword = (LinearLayout) findViewById(R.id.modifyPassword);

        mSave = (ImageButton) findViewById(R.id.saveImage);

        mProfile = (ImageView) findViewById(R.id.profile);
        mName = (TextView) findViewById(R.id.name);
        mUser = (TextView) findViewById(R.id.username);
        mEmail = (TextView) findViewById(R.id.email);
        mGender = (TextView) findViewById(R.id.gender);
        mBack = (ImageView) findViewById(R.id.back);


        final ProgressDialog progressDialog = new ProgressDialog(SettingActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Saving New Profile...");


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nam = (String) dataSnapshot.child("Name").getValue();
                String email = (String) dataSnapshot.child("Email").getValue();
                String us = (String) dataSnapshot.child("UserName").getValue();
                String gender = (String) dataSnapshot.child("Gender").getValue();
                String image = (String) dataSnapshot.child("ProfilePicture").getValue();
                if(!nam.equals("null")){
                    mName.setText(nam);
                }
                if(!image.equals("null")){
                    Picasso.with(SettingActivity.this).load(image).into(mProfile);
                }
                if (!gender.equals("null")){
                    mGender.setText(gender);
                }
                mUser.setText(us);
                mEmail.setText(email);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mModifyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SettingActivity.this);
                LayoutInflater inflater = SettingActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_dialog, null);
                dialogBuilder.setView(dialogView);

                final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = (String) dataSnapshot.child("Name").getValue();
                        edt.setText(name);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                dialogBuilder.setTitle("Full name");
                dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        final String newName = edt.getText().toString().trim();
                        mDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                mDatabase.child("Name").setValue(newName);
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });
                dialogBuilder.setNegativeButton("Cancel", null);
                AlertDialog b = dialogBuilder.create();
                b.show();
            }
        });
        mModifyGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SettingActivity.this);
                LayoutInflater inflater = SettingActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_dialog, null);
                dialogBuilder.setView(dialogView);

                final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String gender = (String) dataSnapshot.child("Gender").getValue();
                        edt.setText(gender);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                dialogBuilder.setTitle("Full name");
                dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        final String newGender = edt.getText().toString().trim();
                        mDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                mDatabase.child("Gender").setValue(newGender);
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });
                dialogBuilder.setNegativeButton("Cancel", null);
                AlertDialog b = dialogBuilder.create();
                b.show();
            }
        });
        mModifyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        StorageReference filepath = mStorage.child("ProfilePicture").child(mImageUri.getLastPathSegment());
                        filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                if(!mImageUri.equals(null)){
                                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                    DatabaseReference newPost = mDatabase.child("ProfilePicture");
                                    newPost.setValue(downloadUrl.toString());
                                    progressDialog.dismiss();
                                }else{
                                    Toast.makeText(SettingActivity.this,"Please Try Again",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        mModifyPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAuth.getCurrentUser().isEmailVerified()){
                    final AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(SettingActivity.this);
                    builder.setTitle("Reset Your Account Password")
                            .setMessage("Check your Email To reset password..")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    mAuth.sendPasswordResetEmail(mAuth.getCurrentUser().getEmail().toString());
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }else{
                    final AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(SettingActivity.this);
                    builder.setTitle("Verify Your Account")
                            .setMessage("Check your Email To verify password..")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    sendVerificationEmail();
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this,UserProfile.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SettingActivity.this,"Link Has Send To "+mAuth.getCurrentUser().getEmail(),Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(SettingActivity.this,"Error in Internet Connection!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            mImageUri = data.getData();
            CropImage.activity(mImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mImageUri = result.getUri();
                mProfile.setImageURI(mImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SettingActivity.this, EntryActivity.class);
        startActivity(intent);
        finish();
    }
}
