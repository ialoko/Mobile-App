package com.uottawa.emilybonneville.seg2105_project;

/*
 * @author Emily Bonneville
 */

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ApproveRequest extends AppCompatActivity {

    private Button submit, buttonBackTo;
    private ToggleButton toggle;
    private HashMap<String, String> listOfRequests; //store firebase key and value for each request
    private ArrayList<Integer> listOfSwitchId;
    private String[] approveReject = {"approved", "rejected"}; //use to keep track of strings
    private String branchUserName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_request);
        //get buttons
        submit = (Button) findViewById(R.id.buttonSubmit);
        buttonBackTo = findViewById(R.id.btnBackToEmp);
        toggle = findViewById(R.id.toggleStatus);

        listOfRequests = new HashMap<>();
        listOfSwitchId = new ArrayList<>();
        Intent intent = getIntent(); //get intent
        branchUserName =  intent.getStringExtra("username");

        retrieveRequests(branchUserName); //retrieve all requests and add a toggle for each one


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get request status from toggle
                String requestStatus;
                if(toggle.isChecked()){ //get toggle state
                    requestStatus = approveReject[0]; //if checked, its approve
                }else{
                    requestStatus = approveReject[1]; //else its reject
                }
                saveRequests(branchUserName, requestStatus);
            }
        });

        buttonBackTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApproveRequest.this,EmployeeWelcome.class); //takes user back to employee welcome
                intent.putExtra("username", branchUserName); //pass username back to employee welcome page
                startActivity(intent);
                finish();
            }
        });
    }


    public void retrieveRequests(String branchAccount){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("requests/pending"); //get all pending requests
        db.child(branchAccount).addListenerForSingleValueEvent(new ValueEventListener() {//get pending requests for this branch
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 for (DataSnapshot dsp : dataSnapshot.getChildren()) { //loop through all pending requests
                     String request = dsp.getValue(String.class);
                     listOfRequests.put(dsp.getKey(), request);
                 }

                 LinearLayout linearLayout  = findViewById(R.id.listRequests);
                 //int i = 1;
                 for(Map.Entry mapElement: listOfRequests.entrySet()) {
                     Switch sw = new Switch(ApproveRequest.this); //create new switch for each request
                     int i = View.generateViewId(); //generate unique id
                     sw.setId(i);
                     listOfSwitchId.add(i); //so we can track randomly generated IDs
                     String value = (String)mapElement.getValue();
                     sw.setText(value); //setting text of each switch to the request
                     sw.setTextSize(25);
                     LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                     layoutParams.setMargins(60, 30, 30, 30);
                     sw.setLayoutParams(layoutParams);
                     linearLayout.post(new Runnable() { //add view in runnable thread
                         @Override
                         public void run() {
                             linearLayout.addView(sw, 0); //add switch to layout at top

                         }
                     });
                 }

                 if(listOfRequests.isEmpty()){
                     toggle.setText("You have no requests. Come back later");
                     submit.setText("Submission is disabled. Use back button");
                     submit.setEnabled(false);//disable submit button to avoid issues
                 }
             }
             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
        });


    }

    public void saveRequests(String branchAccount, String status){
        //loop through switches to get which ones are checked, remove from pending. If status is approved, add to approved, if status is rejected, add to rejected
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("requests"); // first remove from pending
        boolean someChecked = false; //if no switch is checked, tell user to check something
        for(Integer switchID : listOfSwitchId){ //loop through SwitchIDs
            Switch switchR = (Switch)findViewById(switchID.intValue()); //get Id of switch
            if(switchR != null){
                if(switchR.isChecked()){ //if the switch is checked, then approve or reject
                    //iterate through listofrequests. If switch text equals to value, then remove key from
                    someChecked = true;
                    for(Map.Entry mapElement: listOfRequests.entrySet()) {
                        String requestValue = (String)mapElement.getValue();
                        String compareTo = switchR.getText().toString();
                        if(compareTo.equals(requestValue)){
                            String requestKey = (String)mapElement.getKey();
                            //remove from pending
                            db.child("pending").child(branchAccount).child(requestKey).removeValue(); //remove that request from pending
                            db.child(status).child(branchAccount).child(requestKey).setValue(requestValue); //add that request to approved or rejected
                        }
                    }
                }
            }
        }
        if(!someChecked){ //if nothing is checked, tell user to check something
            notify("You must check something");
        }else{
            notify("Saved successfully");
            Intent intent = new Intent(ApproveRequest.this,EmployeeWelcome.class);
            intent.putExtra("username", branchUserName); //pass username back to employee welcome page
            startActivity(intent);
            finish();
        }
    }

    public void notify(String notification) { //reuse code to notify
        Toast t = Toast.makeText(this, notification, Toast.LENGTH_SHORT);
        t.show();
    }
}