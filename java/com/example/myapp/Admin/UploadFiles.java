
/*******************************************************************************************
 * Name:            Andrea Panetta
 * Page Name:       Upload Files - Admin
 * Description:     Allows the teacher to upload files for the students to view
 *
 * XML File:        activity_upload_files.xml
 *
 *References:       3rd Year Labs and Lectures
 *                  https://firebase.google.com/docs/storage/android/upload-files
 *
 *******************************************************************************************/


package com.example.myapp.Admin;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapp.Model.uploadPDF;
import com.example.myapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;

public class UploadFiles extends Fragment {

    //Initialise variables  for views and database
    EditText editPDFName;
    Button btn_upload, btn_view;

    StorageReference storageReference;
    DatabaseReference databaseReference;

    public UploadFiles() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_upload_files, container, false);

        editPDFName = view.findViewById(R.id.txt_pdfName);
        btn_upload = view.findViewById(R.id.button_upload);
        btn_view = view.findViewById(R.id.button_view);

        //Referencing the Uploads database that holds the pdf files
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("Uploads");

            btn_upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String enter = editPDFName.getText().toString();

                    //Error checking to make sure the teacher adds a name to the file before uploading
                    if(enter.isEmpty()) {
                        displayMessage("Please enter a file name...");
                    }
                    else {
                        selectPDFFile();
                    }

                }
            });

            btn_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(),AdminViewFiles.class));
                }
            });



        return view;
    }

    //Method to select a pdf from your device - setType is set to only allow pdf files to be uploaded
    private void selectPDFFile() {

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF File"),1);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data !=null && data.getData() !=null) {
            uploadPDFFile(data.getData());
        }
    }

    //Method to upload the file using a progress dialog to view the upload progress
    private void uploadPDFFile(Uri data) {

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        //Stores the file in the database under an Uploads folder
        StorageReference reference = storageReference.child("Uploads/"+System.currentTimeMillis()+ ".pdf");
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        //Handles the successful upload
                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uri.isComplete());
                        Uri url = uri.getResult();

                        uploadPDF uploadPDF = new uploadPDF(editPDFName.getText().toString(),url.toString());
                        databaseReference.child(databaseReference.push().getKey()).setValue(uploadPDF);
                        displayMessage("File Uploaded");
                        progressDialog.dismiss();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                double progress = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();

                progressDialog.setMessage("Uploaded: "+(int)progress+"%");

            }
        });

    }

    //Method to display a simple toast message
    private void displayMessage(String text) {
        Toast.makeText(getActivity(), text ,Toast.LENGTH_SHORT).show();
    }

}
