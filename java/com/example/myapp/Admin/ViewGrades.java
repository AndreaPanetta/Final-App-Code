
/*******************************************************************************************
 * Name:            Andrea Panetta
 * Page Name:       View Grades - Admin
 * Description:     Allows the teacher view the students they have added
 *
 * XML File:        activity_view_grades.xml
 *
 *References:       3rd Year Labs and Lectures
 *                  https://developer.android.com/guide/topics/ui/layout/recyclerview
 *
 *******************************************************************************************/


package com.example.myapp.Admin;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.R;
import com.example.myapp.Model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ViewGrades extends Fragment {

    //Initialise variables and database
    ListView list;
    RecyclerView recyclerView;
    Button addStudent;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<User, ViewGradesViewHolder> mFirebaseAdapter;

    public ViewGrades() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_view_grades, container, false);

        //Set up the database environment
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Students");

        addStudent = (Button)view.findViewById(R.id.add_student);

        //Set up the recyclerview to create a list like structure
        recyclerView = (RecyclerView)view.findViewById(R.id.myList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //onClick listener when add student is pressed
        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddStudent.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        //Starting the recycler view using the student item xml file
        mFirebaseAdapter = new FirebaseRecyclerAdapter<User, ViewGradesViewHolder>
                (User.class, R.layout.student_item, ViewGradesViewHolder.class, databaseReference)
        {

            //Populates the list like view from the User model class that references to the User database
            public void populateViewHolder(final ViewGradesViewHolder viewHolder, User model, final int position) {
                viewHolder.studentname(model.getName());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(final View v) {
                        //When an item is pressed, the student detail activity is shown
                        Intent intent = new Intent(getActivity(),StudentDetail.class);
                        startActivity(intent);
                    }
                });


            }
        };

        recyclerView.setAdapter(mFirebaseAdapter);
    }

    //View Holder For Recycler View
    public static class ViewGradesViewHolder extends RecyclerView.ViewHolder {
        private final TextView file_name;



        public ViewGradesViewHolder(final View itemView) {
            super(itemView);

            file_name = (TextView) itemView.findViewById(R.id.fileText);


        }

        private void studentname(String title) {
            file_name.setText(title);
        }

    }
}
