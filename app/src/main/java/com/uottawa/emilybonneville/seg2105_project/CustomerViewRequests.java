package com.uottawa.emilybonneville.seg2105_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class CustomerViewRequests extends AppCompatActivity {
    TextView pending, rejected, approved;
    Button buttonBack;
    String customerUserName;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view_requests);

        Intent intent = getIntent();//get username intent
        customerUserName = intent.getStringExtra("username");

        buttonBack = findViewById(R.id.btnBack);
        pending = findViewById(R.id.pending);
        rejected = findViewById(R.id.rejected);
        approved = findViewById(R.id.approved);
        user = new User();
        customerRequestList("retrieve");


        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerRequestList("set");
            }
        });
    }

    public void retrieveRequests(){
        DatabaseReference db1 = FirebaseDatabase.getInstance().getReference("requests").child("approved");
        DatabaseReference db2 = FirebaseDatabase.getInstance().getReference("requests").child("rejected");

        //loop through approved
        db1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    for (DataSnapshot dsp : dataSnapshot.getChildren()) { //children are branches
                        if(dsp.hasChildren()){
                            for (DataSnapshot snapshot : dsp.getChildren()) { //children are requests
                                String requestKey = snapshot.getKey();
                                String requestString = snapshot.getValue(String.class);
                                if(user.pendingRequests.containsKey(requestKey)){ //if user pending requests contains this key then add to approved
                                    user.approvedRequests.put(requestKey, requestString);
                                    //remove from pending
                                    user.pendingRequests.remove(requestKey);
                                }
                            }
                        }
                    }
                }
                approved.setText(String.format("Here are your approved requests: \n%s", user.approvedRequests));
                pending.setText(String.format("Here are your pending requests: \n %s", user.pendingRequests));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //loop through rejected
        db2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    for (DataSnapshot dsp : dataSnapshot.getChildren()) { //children are branches
                        if(dsp.hasChildren()){
                            for (DataSnapshot snapshot : dsp.getChildren()) { //children are requests
                                String requestKey = snapshot.getKey();
                                String requestString = snapshot.getValue(String.class);
                                if(user.pendingRequests.containsKey(requestKey)){ //if user pending requests contains this key then add to approved
                                    user.rejectedRequests.put(requestKey, requestString);
                                    //remove from pending
                                    user.pendingRequests.remove(requestKey);
                                }
                            }
                        }
                    }
                }
                rejected.setText(String.format("Here are your rejected requests: \n %s", user.rejectedRequests));
                pending.setText(String.format("Here are your pending requests: \n %s", user.pendingRequests)); //set text twice just in case
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void customerRequestList(String flag){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("users/customer");
        db.child(customerUserName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(flag.equals("set")){
                    db.child(customerUserName).setValue(user); //set value of current user after updating
                    Intent intent = new Intent(CustomerViewRequests.this,CustomerWelcomeActivity.class);
                    intent.putExtra("username", customerUserName); //pass username back to customer welcome page
                    startActivity(intent);
                    finish();
                }else{
                    user = snapshot.getValue(User.class); //retrieve user from database
                    retrieveRequests(); //then call retrieve requests
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}