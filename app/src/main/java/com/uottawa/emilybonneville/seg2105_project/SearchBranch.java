package com.uottawa.emilybonneville.seg2105_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchBranch extends AppCompatActivity {
    private AutoCompleteTextView day,  address, service;
    private EditText start, end;
    private Button back, search;
    private final String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    private final String[] listOfServices = {"CarRental", "TruckRental", "MovingAssistance"};
    private final String[] Aservices = {"Car Rental", "Truck Rental", "Moving Assistance"};
    private ArrayList<String> listAddresses;
    private ArrayAdapter<String> daysAdapter;
    private ArrayAdapter<String> addressAdapter;
    private ArrayAdapter<String> serviceAdapter;
    private String customerUserName;
    private ArrayList<Employee> sendEmployees; //list of employees I will send through intent
    private boolean enterTime, enterAddress, enterService; //use to track what input the user used
    private ArrayList<Employee> byDay; //search for each and do intersection
    private ArrayList<Employee> byA;
    private ArrayList<Employee> byS;
    private ArrayList<Employee> Temp; //get all employees in firebase and filter by search, then put in sendEmployees



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_branch);

        listAddresses = new ArrayList<>();
        sendEmployees = new ArrayList<>();
        byDay = new ArrayList<>(); //search for each and do intersection
        byA = new ArrayList<>();
        byS = new ArrayList<>();
        Temp = new ArrayList<>();
        getAllAddresses();//get all employees

        day = findViewById(R.id.day);
        start = findViewById(R.id.Start);
        end = findViewById(R.id.End);
        service = findViewById(R.id.services);
        address = findViewById(R.id.address);
        back = findViewById(R.id.button2);
        search = findViewById(R.id.search);

        Intent intent = getIntent();//get username intent
        customerUserName = intent.getStringExtra("username");

        //set all array adapters
        daysAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, daysOfWeek);
        serviceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Aservices);
        addressAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listAddresses);
        day.setAdapter(daysAdapter);
        service.setAdapter(serviceAdapter);
        address.setAdapter(addressAdapter);





        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchBranch.this,CustomerWelcomeActivity.class);
                intent.putExtra("username", customerUserName); //pass username back to customer welcome page
                startActivity(intent);
                finish();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set flag for each entered input
                enterAddress = !isEmpty(address); //true is address isn't empty
                enterService = !isEmpty(service);
                enterTime = !isEmpty(day) || !isEmpty(start) || !isEmpty(end); //if any of them isn't empty then it sets to true for time

                if(enterAddress && enterService && enterTime){ //entered everything
                    searchBranches("AST");

                }else if(enterAddress && enterService && !enterTime){ //entered all except time
                    searchBranches("AS");

                }else if(enterAddress && !enterService && enterTime){ //entered all except service
                    searchBranches("AT");

                }else if(enterAddress && !enterService && !enterTime){//entered address only
                    searchBranches("A");

                }else if(!enterAddress && enterService && enterTime){//entered all except address
                    searchBranches("ST");

                }else if(!enterAddress && enterService && !enterTime){//entered only service
                    searchBranches("S");

                }else if(!enterAddress && !enterService && enterTime){//entered only time
                    searchBranches("T");

                }else if(!enterAddress && !enterService && !enterTime){ //didnt enter anything
                    notif("You must enter something");
                }
            }
        });

    }

    public void openActivity(){
        Intent intent = new Intent(SearchBranch.this, SelectBranch.class);
        intent.putExtra("username", customerUserName);
        intent.putExtra("listOfBranches", (Serializable) sendEmployees);
        startActivity(intent);
        finish();

    }

    public void searchBranches(String flag){ //search by what
        //loop through temp array list to
        String addressString = address.getText().toString();
        String serviceString = service.getText().toString();
        String dayString = day.getText().toString();
        if(flag.equals("AST")){ //check what flag is
            for(Employee e: Temp){ //loop through Temp to match employees
                //by address
                if(e.address.equals(addressString)){
                    byA.add(e);
                }
                //by services
                for(int i = 0; i < listOfServices.length; i++){
                    if(serviceString.equals(Aservices[i])){
                        if(e.services.get(listOfServices[i]).equals("true")){
                            byS.add(e);

                        }
                    }
                }
                //by hours
                for(int i = 0; i < daysOfWeek.length; i++){
                    if(dayString.equalsIgnoreCase(daysOfWeek[i])){ //ignore upper and lower case
                        if(e.hours.get(daysOfWeek[i])){ //if open on that day
                            if(timeSlotMatches(e.timeslot.get(daysOfWeek[i]))){
                                byDay.add(e);
                            }

                        }
                    }
                }
            }
            //intersection
            byA.retainAll(byS);
            byA.retainAll(byDay);
            if(byA.isEmpty()){
                notif("Sorry no branches match your description. Try again");
            }else{
                sendEmployees.addAll(byA);
                //then send all employees to other page
                openActivity();
            }



        }else if(flag.equals("AT")){ //check what flag is
            for(Employee e: Temp){ //loop through Temp to match employees
                //by address
                if(e.address.equals(addressString)){
                    byA.add(e);
                }
                //by hours
                for(int i = 0; i < daysOfWeek.length; i++){
                    if(dayString.equalsIgnoreCase(daysOfWeek[i])){ //ignore upper and lower case
                        if(e.hours.get(daysOfWeek[i])){ //if open on that day
                            if(timeSlotMatches(e.timeslot.get(daysOfWeek[i]))){
                                byDay.add(e);
                            }

                        }
                    }
                }
            }
            //intersection
            byA.retainAll(byDay);
            if(byA.isEmpty()){
                notif("Sorry no branches match your description. Try again");
            }else{
                sendEmployees.addAll(byA);
                //then send all employees to other page
                openActivity();
            }

        }else if(flag.equals("AS")){ //check what flag is
            for(Employee e: Temp){ //loop through Temp to match employees
                //by address
                if(e.address.equals(addressString)){
                    byA.add(e);
                }
                //by services
                for(int i = 0; i < listOfServices.length; i++){
                    if(serviceString.equals(Aservices[i])){
                        if(e.services.get(listOfServices[i]).equals("true")){
                            byS.add(e);

                        }
                    }
                }
            }
            //intersection
            byA.retainAll(byS);
            if(byA.isEmpty()){
                notif("Sorry no branches match your description. Try again");
            }else{
                sendEmployees.addAll(byA);
                //then send all employees to other page
                openActivity();
            }

        }else if(flag.equals("ST")){ //check what flag is
            for(Employee e: Temp){ //loop through Temp to match employees
                //by services
                for(int i = 0; i < listOfServices.length; i++){
                    if(serviceString.equals(Aservices[i])){
                        if(e.services.get(listOfServices[i]).equals("true")){
                            byS.add(e);

                        }
                    }
                }
                //by hours
                for(int i = 0; i < daysOfWeek.length; i++){
                    if(dayString.equalsIgnoreCase(daysOfWeek[i])){ //ignore upper and lower case
                        if(e.hours.get(daysOfWeek[i])){ //if open on that day
                            if(timeSlotMatches(e.timeslot.get(daysOfWeek[i]))){
                                byDay.add(e);
                            }

                        }
                    }
                }
            }
            //intersection
            byS.retainAll(byDay);
            if(byS.isEmpty()){
                notif("Sorry no branches match your description. Try again");
            }else{
                sendEmployees.addAll(byS);
                //then send all employees to other page
                openActivity();
            }

        }else if(flag.equals("A")){ //check what flag is
            for(Employee e: Temp){ //loop through Temp to match employees
                //by address
                if(e.address.equals(addressString)){
                    byA.add(e);
                }
            }

            if(byA.isEmpty()){
                notif("Sorry no branches match your description. Try again");
            }else{
                sendEmployees.addAll(byA);
                //then send all employees to other page
                openActivity();
            }

        }else if(flag.equals("S")){ //check what flag is
            for(Employee e: Temp){ //loop through Temp to match employees
                //by services
                for(int i = 0; i < listOfServices.length; i++){
                    if(serviceString.equals(Aservices[i])){
                        if(e.services.get(listOfServices[i]).equals("true")){
                            byS.add(e);

                        }
                    }
                }
            }

            if(byS.isEmpty()){
                notif("Sorry no branches match your description. Try again");
            }else{
                sendEmployees.addAll(byS);
                //then send all employees to other page
                openActivity();
            }

        }else if(flag.equals("T")){ //check what flag is
            for(Employee e: Temp){ //loop through Temp to match employees
                //by hours
                for(int i = 0; i < daysOfWeek.length; i++){
                    if(dayString.equalsIgnoreCase(daysOfWeek[i])){ //ignore upper and lower case
                        if(e.hours.get(daysOfWeek[i])){ //if open on that day
                            if(timeSlotMatches(e.timeslot.get(daysOfWeek[i]))){
                                byDay.add(e);
                            }

                        }
                    }
                }
            }

            if(byDay.isEmpty()){
                notif("Sorry no branches match your description. Try again");
            }else{
                sendEmployees.addAll(byDay);
                //then send all employees to other page
                openActivity();
            }
        }
    }

    private boolean timeSlotMatches(String employeeTimeSlot){
        String startString = start.getText().toString();
        String endString = end.getText().toString();

        String[] startArray = startString.split("\\s*:\\s*"); //split by colon and remove whitespace
        String[] endArray = endString.split("\\s*:\\s*");
        String[] tS = employeeTimeSlot.split("\\s*-\\s*"); //split by seperator
        String[] eStartArray = tS[0].split("\\s*:\\s*"); //split by colon and remove whitespace
        String[] eEndArray = tS[1].split("\\s*:\\s*");

        int hrStart = Integer.parseInt(startArray[0]); //convert string to integer
        int hrEnd = Integer.parseInt(endArray[0]);
        int hrStartEmp = Integer.parseInt(eStartArray[0]); //convert string to integer
        int hrEndEmp = Integer.parseInt(eEndArray[0]);

        if((hrStartEmp <= hrStart) && (hrEnd <= hrEndEmp)){ //time falls between open hours
            return true;
        }else{
            return false;
        }





    }

    public void getAllAddresses(){
        DatabaseReference employeeref = FirebaseDatabase.getInstance().getReference("users/employee");
        employeeref.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child : snapshot.getChildren()){
                    Employee employee = child.getValue(Employee.class);
                    if(employee == null){ //verify only valid entries getting into array list
                        continue;
                    }
                    listAddresses.add(employee.address); //then add to list of addresses
                    Temp.add(employee); //to use later when searching
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    public boolean validAddress(){
        String addressString = address.getText().toString();
        return listAddresses.contains(addressString);

    }

    public boolean validService(String serviceString){
        if(serviceString.equals(Aservices[0]) || serviceString.equals(Aservices[1]) || serviceString.equals(Aservices[2])){
            return true;
        }else{
            return false;
        }
    }

    public boolean validWorkingHours(){
        String dayString = day.getText().toString();
        String startString = start.getText().toString();
        String endString = end.getText().toString();

        if(isEmpty(day)){
            day.setError("Day is mandatory.");
            return false;
        }

        if(notValidTimeSlot(startString, endString)){
            start.setError("Invalid start and end time");
            return false;
        }



        for(String element : daysOfWeek){
            if(dayString.equalsIgnoreCase(element)){ //ignore upper and lower case
                return true;
            }
        }
        return false; //if reaches here then false
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

    boolean isEmpty(TextView text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    public void notif(String notification){
        Toast t = Toast.makeText(this, notification, Toast.LENGTH_LONG);
        t.show();
    }
}