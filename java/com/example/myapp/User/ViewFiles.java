
/*******************************************************************************************
 * Name:            Andrea Panetta
 * Page Name:       View Files - Student
 * Description:     Allows the user to view the files on their device that the teacher had previously
 *                  uploaded. They also have the ability to print these if they have the feature enabled
 *                  on their device.
 *
 * XML File:        activity_view_activities.xml
 *
 *References:       3rd Year Labs and Lectures
 *                  https://firebase.google.com/docs/storage/android/download-files
 *
 *******************************************************************************************/


package com.example.myapp.User;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapp.Model.uploadPDF;
import com.example.myapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ViewFiles extends Fragment {

    //Initialise variables and database
    ListView myPDFListView;
    DatabaseReference databaseReference;
    List<uploadPDF> uploadPDFS;

    public ViewFiles() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_view_activities, container, false);

        myPDFListView = (ListView)view.findViewById(R.id.myListView);
        uploadPDFS = new ArrayList<>();

        //Method to view the files in a list format
        viewAllFiles();

        //Sets the ability to click an item where the device then displays the file using the fileViewer of your choice
        myPDFListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                uploadPDF uploadPDF = uploadPDFS.get(position);

                Intent intent = new Intent();
                intent.setType(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(uploadPDF.getUrl()));
                startActivity(intent);
            }
        });

        return view;
    }

    private void viewAllFiles() {

        //Loops through the Uploads database and added the items to the list
        databaseReference = FirebaseDatabase.getInstance().getReference("Uploads");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){

                    uploadPDF uploadPDF = postSnapshot.getValue(com.example.myapp.Model.uploadPDF.class);
                    uploadPDFS.add(uploadPDF);

                }

                String[] uploads = new String[uploadPDFS.size()];

                for(int i=0;i<uploads.length;i++){

                    uploads[i] = uploadPDFS.get(i).getName();
                }

                //Sets up the list view using the adapter and the android list item layout while referencing to the upload database
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,uploads){

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {

                        View view = super.getView(position, convertView, parent);
                        TextView myText = (TextView) view.findViewById(android.R.id.text1);
                        myText.setTextColor(Color.BLACK);

                        return view;
                    }
                };
                myPDFListView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
