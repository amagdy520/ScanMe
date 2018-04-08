package com.scan.me.SignUpScreen;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scan.me.EnteryScreen.EntryActivity;
import com.scan.me.R;
import com.scan.me.StartScreen.StartScreen;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Ahmed Magdy on 2/10/2018.
 */

public class SignUpDialog {
    private static final String TAG = "SignupActivity";


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    boolean valid = true;
    private EditText mEmailInput, mPasswordInput, mUserNameInput ;
    private TextInputLayout mEmailInputLayout, mPasswordInputLayout, mUserNameInputLayout;
    Activity mActivity;

    Button _signupButton;
    public void showDialog(final Activity activity){
        final Dialog dialog = new Dialog(activity);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");



        mActivity = activity;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_sign_up);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        ImageView dialogBtn_cancel = (ImageView) dialog.findViewById(R.id.btn_cancel);
        dialogBtn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



        mEmailInput = (EditText) dialog.findViewById(R.id.input_email);
        mEmailInputLayout = (TextInputLayout) dialog.findViewById(R.id.input_email_layout);
        mPasswordInput = (EditText) dialog.findViewById(R.id.input_password);
        mPasswordInputLayout = (TextInputLayout) dialog.findViewById(R.id.input_password_layout);
        mUserNameInput = (EditText) dialog.findViewById(R.id.input_username);
        mUserNameInputLayout = (TextInputLayout) dialog.findViewById(R.id.input_username_layout);



        _signupButton = (Button) dialog.findViewById(R.id.btn_okay);
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(mActivity,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final String userName = mUserNameInput.getText().toString();
        final String email = mEmailInput.getText().toString();
        final String password = mPasswordInput.getText().toString();

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if( !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(userName)) {
                            mAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if( task.isSuccessful())
                                            {
                                                DatabaseReference current_user_db = mDatabase.child(email.replace(".","&"));
                                                current_user_db.child("UserName").setValue("@"+userName);
                                                current_user_db.child("Email").setValue(email);
                                                current_user_db.child("Password").setValue(password);
                                                current_user_db.child("Name").setValue("null");
                                                current_user_db.child("Gender").setValue("null");
                                                current_user_db.child("Follower").setValue("0");
                                                current_user_db.child("Following").setValue("0");
                                                current_user_db.child("ProfilePicture").setValue("null");
                                                onSignupSuccess();
                                                progressDialog.dismiss();
                                            }else
                                            {
                                                Toast.makeText(mActivity, "Unable to Create user", Toast.LENGTH_SHORT).show();
                                                onSignupFailed();
                                                progressDialog.dismiss();
                                            }
                                        }

                                    });
                        }else
                        {
                            Toast.makeText(mActivity, "Please Check the internet Connection", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    }
                }, 1500);
    }
    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        mActivity.setResult(RESULT_OK, null);
        Intent intent = new Intent(mActivity.getApplicationContext(),EntryActivity.class);
        mActivity.startActivity(intent);
        mActivity.finish();
    }
    public void onSignupFailed() {
        Toast.makeText(mActivity.getBaseContext(), "SignUp failed", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        final String username = mUserNameInput.getText().toString();
        final String email = mEmailInput.getText().toString();
        String password = mPasswordInput.getText().toString();

        if (username.isEmpty() || username.length() < 3) {
            mUserNameInput.setError("at least 3 characters");
            valid = false;
        } else {
            mUserNameInput.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailInput.setError("enter a valid email address");
            valid = false;
        } else {
            mEmailInput.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            mPasswordInput.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            mPasswordInput.setError(null);
        }


        return valid;
    }
}
