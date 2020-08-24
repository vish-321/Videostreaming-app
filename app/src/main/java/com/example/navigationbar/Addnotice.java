package com.example.navigationbar;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Addnotice extends AppCompatActivity {


    private Button nAddButton;

    private EditText nHeading,nContent ;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mFirebaseDatabaseReference;
    public  String MESSAGES_CHILD = "Notices";

    DocumentReference documentReference ;
    String uid, standard ;

    Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnotice);

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("Add notice");




        uid= mFirebaseAuth.getCurrentUser().getUid() ;

        documentReference=  FirebaseFirestore.getInstance().collection("users").document(uid);

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {


                standard=documentSnapshot.getString("Class");





            }
        });






        nHeading = (EditText) findViewById(R.id.nHeading);
        nContent=(EditText) findViewById(R.id.nContent) ;

        nAddButton = (Button) findViewById(R.id.nAddButton);
        nAddButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference(MESSAGES_CHILD);
                noticeclass notice = new
                        noticeclass(nContent.getText().toString(),
                        nHeading.getText().toString());
                mFirebaseDatabaseReference
                        .push().setValue(notice).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Addnotice.this, e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Addnotice.this, "Successful!",Toast.LENGTH_LONG).show();
                    }
                });
                nContent.setText("");
                nHeading.setText("");
            }
        });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Addnotice.this,Homepage.class));
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
            startActivity(new Intent(Addnotice.this, MainActivity.class));


        }




        if (id == R.id.home) {
            finish();

            startActivity(new Intent(Addnotice.this, Homepage.class));

        }

        if (id == R.id.upload_videos) {
            finish();

            startActivity(new Intent(Addnotice.this, Upload_videos.class));

        }
        if (id == R.id.notices_add) {
            finish();

            startActivity(new Intent(Addnotice.this, Addnotice.class));

        }

        if (id == R.id.newuser) {
            finish();

            startActivity(new Intent(Addnotice.this, CreateUser.class));

        }
        if (id == R.id.viewprofile) {
            finish();

            startActivity(new Intent(Addnotice.this, ViewProfile.class));

        }



        return super.onOptionsItemSelected(item);

    }





}