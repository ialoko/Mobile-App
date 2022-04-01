package com.uottawa.emilybonneville.seg2105_project;

import androidx.annotation.NonNull;
/*
 * @author Emily Bonneville
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;


public class LoginPage extends AppCompatActivity {

    private EditText textUsername;
    private EditText textPassword;
    private Button buttonLoginCustomer;
    private Button buttonLoginEmployee;
    private Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        textUsername = findViewById(R.id.loginUsername);
        textPassword = findViewById(R.id.loginPassword);

        buttonBack = findViewById(R.id.backFromLogin);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });

        buttonLoginEmployee = findViewById(R.id.loginEmployee);
        buttonLoginEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textUsername.getText().toString().equals("") || textPassword.getText().toString().equals("")) {
                    Toast.makeText(LoginPage.this, "Please insert a username/password", Toast.LENGTH_SHORT).show();
                } else {
                    //log in user
                    String username = textUsername.getText().toString();
                    String password = textPassword.getText().toString();

                    //if username and password is admin, then then go to admin welcome page
                    if (username.equals("admin") && password.equals("admin")) {
                        //call openWelcome activity with username and role = admin
                        openWelcomeActivity("admin", "admin");

                    } else {//pull from firestore to sign in user
                        loginEmployee(password, username);
                    }
                }
            }
        });
        buttonLoginCustomer = findViewById(R.id.loginCustomer);
        buttonLoginCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textUsername.getText().toString().equals("") || textPassword.getText().toString().equals("")) {
                    Toast.makeText(LoginPage.this, "Please insert a username/password", Toast.LENGTH_SHORT).show();
                } else {
                    //log in user
                    String username = textUsername.getText().toString();
                    String password = textPassword.getText().toString();
                    loginCustomer(password, username);
                }
            }
        });
    }

    public void openMainActivity() {
        Intent intent = new Intent(LoginPage.this, MainActivity.class);
        startActivity(intent);
    }

    public void openWelcomeActivity(String username, String role) {
        switch (role) {
            case "admin": {
                //open admin page
                Intent intent = new Intent(LoginPage.this, AdminWelcome.class);
                intent.putExtra("username", username);
                startActivity(intent);
                break;
            }
            case "customer": {
                //open customer page
                Intent intent = new Intent(LoginPage.this, CustomerWelcomeActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                break;
            }
            case "employee": {
                //open employee
                Intent intent = new Intent(LoginPage.this, EmployeeWelcome.class);
                intent.putExtra("username", username);
                startActivity(intent);
                break;
            }
        }
    }

    //login customer
    public void loginCustomer(String password, String username){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("users/customer");
        /*db.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    Log.e("firebase", "Error getting data", task.getException());
                }else{
                    //use the getvalue method to return the data snapshot back to user instance
                    User user = task.getResult().getValue(User.class);
                    assert user != null;
                    if(check_match(username, password, user.username, user.password)){
                        Log.d("firebase", "Successfully logged in");
                        //call activity for role
                        openWelcomeActivity(user.username, user.role);
                    }
                }
            }
        });*/

        db.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(user == null){
                    textUsername.setError("User does not exist");
                    Log.d("firebase", "Username is not in database");

                }else if(check_match(username, password, user.username, user.password)){
                    Log.d("firebase", "Successfully logged in");
                    //call activity for role
                    openWelcomeActivity(user.username, user.role);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    //login employee
    public void loginEmployee(String password, String username){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("users/employee");
        /*db.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    Log.e("firebase", "Error getting data", task.getException());
                }else{
                    //use the getvalue method to return the data snapshot back to user instance
                    Employee employee = task.getResult().getValue(Employee.class);
                    assert employee != null;
                    if(check_match(username, password, employee.username, employee.password)){
                        Log.d("firebase", "Successfully logged in");
                        //call activity for role
                        openWelcomeActivity(employee.username, employee.role);
                    }
                }
            }
        });*/

        db.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Employee employee = snapshot.getValue(Employee.class);
                if(employee == null){
                    textUsername.setError("User does not exist");
                    Log.d("firebase", "user does not exist");

                }else if(check_match(username, password, employee.username, employee.password)){
                    Log.d("firebase", "Successfully logged in");
                    //call activity for role
                    openWelcomeActivity(employee.username, employee.role);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //check username and password patch
    public boolean check_match(String username, String password, String dbName, String dbPassword){
        if(dbName.equals(username) && dbPassword.equals(password)){
            //then username and password correct
            return true;
        }else{
            //ask user to rewrite
            Toast t = Toast.makeText(this, getString(R.string.passwordCheck), Toast.LENGTH_SHORT);
            t.show();
            return false;
        }
    }
}