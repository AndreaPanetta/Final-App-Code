
/*******************************************************************************************
 * Name:            Andrea Panetta
 * Page Name:       View Files - Admin
 * Description:     Teachers can view the files they just uploaded and delete them if need be
 *
 * XML File:        activity_admin_view_files.xml
 *
 *References:       3rd Year Labs and Lectures
 *                  https://developer.android.com/guide/topics/ui/layout/recyclerview
 *
 *******************************************************************************************/

package com.example.myapp.Admin;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.example.myapp.Model.uploadPDF;
import com.example.myapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminViewFiles extends AppCompatActivity {

    //Initialize variables and database
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<uploadPDF, AdminViewFilesViewHolder> mFirebaseAdapter;

    public AdminViewFiles() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_files);

        //Set up database environment with reference to the Uploads database as the opening screen is upload files
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Uploads");

        //Setting up the list like recycler view
        recyclerView = (RecyclerView)findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(AdminViewFiles.this));

        //support for the action bar logout option
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //Setup for back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //uses the list_item layout and recycles the database attributes into a list view
        mFirebaseAdapter = new FirebaseRecyclerAdapter<uploadPDF, AdminViewFilesViewHolder>
                (uploadPDF.class, R.layout.list_item, AdminViewFilesViewHolder.class, databaseReference)
        {

            //populates the list with the name of the file uploaded by the teacher
            public void populateViewHolder(final AdminViewFilesViewHolder viewHolder, uploadPDF model, final int position) {
                viewHolder.pdfname(model.getName());


                //OnClick to delete the file from the database through an alert dialog
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(final View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminViewFiles.this);
                        builder.setMessage("Do you want to Delete this data ?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int selectedItems = position;
                                        mFirebaseAdapter.getRef(selectedItems).removeValue();
                                        mFirebaseAdapter.notifyItemRemoved(selectedItems);
                                        recyclerView.invalidate();
                                        onStart();
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


            }
        };

        recyclerView.setAdapter(mFirebaseAdapter);
    }

    //View Holder For Recycler View
    public static class AdminViewFilesViewHolder extends RecyclerView.ViewHolder {
        private final TextView file_name;


        //Initialises the Textview id
        public AdminViewFilesViewHolder(final View itemView) {
            super(itemView);

            file_name = (TextView) itemView.findViewById(R.id.fileText);


        }

        //Sets the text view element
        private void pdfname(String title) {
            file_name.setText(title);
        }

    }

    //method to display simple toast messages
    private void displayMessage(String text) {

        Toast.makeText(getApplicationContext(), text , Toast.LENGTH_LONG).show();

    }
}
