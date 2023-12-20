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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser user;
    Button login2, exit2, register;
    EditText email, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mAuth = FirebaseAuth.getInstance();
        //buttons on the login page
        login2 = findViewById(R.id.loginBtn2);
        exit2 = findViewById(R.id.exitBtn2);
        register = findViewById(R.id.registerBtn5);
        //textFields on the login page
        email = findViewById(R.id.emailTextLogin);
        pass = findViewById(R.id.passTextLogin);


        user = mAuth.getCurrentUser();

        login2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUserAccount();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goRegister = new Intent(getApplicationContext(), Register.class);
                startActivity(goRegister);
            }
        });

        exit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void loginUserAccount(){

        Log.i("info", "clicked login");
        Log.i("info", email.getText().toString());
        Log.i("info", pass.getText().toString());
        mAuth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
            .addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i("info", "email login success\n" + task.getResult());
                            Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                            startActivity(intent);
                        } else {
                            Log.i("info", "error\n" + task.getException());
                            if (task.getException().toString().contains("com.google.firebase.auth.FirebaseAuthInvalidUserException")) {
                                Toast.makeText(getApplicationContext(), "Error: You need to register first", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            }
        );

    }

}