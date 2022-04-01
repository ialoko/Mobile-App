package com.uottawa.emilybonneville.seg2105_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BranchHours extends AppCompatActivity {
    public Switch monday, tuesday, wednesday, thursday, friday;
    private EditText monStart, monEnd, tueStart, tueEnd, wedStart, wedEnd, thurStart, thurEnd, friStart, friEnd;
    private Button buttonSubmit;
    private Button buttonBack;
    private String branchUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_hours);

        //instantiate switches and buttons
        monday = findViewById(R.id.monday);
        tuesday = findViewById(R.id.tuesday);
        wednesday = findViewById(R.id.wednesday);
        thursday = findViewById(R.id.thursday);
        friday = findViewById(R.id.friday);
        buttonBack = findViewById(R.id.buttonBack); //remember to fill in
        buttonSubmit = findViewById(R.id.buttonSubmit);

        //instantiate edittext
        monStart = findViewById(R.id.monStart);
        monEnd = findViewById(R.id.monEnd);
        tueStart = findViewById(R.id.tueStart);
        tueEnd = findViewById(R.id.tueEnd);
        wedStart = findViewById(R.id.wedStart);
        wedEnd = findViewById(R.id.wedEnd);
        thurStart = findViewById(R.id.thurStart);
        thurEnd = findViewById(R.id.thurEnd);
        friStart = findViewById(R.id.friStart);
        friEnd = findViewById(R.id.friEnd);


        //get current employee username
        Intent intent = getIntent();
        branchUserName =  intent.getStringExtra("username");

        retrieveHours(branchUserName); //retrieve hours from database

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BranchHours.this,EmployeeWelcome.class); //takes user back to employee welcome
                intent.putExtra("username", branchUserName); //pass username back to employee welcome page
                startActivity(intent);
                finish();
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //save hours to firebase
                if(validHours()){
                    saveHours(branchUserName);
                    notif("Saved Successfully");
                }
            }
        });
    }

    //retrieveHours
    public void retrieveHours(String username){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("users/employee");
        db.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Employee employee = snapshot.getValue(Employee.class);
                assert employee != null;
                //get hours for employee and set checked for each switch
                Boolean Mon = employee.hours.get("Monday"); //true = open; false = closed/default
                Boolean Tue = employee.hours.get("Tuesday");
                Boolean Wed = employee.hours.get("Wednesday");
                Boolean Thur = employee.hours.get("Thursday");
                Boolean Fri = employee.hours.get("Friday");

                //add check to make sure Boolean isn't null
                if(Mon == null) Mon = false;
                if(Tue == null) Tue = false;
                if(Wed == null) Wed = false;
                if(Thur == null) Thur = false;
                if(Fri == null) Fri = false;

                monday.setChecked(Mon); //set the toggles
                tuesday.setChecked(Tue);
                wednesday.setChecked(Wed);
                thursday.setChecked(Thur);
                friday.setChecked(Fri);


                //set time slot if its open for that day
                if(Mon){
                    String monSlot = employee.timeslot.get("Monday");
                    //split by "-"
                    String[] monSlots = monSlot.split("\\s*-\\s*");
                    monStart.setText(monSlots[0]);
                    monEnd.setText(monSlots[1]);
                }
                if(Tue){
                    String tueSlot = employee.timeslot.get("Tuesday");
                    //split by "-"
                    String[] tueSlots = tueSlot.split("\\s*-\\s*");
                    tueStart.setText(tueSlots[0]);
                    tueEnd.setText(tueSlots[1]);
                }
                if(Wed){
                    String wedSlot = employee.timeslot.get("Wednesday");
                    //split by "-"
                    String[] wedSlots = wedSlot.split("\\s*-\\s*");
                    wedStart.setText(wedSlots[0]);
                    wedEnd.setText(wedSlots[1]);
                }
                if(Thur){
                    String thurSlot = employee.timeslot.get("Thursday");
                    //split by "-"
                    String[] thurSlots = thurSlot.split("\\s*-\\s*");
                    thurStart.setText(thurSlots[0]);
                    thurEnd.setText(thurSlots[1]);
                }
                if(Fri){
                    String friSlot = employee.timeslot.get("Friday");
                    //split by "-"
                    String[] friSlots = friSlot.split("\\s*-\\s*");
                    friStart.setText(friSlots[0]);
                    friEnd.setText(friSlots[1]);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase","Error saving branch description to database");

            }
        });


    }

    //save hours
    public void saveHours(String username){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("users/employee");
        db.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Employee employee = snapshot.getValue(Employee.class);
                assert employee != null;
                //get isChecked for each switch and set in database
                employee.hours.put("Monday", monday.isChecked());
                employee.hours.put("Tuesday", tuesday.isChecked());
                employee.hours.put("Wednesday", wednesday.isChecked());
                employee.hours.put("Thursday", thursday.isChecked());
                employee.hours.put("Friday", friday.isChecked());

                //set time slot
                String monTS = monStart.getText().toString() + "-" + monEnd.getText().toString();
                String tueTS = tueStart.getText().toString() + "-" + tueEnd.getText().toString();
                String wedTS = wedStart.getText().toString() + "-" + wedEnd.getText().toString();
                String thurTS = thurStart.getText().toString() + "-" + thurEnd.getText().toString();
                String friTS = friStart.getText().toString() + "-" + friEnd.getText().toString();

                employee.timeslot.put("Monday", monTS);
                employee.timeslot.put("Tuesday",tueTS);
                employee.timeslot.put("Wednesday", wedTS);
                employee.timeslot.put("Thursday", thurTS);
                employee.timeslot.put("Friday", friTS);


                db.child(username).setValue(employee); //update data in firebase

                Intent intent = new Intent(BranchHours.this,EmployeeWelcome.class); //takes user back to employee welcome
                intent.putExtra("username", branchUserName); //pass username back to employee welcome page
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase","Error saving branch hours to database");

            }
        });
    }

    public boolean validHours(){
        if(monday.isChecked()){ //only validate if the toggle says open
            String m1 = monStart.getText().toString();
            String m2 = monEnd.getText().toString();
            if(notValidTimeSlot(m1, m2)){
                monStart.setError("Check format");
                monEnd.setError("Check format");
                return false;
            }
        }
        if(tuesday.isChecked()){
            String t1 = tueStart.getText().toString();
            String t2 = tueEnd.getText().toString();
            if(notValidTimeSlot(t1, t2)){
                tueStart.setError("Check format");
                tueEnd.setError("Check format");
                return false;
            }
        }
        if(wednesday.isChecked()){
            String w1 = wedStart.getText().toString();
            String w2 = wedEnd.getText().toString();
            if(notValidTimeSlot(w1, w2)){
                wedStart.setError("Check format");
                wedEnd.setError("Check format");
                return false;
            }
        }
        if(thursday.isChecked()){
            String th1 = thurStart.getText().toString();
            String th2 = thurEnd.getText().toString();
            if(notValidTimeSlot(th1, th2)){
                thurStart.setError("Check format");
                thurEnd.setError("Check format");
                return false;
            }
        }
        if(friday.isChecked()){
            String f1 = friStart.getText().toString();
            String f2 = friEnd.getText().toString();
            if(notValidTimeSlot(f1, f2)){
                friStart.setError("Check format");
                friEnd.setError("Check format");
                return false;
            }
        }
        return true;
    }

    public boolean notValidTimeSlot(String start, String end){//returns true if not valid time slot
        if(!start.contains(":") || !end.contains(":")){
            notif("Must use : ");
            return true;
        }

        char colon = ':';
        int countStart = 0;
        for (int i = 0; i < start.length(); i++) { //counting occurrences of colon in string
            if (start.charAt(i) == colon) {
                countStart++;
            }
        }

        int countEnd = 0;
        for (int i = 0; i < end.length(); i++) {
            if (end.charAt(i) == colon) {
                countEnd++;
            }
        }
        if((countStart != 1) || countEnd != 1){
            notif("Only use one colon");
            return true;
        }

        String[] startArray = start.split("\\s*:\\s*"); //split by colon and remove whitespace
        String[] endArray = end.split("\\s*:\\s*");
        if((startArray.length != 2) || (endArray.length != 2)){ //length should be 2 for hr:min
            return true;
        }

        if((!startArray[1].equals("00")) || (!endArray[1].equals("00"))){ //minute must be zero zero
            notif("For simplicity, minutes must be 00");
            return true;
        }

        String hourStart = startArray[0];
        String hourEnd = endArray[0];
        Pattern p1 = Pattern.compile("^\\d{1,2}$"); //number has to be 1 to 2 digits
        Matcher matchStart = p1.matcher(hourStart);
        Matcher matchEnd = p1.matcher(hourEnd);
        if(!matchStart.matches() || !matchEnd.matches()){
            notif("hours must be 1 to 2 digits");
            return true;
        }

        //hours must be between 0 and 24
        int hrStart;
        int hrEnd;

        try {
            hrStart = Integer.parseInt(hourStart); //convert string to integer
        } catch(NumberFormatException | NullPointerException e) {
            notif("Hours must be integers");
            return true;
        }
        try {
            hrEnd = Integer.parseInt(hourEnd); //convert string to integer
        } catch(NumberFormatException | NullPointerException e) {
            notif("Hours must be integers");
            return true;
        }

        if(!(hrStart < 24) || !(hrEnd < 24)){ //hour 24 corresponds to next day
            notif("Max hour is 23:00. 24:00 corresponds to 00:00 on next day");
            return true;
        }
        if(!(hrStart >= 0) || !(hrEnd >= 0)){ //min hour is 0
            notif("Min hour is 00:00 for midnight");
            return true;
        }

        //now make sure start is less than end
        if(!(hrStart < hrEnd)){
            notif("Start time is greater than end time");
            return true;
        }

        //reaches here then is valid
        return false;
    }

    private void notif(String notification){
        Toast t = Toast.makeText(this, notification, Toast.LENGTH_SHORT);
        t.show();
    }
}

