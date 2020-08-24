package com.example.navigationbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
        import android.view.Menu;

import com.example.navigationbar.ui.home.HomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
        import com.google.android.material.snackbar.Snackbar;
        import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
        import androidx.navigation.Navigation;
        import androidx.navigation.ui.AppBarConfiguration;
        import androidx.navigation.ui.NavigationUI;
        import androidx.drawerlayout.widget.DrawerLayout;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.appcompat.widget.Toolbar;

public class Homepage extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_chat, R.id.nav_notices,R.id.nav_profile)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to Exit?")
                    .setNegativeButton("No",null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            finishAffinity();
                        }
                    }).show();
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.main_menu, menu);

        //add condition for only admin of setvisibility  later

        String usmail=FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();



        if(usmail.equals("admin@11.com")||usmail.equals("admin@12.com")) {
            menu.findItem(R.id.notices_add).setVisible(true);
            menu.findItem(R.id.upload_videos).setVisible(true);
            menu.findItem(R.id.newuser).setVisible(true);
            menu.findItem(R.id.viewprofile).setVisible(true);

        }

        menu.findItem(R.id.home).setVisible(false);
        return super.onCreateOptionsMenu(menu);





    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.logout) {

            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(Homepage.this, MainActivity.class));


        }




        if (id == R.id.home) {
            finish();

            startActivity(new Intent(Homepage.this, Homepage.class));

        }

        if (id == R.id.viewprofile) {
            finish();

            startActivity(new Intent(Homepage.this, ViewProfile.class));

        }


        if (id == R.id.upload_videos) {
            finish();

            startActivity(new Intent(Homepage.this, Upload_videos.class));

        }
        if (id == R.id.notices_add) {
            finish();

            startActivity(new Intent(Homepage.this, Addnotice.class));

        }
        if (id == R.id.newuser) {
            finish();

            startActivity(new Intent(Homepage.this, CreateUser.class));

        }




        return super.onOptionsItemSelected(item);

    }
}