package com.example.navigationbar.ui;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navigationbar.R;
import com.example.navigationbar.noticeclass;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Notices extends Fragment {

    private NoticesViewModel mViewModel;

    public static class nMessageViewHolder extends RecyclerView.ViewHolder {
        TextView noticeTextView, nmessageTime ,noticeHeading;





        public nMessageViewHolder(View v) {
            super(v);
            noticeTextView = (TextView) itemView.findViewById(R.id.noticeTextView);

            noticeHeading = (TextView) itemView.findViewById(R.id.noticeHeading);

            nmessageTime = (TextView) itemView.findViewById(R.id.nmessage_time);




        }
    }


    DocumentReference documentReference;
    String uid;


    private static final String TAG = "NoticesActivity";
    public  String MESSAGES_CHILD = "Notices";


    private RecyclerView nMessageRecyclerView;


    private FirebaseAuth nFirebaseAuth;
    private FirebaseUser nFirebaseUser;
    private DatabaseReference nFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<noticeclass, nMessageViewHolder>
            nFirebaseAdapter;


    private LinearLayoutManager nLinearLayoutManager;

    String standard ;


    public static Notices newInstance() {
        return new Notices();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       View v= inflater.inflate(R.layout.notices_fragment, container, false);


        nFirebaseAuth = FirebaseAuth.getInstance();
        nFirebaseUser = nFirebaseAuth.getCurrentUser();


        nMessageRecyclerView = (RecyclerView)v. findViewById(R.id.noticeRecyclerView);

        uid= nFirebaseAuth.getCurrentUser().getUid() ;

        documentReference=  FirebaseFirestore.getInstance().collection("users").document(uid);

        documentReference.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {


              standard=documentSnapshot.getString("Class");





            }
        });









        nLinearLayoutManager = new LinearLayoutManager(getActivity());
        nLinearLayoutManager.setStackFromEnd(true);

        nMessageRecyclerView.setLayoutManager(nLinearLayoutManager);




        nFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        SnapshotParser<noticeclass> nparser = new SnapshotParser<noticeclass>() {
            @Override
            public noticeclass parseSnapshot(DataSnapshot dataSnapshot) {
                noticeclass notices = dataSnapshot.getValue(noticeclass.class);
                if (notices != null) {
                    notices.setId(dataSnapshot.getKey());

                    //    Toast.makeText(NoticesActivity.this,"2 done",Toast.LENGTH_SHORT).show();

                }


                return notices;
            }
        };

        final DatabaseReference nmessagesRef = nFirebaseDatabaseReference.child(MESSAGES_CHILD);
        final FirebaseRecyclerOptions<noticeclass> options =
                new FirebaseRecyclerOptions.Builder<noticeclass>()
                        .setQuery(nmessagesRef, nparser)
                        .build();


        nFirebaseAdapter = new FirebaseRecyclerAdapter<noticeclass, nMessageViewHolder>(options) {




            @Override
            public nMessageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {


                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new nMessageViewHolder(inflater.inflate(R.layout.item_notices, viewGroup, false));
            }




            @Override
            protected void onBindViewHolder(final nMessageViewHolder viewHolder,
                                            int position,
                                            noticeclass notices) {











                viewHolder.noticeTextView.setText(notices.getnText());
                viewHolder.noticeTextView.setVisibility(TextView.VISIBLE);

                viewHolder.noticeHeading.setText(notices.getHeading());
                viewHolder.noticeHeading.setVisibility(TextView.VISIBLE);

                viewHolder.nmessageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", notices.getnMessageTime()));







            }




        };

        nFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = nFirebaseAdapter.getItemCount();
                int lastVisiblePosition =
                        nLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    nMessageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });




        nMessageRecyclerView.setAdapter(nFirebaseAdapter);






        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(NoticesViewModel.class);
        // TODO: Use the ViewModel
    }
    @Override
    public void onPause() {
        nFirebaseAdapter.stopListening();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        nFirebaseAdapter.startListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}