package com.uottawa.emilybonneville.seg2105_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import java.util.HashMap;



public class ServiceInfo extends AppCompatActivity implements View.OnClickListener {
    private final String[] listOfServices = {"CarRental", "TruckRental", "MovingAssistance"}; // using an array to make sure the names of service is automatically valid
    private Button backBtn, buttonCar, buttonTruck, buttonMoving, editCar, editTruck, editMoving;
    private Switch firstNameCar, lastNameCar, dobCar, addressCar, emailCar, licenseTypeCar, carType, pickupDateCar, pickupTimeCar, returnDateCar, returnTimeCar,
            firstNameTruck, lastNameTruck, dobTruck, addressTruck, emailTruck, licenseTypeTruck, pickupDateTruck, pickupTimeTruck, returnDateTruck, returnTimeTruck,
            maxkm, areaUsed, firstNameMoving, lastNameMoving, dobMoving, addressMoving, emailMoving, startLocation, endLocation, numMovers, numBoxes;
    private EditText priceCar, priceTruck, priceMoving;
    /* Quick Explanation of Service Info
    *
    * If boolean value for input field is true, then that service requires that input field on the form
    * retrieve saved state from firebase in on create method and update toggle
    *
    *
    *  */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_info);

        //get buttons
        backBtn = findViewById(R.id.backBtn);
        buttonCar = findViewById(R.id.buttonCar);
        buttonTruck = findViewById(R.id.buttonTruck);
        buttonMoving = findViewById(R.id.buttonMoving);
        editCar = findViewById(R.id.editCar);
        editTruck = findViewById(R.id.editTruck);
        editMoving = findViewById(R.id.editMoving);

        priceCar = findViewById(R.id.priceCarRental);
        priceTruck = findViewById(R.id.priceTruckRental);
        priceMoving = findViewById(R.id.priceMovingAssistance);

        //update buttons for services for add/delete
        serviceExists(listOfServices[0]);
        serviceExists(listOfServices[1]);
        serviceExists(listOfServices[2]);

        //set on click listener
        backBtn.setOnClickListener(this);
        buttonCar.setOnClickListener(this);
        buttonTruck.setOnClickListener(this);
        buttonMoving.setOnClickListener(this);
        editCar.setOnClickListener(this);
        editTruck.setOnClickListener(this);
        editMoving.setOnClickListener(this);



        //get switches
        firstNameCar = findViewById(R.id.firstNameCar);
        lastNameCar = findViewById(R.id.lastNameCar);
        dobCar = findViewById(R.id.dobCar);
        addressCar = findViewById(R.id.addressCar);
        emailCar = findViewById(R.id.emailCar);
        licenseTypeCar = findViewById(R.id.licenseTypeCar);
        carType = findViewById(R.id.carType);
        pickupDateCar = findViewById(R.id.pickupDateCar);
        pickupTimeCar = findViewById(R.id.pickupTimeCar);
        returnDateCar = findViewById(R.id.returnDateCar);
        returnTimeCar = findViewById(R.id.returnTimeCar);
        firstNameTruck = findViewById(R.id.firstNameTruck);
        lastNameTruck = findViewById(R.id.lastNameTruck);
        dobTruck = findViewById(R.id.dobTruck);
        addressTruck = findViewById(R.id.addressTruck);
        emailTruck = findViewById(R.id.emailTruck);
        licenseTypeTruck = findViewById(R.id.licenseTypeTruck);
        pickupDateTruck = findViewById(R.id.pickupDateTruck);
        pickupTimeTruck = findViewById(R.id.pickupTimeTruck);
        returnDateTruck = findViewById(R.id.returnDateTruck);
        returnTimeTruck = findViewById(R.id.returnTimeTruck);
        maxkm = findViewById(R.id.maxkm);
        areaUsed = findViewById(R.id.areaUsed);
        firstNameMoving = findViewById(R.id.firstNameMoving);
        lastNameMoving = findViewById(R.id.lastNameMoving);
        dobMoving = findViewById(R.id.dobMoving);
        addressMoving = findViewById(R.id.addressMoving);
        emailMoving = findViewById(R.id.emailMoving);
        startLocation = findViewById(R.id.startLocation);
        endLocation = findViewById(R.id.endLocation);
        numMovers = findViewById(R.id.numberMovers);
        numBoxes = findViewById(R.id.numBoxes);

        //get toggle status from database and then update UI
        getToggle(listOfServices[0]);
        getToggle(listOfServices[1]);
        getToggle(listOfServices[2]);
    }


    //buttons

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                //switch intent back to AdminWelcome
                Intent intent = new Intent(ServiceInfo.this,AdminWelcome.class);
                startActivity(intent);
                break;
            case R.id.buttonCar:
                //check for whether the text says add or delete, then after operation, change text
                //if add then call add service
                if(buttonCar.getText().toString().equals("Add Service")){
                    if(validPrice(listOfServices[0])){
                        addService(listOfServices[0]); //using array to make sure I dont make mistakes with name of service
                        buttonCar.setText("Delete Service");
                    }
                }else{//if delete then call delete service
                    deleteService(listOfServices[0]);
                    buttonCar.setText("Add Service");
                }
                break;
            case R.id.buttonTruck:
                //check for whether the text says add or delete
                if(buttonTruck.getText().toString().equals("Add Service")){
                    if(validPrice(listOfServices[1])){
                        addService(listOfServices[1]);
                        buttonTruck.setText("Delete Service");
                    }
                }else{//if delete then call delete service
                    deleteService(listOfServices[1]);
                    buttonTruck.setText("Add Service");
                }
                break;
            case R.id.buttonMoving:
                //check for whether the text says add or delete
                if(buttonMoving.getText().toString().equals("Add Service")){
                    if(validPrice(listOfServices[2])){
                        addService(listOfServices[2]);
                        buttonMoving.setText("Delete Service");
                    }
                }else{//if delete then call delete service
                    deleteService(listOfServices[2]);
                    buttonMoving.setText("Add Service");
                }
                break;
            //edit buttons only work if the service has already been added
            case R.id.editCar:
                if(buttonCar.getText().toString().equals("Add Service")){ //then service isn't added, tell them to add service first
                    Toast t = Toast.makeText(this, "Please add service before editing service info", Toast.LENGTH_SHORT);
                    t.show();
                }else if(validPrice(listOfServices[0])){//if delete then update service info
                    HashMap<String, Boolean> serviceInfo = new HashMap<String, Boolean>();
                    serviceInfo.put("firstName", firstNameCar.isChecked());
                    serviceInfo.put("lastName", lastNameCar.isChecked());
                    serviceInfo.put("dob", dobCar.isChecked());
                    serviceInfo.put("address", addressCar.isChecked());
                    serviceInfo.put("email", emailCar.isChecked());
                    serviceInfo.put("licenseType", licenseTypeCar.isChecked());
                    serviceInfo.put("carType", carType.isChecked());
                    serviceInfo.put("pickupDate", pickupDateCar.isChecked());
                    serviceInfo.put("pickupTime", pickupTimeCar.isChecked());
                    serviceInfo.put("returnDate", returnDateCar.isChecked());
                    serviceInfo.put("returnTime", returnTimeCar.isChecked());

                    String mString = priceCar.getText().toString();
                    //save to database
                    editService(listOfServices[0], mString, serviceInfo);
                }
                break;

            case R.id.editTruck:
                if(buttonTruck.getText().toString().equals("Add Service")){ //then service isn't added, tell them to add service first
                    Toast t = Toast.makeText(this, "Please add service before editing service info", Toast.LENGTH_SHORT);
                    t.show();
                }else if(validPrice(listOfServices[1])){//if delete then update service info
                    HashMap<String, Boolean> serviceInfo = new HashMap<String, Boolean>();
                    serviceInfo.put("firstName", firstNameTruck.isChecked());
                    serviceInfo.put("lastName", lastNameTruck.isChecked());
                    serviceInfo.put("dob", dobTruck.isChecked());
                    serviceInfo.put("address", addressTruck.isChecked());
                    serviceInfo.put("email", emailTruck.isChecked());
                    serviceInfo.put("licenseType", licenseTypeTruck.isChecked());
                    serviceInfo.put("pickupDate", pickupDateTruck.isChecked());
                    serviceInfo.put("pickupTime", pickupTimeTruck.isChecked());
                    serviceInfo.put("returnDate", returnDateTruck.isChecked());
                    serviceInfo.put("returnTime", returnTimeTruck.isChecked());
                    serviceInfo.put("maxDistance", maxkm.isChecked());
                    serviceInfo.put("areaUsed", areaUsed.isChecked());

                    //save to database
                    String mString = priceTruck.getText().toString();
                    editService(listOfServices[1], mString, serviceInfo);
                }

                break;


            case R.id.editMoving:
                if(buttonMoving.getText().toString().equals("Add Service")){ //then service isn't added, tell them to add service first
                    Toast t = Toast.makeText(this, "Please add service before editing service info", Toast.LENGTH_SHORT);
                    t.show();
                }else if(validPrice(listOfServices[2])){//if delete then update service info
                    HashMap<String, Boolean> serviceInfo = new HashMap<String, Boolean>();
                    serviceInfo.put("firstName", firstNameMoving.isChecked());
                    serviceInfo.put("lastName", lastNameMoving.isChecked());
                    serviceInfo.put("dob", dobMoving.isChecked());
                    serviceInfo.put("address", addressMoving.isChecked());
                    serviceInfo.put("email", emailMoving.isChecked());
                    serviceInfo.put("startLocation", startLocation.isChecked());
                    serviceInfo.put("endLocation", endLocation.isChecked());
                    serviceInfo.put("numOfMovers", numMovers.isChecked());
                    serviceInfo.put("numOfBoxes", numBoxes.isChecked());
                    //save to database
                    String mString = priceMoving.getText().toString();
                    editService(listOfServices[2], mString, serviceInfo);
                }
                break;

        }
    }


    //admin can add service offered to global list of services
    public void addService(String service){
        //add to service globally under admin
        DatabaseReference adminServices = FirebaseDatabase.getInstance().getReference("users/admin/services"); // get instance of database user
        //adds the service by setting the service status to true
        adminServices.child(service).child("status").setValue(true); //trues means the service is globally offered


        //then add service form
        HashMap<String, Boolean> serviceInfo = new HashMap<String, Boolean>();

        //by default, all fields are required for simplicity
        if(service.equals("CarRental")){
            //info for CarRentalInfo
            adminServices.child(service).child("price").setValue(priceCar.getText().toString());
            serviceInfo.put("firstName", true);
            serviceInfo.put("lastName", true);
            serviceInfo.put("dob", true);
            serviceInfo.put("address", true);
            serviceInfo.put("email", true);
            serviceInfo.put("licenseType", true);
            serviceInfo.put("carType", true);
            serviceInfo.put("pickupDate", true);
            serviceInfo.put("pickupTime", true);
            serviceInfo.put("returnDate", true);
            serviceInfo.put("returnTime", true);
        }else if(service.equals("TruckRental")){
            //info for TruckRental
            adminServices.child(service).child("price").setValue(priceTruck.getText().toString());
            serviceInfo.put("firstName", true);
            serviceInfo.put("lastName", true);
            serviceInfo.put("dob", true);
            serviceInfo.put("address", true);
            serviceInfo.put("email", true);
            serviceInfo.put("licenseType", true);
            serviceInfo.put("pickupDate", true);
            serviceInfo.put("pickupTime", true);
            serviceInfo.put("returnDate", true);
            serviceInfo.put("returnTime", true);
            serviceInfo.put("maxDistance", true);
            serviceInfo.put("areaUsed", true);
        }else{
            //info for Moving Assistance
            adminServices.child(service).child("price").setValue(priceMoving.getText().toString());
            serviceInfo.put("firstName", true);
            serviceInfo.put("lastName", true);
            serviceInfo.put("dob", true);
            serviceInfo.put("address", true);
            serviceInfo.put("email", true);
            serviceInfo.put("startLocation", true);
            serviceInfo.put("endLocation", true);
            serviceInfo.put("numOfMovers", true);
            serviceInfo.put("numOfBoxes", true);
        }
        //adds the service info under the service
        adminServices.child(service).child("ServiceInfo").setValue(serviceInfo);


        //then add to all branches
        DatabaseReference employee = FirebaseDatabase.getInstance().getReference("users/employee");
        employee.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //this loops through all employees
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Employee branch = snapshot.getValue(Employee.class); //get employee object from database
                    //then get their list of services
                    assert branch != null;
                    branch.services.put(service, "false"); //add service to branch. default to false
                    employee.child(branch.username).setValue(branch); //update in database
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        Toast t = Toast.makeText(this, "Successfully added service", Toast.LENGTH_SHORT);
        t.show();
    }

    //admin can edit service info form offered by branch
    public void editService(String service, String price, HashMap<String, Boolean> serviceInfo){
        DatabaseReference adminServices = FirebaseDatabase.getInstance().getReference("users/admin/services/" + service); // get instance of database referenc
        adminServices.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adminServices.child("ServiceInfo").setValue(serviceInfo); //update in database
                adminServices.child("price").setValue(price);//update price
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        notify("Edits saved");

    }

    //admin can delete services offered globally
    public void deleteService(String service){
        //delete from service globally under admin
        DatabaseReference adminServices = FirebaseDatabase.getInstance().getReference("users/admin/services"); // get instance of database user
        //deletes the service by setting the service name to false
        adminServices.child(service).child("status").setValue(false);

        //then delete from all branches
        DatabaseReference employee = FirebaseDatabase.getInstance().getReference("users/employee");
        employee.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //this loops through all employees
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Employee branch = snapshot.getValue(Employee.class); //get employee object from database
                    //then get their list of services
                    assert branch != null;
                    branch.services.put(service, "none"); //set to none so branch can't select it
                    employee.child(branch.username).setValue(branch); //update in database
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        Toast t = Toast.makeText(this, "Successfully deleted service", Toast.LENGTH_SHORT);
        t.show();

    }


    public void getToggle(String service){
        DatabaseReference adminServices = FirebaseDatabase.getInstance().getReference("users/admin/services/" + service); // get instance of database referenc
        adminServices.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //first check if that serviceInfo returns something.
                if(!dataSnapshot.child("ServiceInfo").hasChildren()){ //then put true for all in database
                    if(service.equals(listOfServices[0])){
                        firstNameCar.setChecked(true);
                        lastNameCar.setChecked(true);
                        dobCar.setChecked(true);
                        addressCar.setChecked(true);
                        emailCar.setChecked(true);
                        licenseTypeCar.setChecked(true);
                        carType.setChecked(true);
                        pickupDateCar.setChecked(true);
                        pickupTimeCar.setChecked(true);
                        returnDateCar.setChecked(true);
                        returnTimeCar.setChecked(true);

                    }else if(service.equals(listOfServices[1])){
                        firstNameTruck.setChecked(true);
                        lastNameTruck.setChecked(true);
                        dobTruck.setChecked(true);
                        addressTruck.setChecked(true);
                        emailTruck.setChecked(true);
                        licenseTypeTruck.setChecked(true);
                        pickupDateTruck.setChecked(true);
                        pickupTimeTruck.setChecked(true);
                        returnDateTruck.setChecked(true);
                        returnTimeTruck.setChecked(true);
                        maxkm.setChecked(true);
                        areaUsed.setChecked(true);
                    }else{
                        firstNameMoving.setChecked(true);
                        lastNameMoving.setChecked(true);
                        dobMoving.setChecked(true);
                        addressMoving.setChecked(true);
                        emailMoving.setChecked(true);
                        startLocation.setChecked(true);
                        endLocation.setChecked(true);
                        numMovers.setChecked(true);
                        numBoxes.setChecked(true);
                    }

                }else{
                    HashMap<String, Boolean> serviceInfo = (HashMap)dataSnapshot.child("ServiceInfo").getValue(); //update in database
                    if(service.equals(listOfServices[0])){
                        firstNameCar.setChecked(serviceInfo.get("firstName"));
                        lastNameCar.setChecked(serviceInfo.get("lastName"));
                        dobCar.setChecked(serviceInfo.get("dob"));
                        addressCar.setChecked(serviceInfo.get("address"));
                        emailCar.setChecked(serviceInfo.get("email"));
                        licenseTypeCar.setChecked(serviceInfo.get("licenseType"));
                        carType.setChecked(serviceInfo.get("carType"));
                        pickupDateCar.setChecked(serviceInfo.get("pickupDate"));
                        pickupTimeCar.setChecked(serviceInfo.get("pickupTime"));
                        returnDateCar.setChecked(serviceInfo.get("returnDate"));
                        returnTimeCar.setChecked(serviceInfo.get("returnTime"));
                        priceCar.setText(dataSnapshot.child("price").getValue(String.class)); //get price

                    }else if(service.equals(listOfServices[1])){
                        firstNameTruck.setChecked(serviceInfo.get("firstName"));
                        lastNameTruck.setChecked(serviceInfo.get("lastName"));
                        dobTruck.setChecked(serviceInfo.get("dob"));
                        addressTruck.setChecked(serviceInfo.get("address"));
                        emailTruck.setChecked(serviceInfo.get("email"));
                        licenseTypeTruck.setChecked(serviceInfo.get("licenseType"));
                        pickupDateTruck.setChecked(serviceInfo.get("pickupDate"));
                        pickupTimeTruck.setChecked(serviceInfo.get("pickupTime"));
                        returnDateTruck.setChecked(serviceInfo.get("returnDate"));
                        returnTimeTruck.setChecked(serviceInfo.get("returnTime"));
                        maxkm.setChecked(serviceInfo.get("maxDistance"));
                        areaUsed.setChecked(serviceInfo.get("areaUsed"));
                        priceTruck.setText(dataSnapshot.child("price").getValue(String.class)); //get price
                    }else{
                        firstNameMoving.setChecked(serviceInfo.get("firstName"));
                        lastNameMoving.setChecked(serviceInfo.get("lastName"));
                        dobMoving.setChecked(serviceInfo.get("dob"));
                        addressMoving.setChecked(serviceInfo.get("address"));
                        emailMoving.setChecked(serviceInfo.get("email"));
                        startLocation.setChecked(serviceInfo.get("startLocation"));
                        endLocation.setChecked(serviceInfo.get("endLocation"));
                        numMovers.setChecked(serviceInfo.get("numOfMovers"));
                        numBoxes.setChecked(serviceInfo.get("numOfBoxes"));
                        priceMoving.setText(dataSnapshot.child("price").getValue(String.class)); //get price
                    }

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public void serviceExists(String service){
        //if service status is true then update button
        DatabaseReference adminServices = FirebaseDatabase.getInstance().getReference("users/admin/services/" + service + "/status"); // get instance of database referenc
        adminServices.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //first check if that serviceInfo returns something.
                Boolean status = (Boolean) dataSnapshot.getValue();
                if(status == null){status = false;}
                if(status){ //if status is true, then button should be set to delete
                    if(service.equals(listOfServices[0])){
                        buttonCar.setText("Delete Service");
                    }else if(service.equals(listOfServices[1])){
                        buttonTruck.setText("Delete Service");
                    }else{
                        buttonMoving.setText("Delete Service");
                    }
                }else{
                    if(service.equals(listOfServices[0])){
                        buttonCar.setText("Add Service");
                    }else if(service.equals(listOfServices[1])){
                        buttonTruck.setText("Add Service");
                    }else {
                        buttonMoving.setText("Add Service");
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public boolean validPrice(String servicePrice){{
        int price;
        String mString;
        if(servicePrice.equals(listOfServices[0])){
            if(isEmpty(priceCar)){return false;}
            try {
                mString = priceCar.getText().toString();
                price = Integer.parseInt(mString); //convert string to integer
            } catch(NumberFormatException | NullPointerException e) {
                notify("Price must be integer");
                return false;
            }

        }else if(servicePrice.equals(listOfServices[1])){
            if(isEmpty(priceTruck)){return false;}
            try {
                mString = priceTruck.getText().toString();
                price = Integer.parseInt(mString); //convert string to integer
            } catch(NumberFormatException | NullPointerException e) {
                notify("Price must be integer");
                return false;
            }

        }else if(servicePrice.equals(listOfServices[2])){
            if(isEmpty(priceMoving)){return false;}
            try {
                mString = priceMoving.getText().toString();
                price = Integer.parseInt(mString); //convert string to integer
            } catch(NumberFormatException | NullPointerException e) {
                notify("Price must be integer");
                return false;
            }
        }else{
            return false;
        }


        if(price < 0 || price > 200){
            notify("Price too low or high");
            return false;
        }

        return true;
    }

    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    private void notify(String notification){
        Toast t = Toast.makeText(this, notification, Toast.LENGTH_SHORT);
        t.show();
    }
}