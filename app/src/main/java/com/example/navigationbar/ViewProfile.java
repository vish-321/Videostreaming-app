package com.example.navigationbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navigationbar.ui.ProfileViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ViewProfile extends AppCompatActivity {


    Toolbar mActionBarToolbar;

    private  static final int PERMISSION_STORAGE_CODE = 1000;

    private ProfileViewModel mViewModel;
    private TextView Name ,Standard , Contact1 ,Contact2 ,Adress ;
    private Button update;
    private EditText userName ;
    FirebaseAuth firebaseAuth;

    FirebaseFirestore fstore;

    String uid ;

    DocumentReference documentReference ;

    FirebaseStorage firebaseStorage ;
    StorageReference storageReference ;
    StorageReference fileref ;
    ImageView profileImage ;

    DatabaseReference databaseReference ;

    Button viewp , dwnld;
    String uname;
    String downloadurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("View Profile");


        Name =findViewById(R.id.name) ;
        Standard =findViewById(R.id.standard) ;
        Contact1 =findViewById(R.id.phone1) ;
        Contact2=findViewById(R.id.phone2) ;
        Adress=findViewById(R.id.adress) ;
        viewp=findViewById(R.id.btnviewprofile);
        dwnld=findViewById(R.id.btndownloadimg);
        userName=findViewById(R.id.usname);

        profileImage =findViewById((R.id.profilepic));


        firebaseAuth =FirebaseAuth.getInstance() ;
        fstore = FirebaseFirestore.getInstance();
        firebaseStorage =FirebaseStorage.getInstance() ;

        storageReference =firebaseStorage.getReference() ;
        databaseReference= FirebaseDatabase.getInstance().getReference("EmailtoUid");



        viewp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uname = userName.getText().toString().trim();

                StringBuilder sb = new StringBuilder();

                char[] carray = uname.toCharArray();


                for (int i = 0; i < uname.length(); i++) {
                    if (carray[i] != '@' && carray[i] != '.') {
                        sb.append(carray[i]);
                    }
                }

                final String umail2 = sb.toString();

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {


                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        if (snapshot.child(umail2).exists()) {
                            uid = snapshot.child(umail2).getValue().toString();
                        } else {
                            Toast.makeText(ViewProfile.this, "Invalid Username!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ViewProfile.this, "error: " + error, Toast.LENGTH_LONG).show();
                    }
                });

                if(uid!=null){
                documentReference = fstore.collection("users").document(uid);


                fileref = storageReference.child("Users/" + uid + "ProfilePic.jpg");
                documentReference.addSnapshotListener(ViewProfile.this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {


                        Standard.setText(documentSnapshot.getString("Class"));
                        Name.setText(documentSnapshot.getString("Name"));
                        Contact1.setText(documentSnapshot.getString("Contact1"));
                        Contact2.setText(documentSnapshot.getString("Contact2"));
                        Adress.setText(documentSnapshot.getString("Adress"));


                    }
                });

                fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImage);
                        downloadurl =uri.toString();
                    }
                });
            }

            }


        });


        dwnld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED){
                        String permission = (Manifest.permission.WRITE_EXTERNAL_STORAGE);

                        requestPermissions(new String[]{permission},PERMISSION_STORAGE_CODE);
                    }else {

                    if(downloadurl!=null)    startDownloading(downloadurl);
                    }
                }else {

                   if(downloadurl!=null) startDownloading(downloadurl);
                }

            }
        });






    }

    private void startDownloading( String  downloadurl ) {

        DownloadManager.Request request  = new DownloadManager.Request(Uri.parse(downloadurl));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("Download");
        request.setDescription("Downloading file...");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,""+System.currentTimeMillis());

        DownloadManager manager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ViewProfile.this,Homepage.class));
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
            startActivity(new Intent(ViewProfile.this, MainActivity.class));


        }




        if (id == R.id.home) {
            finish();

            startActivity(new Intent(ViewProfile.this, Homepage.class));

        }

        if (id == R.id.viewprofile) {
            finish();

            startActivity(new Intent(ViewProfile.this, ViewProfile.class));

        }

        if (id == R.id.upload_videos) {
            finish();

            startActivity(new Intent(ViewProfile.this, Upload_videos.class));

        }
        if (id == R.id.notices_add) {
            finish();

            startActivity(new Intent(ViewProfile.this, Addnotice.class));

        }

        if (id == R.id.newuser) {
            finish();

            startActivity(new Intent(ViewProfile.this, CreateUser.class));

        }




        return super.onOptionsItemSelected(item);

    }

}