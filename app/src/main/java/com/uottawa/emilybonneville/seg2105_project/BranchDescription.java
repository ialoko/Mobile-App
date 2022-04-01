package com.uottawa.emilybonneville.seg2105_project;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BranchDescription extends AppCompatActivity {

    private TextView textDescription;
    private EditText textAddress;
    private EditText textPhone;
    private Button buttonSubmitChanges;
    private Button buttonBackTo;
    private String branchUserName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_description);

        textDescription = findViewById(R.id.description);
        textAddress = findViewById(R.id.branchAddress);
        textPhone = findViewById(R.id.branchPhone);
        buttonSubmitChanges = findViewById(R.id.btnChanges);
        buttonBackTo = findViewById(R.id.btnBackTo);
        Intent intent = getIntent();
        branchUserName =  intent.getStringExtra("username");

        getDescription(branchUserName); //method to get description of branch from database


        buttonBackTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BranchDescription.this,EmployeeWelcome.class); //takes user back to employee welcome
                intent.putExtra("username", branchUserName); //pass username back to employee welcome page
                startActivity(intent);
                finish();
            }
        });

        buttonSubmitChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validEntry()){ //save to database if its valid entry
                    saveDescription(branchUserName); //save description to firebase
                    notif("Saved Successfully");
                }
            }
        });


    }


    public void saveDescription(String username){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("users/employee");
        db.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Employee employee = snapshot.getValue(Employee.class);
                assert employee != null;
                String phoneNumber = textPhone.getText().toString();
                String address = textAddress.getText().toString();
                //set the values of employee phone and address
                employee.phone = phoneNumber;
                employee.address = address;
                db.child(username).setValue(employee); //update data in firebase

                //take back to employee welcome page after saving description
                Intent intent = new Intent(BranchDescription.this,EmployeeWelcome.class); //takes user back to employee welcome
                intent.putExtra("username", branchUserName); //pass username back to employee welcome page
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase","Error saving branch description to database");

            }
        });
    }

    //helper methods to validate user entry
    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    boolean isValidNumber(EditText text){
        String str = text.getText().toString();
        Pattern p = Pattern.compile("^\\d{9,12}$"); //number has to be 9-12 digits
        Matcher m = p.matcher(str);
        return (m.matches());
    }

    boolean isValidAddress(EditText text){
        String str = text.getText().toString();
        String[] addressFields = str.split("\\s*,\\s*"); //split by comma and remove whitespace
        //Pattern p = Pattern.compile("^[#.0-9a-zA-Z\\s,-]+$"); //address validation, values address can contain
        //Matcher m = p.matcher(str);
        //return (m.matches());

        if(addressFields.length != 6){
            notify("Please follow the address format");
            return false;
        }

        if(!validUnit(addressFields[0])){
            notify("Not valid Unit number. # and numbers only");
            return false;
        }
        if(!validLocation(addressFields[1])){
            notify("Invalid street name. No special characters or numbers allowed. Atleast 2 characters");
            return false;
        }
        if(!validLocation(addressFields[2])){
            notify("invalid city. No special characters or numbers allowed. Atleast 2 characters");
            return false;
        }
        if(!validLocation(addressFields[3])){
            notify("invalid state. No special characters or numbers allowed. Atleast 2 characters");
            return false;
        }
        if(!validPostalCode(addressFields[4])){
            notify("invalid postal code. Must be at least 3 digits.");
            return false;
        }
        if(!validLocation(addressFields[5])){
            notify("invalid country. No special characters or numbers allowed. Atleast 2 characters");
            return false;
        }
        return true;

    }
    boolean validUnit(String str){
        Pattern p = Pattern.compile("^[#.0-9\\s,-]+$"); //address validation, values address can contain
        Matcher m = p.matcher(str);
        return (m.matches());

    }

    boolean validPostalCode(String str){
        Pattern p = Pattern.compile("^[#.0-9a-zA-Z\\s,-]{3,}$"); //address validation 3 or more characters
        Matcher m = p.matcher(str);
        return (m.matches());
    }

    boolean validLocation(String str){ //used for street name, city, state, country
        Pattern p = Pattern.compile("^[a-zA-Z\\u0080-\\u024F\\s\\/\\-\\)\\(`\\.\\\"\\']{2,}"); //address validation, values address can contain
        Matcher m = p.matcher(str);
        return (m.matches());
    }

    public boolean validEntry(){
        if(isEmpty(textAddress) || isEmpty(textPhone)) { //checking if user didn't enter anything
            textAddress.setError("Mandatory field!");
            textPhone.setError("Mandatory field!");
            return false;
        }
        if(!isValidNumber(textPhone)){
            notify("Phone number must be 9 to 12 digits");
            return false;
        }
        if(!isValidAddress(textAddress)){
            //notify("Address not valid. Letters, numbers, # and - are allowed");
            return false;
        }
        notify("Changes Saved Successfully");
        return true; //if we reach here, then everything is valid

    }

    private void notify(String notification) { //helper method so I dont have to rewrite toast all the time
        Toast t = Toast.makeText(this, notification, Toast.LENGTH_SHORT);
        t.show();
    }

    public void getDescription(String username){
        //if info retrieved from firebase is null, then ask user to enter info
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("users/employee");
        db.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Employee employee = snapshot.getValue(Employee.class);
                assert employee != null;
                String phoneNumber = employee.phone; //add to employe class
                String address = employee.address; //add to employee class
                String enterInfo = "Please enter info";

                if(phoneNumber == null){
                    phoneNumber = "none";
                }
                if(address == null){
                    address = "none";
                }

                if(phoneNumber.equals("none") && address.equals("none")) { //neither are set in database
                    textDescription.setText(String.format("Phone Number: %1$s \n Address: %2$s ", enterInfo, enterInfo)); //prompt user to enter info
                }else{
                    //set description to information retrieved from database
                    textDescription.setText(String.format("Phone Number: %1$s \n Address: %2$s ", phoneNumber, address)); //both are set
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase","Error retrieving branch description");

            }
        });

    }
    private void notif(String notification){
        Toast t = Toast.makeText(this, notification, Toast.LENGTH_SHORT);
        t.show();
    }


}
