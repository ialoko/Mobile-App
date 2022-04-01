package com.uottawa.emilybonneville.seg2105_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectBranch extends AppCompatActivity {
    private Button submit, buttonBack;
    private AutoCompleteTextView branchInput, service;
    private String customerUserName;
    private ArrayList<Employee> receiveEmployees;
    private ArrayList<String> employeeNames;
    private final String[] listOfServices = {"CarRental", "TruckRental", "MovingAssistance"};
    private final String[] Aservices = {"Car Rental", "Truck Rental", "Moving Assistance"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_branch);

        //set all buttons and textviews
        branchInput = findViewById(R.id.selectedBranch);
        service = findViewById(R.id.selectedService);
        submit = findViewById(R.id.buttonSubmit);
        buttonBack = findViewById(R.id.btnBackToCust);


        Intent intent = getIntent(); //get intent
        customerUserName =  intent.getStringExtra("username");
        receiveEmployees = (ArrayList<Employee>) intent.getSerializableExtra("listOfBranches");
        employeeNames = new ArrayList<>();


        LinearLayout linearLayout  = findViewById(R.id.listBranches);
        //int i = 1;
        for(Employee e: receiveEmployees) {
            TextView tv = new TextView(SelectBranch.this); //create new textview for each employee
            int i = View.generateViewId(); //generate unique id
            tv.setId(i);
            String value = getRepresentation(e); //create a method that turns employee to string
            tv.setText(value); //setting text of each switch to the request
            tv.setTextSize(25);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(60, 30, 30, 30);
            tv.setLayoutParams(layoutParams);
            linearLayout.post(new Runnable() { //add view in runnable thread
                @Override
                public void run() {
                    linearLayout.addView(tv, 3); //add switch to layout at 3rd position
                }
            });
            //add employee name to employeeNames
            employeeNames.add(e.username);
        }


        //set all array adapters
        ArrayAdapter<String> employeeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, employeeNames);
        ArrayAdapter<String> serviceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Aservices);
        service.setAdapter(serviceAdapter);
        branchInput.setAdapter(employeeAdapter);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectBranch.this,CustomerWelcomeActivity.class);
                intent.putExtra("username", customerUserName); //pass username back to customer welcome page
                startActivity(intent);
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkEntry()){
                    //open form based on what service.
                    String serviceString = service.getText().toString();
                    String branchInputString = branchInput.getText().toString();
                    if(serviceString.equals(Aservices[0])){
                        Intent intent = new Intent(SelectBranch.this,CarRentalForm.class);
                        intent.putExtra("customerUsername", customerUserName); //pass username
                        intent.putExtra("customerBranchRequest", branchInputString); //pass username
                        startActivity(intent);
                        finish();
                    }else if(serviceString.equals(Aservices[1])){
                        Intent intent = new Intent(SelectBranch.this,TruckRentalForm.class);
                        intent.putExtra("customerUsername", customerUserName); //pass username
                        intent.putExtra("customerBranchRequest", branchInputString); //pass username
                        startActivity(intent);
                        finish();
                    }else if(serviceString.equals(Aservices[2])){
                        Intent intent = new Intent(SelectBranch.this,MovingAssistanceForm.class);
                        intent.putExtra("customerUsername", customerUserName); //pass username
                        intent.putExtra("customerBranchRequest", branchInputString); //pass username
                        startActivity(intent);
                        finish();
                    }

                }
            }
        });
    }

    public boolean checkEntry(){
        if(!validEmployee()){
            //set error
            branchInput.setError("Branch doesn't exist");
            return false;
        }
        if(!validService()){
            //set error
            service.setError("Service doesn't exist");
            return false;
        }
        //check if employee offers service
        //loop through receiveEmployees and if input matches e name, then check if service offered
        String serviceString = service.getText().toString();
        String eService = "";
        for(int i = 0; i < Aservices.length; i++){
            if(serviceString.equals(Aservices[i])){
                eService = listOfServices[i];
                break;
            }
        }
        String branchInputString = branchInput.getText().toString();
        for(Employee e : receiveEmployees){
            if(e.username.equals(branchInputString)){
                if(e.services.get(eService).equals("true")){
                    return true;
                }
            }
        }
        notif("That branch doesn't offer that service");
        return false; //reaches here then false

    }

    public boolean validEmployee(){
        String employeeNameString = branchInput.getText().toString();
        return employeeNames.contains(employeeNameString);

    }

    public boolean validService(){
        String serviceString = service.getText().toString();
        if(serviceString.equals(Aservices[0]) || serviceString.equals(Aservices[1]) || serviceString.equals(Aservices[2])){
            return true;
        }else{
            return false;
        }
    }

    public String getRepresentation(Employee e){
        String Description = "Branch Name: "+ e.username + "\n" +  "Branch Email: " + e.email + "\n" + "Branch Phone: " +e.phone + "\n" + "Branch Address: " + e.address;
        String workingHours = "Monday: " + e.timeslot.get("Monday") + "\n" + "Tuesday: " + e.timeslot.get("Tuesday") + "\n" + "Wednesday: " + e.timeslot.get("Wednesday") +
                "\n" + "Thursday: " + e.timeslot.get("Thursday") + "\n" + "Friday: " + e.timeslot.get("Friday");

        StringBuilder servicesOffered = new StringBuilder();
        for(int i = 0; i < listOfServices.length; i++){
            if(e.services.get(listOfServices[i]).equals("true")){
                servicesOffered.append(Aservices[i]).append(", ");
            }
        }


        return Description + "\n Services Offered: " + servicesOffered.toString() + "\n Working Hours: \n" + workingHours;
    }

    public void notif(String notification){
        Toast t = Toast.makeText(this, notification, Toast.LENGTH_LONG);
        t.show();
    }
}