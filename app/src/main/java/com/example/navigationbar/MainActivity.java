package com.example.navigationbar;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    /* Define the UI elements */
    private EditText eName;
    private EditText ePassword;

    private Button eLogin;

    private FirebaseAuth firebaseAuth ;

    ProgressDialog progressDialog ;



    String userName = "";
    String userPassword = "";




    @Override
    protected void onCreate(Bundle savedInstanceState) {




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Bind the XML views to Java Code Elements */
        eName = findViewById(R.id.Username);
        ePassword = findViewById(R.id.Password);

        eLogin = findViewById(R.id.button1);

        firebaseAuth = FirebaseAuth.getInstance() ;
        progressDialog =new ProgressDialog(this) ;

        FirebaseUser user= firebaseAuth.getCurrentUser() ;

        if(user != null){
            finish();
            startActivity(new Intent(MainActivity.this ,Homepage.class));
        }


        /* Describe the logic when the login button is clicked */
        eLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /* Obtain user inputs */
                userName = eName.getText().toString();
                userPassword = ePassword.getText().toString();

                /* Check if the user inputs are empty */
                if(userName.isEmpty() || userPassword.isEmpty())
                {
                    /* Display a message toast to user to enter the details */
                    Toast.makeText(MainActivity.this, "Please enter username and password!", Toast.LENGTH_LONG).show();

                }
                else {


                    validate(userName, userPassword);



                }
            }
        });
    }

    /* Validate the credentials */





    private void validate(String userName, String userPassword)
    {
        progressDialog.setMessage("Verifying");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(userName, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    startActivity(new Intent(MainActivity.this, Homepage.class));

                    Toast.makeText(MainActivity.this, "login successful!", Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(MainActivity.this, "error: "+task.getException(), Toast.LENGTH_LONG).show();


                    progressDialog.dismiss();

                }
            }
        });






    }
}