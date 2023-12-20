package com.gublen.infopill;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainMenu extends AppCompatActivity {

    TextView username1;
    Button logout1, exit3, addMedication, medicationList;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        username1 = findViewById(R.id.username1);

        logout1 = findViewById(R.id.logoutBtn1);
        exit3 = findViewById(R.id.exitBtn3);
        addMedication = findViewById(R.id.addMedication);
        medicationList = findViewById(R.id.viewMedication);

    }

    @Override
    protected void onResume() {
        super.onResume();

        username1.setText(user.getEmail());

        addMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goAddMed = new Intent(getApplicationContext(), AddMedication.class);
                startActivity(goAddMed);
            }
        });

        medicationList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goList = new Intent(getApplicationContext(), MedicationList.class);
                startActivity(goList);
            }
        });

        logout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("info", "logging out");
                FirebaseAuth.getInstance().signOut();
                Intent goMain = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(goMain);
            }
        });

        exit3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });

    }
}