package com.uottawa.emilybonneville.seg2105_project;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import android.content.Intent;



public class EmployeeWelcome extends AppCompatActivity{
    private TextView user;
    private String TAG = this.getClass().getName();
    private String userName;
    private Button btnLogout, btnWorkingHours, btnBranchDescription, btnADservices, btnBranchServices, btnViewReviews;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_welcome_page);

        btnLogout = findViewById(R.id.btnLogout);
        btnWorkingHours = findViewById(R.id.btnWorkingHours);
        btnBranchDescription = findViewById(R.id.btnBranchDescription);
        btnADservices = findViewById(R.id.btnADservices);
        btnBranchServices = findViewById(R.id.btnBranchServices);
        btnViewReviews=findViewById(R.id.btnViewRatings);
        user = findViewById(R.id.TextView);


        Intent intent = getIntent();
        userName = intent.getStringExtra("username");
        user.setText(String.format("Welcome %s", userName));

        //set on click listeners for the button
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeeWelcome.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnADservices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeeWelcome.this,ApproveRequest.class);
                intent.putExtra("username", userName); //passing username of employee to Approve and decline services page
                startActivity(intent);
                finish();
            }
        });
        btnBranchDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeeWelcome.this,BranchDescription.class);
                intent.putExtra("username", userName); //passing username of employee to branch description page
                startActivity(intent);
                finish();
            }
        });
        btnWorkingHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeeWelcome.this,BranchHours.class);
                intent.putExtra("username", userName);
                startActivity(intent);
                finish();

            }
        });

        btnBranchServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeeWelcome.this,BranchServices.class);
                intent.putExtra("username", userName);
                startActivity(intent);
                finish();

            }
        });
        btnViewReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeeWelcome.this,BranchViewRating.class);
                intent.putExtra("username", userName);
                startActivity(intent);
                finish();

            }
        });
    }
}

