package com.example.streetviewmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends BaseActivity {
    EditText emailText;
    EditText passwordText;
    Button sighInButton;
    private final FirebaseFirestore dataBase=FirebaseFirestore.getInstance();
    FirebaseAuth userAuth;
    private final FireBaseUtil fireBaseUtil = FireBaseUtil.FireBaseHandlerCreator();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        findViewsByIds();
        sighInButton.setOnClickListener(view -> {
           String email=emailText.getText().toString();
           String password=passwordText.getText().toString();
            userAuth = FirebaseAuth.getInstance();
            createUser(email,password);
        });
    }
    public void findViewsByIds(){
        emailText=findViewById(R.id.editTextTextEmailAddressSighIn);
        passwordText=findViewById(R.id.editTextTextPasswordSingIn);
        sighInButton=findViewById(R.id.sighInButton);
    }
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
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
}