package com.example.navigationbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateUser extends AppCompatActivity {



    String username ,useremail ,userclass ,userpassword ;

    EditText name ,email , standard ,password ;

    Button button ;

    FirebaseAuth firebaseAuth ;
    FirebaseUser newuser ;
    FirebaseFirestore fstore;

    String uid;
    DocumentReference documentReference;
    Toolbar mActionBarToolbar;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        name= findViewById(R.id.uname);
       email= findViewById(R.id.uEmail);
       standard= findViewById(R.id.uclass);
        password= findViewById(R.id.upassword);
        button=findViewById(R.id.ucreate);


       firebaseAuth= FirebaseAuth.getInstance();

        fstore = FirebaseFirestore.getInstance();


        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("Create User");


        databaseReference= FirebaseDatabase.getInstance().getReference("EmailtoUid");

       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               username = name.getText().toString().trim();
               userclass = standard.getText().toString().trim();
               useremail = email.getText().toString().trim();
               userpassword = password.getText().toString().trim();
               if(Validate()){
                 firebaseAuth .createUserWithEmailAndPassword(useremail,userpassword).addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           Toast.makeText(CreateUser.this, e.getMessage(),Toast.LENGTH_LONG).show();
                       }
                   }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         if(task.isSuccessful()){
                             newuser= task.getResult().getUser();
                             uid = newuser.getUid();

                            
                             documentReference = fstore.collection("users").document(uid);

                             Updateuser();
                             Adduid();



                         }
                     }


                 });


               }
           }


       });



    }

    private void Adduid() {


        StringBuilder sb = new StringBuilder();

        char[] carray = useremail.toCharArray();


        for (int i = 0; i < useremail.length(); i++) {
            if(carray[i]!='@' && carray[i]!='.'){
            sb.append(carray[i]);}
        }

        String umail2=sb.toString();
        databaseReference.child(umail2).setValue(uid);




    }

    private void Updateuser() {

        Map<String, Object> user = new HashMap<>();
        user.put("Name", username);
        user.put("Class", userclass);


        documentReference.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CreateUser.this, "Sucessful!", Toast.LENGTH_LONG).show();



                } else {
                    Toast.makeText(CreateUser.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private boolean Validate() {
        if(!username.isEmpty() && !userpassword.isEmpty() && !useremail.isEmpty() && !userclass.isEmpty()){
            return true ;
        }
        else {
            Toast.makeText(CreateUser.this,"Please enter all details",Toast.LENGTH_LONG).show();
            return false ;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(CreateUser.this,Homepage.class));
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.main_menu, menu);



            menu.findItem(R.id.home).setVisible(true);
            menu.findItem(R.id.notices_add).setVisible(true);
            menu.findItem(R.id.upload_videos).setVisible(true);
            menu.findItem(R.id.newuser).setVisible(true);
            menu.findItem(R.id.viewprofile).setVisible(true);







        return true;


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.logout) {

            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(CreateUser.this, MainActivity.class));


        }

        if (id == R.id.viewprofile) {
            finish();

            startActivity(new Intent(CreateUser.this, ViewProfile.class));

        }


        if (id == R.id.home) {
            finish();

            startActivity(new Intent(CreateUser.this, Homepage.class));

        }

        if (id == R.id.upload_videos) {
            finish();

            startActivity(new Intent(CreateUser.this, Upload_videos.class));

        }
        if (id == R.id.notices_add) {
            finish();

            startActivity(new Intent(CreateUser.this, Addnotice.class));

        }

        if (id == R.id.newuser) {
            finish();

            startActivity(new Intent(CreateUser.this, CreateUser.class));

        }




        return super.onOptionsItemSelected(item);

    }

}