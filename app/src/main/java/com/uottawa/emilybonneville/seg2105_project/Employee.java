package com.uottawa.emilybonneville.seg2105_project;


import java.io.Serializable;
import java.util.HashMap;

public class Employee implements Serializable {
    public String username, password, email, role;
    public HashMap<String, String> services = new HashMap<>(); // Add keys and values (service, true/false/none)
    public String phone, address; //added for del3
    public HashMap<String, Boolean> hours = new HashMap<>(); //added for del 3
    public HashMap<String, String> timeslot = new HashMap<>(); //added for del 3

    public Employee(){}
    public Employee(String username, String password, String email, String role){
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.phone = "none";
        this.address = "none"; //branch hasn't set phone and address by default
        this.hours.put("Monday", false); //by default, branch is closed so false
        this.hours.put("Tuesday", false);
        this.hours.put("Wednesday", false);
        this.hours.put("Thursday", false);
        this.hours.put("Friday", false);
        this.timeslot.put("Monday", "-"); //for timeslots
        this.timeslot.put("Tuesday", "-");
        this.timeslot.put("Wednesday", "-");
        this.timeslot.put("Thursday", "-");
        this.timeslot.put("Friday", "-");
    }

    public void setServiceStatus(boolean CarRental, boolean TruckRental, boolean MovingAssistance){ //change to whatever admin wants
        String car, truck, moving;
        //if status is true, then set service to false by default. if status is false, then service isn't offered so set to none
        if(CarRental){
            car = "false";
        }else {
            car = "none";
        }

        if(MovingAssistance){
            moving = "false";
        }else {
            moving = "none";
        }

        if(TruckRental){
            truck = "false";
        }else {
            truck = "none";
        }

        this.services.put("CarRental", car);
        this.services.put("TruckRental", truck);
        this.services.put("MovingAssistance", moving);
    }

}
