/*******************************************************************************************
 * Name:            Andrea Panetta
 * Page Name:       Opening Screen
 *
 * XML file:        opening.xml
 *
 * Description:     A simple page to display the two path options, one for teacher login and the
 *                  other for student login. The page has been given a colourful style to attract
 *                  the attention of the young users.
 *
 *******************************************************************************************/

package com.example.myapp.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.example.myapp.R;

public class OpeningScreen extends AppCompatActivity {

    ImageButton userButton, teacherButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opening);

        userButton = findViewById(R.id.studentLogin);
        teacherButton = findViewById(R.id.teacherLogin);

        //Path to the student login page
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OpeningScreen.this, UserLoginActivity.class);
                startActivity(intent);

            }
        });

        //Path to the teacher login page
        teacherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpeningScreen.this, LoginActivity.class);
                startActivity(intent);
            }
        });


    }
}
