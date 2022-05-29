package com.example.streetviewmap;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends BaseActivity {
    Button logInButton;
    EditText emailText;
    EditText passwordText;
    FirebaseAuth userAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        findViewsByIds();
        logInButton.setOnClickListener(view -> {
            if(!emailText.getText().toString().equals("")&&!passwordText.getText().toString().equals("")) {
                userAuth = FirebaseAuth.getInstance();
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();
                Log.i("banana", "onCreate: "+password);
                login(email, password);
            }else{
                Toast.makeText(this,"one or more fields are empty",Toast.LENGTH_LONG).show();
            }
            Log.i("work", "onCreate: ");
        });

    }
    public void findViewsByIds(){
        logInButton=findViewById(R.id.LogInButton);
        emailText=findViewById(R.id.editTextTextEmailAddressLogIn);
        passwordText=findViewById(R.id.editTextTextPasswordLogIn);
    }
    public void login(String email,String password){
        userAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isComplete()){
                    Log.i("banana", "onComplete: ");
                }
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("banana", "signInWithEmail:success");
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("banana", "signInWithEmail:failure", task.getException());
                    Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}