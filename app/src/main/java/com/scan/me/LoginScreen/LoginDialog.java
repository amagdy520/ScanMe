package com.scan.me.LoginScreen;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.scan.me.EnteryScreen.EntryActivity;
import com.scan.me.R;
import com.scan.me.StartScreen.StartScreen;

import static android.content.ContentValues.TAG;


/**
 * Created by Ahmed Magdy on 1/21/2018.
 */

public class LoginDialog {

    TextView mForget;
    FirebaseAuth mAuth;
    private EditText mEmailInput, mPasswordInput;
    private TextInputLayout mEmailInputLayout, mPasswordInputLayout;
    Activity mActivity;
    public void showDialog(final Activity activity){
        final Dialog dialog = new Dialog(activity);
        mActivity = activity;
        mAuth = FirebaseAuth.getInstance();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_login);
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
        mForget = (TextView) dialog.findViewById(R.id.forget_password);

        Button dialogBtn_okay = (Button) dialog.findViewById(R.id.btn_okay);
        final String email = mEmailInput.getText().toString().trim();
        final String password = mPasswordInput.getText().toString();

        dialogBtn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEmailInput.getText().toString().isEmpty() || mPasswordInput.getText().toString().isEmpty()) {
                    if (mEmailInput.getText().toString().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        mEmailInput.setError("Please, Enter Email!");
                    } else {
                        mEmailInput.setError(null);
                    }
                    if (mPasswordInput.getText().toString().isEmpty() || mPasswordInput.getText().toString().length() < 4 || mPasswordInput.getText().toString().length() > 10) {
                        mPasswordInput.setError("Please, Enter Password!");
                    } else {
                        mPasswordInput.setError(null);
                    }
                }else {
                    Login(mEmailInput.getText().toString(), mPasswordInput.getText().toString());
                }
            }
        });


        mForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mEmailInput.getText().toString().isEmpty()) {
                    mEmailInput.setError("Please, Enter Email!");
                } else {
                    mEmailInput.setError(null);
                    final AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(mActivity);
                    builder.setTitle("Reset Password")
                            .setMessage("Check your Email To Reset password..")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    mAuth.sendPasswordResetEmail(mEmailInput.getText().toString());
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });


        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
    public void Login(String e, String p) {
        Log.d(TAG, "Login");

        final String email = e;
        final String password = p;
        final ProgressDialog progressDialog = new ProgressDialog(mActivity,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                            mAuth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(mActivity, "Login user Successfully!", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                                Intent intent = new Intent(mActivity.getApplicationContext(),EntryActivity.class);
                                                mActivity.startActivity(intent);
                                                mActivity.finish();
                                            }else{
                                                Toast.makeText(mActivity, "Unable to login user", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(mActivity, "Please Check email and password", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                }, 1500);
    }
}
