package com.gublen.infopill;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Button login1, exit1, register1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        login1 = findViewById(R.id.loginBtn1);
        exit1 = findViewById(R.id.exitBtn1);
        register1 = findViewById(R.id.registerBtn1);

    }

    @Override
    protected void onStart() {
        super.onStart();

        //check if user singed-in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            //algo si esta logeado
            Intent intent = new Intent(this, MainMenu.class);
            intent.putExtra("user", currentUser);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("info", "main login ");
                Intent intent = new Intent(getApplicationContext(), LoginPage.class);
                startActivity(intent);
            }
        });

        exit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                FirebaseAuth.getInstance().signOut();
            }
        });

        register1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("info", "going to register");
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });

    }
}