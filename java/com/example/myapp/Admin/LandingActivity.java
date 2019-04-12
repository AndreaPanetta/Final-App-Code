
/*******************************************************************************************
 * Name:            Andrea Panetta
 * Page Name:       Landing Activity - Admin
 * Description:     Mainly a navigation page, the Landing page gives the user different options to
 *                  the student.
 *
 * XML File:        activity_landing.xml
 *
 *References:       3rd Year Labs and Lectures
 *                  https://firebase.google.com/docs/auth/android/manage-users
 *
 *******************************************************************************************/


package com.example.myapp.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.myapp.Auth.OpeningScreen;
import com.example.myapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class LandingActivity extends AppCompatActivity {

    //Initialise the database
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        //Set up the database environment
        firebaseAuth = FirebaseAuth.getInstance();

        //Link navigation variable to the XML file
        BottomNavigationView navigation = findViewById(R.id.navigation);

        final UploadFiles uploadFiles = new UploadFiles();
        final ViewGrades viewGrades = new ViewGrades();


        //Setup for the bottom navigation tabs - References the navigation menu item
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.navigation_upload) {
                    setFragment(uploadFiles);
                    return true;
                }
                else if (id == R.id.navigation_grades) {
                    setFragment(viewGrades);
                    return true;
                }
                return false;
            }
        });

        navigation.setSelectedItemId(R.id.navigation_upload);

    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }

    //Create the action bar option for the user to logout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:

                //Used to sign out the current user and transfer them to the opening screen
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, OpeningScreen.class));


            break;
        }
        return super.onOptionsItemSelected(item);
    }
}
