
/*******************************************************************************************
 * Name:            Andrea Panetta
 * Page Name:       Student Detail - Admin
 * Description:     An activity page where the teacher can view the details of the student they added,
 *                  They can also delete them from the database.
 *
 * XML File:        student_detail.xml
 *
 *References:       3rd Year Labs and Lectures
 *                  https://firebase.google.com/docs/auth/android/manage-users
 *
 *******************************************************************************************/


package com.example.myapp.Admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.Model.User;
import com.example.myapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class StudentDetail extends AppCompatActivity {

    //Initialise the database and the multiple text views
    DatabaseReference databaseReference;
    TextView studentname,studentemail,studentclass,studentpassword;
    Button delete;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_detail);

        databaseReference = FirebaseDatabase.getInstance().getReference("Students");

        //Link string names to the corresponding xml attributes
        studentname = (TextView)findViewById(R.id.studentName);
        studentemail = (TextView)findViewById(R.id.studentEmail);
        studentclass = (TextView)findViewById(R.id.studentClass);
        studentpassword = (TextView)findViewById(R.id.studentPass);
        delete = (Button)findViewById(R.id.deleteButton);


        //Used to loop through the database and retrieve the users details through the User database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                for(final DataSnapshot cSnapshot:dataSnapshot.getChildren()){
                        User user = cSnapshot.getValue(User.class);

                        studentname.setText("Name: " +user.getName());
                        studentemail.setText("Email: "+user.getEmail());
                        studentclass.setText("Class: " +user.getStudentClass());
                        studentpassword.setText("Password: " +user.getPassword());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Button to delete a user from the database through an alert dialog
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(StudentDetail.this);
                builder.setMessage("Do you want to Delete this data ?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                databaseReference.removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()) {
                                                    displayMessage("User deleted");
                                                }
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.setTitle("Confirm");
                dialog.show();
            }
        });

        //Back Button support
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    //Method for the back button on the action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //Method to display a simple toast message
    private void displayMessage(String text) {
        Toast.makeText(getApplicationContext(), text ,Toast.LENGTH_SHORT).show();
    }


}
