package com.scan.me.SubmittionScreen;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.scan.me.R;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

public class Submittion extends AppCompatActivity {


    public final static int WHITE = 0xFFFFFFFF;
    public final static int BLACK = 0xFF000000;
    public final static int WIDTH = 900;
    public final static int HEIGHT = 900;
    EditText sName, sAge, sAddress, sEmail, sPhone, sSchool, sGender, sAcademic, sSSN, sDOT;
    private String mField = null;
    private String[] mFieldChoose;

    public String STR , UID ;

    DatabaseReference mDatabase;
    StorageReference mStorage;
    FirebaseAuth mAuth;
    ImageView mCode;
    ProgressDialog progressDialog;
    LinearLayout mLinear;
    String post_key, UD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submittion);
        mField = getIntent().getExtras().getString("field");
        mFieldChoose = mField.split("#");
        UD = mFieldChoose[0];
        post_key = mFieldChoose[1];
        progressDialog = new ProgressDialog(Submittion.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Code...");


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mDatabase.keepSynced(true);
        UID = mAuth.getCurrentUser().getEmail().replace(".","&");

        STR = UID;
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

        mCode = (ImageView) findViewById(R.id.showCode);

        mLinear = (LinearLayout) findViewById(R.id.data);

        if(Arrays.toString(mFieldChoose).contains("Name")){
            Toast.makeText(this,"True",Toast.LENGTH_SHORT).show();
            sName.setBackgroundColor(getResources().getColor(R.color.accent));
        }else{
            sName.setFocusable(false);
            sName.setBackgroundColor(getResources().getColor(R.color.base));
        }
        if(Arrays.toString(mFieldChoose).contains("Age")){
            Toast.makeText(this,"True",Toast.LENGTH_SHORT).show();
            sAge.setBackgroundColor(getResources().getColor(R.color.accent));
        }else{
            sAge.setFocusable(false);
            sAge.setBackgroundColor(getResources().getColor(R.color.base));
        }
        if(Arrays.toString(mFieldChoose).contains("Address")){
            Toast.makeText(this,"True",Toast.LENGTH_SHORT).show();
            sAddress.setBackgroundColor(getResources().getColor(R.color.accent));
        }else{
            sAddress.setFocusable(false);
            sAddress.setBackgroundColor(getResources().getColor(R.color.base));
        }
        if(Arrays.toString(mFieldChoose).contains("Email")){
            Toast.makeText(this,"True",Toast.LENGTH_SHORT).show();
            sEmail.setBackgroundColor(getResources().getColor(R.color.accent));
        }else{
            sEmail.setFocusable(false);
            sEmail.setBackgroundColor(getResources().getColor(R.color.base));
        }
        if(Arrays.toString(mFieldChoose).contains("Phone")){
            Toast.makeText(this,"True",Toast.LENGTH_SHORT).show();
            sPhone.setBackgroundColor(getResources().getColor(R.color.accent));
        }else{
            sPhone.setFocusable(false);
            sPhone.setBackgroundColor(getResources().getColor(R.color.base));
        }
        if(Arrays.toString(mFieldChoose).contains("School")){
            Toast.makeText(this,"True",Toast.LENGTH_SHORT).show();
            sSchool.setBackgroundColor(getResources().getColor(R.color.accent));
        }else{
            sSchool.setFocusable(false);
            sSchool.setBackgroundColor(getResources().getColor(R.color.base));
        }
        if(Arrays.toString(mFieldChoose).contains("Gender")){
            Toast.makeText(this,"True",Toast.LENGTH_SHORT).show();
            sGender.setBackgroundColor(getResources().getColor(R.color.accent));
        }else{
            sGender.setFocusable(false);
            sGender.setBackgroundColor(getResources().getColor(R.color.base));
        }
        if(Arrays.toString(mFieldChoose).contains("Academic Year")){
            Toast.makeText(this,"True",Toast.LENGTH_SHORT).show();
            sAcademic.setBackgroundColor(getResources().getColor(R.color.accent));
        }else{
            sAcademic.setFocusable(false);
            sAcademic.setBackgroundColor(getResources().getColor(R.color.base));
        }
        if(Arrays.toString(mFieldChoose).contains("SSN")){
            Toast.makeText(this,"True",Toast.LENGTH_SHORT).show();
            sSSN.setBackgroundColor(getResources().getColor(R.color.accent));
        }else{
            sSSN.setFocusable(false);
            sSSN.setBackgroundColor(getResources().getColor(R.color.base));
        }
        if(Arrays.toString(mFieldChoose).contains("Date of Birth")){
            Toast.makeText(this,"True",Toast.LENGTH_SHORT).show();
            sDOT.setBackgroundColor(getResources().getColor(R.color.accent));
        }else{
            sDOT.setFocusable(false);
            sDOT.setBackgroundColor(getResources().getColor(R.color.base));
        }
    }

    public void create(View view){
        if (!sName.getText().toString().isEmpty()){
            STR += "#"+sName.getText().toString();
        }else{
            STR += "#"+"null";
        }
        if (!sAge.getText().toString().isEmpty()){
            STR += "#"+sAge.getText().toString();
        }else{
            STR += "#"+"null";
        }
        if (!sAddress.getText().toString().isEmpty()){
            STR += "#"+sAddress.getText().toString();
        }else{
            STR += "#"+"null";
        }
        if (!sEmail.getText().toString().isEmpty()){
            STR += "#"+sEmail.getText().toString();
        }else{
            STR += "#"+"null";
        }
        if (!sPhone.getText().toString().isEmpty()){
            STR += "#"+sPhone.getText().toString();
        }else{
            STR += "#"+"null";
        }
        if (!sSchool.getText().toString().isEmpty()){
            STR += "#"+sSchool.getText().toString();
        }else{
            STR += "#"+"null";
        }
        if (!sGender.getText().toString().isEmpty()){
            STR += "#"+sGender.getText().toString();
        }else{
            STR += "#"+"null";
        }
        if (!sAcademic.getText().toString().isEmpty()){
            STR += "#"+sAcademic.getText().toString();
        }else{
            STR += "#"+"null";
        }
        if (!sSSN.getText().toString().isEmpty()){
            STR += "#"+sSSN.getText().toString();
        }else{
            STR += "#"+"null";
        }
        if (!sDOT.getText().toString().isEmpty()){
            STR += "#"+sDOT.getText().toString();
        }else{
            STR += "#"+"null";
        }

        try {
            Bitmap bitmap = encodeAsBitmap(STR);
            mCode.setImageBitmap(bitmap);
            mCode.setVisibility(View.VISIBLE);
            mLinear.setVisibility(View.INVISIBLE);
            uploadingCoverImage(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, HEIGHT, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }

        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }
    private void uploadingCoverImage(Bitmap bitmap){
        // Create a reference to 'ProfileImages/<userName>/<randomName>.jpg'
        String imageName = UID+".jpg";
        StorageReference ImagesRef = mStorage.child(UID).child(imageName);
        // Get the data from an ImageView as bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        final byte[] data = baos.toByteArray();
        UploadTask uploadTask = ImagesRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") final
                Uri downloadedUrl = taskSnapshot.getDownloadUrl();
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = (String)dataSnapshot.child("Users").child(UID).child("Name").getValue();
                        String username = (String)dataSnapshot.child("Users").child(UID).child("UserName").getValue();
                        String email = (String)dataSnapshot.child("Users").child(UID).child("Email").getValue();
                        String image = (String)dataSnapshot.child("Users").child(UID).child("Image").getValue();
                        DatabaseReference databaseReference = mDatabase.child("Users").child(UD).child("Posts").child(post_key).child("Submit");
                        databaseReference.child(UID).child("Qr").setValue(downloadedUrl.toString());
                        databaseReference.child(UID).child("STR").setValue(STR);
                        databaseReference.child(UID).child("name").setValue(name);
                        databaseReference.child(UID).child("username").setValue(username);
                        databaseReference.child(UID).child("email").setValue(email);
                        databaseReference.child(UID).child("image").setValue(image);
                        databaseReference.child(UID).child("attend").setValue("No");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


            }
        });
    }
}
