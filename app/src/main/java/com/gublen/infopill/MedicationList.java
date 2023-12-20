package com.gublen.infopill;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicationList extends AppCompatActivity {

    private SimpleAdapter sa;

    private ListView listView;

    FirebaseAuth mAuth;
    FirebaseUser user;

    Button goBack;
    String validEmail;

    ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    String[][] dataFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_list);

        listView = findViewById(R.id.listView);
        goBack = findViewById(R.id.goBackBtn);

        //goBack = findViewById(R.id.goBack);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        String email = user.getEmail();
        validEmail = email.replace(Character.toString('@'), "");
        validEmail = validEmail.replace(Character.toString('.'), "");
        validEmail = validEmail.replace(Character.toString('-'), "");
        validEmail = validEmail.replace(Character.toString('_'), "");
        validEmail = validEmail.replace(Character.toString(' '), "");

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainMenu = new Intent(getApplicationContext(), MainMenu.class);
                startActivity(mainMenu);
            }
        });

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child("email").child(validEmail);

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int numMeds = (int) dataSnapshot.getChildrenCount();
                    String[][] meds = new String[numMeds][5];

                    int index = 0;

                    dataFirebase = new String[numMeds][2];
                    for (DataSnapshot medSnapshot : dataSnapshot.getChildren()) {
                        String medKey = medSnapshot.getKey();

                        String medCant = medSnapshot.child("medCant").getValue(String.class);
                        String medName = medSnapshot.child("medName").getValue(String.class);
                        String observaciones = medSnapshot.child("observaciones").getValue(String.class);
                        String tiempos = medSnapshot.child("tiempos").getValue(String.class);

                        // Store the data in the meds array
                        meds[index][0] = medKey;
                        meds[index][1] = medCant;
                        meds[index][2] = medName;
                        meds[index][3] = observaciones;
                        meds[index][4] = tiempos;

                        dataFirebase[index][0] = medName;
                        dataFirebase[index][1] = medCant + ", " + observaciones + ", " + tiempos;
                        index++;
                    }
                    populateListView();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
                Log.i("info", "error extrayendo los datos");
            }
        });
    }

    public void populateListView(){

        Log.d("MedicationList", "DataFirebase array:");
        for (int i = 0; i < dataFirebase.length; i++) {
            String[] data = dataFirebase[i];
            String logEntry = "Index: " + i + ", Values: " + Arrays.toString(data);
            Log.d("MedicationList", logEntry);
        }

        HashMap<String, String> item;
        for (int i = 0; i < dataFirebase.length; i++){
            item = new HashMap<>();
            item.put("line1", dataFirebase[i][0]);
            item.put("line2", dataFirebase[i][1]);
            list.add(item);
        }
        sa = new SimpleAdapter(getApplicationContext(), list,
                R.layout.list_item,
                new String[] {"line1", "line2"},
                new int[] {R.id.titleTextView, R.id.subtitleTextView});

        listView.setAdapter(sa);
    }
}