package com.gublen.infopill;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AddMedication extends AppCompatActivity {

    EditText pillName, pillAmount, pillTimes, pillObservations;
    Button cancel, add;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);
        //añadir otro campo para la patologia de la med
        //alarma de la medicacion
        //en lugar de antes de comer elegir una hora y cuantos dias dura la med
        //forma de decir si se ha tomado la medicacion
        //pagina de opciones modo oscuro/luz

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        pillName = findViewById(R.id.pillName);
        pillAmount = findViewById(R.id.pillAmount);
        pillTimes = findViewById(R.id.pillCuantity);
        pillObservations = findViewById(R.id.pillDescription);

        cancel = findViewById(R.id.cancelAdd);
        add = findViewById(R.id.sendAdd);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goMenu = new Intent(getApplicationContext(), MainMenu.class);
                startActivity(goMenu);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, amount, times, desc;
                name = pillName.getText().toString();
                amount = pillAmount.getText().toString();
                times = pillTimes.getText().toString();
                desc = pillObservations.getText().toString();
                addPillDatabase(name, amount, times, desc);
            }
        });

    }

    public void addPillDatabase(String name, String amount, String times, String desc){

        String email = user.getEmail();
        String validEmail = email.replace(Character.toString('@'), "");
        validEmail = validEmail.replace(Character.toString('.'), "");
        validEmail = validEmail.replace(Character.toString('-'), "");
        validEmail = validEmail.replace(Character.toString('_'), "");
        validEmail = validEmail.replace(Character.toString(' '), "");


        FirebaseApp.initializeApp(this);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        Map<String, String> datos = new HashMap<>();
        datos.put("medName", name);
        datos.put("medCant", amount);
        datos.put("tiempos", times);
        datos.put("observaciones", desc);

        DatabaseReference numChilds = FirebaseDatabase.getInstance().getReference().child("users").child("email").child(validEmail);
        String finalValidEmail = validEmail;

        numChilds.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int numChild = (int) snapshot.getChildrenCount();
                    databaseReference.child("users").child("email").child(finalValidEmail).child("med" + (numChild+1)).setValue(datos);
                } else {
                    databaseReference.child("users").child("email").child(finalValidEmail).child("med" + 1).setValue(datos);
                }
                Toast.makeText(getApplicationContext(), "Medication added, going to MainMenu", Toast.LENGTH_SHORT).show();
                Intent goMenu = new Intent(getApplicationContext(), MainMenu.class);
                startActivity(goMenu);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}