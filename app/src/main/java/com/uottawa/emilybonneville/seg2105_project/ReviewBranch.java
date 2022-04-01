package com.uottawa.emilybonneville.seg2105_project;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReviewBranch extends AppCompatActivity {

    private AutoCompleteTextView textBranchName;
    private EditText textBranchRate;
    private EditText textBranchComment;
    private Button buttonSubmitReview;
    private Button buttonBackFromReview;
    public ArrayAdapter<String> aa;
    private ArrayList<String> listBranches;
    private String customerUserName;
    private HashMap<String, ArrayList<String>> rating;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_branch);

        rating = new HashMap<>();
        listBranches = new ArrayList<>(); //create arraylist to store all employees
        getAllEmployees();//get all employees

        textBranchName = findViewById(R.id.branchName);
        textBranchRate = findViewById(R.id.branchRate);
        textBranchComment = findViewById(R.id.branchComment);
        buttonSubmitReview = findViewById(R.id.btnSubmitReview);
        buttonBackFromReview = findViewById(R.id.btnBackFromReview);

        Intent intent = getIntent();//get username intent
        customerUserName = intent.getStringExtra("username");



        //create array adapter
        aa = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listBranches); //for employees
        textBranchName.setAdapter(aa);



        buttonBackFromReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReviewBranch.this,CustomerWelcomeActivity.class);
                intent.putExtra("username", customerUserName); //pass username back to customer welcome page
                startActivity(intent);
                finish();
            }
        });

        buttonSubmitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validEntry()){
                    saveReview(textBranchName.getText().toString(), textBranchRate.getText().toString(), textBranchComment.getText().toString());
                }
            }
        });
    }

    public boolean validEntry(){ //check if any of the text are empty. check if rating is a number between 0 and 5
        //reaches here then valid entry
        if(isEmpty(textBranchName)){
            textBranchName.setError("Please select a branch");
            return false;
        }
        if(isEmpty(textBranchRate)){
            textBranchRate.setError("Please enter a rating");
            return false;
        }
        if(!validRating(textBranchRate.getText().toString())){
            textBranchRate.setError("Fix error");
            return false;
        }
        //check if branch is even a valid branch
        String branchName = textBranchName.getText().toString();
        if(!listBranches.contains(branchName)){
            textBranchName.setError("This branch doesn't exist");
        }

        return true;

    }

    boolean validRating(String ratingString){
        Pattern p = Pattern.compile("^\\d{1}$"); //number has to be 1 digit
        Matcher match = p.matcher(ratingString);
        if(!match.matches()){
            notif("Rating must be 1 digit. No special characters");
            return false;
        }
        int ratingNum;
        try {
            ratingNum = Integer.parseInt(ratingString); //convert string to integer
        } catch(NumberFormatException | NullPointerException e) {
            notif("Rating must be integers");
            return false;
        }

        if(ratingNum > 5 || ratingNum <0){ //number has to be between 0 and 5
            notif("Rating must be between 0 and 5");
            return false;
        }

        if(isEmpty(textBranchComment)){
            notif("Must leave a comment");
            return false;
        }
        return true;
    }


    boolean isEmpty(TextView text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }


    public void saveReview(String branch, String ratingString, String ratingComment){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("review").child(branch); //storing reviews in separate area
        //store review in a hashmap
        ArrayList<String> ratingArray = new ArrayList<>();
        ratingArray.add(ratingString);
        ratingArray.add(ratingComment);
        //retrieve current rating hashmap from firebase
        db.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    rating = (HashMap<String, ArrayList<String>>)snapshot.getValue();
                    if(rating.containsKey(customerUserName)){
                        notif("This will overwrite your previous rating: ");
                    }
                    rating.put(customerUserName, ratingArray);
                }else{
                    notif("Changes made");
                    rating.put(customerUserName, ratingArray); //overwrites customers old review
                }
                notif("Changes made");
                db.setValue(rating);
                Intent intent = new Intent(ReviewBranch.this,CustomerWelcomeActivity.class);
                intent.putExtra("username", customerUserName); //pass username back to customer welcome page
                startActivity(intent);
                finish();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

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
                    listBranches.add(employee.username); //then add to list of employees
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    public void notif(String notification){
        Toast t = Toast.makeText(this, notification, Toast.LENGTH_LONG);
        t.show();
    }
}
