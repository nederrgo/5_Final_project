package com.example.streetviewmap;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Sign in activity.
 */
public class SignInActivity extends BaseActivity {
    /**
     * The Email text.
     */
    EditText emailText;
    /**
     * The Password text.
     */
    EditText passwordText;
    /**
     * The Sigh in button.
     */
    Button sighInButton;
    private final FirebaseFirestore dataBase=FirebaseFirestore.getInstance();
    FirebaseAuth userAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        findViewsByIds();
        //trying to sigh in when clicked and if a textView is empty it say so in the toast and dont sigh in
        sighInButton.setOnClickListener(view -> {
            if(!emailText.getText().toString().equals("")&&!passwordText.getText().toString().equals("")) {
                String email=emailText.getText().toString();
           String password=passwordText.getText().toString();
            userAuth = FirebaseAuth.getInstance();
            createUser(email,password);
            }else{
                Toast.makeText(this,"one or more fields are empty",Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Find views by ids.
     */
    public void findViewsByIds(){
        emailText=findViewById(R.id.editTextTextEmailAddressSighIn);
        passwordText=findViewById(R.id.editTextTextPasswordSingIn);
        sighInButton=findViewById(R.id.sighInButton);
    }

    /**
     * Create user.
     *
     * @param email    the email
     * @param password the password
     */
    public void createUser(String email,String password){
        userAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Map<String,Object> data=new HashMap<>();
                    data.put("email",email);
                    data.put("password",password);
                    data.put("points",0);
                    data.put("role","user");
                    DocumentReference userDuc= dataBase.collection("users").document(email);
                    userDuc.set(data);
                    //starting mainActivity and stopping this activity.
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                }else if(task.isComplete()){
                    Toast.makeText(getApplicationContext(),"something went wrong maybe the email is already in use/the password doesn't have 6 letters",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}