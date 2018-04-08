package com.scan.me.UserProfile.ChooseDialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.scan.me.R;

/**
 * Created by Ahmed Magdy on 2/13/2018.
 */

public class ChooseDialog {
    CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6, checkBox7, checkBox8, checkBox9, checkBox10;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    Button mSubmit;
    Activity mActivity;
    RadioGroup mGroup;
    String check, addToDatabase;
    public void showDialog(final Activity activity) {
        final Dialog dialog = new Dialog(activity);
        mActivity = activity;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_choose);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCanceledOnTouchOutside(true);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);

        mGroup = (RadioGroup) dialog.findViewById(R.id.radio);
        mSubmit = (Button) dialog.findViewById(R.id.submit);
        checkBox1 = (CheckBox) dialog.findViewById(R.id.name);
        checkBox2 = (CheckBox) dialog.findViewById(R.id.age);
        checkBox3 = (CheckBox) dialog.findViewById(R.id.add);
        checkBox4 = (CheckBox) dialog.findViewById(R.id.email);
        checkBox5 = (CheckBox) dialog.findViewById(R.id.phone_number);
        checkBox6 = (CheckBox) dialog.findViewById(R.id.school);
        checkBox7 = (CheckBox) dialog.findViewById(R.id.gender);
        checkBox8 = (CheckBox) dialog.findViewById(R.id.academic_year);
        checkBox9 = (CheckBox) dialog.findViewById(R.id.ssn);
        checkBox10 = (CheckBox) dialog.findViewById(R.id.dot);

        final TextView mFields = (TextView) activity.findViewById(R.id.e_field);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox1.isChecked()){
                    addToDatabase +="#Name";
                }
                if (checkBox2.isChecked()){
                    addToDatabase +="#Age";
                }
                if (checkBox3.isChecked()){
                    addToDatabase +="#Address";
                }
                if (checkBox4.isChecked()){
                    addToDatabase +="#Email";
                }
                if (checkBox5.isChecked()){
                    addToDatabase +="#Phone";
                }
                if (checkBox6.isChecked()){
                    addToDatabase +="#School";
                }
                if (checkBox7.isChecked()){
                    addToDatabase +="#Gender";
                }
                if (checkBox8.isChecked()){
                    addToDatabase +="#Academic Year";
                }
                if (checkBox9.isChecked()){
                    addToDatabase +="#SSN";
                }
                if (checkBox10.isChecked()){
                    addToDatabase +="#Date of Birth";
                }
                mFields.setText(addToDatabase.substring(4,addToDatabase.length()));
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
