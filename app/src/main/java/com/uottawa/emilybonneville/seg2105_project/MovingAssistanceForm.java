package com.uottawa.emilybonneville.seg2105_project;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MovingAssistanceForm extends AppCompatActivity {
    private TextView title;
    private Button submit;
    private String customerUserName, branchRequest;
    private EditText firstName, lastName, dob, address, email, startLocation, endLocation, numberMovers, numberBoxes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moving_assistance_form);

        title = findViewById(R.id.title);
        showPrice();
        submit = findViewById(R.id.Submitbutton3);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        dob = findViewById(R.id.dob);
        address = findViewById(R.id.address);
        email = findViewById(R.id.email);
        startLocation = findViewById(R.id.startLocation);
        endLocation = findViewById(R.id.endLocation);
        numberMovers = findViewById(R.id.numberMovers);
        numberBoxes = findViewById(R.id.numberBoxes);

        Intent intent = getIntent(); //get intent
        customerUserName =  intent.getStringExtra("customerUsername");
        branchRequest = intent.getStringExtra("customerBranchRequest");


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkDataEntered()){
                    saveRequest();
                }
            }
        });
    }

    public void saveRequest(){
        //converting EditTexts to Strings
        String firstNameStr = firstName.getText().toString();
        String lastNameStr = lastName.getText().toString();
        String dobStr = dob.getText().toString();
        String addressStr = address.getText().toString();
        String emailStr = email.getText().toString();
        String startStr = startLocation.getText().toString();
        String endStr = endLocation.getText().toString();
        String movStr = numberMovers.getText().toString();
        String boxStr = numberBoxes.getText().toString();


        StringBuilder str = new StringBuilder();
        str.append("Service: ").append("Truck Rental").append("\n First Name: ").append(firstNameStr).append("\n Last Name: ").append(lastNameStr)
                .append("\n Dob: ").append(dobStr).append("\n Address: ").append(addressStr).append("\n Email: ").append(emailStr)
                .append("\n Start Location: ").append(startStr).append("\n End Location: ").append(endStr).append("\n Number of Movers: ").append(movStr)
                .append("\n Number of Boxes: ").append(boxStr);

        String request = str.toString();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("requests").child("pending").child(branchRequest);
        String key = db.push().getKey(); //get unique key
        db.child(key).setValue(request);
        addToCustomerRequestList(key, request);
        notif("Request Submitted");
        Intent intent = new Intent(MovingAssistanceForm.this,CustomerWelcomeActivity.class);
        intent.putExtra("username", customerUserName);
        startActivity(intent);
        finish();
    }

    public void addToCustomerRequestList(String key, String requestString){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("users/customer");
        db.child(customerUserName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                user.pendingRequests.put(key, requestString);
                db.child(customerUserName).setValue(user);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void notif(String notification){
        Toast t = Toast.makeText(this, notification, Toast.LENGTH_LONG);
        t.show();
    }

    //method that makes sure all the fields are valid
    public boolean checkDataEntered() {

        //converting EditTexts to Strings
        String firstNameStr = firstName.getText().toString();
        String lastNameStr = lastName.getText().toString();
        String dobStr = dob.getText().toString();
        String addressStr = address.getText().toString();
        String emailStr = email.getText().toString();
        String startStr = startLocation.getText().toString();
        String endStr = endLocation.getText().toString();
        String movers = numberMovers.getText().toString();
        String boxes = numberBoxes.getText().toString();


        // validating first and last name
        if (!isNameValid(firstNameStr) || !isNameValid(lastNameStr)) {
            Toast t = Toast.makeText(this, "You must enter a valid name", Toast.LENGTH_SHORT);
            t.show();
            return false;
        }

        //validating date of birth field
        if (!isDateValid(dobStr)) {
            Toast t = Toast.makeText(this, "Invalid format for date of birth", Toast.LENGTH_SHORT);
            t.show();
            return false;
        } else if (!ofAge(dobStr)) {
            Toast t = Toast.makeText(this, "You must be at least 18 years old", Toast.LENGTH_SHORT);
            t.show();
            return false;
        }

        //validating address field
        if (!isAddressValid(addressStr)) {
            Toast t = Toast.makeText(this, "Invalid address, please follow format", Toast.LENGTH_SHORT);
            t.show();
            return false;
        }

        //validating email field
        if (!isEmailValid(emailStr)) {
            Toast t = Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT);
            t.show();
            return false;
        }

        if (!isAddressValid(startStr)) {
            Toast t = Toast.makeText(this, "Enter valid start location (city, province)", Toast.LENGTH_SHORT);
            t.show();
            return false;
        }

        if (!isAddressValid(endStr)) {
            Toast t = Toast.makeText(this, "Enter valid start location (city, province)", Toast.LENGTH_SHORT);
            t.show();
            return false;
        }

        if (!isMoversValid(movers)) {
            Toast t = Toast.makeText(this, "Number of movers must be between 1 and 10", Toast.LENGTH_SHORT);
            t.show();
            return false;
        }

        if (!isBoxesValid(boxes)) {
            Toast t = Toast.makeText(this, "Number of boxes must be between 1 and 800", Toast.LENGTH_SHORT);
            t.show();
            return false;
        }




        // if we reach here then all entries are valid
        return true;
    }

    //validating name field
    public Boolean isNameValid(String name) {
        Pattern p = Pattern.compile("(?i)[a-z]([- ',.a-z]{0,23}[a-z])?");
        Matcher m = p.matcher(name);
        return (m.matches());
    }

    //validating date field
    public Boolean isDateValid(String date) {
        Date date1;
        try {
            date1 = new SimpleDateFormat("yyyy/MM/dd").parse(date);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    //following 2 methods are to check if the requester is over 18 years old
    public Boolean ofAge(String date) {
        Date now = new Date();
        Date date1;
        try {
            date1 = new SimpleDateFormat("yyyy/MM/dd").parse(date);
        } catch (ParseException e) {
            return false; //if there is a parse error, then the date is in wrong format
        }
        //if try statement succeeds, then get the difference in years between dob and today's date
        Calendar a = getCalendar(date1);
        Calendar b = getCalendar(now);
        int diff = b.get(YEAR) - a.get(YEAR);
        if (a.get(MONTH) > b.get(MONTH) ||
                (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
            diff--;
        }
        if (diff < 18) {
            return false;
        }
        return true;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }

    public boolean isAddressValid(String address){
        String[] addressFields = address.split("\\s*,\\s*"); //split by comma and remove whitespace
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

    private void notify(String notification) { //helper method so I dont have to rewrite toast all the time
        Toast t = Toast.makeText(this, notification, Toast.LENGTH_SHORT);
        t.show();
    }

    //check if email is valid
    public Boolean isEmailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$"; // email should follow this format
        Pattern p = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return p.matcher(email).matches();
    }

    public Boolean isMoversValid(String movers) {
        int x;
        try {
            x = Integer.parseInt(movers);
        } catch (NumberFormatException e) {
            return false; //String is not an Integer
        }
        if (x >= 1 && x <= 10) {
            return true;
        }
        return false;
    }

    public Boolean isBoxesValid(String boxes) {
        int x;
        try {
            x = Integer.parseInt(boxes);
        } catch (NumberFormatException e) {
            return false; //String is not an Integer
        }
        if (x >= 1 && x <= 800) {
            return true;
        }
        return false;
    }

    public void showPrice(){
        //if service status is true then update button
        DatabaseReference adminServices = FirebaseDatabase.getInstance().getReference("users/admin/services/MovingAssistance/price"); // get instance of database referenc
        adminServices.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    StringBuilder current = new StringBuilder();
                    current.append(title.getText().toString());
                    current.append("\n Price: $");
                    current.append(dataSnapshot.getValue(String.class));
                    current.append(" per hr");
                    title.setText(current.toString());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}