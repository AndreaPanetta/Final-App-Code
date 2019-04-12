/*******************************************************************************************
 * Name:            Andrea Panetta
 * Page Name:       Login Activity - Admin
 *
 * XML file:        activity_login.xml
 *
 * Description:     Teachers may use this login page to use their facilities. Teachers can also be
 *                  registered here using the alert dialog button. Students may not enter the
 *                  Teacher page.
 *
 * References:      https://firebase.google.com/docs/auth/android/email-link-auth
 *******************************************************************************************/

package com.example.myapp.Auth;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.myapp.Admin.LandingActivity;
import com.example.myapp.Model.User;
import com.example.myapp.R;
import com.example.myapp.Teacher;
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

public class LoginActivity extends AppCompatActivity {

    //Initialise Variables and database
    MaterialEditText email, userPassword;   //Sign In Teacher
    MaterialEditText teacherName, teacherEmail, teacherPass, confPass;  //Register Teacher

    Button loginButton, regButton;
    ProgressBar loginProgress;

    private FirebaseAuth myAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Set up the different views for this activity
        loginButton = findViewById(R.id.signin);
        regButton = findViewById(R.id.register);
        loginProgress = findViewById(R.id.progressBar);

        email = findViewById(R.id.login_email);
        userPassword = findViewById(R.id.login_password);

        //Sets the progress bar to invisable until the login button is pressed
        loginProgress.setVisibility(View.INVISIBLE);

        //Setting up Database and Authentication
        myAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginProgress.setVisibility(View.VISIBLE);

                final String userEmail = email.getText().toString();
                final String pass = userPassword.getText().toString();

                //Eror checking to make sure the user enters all fields before proceeding
                if(userEmail.isEmpty() || pass.isEmpty()) {

                    displayMessage("Please enter all fields!");
                    loginButton.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.INVISIBLE);
                } 
                else {

                    //The user will sign in using their verified email address, they cannot enter the application otherwise
                    myAuth.signInWithEmailAndPassword(email.getText().toString(),
                            userPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    loginProgress.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        if (myAuth.getCurrentUser().isEmailVerified()) {
                                            startActivity(new Intent(LoginActivity.this, LandingActivity.class));
                                        } else {
                                            loginButton.setVisibility(View.VISIBLE);
                                            displayMessage("Please verify your Email Address");
                                        }
                                    } else {
                                        loginButton.setVisibility(View.VISIBLE);
                                        Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                showSignUpDialog();
            }
        });

    }

    //alert Dialog set up to register a teacher if need be
    private void showSignUpDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
        alertDialog.setTitle("Register As Teacher");
        alertDialog.setMessage("Please enter all information..");

        //Uses a different layout file to view the edit text inputs - activity_register.xml
        LayoutInflater inflater = this.getLayoutInflater();
        View activity_register = inflater.inflate(R.layout.activity_register,null);

        teacherName = (MaterialEditText)activity_register.findViewById(R.id.new_teacher);
        teacherEmail = (MaterialEditText)activity_register.findViewById(R.id.new_email);
        teacherPass = (MaterialEditText)activity_register.findViewById(R.id.new_password);
        confPass = (MaterialEditText)activity_register.findViewById(R.id.conf_pass);

        alertDialog.setView(activity_register);
        alertDialog.setIcon(R.drawable.ic_account);

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.setPositiveButton("Create", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                final String name = teacherName.getText().toString();
                final String email = teacherEmail.getText().toString();
                final String pass = teacherPass.getText().toString();
                final String cnf = confPass.getText().toString();

                //Error checking to make sure all fields are filled and passwords match
                if(name.equals("") || email.equals("") || pass.equals("") || cnf.equals("") || !cnf.equals(pass)) {

                    displayMessage("Please enter all fields and make sure passwords match");
                }
                else {

                    //The user will be registered in the authentication database
                    myAuth.createUserWithEmailAndPassword(teacherEmail.getText().toString(),
                            teacherPass.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        //The user will be sent a verification email and can only proceed to login in if they verify it
                                        myAuth.getCurrentUser().sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            displayMessage("Registered Successfully. Verification email was sent");
                                                        } else {
                                                            Toast.makeText(getApplicationContext(),
                                                                    task.getException().getMessage(),
                                                                    Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(getApplicationContext(),
                                                task.getException().getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }

                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    //Method to display a simple toast message
    private void displayMessage(String text) {

        Toast.makeText(getApplicationContext(), text , Toast.LENGTH_LONG).show();

    }

}
