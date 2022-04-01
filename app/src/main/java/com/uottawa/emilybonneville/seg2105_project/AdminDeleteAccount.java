package com.uottawa.emilybonneville.seg2105_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class AdminDeleteAccount extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    public Button button1;
    public Button button2;
    public Button back;
    public Spinner spinner1;
    public Spinner spinner2;
    public ArrayList<String> listEmp;
    public ArrayList<String> listCust;
    public ArrayAdapter<String> aa;
    public ArrayAdapter<String> bb;
    public EditText eText;
    public EditText cText;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_delete_account);

        eText = findViewById(R.id.textViewE);
        cText = findViewById(R.id.textViewC);

        back = findViewById(R.id.backFromDeleteAccount);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDeleteAccount.this,AdminWelcome.class);
                startActivity(intent);
            }
        });

        listEmp = new ArrayList<>(); //create arraylist to store all employees
        listCust = new ArrayList<>(); //create arraylist to store all customers

        spinner1 = findViewById(R.id.spinner1); //spinner for employees
        spinner2 = findViewById(R.id.spinner2); //spinner for customers
        getAllEmployees();//get all employees
        getAllCustomers(); //get al customers


        //create arrayadapter
        aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listEmp); //for employees
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bb = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listCust); //for customers
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //set array adapter on spinner
        spinner1.setAdapter(aa);
        spinner2.setAdapter(bb);

        spinner1.setOnItemSelectedListener(this);
        spinner2.setOnItemSelectedListener(this);

        button1 = (Button) findViewById(R.id.btndeleteE);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = eText.getText().toString();
                if(validEntry(user, listEmp)){
                    deleteBranchAccount(user);
                }
            }
        });
        button2 = (Button) findViewById(R.id.btndeleteC);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = cText.getText().toString();
                if(validEntry(user, listCust)){
                    deleteCustomerAccount(user); //whatever item selected is username
                }
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        // your code here
    }

    @Override
    public void onNothingSelected(AdapterView<?> parentView) {
        // your code here
    }

    public Boolean validEntry(String username, ArrayList<String> role){
        if(username.equals("")){
            notify("Please enter a username");
            return false;
        }else if(!role.contains(username)){
            notify("invalid entry");
            return false;
        }else{
            return true;
        }
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
                    listEmp.add(employee.username); //then add to list of employees
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    public void getAllCustomers(){
        DatabaseReference customerref = FirebaseDatabase.getInstance().getReference("users/customer");
        customerref.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child : snapshot.getChildren()){
                    User user = child.getValue(User.class);
                    if(user == null){ //verify only valid entries getting into array list
                        continue;
                    }
                    listCust.add(user.username); //then add to list of employees
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }


    public void deleteBranchAccount(String username) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("users/employee").child(username); // get instance of database employee
        //remove branch account
        dR.removeValue();
        notify("Deleted Branch.");
        aa.remove(username);
        aa.notifyDataSetChanged();
        listEmp.remove(username);

    }

    public void deleteCustomerAccount(String username) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("users/customer").child(username); // get instance of database employee
        //remove branch account
        dR.removeValue();
        notify("Deleted Customer.");
        bb.remove(username);
        bb.notifyDataSetChanged();
        listCust.remove(username);

    }

    public void notify(String notification){
        Toast t = Toast.makeText(this, notification, Toast.LENGTH_SHORT);
        t.show();
    }
}