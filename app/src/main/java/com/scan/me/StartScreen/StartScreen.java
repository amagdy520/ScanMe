package com.scan.me.StartScreen;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.scan.me.EnteryScreen.EntryActivity;
import com.scan.me.LoginScreen.LoginDialog;
import com.scan.me.R;
import com.scan.me.SignUpScreen.SignUpDialog;

public class StartScreen extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private Button mLoginButton,mSignUpButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        mDefinitions();
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginDialog alert = new LoginDialog();
                alert.showDialog(StartScreen.this);
            }
        });

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpDialog alert = new SignUpDialog();
                alert.showDialog(StartScreen.this);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //CHECKING USER PRESENCE
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if ((user != null)) {
                    Toast.makeText(StartScreen.this,"Login Success",Toast.LENGTH_SHORT).show();
                    Intent mAccount = new Intent(StartScreen.this, EntryActivity.class);
                    startActivity(mAccount);
                    finish();
                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
    }
    public void mDefinitions(){
        mLoginButton = (Button) findViewById(R.id.loginButton);
        mSignUpButton = (Button) findViewById(R.id.signUpButton);
    }
}
