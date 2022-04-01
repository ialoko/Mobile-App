package com.uottawa.emilybonneville.seg2105_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;

import java.util.HashMap;


/**
 * @authors Iyiola Aloko, Emily Bonneville
 */

public class AdminWelcome extends AppCompatActivity  {

    public Button button1, button2, button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_welcome);

        button1 = (Button) findViewById(R.id.services);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminWelcome.this,ServiceInfo.class);
                startActivity(intent);
                finish();
            }
        });

        button2 = (Button) findViewById(R.id.deleteAccount);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminWelcome.this,AdminDeleteAccount.class);
                startActivity(intent);
                finish();
            }
        });

        button3 = (Button) findViewById(R.id.btnSearch);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminWelcome.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }



}
