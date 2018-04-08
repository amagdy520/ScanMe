package com.scan.me.ScanToSendData;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.scan.me.EnteryScreen.EntryActivity;
import com.scan.me.R;
import com.scan.me.Scan.CaptureActivityPortrait;
import com.scan.me.Scan.ShowData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SubmitData extends AppCompatActivity {

    EditText sName, sAge, sAddress, sEmail, sPhone, sSchool, sGender, sAcademic, sSSN, sDOT;
    public String STR , UID, date ;

    DatabaseReference mDatabase;
    StorageReference mStorage;
    FirebaseAuth mAuth;


    Button mSubmit;

    Activity activity =this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_data);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mDatabase.keepSynced(true);
        UID = mAuth.getCurrentUser().getEmail().replace(".","&");

        date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        sName = (EditText) findViewById(R.id._sName);
        sAge = (EditText) findViewById(R.id._sAge);
        sAddress = (EditText) findViewById(R.id._sAddress);
        sEmail = (EditText) findViewById(R.id._sEmail);
        sPhone = (EditText) findViewById(R.id._sPhone);
        sSchool = (EditText) findViewById(R.id._sSchool);
        sGender = (EditText) findViewById(R.id._sGender);
        sAcademic = (EditText) findViewById(R.id._sAcademic);
        sSSN = (EditText) findViewById(R.id._sSSN);
        sDOT = (EditText) findViewById(R.id._sDOT);

        mSubmit = (Button) findViewById(R.id.submit_button);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sName.getText().toString().isEmpty()&&sAge.getText().toString().isEmpty()
                        &&sAddress.getText().toString().isEmpty()&&sEmail.getText().toString().isEmpty()
                        &&sPhone.getText().toString().isEmpty()&&sSchool.getText().toString().isEmpty()
                        &&sGender.getText().toString().isEmpty()&&sAcademic.getText().toString().isEmpty()
                        &&sSSN.getText().toString().isEmpty()&&sDOT.getText().toString().isEmpty()){
                    Toast.makeText(SubmitData.this,"Please, Fill Data to Send",Toast.LENGTH_SHORT).show();
                }else{
                    IntentIntegrator integrator = new IntentIntegrator(activity);
                    integrator.setPrompt("Scan a To Send Data");
                    integrator.setDesiredBarcodeFormats(integrator.ALL_CODE_TYPES);
                    integrator.setCameraId(0);
                    integrator.setOrientationLocked(false);
                    integrator.setCaptureActivity(CaptureActivityPortrait.class);
                    integrator.setBeepEnabled(true);
                    integrator.initiateScan();
                }
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "You Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                STR = result.getContents();
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.child("Users").child(STR).child("Submission").child(date).child(UID).exists()) {
                            Toast.makeText(activity, "You Submitted Once", Toast.LENGTH_SHORT).show();
                        } else {
                            mDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    DatabaseReference newSubmission = mDatabase.child("Users").child(STR).child("Submission").child(date);
                                    newSubmission.child("date").setValue(date);
                                    if (!sName.getText().toString().isEmpty()) {
                                        newSubmission.child("users").child(UID).child("name").setValue(sName.getText().toString().toLowerCase());
                                    }
                                    if (!sAge.getText().toString().isEmpty()) {
                                        newSubmission.child("users").child(UID).child("age").setValue(sAge.getText().toString().toLowerCase());
                                    }
                                    if (!sAddress.getText().toString().isEmpty()) {
                                        newSubmission.child("users").child(UID).child("address").setValue(sAddress.getText().toString().toLowerCase());
                                    }
                                    if (!sEmail.getText().toString().isEmpty()) {
                                        newSubmission.child("users").child(UID).child("email").setValue(sEmail.getText().toString().toLowerCase());
                                    }
                                    if (!sPhone.getText().toString().isEmpty()) {
                                        newSubmission.child("users").child(UID).child("phone").setValue(sPhone.getText().toString().toLowerCase());
                                    }
                                    if (!sSchool.getText().toString().isEmpty()) {
                                        newSubmission.child("users").child(UID).child("school").setValue(sSchool.getText().toString().toLowerCase());
                                    }
                                    if (!sGender.getText().toString().isEmpty()) {
                                        newSubmission.child("users").child(UID).child("gender").setValue(sGender.getText().toString().toLowerCase());
                                    }
                                    if (!sAcademic.getText().toString().isEmpty()) {
                                        newSubmission.child("users").child(UID).child("academic").setValue(sAcademic.getText().toString().toLowerCase());
                                    }
                                    if (!sSSN.getText().toString().isEmpty()) {
                                        newSubmission.child("users").child(UID).child("ssn").setValue(sSSN.getText().toString().toLowerCase());
                                    }
                                    if (!sDOT.getText().toString().isEmpty()) {
                                        newSubmission.child("users").child(UID).child("dot").setValue(sDOT.getText().toString().toLowerCase());
                                    }
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
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SubmitData.this,EntryActivity.class);
        startActivity(intent);
        finish();
    }
}
