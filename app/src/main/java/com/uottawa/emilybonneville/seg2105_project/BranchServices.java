package com.uottawa.emilybonneville.seg2105_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class BranchServices extends AppCompatActivity {

    private CheckBox checkCarRental;
    private CheckBox checkTruckRental;
    private CheckBox checkMovingAssistance;
    private Button buttonSubmit;
    private String branchUserName;
    private final String[] listOfServices = {"CarRental", "TruckRental", "MovingAssistance"};
    private String[] savedState = new String[3]; //put saved state in here to use when saving to database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_services);

        checkCarRental = findViewById(R.id.carCheckBox);
        checkTruckRental = findViewById(R.id.truckCheckBox);
        checkMovingAssistance = findViewById(R.id.movingCheckBox);
        buttonSubmit = findViewById(R.id.btnSubmit);
        Intent intent = getIntent();
        branchUserName =  intent.getStringExtra("username"); //get username from employee welcome page
        retrieveCheckedItems(branchUserName); //then retrieve checked items or set enabled or not of each check box

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save check box details to firebase
                saveCheckedItems(branchUserName);

            }
        });
    }

    public void retrieveCheckedItems(String username){ //retrieved saved state of services check box from database
        //get data from firebase for this user
        DatabaseReference employee = FirebaseDatabase.getInstance().getReference("users/employee");
        employee.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Employee branch = dataSnapshot.getValue(Employee.class); //get employee object from database
                //if item is none, then set enabled to false, else set Checked to true or false from database
                if(branch.services.get(listOfServices[0]).equals("none")){ //car rental
                    checkCarRental.setEnabled(false);
                    savedState[0] = "none";
                }else if(branch.services.get(listOfServices[0]).equals("true")){
                    checkCarRental.setChecked(true);
                    savedState[0] = "true";
                }else if(branch.services.get(listOfServices[0]).equals("false")){
                    checkCarRental.setChecked(false);
                    savedState[0] = "false";
                }

                if(branch.services.get(listOfServices[1]).equals("none")){//truck rental
                    checkTruckRental.setEnabled(false);
                    savedState[1] = "none";

                }else if(branch.services.get(listOfServices[1]).equals("true")){
                    checkTruckRental.setChecked(true);
                    savedState[1] = "true";

                }else if(branch.services.get(listOfServices[1]).equals("false")){
                    checkTruckRental.setChecked(false);
                    savedState[1] = "false";

                }
                if(branch.services.get(listOfServices[2]).equals("none")){ //moving assistance
                    checkMovingAssistance.setEnabled(false);
                    savedState[2] = "none";

                }else if(branch.services.get(listOfServices[2]).equals("true")){
                    checkMovingAssistance.setChecked(true);
                    savedState[2] = "true";

                }else if(branch.services.get(listOfServices[2]).equals("false")){
                    checkMovingAssistance.setChecked(false);
                    savedState[2] = "false";
                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


    public void saveCheckedItems(String username){
        DatabaseReference employee = FirebaseDatabase.getInstance().getReference("users/employee");
        employee.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Employee branch = dataSnapshot.getValue(Employee.class); //get employee object from database
                if(!savedState[0].equals("none")){ //if its none then service check box isn't enabled because admin hasn't enabled it.
                    if(checkCarRental.isChecked()){
                        branch.services.put(listOfServices[0], "true"); //true if checkbox is checked
                    }else{
                        branch.services.put(listOfServices[0], "false");
                    }
                }
                if(!savedState[1].equals("none")){ //if its none then service check box isn't enabled because admin hasn't enabled it.
                    if(checkTruckRental.isChecked()){
                        branch.services.put(listOfServices[1], "true"); //true if checkbox is checked
                    }else{
                        branch.services.put(listOfServices[1], "false");
                    }
                }
                if(!savedState[2].equals("none")){ //if its none then service check box isn't enabled because admin hasn't enabled it.
                    if(checkMovingAssistance.isChecked()){
                        branch.services.put(listOfServices[2], "true"); //true if checkbox is checked
                    }else{
                        branch.services.put(listOfServices[2], "false");
                    }
                }
                employee.child(username).setValue(branch); //even if none of them are enabled, it just saves the same thing to firebase
                //take back to employee welcome page after saving services
                Intent intent = new Intent(BranchServices.this,EmployeeWelcome.class); //takes user back to employee welcome
                intent.putExtra("username", branchUserName); //pass username back to employee welcome page
                startActivity(intent);
                finish();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        notif("Saved successfully");
    }

    private void notif(String notification){
        Toast t = Toast.makeText(this, notification, Toast.LENGTH_SHORT);
        t.show();
    }
}
