package com.scan.me.ShowQrCode;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.scan.me.R;
import com.squareup.picasso.Picasso;

public class AlreadySubmit extends AppCompatActivity {

    public String STR, UID;
    ImageView imageView;
    StorageReference mStorage;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_code2);
        STR = getIntent().getExtras().getString("qr");

        mAuth = FirebaseAuth.getInstance();
        UID = mAuth.getCurrentUser().getEmail().replace(".","&");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        mStorage = FirebaseStorage.getInstance().getReference();


        imageView = (ImageView) findViewById(R.id.mQRCode);
        Picasso.with(ctx).load(STR).into(imageView);
    }
}