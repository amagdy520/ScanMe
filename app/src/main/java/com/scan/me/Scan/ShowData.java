package com.scan.me.Scan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.scan.me.R;

public class ShowData extends AppCompatActivity {

    EditText sName, sAge, sAddress, sEmail, sPhone, sSchool, sGender, sAcademic, sSSN, sDOT;
    private String mField = null;
    private String[] mFieldChoose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        mField = getIntent().getExtras().getString("data");
        mFieldChoose = mField.split("#");

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

//        sName.setBackgroundResource(R.color.base);
//        sAge.setBackgroundResource(R.color.base);
//        sAddress.setBackgroundResource(R.color.base);
//        sEmail.setBackgroundResource(R.color.base);
//        sPhone.setBackgroundResource(R.color.base);
//        sSchool.setBackgroundResource(R.color.base);
//        sGender.setBackgroundResource(R.color.base);
//        sAcademic.setBackgroundResource(R.color.base);
//        sSSN.setBackgroundResource(R.color.base);
//        sDOT.setBackgroundResource(R.color.base);


        if (!mFieldChoose[1].equals("null")){
            sName.setText(mFieldChoose[1]);
        }
        if (!mFieldChoose[2].equals("null")){
            sAge.setText(mFieldChoose[2]);
        }
        if (!mFieldChoose[3].equals("null")){
            sAddress.setText(mFieldChoose[3]);
        }
        if (!mFieldChoose[4].equals("null")){
            sEmail.setText(mFieldChoose[4]);
        }
        if (!mFieldChoose[5].equals("null")){
            sPhone.setText(mFieldChoose[5]);
        }
        if (!mFieldChoose[6].equals("null")){
            sSchool.setText(mFieldChoose[6]);
        }
        if (!mFieldChoose[7].equals("null")){
            sGender.setText(mFieldChoose[7]);
        }
        if (!mFieldChoose[8].equals("null")){
            sAcademic.setText(mFieldChoose[8]);
        }
        if (!mFieldChoose[9].equals("null")){
            sSSN.setText(mFieldChoose[9]);
        }
        if (!mFieldChoose[10].equals("null")){
            sDOT.setText(mFieldChoose[10]);
        }
    }
}
