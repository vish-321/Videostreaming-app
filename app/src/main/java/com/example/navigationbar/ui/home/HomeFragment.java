package com.example.navigationbar.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navigationbar.Addnotice;
import com.example.navigationbar.CreateUser;
import com.example.navigationbar.Fullscreen;
import com.example.navigationbar.Homepage;
import com.example.navigationbar.MainActivity;
import com.example.navigationbar.Member;
import com.example.navigationbar.R;
import com.example.navigationbar.Upload_videos;
import com.example.navigationbar.ViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    FirebaseDatabase database;

    String name,url;

    FirebaseAuth firebaseAuth ;
    String UserEmail ;

    private  FirebaseRecyclerAdapter<Member, ViewHolder> firebaseRecyclerAdapter ;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = root.findViewById(R.id.recyclerview_ShowVideo);
       // recyclerView.setHasFixedSize(true);


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("video");
        firebaseAuth =FirebaseAuth.getInstance() ;

        UserEmail = firebaseAuth.getCurrentUser().getEmail().toString();
        setHasOptionsMenu(true);







        return root;
    }

    public void firebaseSearch(String searchtext){
        String query = searchtext.toLowerCase();
        Query firebaseQuery = databaseReference.orderByChild("search").startAt(query).endAt(query + "\uf8ff");



        FirebaseRecyclerOptions<Member> options =
                new FirebaseRecyclerOptions.Builder<Member>()
                        .setQuery(firebaseQuery,Member.class)
                        .build();


        FirebaseRecyclerAdapter<Member,ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter< Member, ViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Member model) {

                        //  FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        //   String currentUserId = user.getUid();
                        //  final  String postkey = getRef(position).getKey();

                        holder.setExoplayer(getActivity().getApplication(),model.getName(),model.getVideourl());




                        holder.setOnClicklistener(new ViewHolder.Clicklistener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                name = getItem(position).getName();
                                url = getItem(position).getVideourl();
                                Intent intent = new Intent(getActivity(), Fullscreen.class);

                                //change above to Fullscreen.class later


                                intent.putExtra("nam", name);
                                intent.putExtra("ur", url);
                                startActivity(intent);


                            }

                            @Override
                            public void onItemLongClick(View view, int position) {

                                name = getItem(position).getName();

                            }



                        });

                    }





                    @NonNull
                    @Override
                    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item,parent,false);

                        return new ViewHolder(view);

                    }
                };

        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);



    }








    @Override
    public void onStart() {
        super.onStart();




        FirebaseRecyclerOptions<Member> options =
                new FirebaseRecyclerOptions.Builder<Member>()
                        .setQuery(databaseReference,Member.class)
                        .build();

        firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter< Member, ViewHolder>(options) {


                    @Override
                    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Member model) {

                        holder.setExoplayer(getActivity().getApplication(), model.getName(), model.getVideourl());



                        holder.setOnClicklistener(new ViewHolder.Clicklistener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                name = getItem(position).getName();
                                url = getItem(position).getVideourl();
                               Intent intent = new Intent(getActivity(), Fullscreen.class) ;





                                intent.putExtra("nam",name);
                               intent.putExtra("ur",url);
                               startActivity(intent);


                            }



                            @Override
                            public void onItemLongClick(View view, int position) {

                                name = getItem(position).getName();

                            }


                        });




                    }
                        @NonNull
                        @Override
                        public ViewHolder onCreateViewHolder(@NonNull ViewGroup  parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.item,parent,false);



                            return new ViewHolder(view);



                        }
                    };


        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }


    @Override
    public void onPause() {
        firebaseRecyclerAdapter.stopListening();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {



        inflater.inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.home).setVisible(false);
        menu.findItem(R.id.logout).setVisible(false);


        String usmail=FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();



        if(usmail.equals("admin@11.com")||usmail.equals("admin@12.com")) {

            menu.findItem(R.id.notices_add).setVisible(true);
            menu.findItem(R.id.upload_videos).setVisible(true);
            menu.findItem(R.id.newuser).setVisible(true);
            menu.findItem(R.id.viewprofile).setVisible(true);

        }

        menu.findItem(R.id.search_firebase).setVisible(true) ;



        MenuItem item = menu.findItem(R.id.search_firebase);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                firebaseSearch(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu,inflater);









    }











}