package com.uottawa.emilybonneville.seg2105_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BranchViewRating extends AppCompatActivity {
    private Button buttonBack;
    private TextView listReviews;
    private String branchUserName;
    private HashMap<String, ArrayList<String>> rating = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_view_rating);
        Intent intent = getIntent();
        branchUserName =  intent.getStringExtra("username"); //get username from employee welcome page

        buttonBack=findViewById(R.id.btnBack);
        listReviews = findViewById(R.id.listReviews);
        retrieveReviews(branchUserName);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BranchViewRating.this,EmployeeWelcome.class);
                intent.putExtra("username", branchUserName); //pass username back to customer welcome page
                startActivity(intent);
                finish();
            }
        });
    }

    public void retrieveReviews(String branch){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("review").child(branch); //storing reviews in separate area
        //retrieve current rating hashmap from firebase
        db.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    rating = (HashMap<String, ArrayList<String>>)snapshot.getValue();
                    StringBuilder ratingString = new StringBuilder();
                    for (Map.Entry<String, ArrayList<String>> entry : rating.entrySet()) {
                        String key = entry.getKey();
                        ArrayList<String> value = entry.getValue();
                        ratingString.append("\n Customer Username: ").append(key);
                        ratingString.append("\n Customer Rating: ").append(value.get(0));
                        ratingString.append("\n Customer Comment: ").append(value.get(1));
                        ratingString.append("\n ----------------------------------------");
                    }
                    listReviews.setText(String.format("Here are your customer ratings: \n %s", ratingString.toString()));

                }else{
                    notif("You have no ratings");
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