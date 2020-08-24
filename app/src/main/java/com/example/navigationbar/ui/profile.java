package com.example.navigationbar.ui;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navigationbar.R;
import com.example.navigationbar.Updateprofile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class profile extends Fragment {

    private ProfileViewModel mViewModel;
    private TextView Name ,Standard , Contact1 ,Contact2 ,Adress ;
    private Button update;

    FirebaseAuth firebaseAuth;

    FirebaseFirestore fstore;

    String uid ;

    DocumentReference documentReference ;

    FirebaseStorage firebaseStorage ;
    StorageReference storageReference ;
    StorageReference fileref ;
    ImageView profileImage ;

    public static profile newInstance() {
        return new profile();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.profile_fragment, container, false);


        Name =v.findViewById(R.id.name) ;
        Standard =v.findViewById(R.id.standard) ;
        Contact1 =v.findViewById(R.id.phone1) ;
        Contact2=v.findViewById(R.id.phone2) ;
        Adress=v.findViewById(R.id.adress) ;
        update =v.findViewById(R.id.btnupdate) ;
        profileImage =v.findViewById((R.id.profilepic));


        firebaseAuth =FirebaseAuth.getInstance() ;
        fstore = FirebaseFirestore.getInstance();
        firebaseStorage =FirebaseStorage.getInstance() ;

        storageReference =firebaseStorage.getReference() ;


        uid= firebaseAuth.getCurrentUser().getUid() ;

        documentReference=  fstore.collection("users").document(uid);



        fileref =storageReference.child("Users/"+ uid +"ProfilePic.jpg") ;



        documentReference.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
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
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), Updateprofile.class));
            }
        });



        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }

}