package com.scan.me.CreateScreen;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.scan.me.EnteryScreen.EntryActivity;
import com.scan.me.R;
import com.scan.me.UserProfile.ChooseDialog.ChooseDialog;
import com.scan.me.UserProfile.NewDocument.NewDocument;
import com.scan.me.UserProfile.UserProfile;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateActivity extends AppCompatActivity {

    LinearLayout mModify_eName, mModify_eDate, mModify_ePicture, mModify_eDescription, mModify_eAddress, mModifyFields, m_eSave;
    TextView mName, mDate, mDescription, mAddress, mFields;
    ImageView mPicture;
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    private final int GALLERY_REQUEST=1;
    private Uri mImageUri = null;
    private StorageReference mStorage;
    private DatabaseReference mDataBase;
    String UID, US, NAM;
    FirebaseAuth mAuth;
    ImageButton mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        final ProgressDialog progressDialog = new ProgressDialog(CreateActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Uploading Post...");


        mStorage = FirebaseStorage.getInstance().getReference();
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        UID = mAuth.getCurrentUser().getEmail().toString().replace(".","&");
        mModify_eName = (LinearLayout) findViewById(R.id.modify_eName);
        mModify_eDate = (LinearLayout) findViewById(R.id.modifyDate);
        mModify_ePicture = (LinearLayout) findViewById(R.id.modify_ePicture);
        mModify_eDescription = (LinearLayout) findViewById(R.id.modifyDescription);
        mModify_eAddress = (LinearLayout) findViewById(R.id.modifyAddress);
        mModifyFields = (LinearLayout) findViewById(R.id.modifyChoose);
        m_eSave = (LinearLayout) findViewById(R.id.saveModify);


        mDataBase.child("Users").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                US = (String) dataSnapshot.child("UserName").getValue();
                NAM = (String) dataSnapshot.child("Name").getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mBack = (ImageButton) findViewById(R.id.back);
        mName = (TextView) findViewById(R.id._eName);
        mDate = (TextView) findViewById(R.id._eDate);
        mDescription = (TextView) findViewById(R.id._eDesc);
        mAddress = (TextView) findViewById(R.id._eAddress);
        mFields = (TextView) findViewById(R.id.e_field);
        mPicture = (ImageView) findViewById(R.id.event_pic);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateActivity.this,EntryActivity.class);
                startActivity(intent);
                finish();
            }
        });



        mModify_eName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CreateActivity.this);
                LayoutInflater inflater = CreateActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_dialog, null);
                dialogBuilder.setView(dialogView);

                final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);
                edt.setHint("Event Name");
                dialogBuilder.setTitle("Event name");
                dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        final String eName = edt.getText().toString().trim();
                        mName.setText(eName);
                    }
                });
                dialogBuilder.setNegativeButton("Cancel", null);
                AlertDialog b = dialogBuilder.create();
                b.show();
            }
        });

        mModify_eDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CreateActivity.this);
                LayoutInflater inflater = CreateActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_dialog, null);
                dialogBuilder.setView(dialogView);

                final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);
                edt.setHint("Event Description");
                dialogBuilder.setTitle("Event Description");
                dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        final String eDesc = edt.getText().toString().trim();
                        mDescription.setText(eDesc);
                    }
                });
                dialogBuilder.setNegativeButton("Cancel", null);
                AlertDialog b = dialogBuilder.create();
                b.show();
            }
        });

        mModify_eAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CreateActivity.this);
                LayoutInflater inflater = CreateActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_dialog, null);
                dialogBuilder.setView(dialogView);

                final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);
                edt.setHint("Event Address");
                dialogBuilder.setTitle("Event Address");
                dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        final String eAddress = edt.getText().toString().trim();
                        mAddress.setText(eAddress);
                    }
                });
                dialogBuilder.setNegativeButton("Cancel", null);
                AlertDialog b = dialogBuilder.create();
                b.show();
            }
        });


        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        mModify_eDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(CreateActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mModify_ePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });


        mModifyFields.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseDialog alert = new ChooseDialog();
                alert.showDialog(CreateActivity.this);
            }
        });
        m_eSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = mName.getText().toString();
                final String date = mDate.getText().toString();
                final String desc = mDescription.getText().toString();
                final String add = mAddress.getText().toString();
                final String field = mFields.getText().toString();
                if (name.equals("Name")||date.equals("Date")||desc.equals("Description")||add.equals("Address")||field.equals("Fields")){
                    Toast.makeText(CreateActivity.this,"Please, Fill Data",Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.show();
                    StorageReference filepath = mStorage.child("Events_Images").child(mImageUri.getLastPathSegment());
                    filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if(!mImageUri.equals(null)) {
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                DatabaseReference newPersonal = mDataBase.child("Users").child(UID).child("Posts").push();
                                newPersonal.child("image").setValue(downloadUrl.toString());
                                newPersonal.child("name").setValue(name);
                                newPersonal.child("details").setValue(desc);
                                newPersonal.child("address").setValue(add);
                                newPersonal.child("date").setValue(date);
                                newPersonal.child("fields").setValue(field);
                                newPersonal.child("uid").setValue(UID);
                                newPersonal.child("username").setValue(US);
                                newPersonal.child("usname").setValue(NAM);
                                progressDialog.dismiss();
                                startActivity(new Intent(CreateActivity.this, UserProfile.class));
                                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            }else{
                                DatabaseReference newPersonal = mDataBase.child("Users").child(UID).child("Posts").push();
                                newPersonal.child("name").setValue(name);
                                newPersonal.child("details").setValue(desc);
                                newPersonal.child("address").setValue(add);
                                newPersonal.child("date").setValue(date);
                                newPersonal.child("fields").setValue(field);
                                newPersonal.child("uid").setValue(UID);
                                newPersonal.child("username").setValue(US);
                                newPersonal.child("usname").setValue(NAM);
                                progressDialog.dismiss();
                                startActivity(new Intent(CreateActivity.this, UserProfile.class));
                                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            }
                        }
                    });
                }
            }
        });

    }
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mDate.setText(sdf.format(myCalendar.getTime()));
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
                mPicture.setImageURI(mImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}