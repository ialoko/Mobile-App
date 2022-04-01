package com.uottawa.emilybonneville.seg2105_project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class CustomerWelcomeActivity extends AppCompatActivity{


    /**
     * @author Fatemeh Omidi
     */

    private String TAG = this.getClass().getName();
    private Button btnLogout, btnRate, btnCreateRequest, btnSearch, btnViewRequest;
    private TextView tvUserName;
    private String userName;
    private ArrayList<Employee> sendEmployees; //list of employees I will send through intent

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_welcome);

        btnLogout = findViewById(R.id.btnLogout);
        btnRate = findViewById(R.id.btnRate);
        btnCreateRequest = findViewById(R.id.btnCreateRequest);
        btnSearch = findViewById(R.id.btnSearch);
        btnViewRequest = findViewById(R.id.btnViewStatus);
        tvUserName = findViewById(R.id.tvUserName);
        sendEmployees = new ArrayList<>();



        //get string username intent from login
        Intent intent = getIntent();
        userName = intent.getStringExtra("username");
        tvUserName.setText(String.format("Welcome %s", userName));

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerWelcomeActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnCreateRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get all Employees. Store in arraylist and send to SelectBranch
                getAllEmployees();
            }
        });

        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerWelcomeActivity.this,ReviewBranch.class);
                intent.putExtra("username", userName);
                startActivity(intent);
                finish();
            }
        });

        btnViewRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerWelcomeActivity.this,CustomerViewRequests.class);
                intent.putExtra("username", userName);
                startActivity(intent);
                finish();
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerWelcomeActivity.this,SearchBranch.class);
                intent.putExtra("username", userName);
                startActivity(intent);
                finish();
            }
        });
    }

    public void getAllEmployees(){
        DatabaseReference employeeref = FirebaseDatabase.getInstance().getReference("users/employee");
        employeeref.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child : snapshot.getChildren()){
                    Employee employee = child.getValue(Employee.class);
                    if(employee == null){ //verify only valid entries getting into array list
                        continue;
                    }
                    sendEmployees.add(employee); //then add to list of employees to send
                }
                Intent intent = new Intent(CustomerWelcomeActivity.this,SelectBranch.class);
                intent.putExtra("username", userName);
                intent.putExtra("listOfBranches", sendEmployees);
                startActivity(intent);
                finish();


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

}
