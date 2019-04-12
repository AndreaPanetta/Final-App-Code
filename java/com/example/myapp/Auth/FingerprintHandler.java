
/*******************************************************************************************
 * Name:            Andrea Panetta
 * Page Name:       Fingerprint Handler - Authentication
 * Description:     Handler for the fingerprint authentication
 *
 * XML File:        activity_view_grades.xml
 *
 *References:       3rd Year Labs and Lectures
 *                  https://www.youtube.com/watch?v=zYA5SJgWrLk
 *
 *******************************************************************************************/


package com.example.myapp.Auth;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.example.myapp.User.UserLanding;

class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;

    public FingerprintHandler(Context context) {
        this.context = context;
    }

    //Sets the permissions for using a fingerprint scanner and authenticates the cryptoObject
    public void startAuthentication(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cenCancellationSignal = new CancellationSignal();
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED)
            return;
        fingerprintManager.authenticate(cryptoObject,cenCancellationSignal,0,this,null);
    }


    //If the user provides a wrong fingerprint or it hasn't been read properly, a toast message will pop up
    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
        Toast.makeText(context, "Fingerprint Authentication failed...", Toast.LENGTH_SHORT).show();
    }

    //Or else if the user succeeds, they weill be brought to the student landing page to access the application
    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        context.startActivity(new Intent(context, UserLanding.class));
    }
}
