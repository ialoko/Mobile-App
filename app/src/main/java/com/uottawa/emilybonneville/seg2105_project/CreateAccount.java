package com.uottawa.emilybonneville.seg2105_project;

/*
 * @author Iyiola Aloko
 */
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

//import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import android.text.TextUtils;
import android.util.Patterns;
//import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast; //used to show a notification
import android.util.Log;

import java.util.HashMap;

public class CreateAccount extends AppCompatActivity {
    // instance variables
    public EditText user_name;
    public EditText pwd;
    public EditText confirm_pwd;
    public EditText email_address;
    public Button employee_role;
    public Button customer_role;
    public Button backRegister;
    public String registration_role; // set to role depending on which button is clicked;
    public Boolean resultE;
    public Boolean resultC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        setTitle("CreateAccount");

        Toast.makeText(CreateAccount.this, "Can you see me", Toast.LENGTH_SHORT).show();

        // retrieve info from xml page
        user_name = findViewById(R.id.username);
        pwd = findViewById(R.id.password);
        confirm_pwd = findViewById(R.id.confirm_password);
        email_address = findViewById(R.id.userEmailAddress);

        customer_role = findViewById(R.id.btnCustomer);
        resultE = false;
        resultC = false;

        // check if either the customer or employee button is clicked
        customer_role.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // when register button is clicked, validate all data entered
                Log.d("Hello", "customer clicked");
                if (checkDataEntered()) {
                    registration_role = "customer";
                    // store data in firebase
                    create_new_user(email_address.getText().toString(), pwd.getText().toString(),
                            user_name.getText().toString(), registration_role);

                    //clear text boxes
                    Intent intent = new Intent(CreateAccount.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        //button for employee
        employee_role = findViewById(R.id.btnEmployee);
        employee_role.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Hello", "employee clicked");
                // when register button is clicked, validate all data entered
                if (checkDataEntered()) {
                    registration_role = "employee";
                    // store data in firebase
                    create_new_user(email_address.getText().toString(), pwd.getText().toString(),
                            user_name.getText().toString(), registration_role);

                    //clear text boxes
                    Intent intent = new Intent(CreateAccount.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        backRegister = findViewById(R.id.backFromRegister);
        backRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccount.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }

    // validate entries
    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    // password validation check
    private boolean isPasswordValid(EditText password) {
        return password != null && password.getText().toString().trim().length() > 5;
    }

    //check username doesn't exist
    private boolean notUniqueUsername(EditText username1){ //returns true if username is unique
        if(username1.getText().toString().equals("admin")){
            resultE = true;  //users shouldn't be able to have username as admin
        }

        DatabaseReference employeeref = FirebaseDatabase.getInstance().getReference("users/employee");
        DatabaseReference customerref = FirebaseDatabase.getInstance().getReference("users/customer");

        employeeref.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child : snapshot.getChildren()){
                    Employee employee = child.getValue(Employee.class);
                    if(employee.username.equals(username1.getText().toString())) {
                        user_name.setError("Username is already taken");
                        resultE = true;
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        customerref.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()){
                    for(DataSnapshot child : snapshot.getChildren()){
                        User customer = child.getValue(User.class);
                        if(customer.username.equals(username1.getText().toString())) {
                            user_name.setError("Username is already taken");
                            resultC = true;
                            break;
                        }
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        if(resultE == true || resultC == true){ //user is either employee or customer
            return false; //false means it is not unique
        }else{
            return true; //no user was found with that username
        }
    }

    public boolean checkDataEntered() {
        // check that username is not empty
        if (isEmpty(user_name)) {
            Toast t = Toast.makeText(this, "You must enter a username to register", Toast.LENGTH_SHORT);
            t.show();
            return false;
        }

        //check that username is not already taken
        if(!notUniqueUsername(user_name)){
            Toast t = Toast.makeText(this, "Username taken", Toast.LENGTH_SHORT);
            t.show();
            return false;

        }
        // check that the email address is a valid entry
        if (!isEmail(email_address)) {
            email_address.setError("Enter valid email!");
            return false;
        }

        // check length of password or that password and confirm password match
        if (!isPasswordValid(pwd)) {
            pwd.setError("Password must be longer than 4 letters");
            return false;
        } else if (!(pwd.getText().toString()).equals(confirm_pwd.getText().toString())) {
            Toast t = Toast.makeText(this, "Confirm Password does not match", Toast.LENGTH_SHORT);
            t.show();
            return false;
        }

        // if we reach here then all entries are valid
        return true;
    }

    // handle adding data to firebase
    public void create_new_user(String email, String password, String username, String role) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("users"); // get instance of database user
        //create new user using user class
        if(role.equals("customer")){
            User user = new User(username, password, email, role);
            //save to database
            db.child(role).child(username).setValue(user);
            Toast t = Toast.makeText(this, "Successfully registered", Toast.LENGTH_SHORT);
            t.show();
        }else{
            Employee branch = new Employee(username, password, email, role); //using employee class so we can track services
            db.child("admin").child("services").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //under services, we have CarRental, TruckRental, MovingAssistance. Under each we have status which has value set to boolean
                    boolean carRentalStatus= (boolean)snapshot.child("CarRental/status").getValue();
                    boolean truckRentalStatus= (boolean)snapshot.child("TruckRental/status").getValue();
                    boolean movingStatus= (boolean)snapshot.child("MovingAssistance/status").getValue();
                    branch.setServiceStatus(carRentalStatus, truckRentalStatus, movingStatus);//update service status
                    //save to database
                    db.child(role).child(username).setValue(branch);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            Toast t = Toast.makeText(this, "Successfully registered", Toast.LENGTH_SHORT);
            t.show();

        }
    }
}
