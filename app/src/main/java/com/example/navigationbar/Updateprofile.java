package com.example.navigationbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Tag;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


public class Updateprofile extends AppCompatActivity {


    private EditText  Contact1, Contact2, Adress;
    private TextView  Name, Standard;

    private Button save, changeprofilepic;

    String  eName,eStandard,eContact1, eContact2, eAdress;

    FirebaseAuth firebaseAuth;

    FirebaseFirestore fstore;

    String uid;
    DocumentReference documentReference;

    ImageView profileImage;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    StorageReference fileref;
    Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateprofile);


        Name = (TextView)findViewById(R.id.name);
        Standard=(TextView) findViewById(R.id.standard);
        Contact1 = findViewById(R.id.phone1);
        Contact2 = findViewById(R.id.phone2);
        Adress = findViewById(R.id.adress);
        save = findViewById(R.id.btnsave);
        profileImage = findViewById((R.id.profilepic));
        changeprofilepic = findViewById(R.id.btnchangepic);
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("Update profile");


        firebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        storageReference = firebaseStorage.getReference();


        uid = firebaseAuth.getCurrentUser().getUid();

        documentReference = fstore.collection("users").document(uid);
        fileref = storageReference.child("Users/" + uid + "ProfilePic.jpg");

        fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Updateprofile.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                eStandard=documentSnapshot.getString("Class");
                eName=documentSnapshot.getString("Name");
                Standard.setText(documentSnapshot.getString("Class"));
                Name.setText(documentSnapshot.getString("Name"));
                Contact1.setText(documentSnapshot.getString("Contact1"));
                Contact2.setText(documentSnapshot.getString("Contact2"));
                Adress.setText(documentSnapshot.getString("Adress"));


            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                eContact1 = Contact1.getText().toString();
                eContact2 = Contact2.getText().toString();
                eAdress = Adress.getText().toString();

                Validate();
            }


        });


        changeprofilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(openGallery, 1000);
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                profileImage.setImageURI(imageUri);

                uploadImagetoFirebase(imageUri);
            }
        }


    }

    private void uploadImagetoFirebase(Uri imageUri) {


        fileref.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Updateprofile.this, "Image Uploaded!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(Updateprofile.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void Validate() {

        if ( eContact1.isEmpty() || eContact2.isEmpty() || eAdress.isEmpty()) {
            Toast.makeText(Updateprofile.this, "Please Enter all Details", Toast.LENGTH_LONG).show();


        } else {
            updateDetails();
        }
    }

    private void updateDetails() {


        //  UserProfile  userProfile= new UserProfile(enickName,eName,eStandard,eContact1,eContact2,eAdress) ;

        Map<String, Object> user = new HashMap<>();
        user.put("Name", eName);
        user.put("Class", eStandard);
        user.put("Contact1", eContact1);
        user.put("Contact2", eContact2);
        user.put("Adress", eAdress);


        documentReference.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Updateprofile.this, "Sucessful!", Toast.LENGTH_LONG).show();

                    finish();
                    startActivity(new Intent(Updateprofile.this, Homepage.class));


                } else {
                    Toast.makeText(Updateprofile.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Updateprofile.this,Homepage.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.main_menu, menu);



        menu.findItem(R.id.logout).setVisible(false);


        return true;


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();




        if (id == R.id.home) {
            finish();

            startActivity(new Intent(Updateprofile.this, Homepage.class));

        }






        return super.onOptionsItemSelected(item);

    }


}

