
/*******************************************************************************************
 * Name:            Andrea Panetta
 * Page Name:       Add Student
 * Description:     Teachers can add students to the database where students can then login
 *                  using these credentials.
 *
 * XML File:        activity_add_student.xml
 *
 *References:       3rd Year Labs and Lectures
 *                  https://firebase.google.com/docs/auth/android/manage-users
 *
 *******************************************************************************************/


package com.example.myapp.Admin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myapp.Model.User;
import com.example.myapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class AddStudent extends AppCompatActivity {

    //Initialise Variables
    MaterialEditText studentName, studentEmail, studentClass, studentPass, confPass;
    Button addButton;
    private ProgressBar progress;
    private FirebaseAuth studentAuth;
    FirebaseDatabase db;
    DatabaseReference users;
    User user;
    long maxid=0;

    public AddStudent() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        //Support for Back Button in Action Bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Link variables with their xml attributes
        studentName = findViewById(R.id.newStudentName);
        studentEmail = findViewById(R.id.newStudentEmail);
        studentClass = findViewById(R.id.newStudentClass);
        studentPass = findViewById(R.id.newStudentPass);
        confPass = findViewById(R.id.newStudentConfirm);
        user = new User();

        //Firebase setup
        studentAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Students");                    //References the Students database on Firebase

        //Increments the user id every time a new user is input
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    maxid=(dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addButton = findViewById(R.id.register);

        progress = findViewById(R.id.progressBar);
        progress.setVisibility(View.INVISIBLE);             //Setting the progress bar to invisible until button is clicked


        //When the add student button is clicked.....
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setVisibility(View.VISIBLE);
                addButton.setVisibility(View.INVISIBLE);

                final String name = studentName.getText().toString();
                final String email = studentEmail.getText().toString();
                final String studentclass = studentClass.getText().toString();
                final String password = studentPass.getText().toString();
                final String confpass = confPass.getText().toString();

                //Sets the credentials to add to the database
                user.setName(name);
                user.setEmail(email);
                user.setStudentClass(studentclass);
                user.setPassword(password);

                //Error checking to make sure user cannot proceed until fields are filled and passwords match
                if(name.isEmpty() || email.isEmpty() || studentclass.isEmpty() || password.isEmpty() || confpass.isEmpty() || !password.equals(confpass)) {
                    displayMessage("Please check fields are filled and Student is checked");
                    progress.setVisibility(View.INVISIBLE);
                    addButton.setVisibility(View.VISIBLE);
                    studentName.setText("");
                    studentEmail.setText("");
                    studentClass.setText("");
                    studentPass.setText("");
                    confPass.setText("");

                } else {

                    //Student is created using email and password so that they may login using these credentials
                    studentAuth.createUserWithEmailAndPassword(studentEmail.getText().toString(),
                            studentPass.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progress.setVisibility(View.GONE);

                                    //If the student hasn't already been made, they can be added to the database..
                                    if(task.isSuccessful()) {
                                        displayMessage("Student Added Successfully");
                                        users.child(String.valueOf(maxid+1)).setValue(user);
                                        progress.setVisibility(View.INVISIBLE);
                                        addButton.setVisibility(View.VISIBLE);

                                        //Sets the fields to blank for the next input
                                        studentName.setText("");
                                        studentEmail.setText("");
                                        studentClass.setText("");
                                        studentPass.setText("");
                                        confPass.setText("");

                                    } else {

                                        //Otherwise they need to try again
                                        displayMessage("Error creating student...Please try again.");
                                        addButton.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                }
            }
        });
}

    //Action Bar method to display back button
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    //Method to display simple toast messages
    private void displayMessage(String text) {
        Toast.makeText(AddStudent.this, text ,Toast.LENGTH_SHORT).show();
    }
}
