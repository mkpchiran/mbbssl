package com.org.chiranz.aitem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import process.Dataset;
import process.Model;

public class DoctorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
         Model model= Dataset.getModel();
        getSupportActionBar().setTitle(
                getSupportActionBar().getTitle().toString()+" : "+String.valueOf(model.getFull_Name()));

        TextView personFullName = (TextView)findViewById(R.id.personFullName);
        TextView personAddress = (TextView)findViewById(R.id.personAddress);
        TextView personaRegNo = (TextView)findViewById(R.id.personaRegNo);
        TextView personQualification = (TextView)findViewById(R.id.personQualification);
        TextView personRegDate = (TextView)findViewById(R.id.personRegDate);

        personFullName.setText(" Full Name : \n " + model.getFull_Name());
        personAddress.setText(" Address : \n " + model.getAddress());
        personaRegNo.setText(" Registration No : \n " + model.getReg_No());
        personRegDate.setText(" Registration Date: \n " + model.getReg_Date());
        personQualification.setText(" Qualifications : \n " + model.getQualifications());

        personFullName.setBackgroundColor(R.color.colorBlue);
        personAddress.setBackgroundColor(R.color.colorBlue);
        personaRegNo.setBackgroundColor(R.color.colorBlue);
        personRegDate.setBackgroundColor(R.color.colorBlue);
        personQualification.setBackgroundColor(R.color.colorBlue);

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

}
