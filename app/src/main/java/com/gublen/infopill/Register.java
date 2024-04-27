package com.gublen.infopill;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;

    Button exit4, register4, login4;
    EditText email, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        exit4 = findViewById(R.id.exitBtn4);
        register4 = findViewById(R.id.registerBtn2);
        login4 = findViewById(R.id.loginBtn4);

        email = findViewById(R.id.emailRegister);
        pass = findViewById(R.id.passRegister);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (user != null){
            Log.i("info", "already logged in, goin main.");
            Intent intent = new Intent(getApplicationContext(), MainMenu.class);
            intent.putExtra("user", user.getDisplayName());
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        register4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("info", "going to register\n" + email.getText().toString() + "\n" + pass.getText().toString());
                String emailStr, passStr;
                emailStr = email.getText().toString();
                passStr = pass.getText().toString();

                mAuth.createUserWithEmailAndPassword(emailStr, passStr)
                        .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Log.i("info", "user created");
                                    createDatabaseEntryUser(emailStr);
                                    Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                                    startActivity(intent);
                                } else {
                                    Log.i("info", "Ha habido un error\n" + task.getException());
                                }
                            }
                        });

            }
        });

        login4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginPage.class);
                startActivity(intent);
            }
        });

        exit4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //crea una entrada a la base de datos usando como clave el email sin caracteres especiales
    //TODO: arreglar el hecho de que elimina datos anteriores al ejecutarse
    //TODO: reemplazar por un usuario/email unicos
    public void createDatabaseEntryUser(String email){

        String validEmail = email.replace(Character.toString('@'), "");
        validEmail = validEmail.replace(Character.toString('.'), "");
        validEmail = validEmail.replace(Character.toString('-'), "");
        validEmail = validEmail.replace(Character.toString('_'), "");
        validEmail = validEmail.replace(Character.toString(' '), "");

        FirebaseApp.initializeApp(this);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("users").child("email").setValue(validEmail);

    }

}