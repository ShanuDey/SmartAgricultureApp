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

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail,etPass;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //my code
        etEmail = (EditText) findViewById(R.id.email);
        etPass = (EditText) findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();
    }

    //my methods
    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser!=null){
            Toast.makeText(this, "Login Successfull", Toast.LENGTH_SHORT).show();
            finish();;
            startActivity(new Intent(this,MainActivity.class));
        }

    }

    // signin button click
    public void onSigninButtonClick(View view){
        String email,pass;
        email = etEmail.getText().toString().trim();
        pass = etPass.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(!email.matches(emailPattern)){
            etEmail.setError("Inalid Email");
            //Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(pass.length()<6){
            etPass.setError("Length must be greater than 6 letter");
            return;
        }
        //sign in
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.v("tag ", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.v("tag", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Email or password is incorrect",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });

    }
    // register button click
    public void onResigterButtonClick(View view){
        finish();
        startActivity(new Intent(this,RegisterActivity.class));
    }

}
