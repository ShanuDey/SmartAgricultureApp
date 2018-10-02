package com.example.user.myfinalstexappbuild;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    //my variables
    private EditText etEmail,etPass,etConfirmPass;
    private FirebaseAuth mAuth;
    private String uid;
    private String[] params={"temperature","sunlightintensity","pump","humidity","smoke","soilmoisture"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etEmail = (EditText) findViewById(R.id.email);
        etPass = (EditText) findViewById(R.id.password);
        etConfirmPass =(EditText) findViewById(R.id.confirmPassword);

        mAuth = FirebaseAuth.getInstance();
    }

    //my methods

    //already have an account sign in
    public void onClickSignin(View view){
        finish();
        startActivity(new Intent(this,LoginActivity.class));
    }

    //sign up
    public void onSignUpButtonClick(View view){
        String email,pass,confirmPass;
        email = etEmail.getText().toString().trim();
        pass = etPass.getText().toString().trim();
        confirmPass = etConfirmPass.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(!email.matches(emailPattern)){
            etEmail.setError("Inalid Email");
            //Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
        }
        if(pass.length()<6){
            etPass.setError("Length must be greater than 6 letter");
            return;
        }
        if(!pass.equals(confirmPass)){
            etConfirmPass.setError("Password not matched");
            return;
        }
        //sign up
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.v("tag", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            uid = user.getUid();
                            for(int i=0;i<6;i++){
                                setData(params[i]);
                            }
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.v("tag", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
    public void setData(String param){
        // Read from the database
        FirebaseDatabase.getInstance().getReference(uid).child("sensor").child(param).setValue(0);
    }

    private void updateUI(FirebaseUser user) {
        if(user!=null){
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }
    }
}
