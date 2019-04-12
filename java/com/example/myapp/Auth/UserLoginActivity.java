/*******************************************************************************************
 * Name:            Andrea Panetta
 * Page Name:       Login Activity - Student
 *
 * XML file:        activity_user_login.xml
 *
 * Description:     Login page that uses the credentials of the already set up students by the
 *                  teacher. There is also the added fingerprint scanner feature to cater for
 *                  younger children
 *
 * References:      https://firebase.google.com/docs/auth/android/email-link-auth
 *                  https://www.youtube.com/watch?v=zYA5SJgWrLk
 *******************************************************************************************/

package com.example.myapp.Auth;

import android.app.KeyguardManager;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.Common;
import com.example.myapp.Model.User;
import com.example.myapp.R;
import com.example.myapp.User.UserLanding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class UserLoginActivity extends AppCompatActivity {

    //Variables and database initialised
    EditText userEmail, userPassword;
    Button userLogin;
    ProgressBar loginProgress;
    FirebaseAuth myAuth;
    DatabaseReference users;
    private KeyStore keyStore;
    private static final String KEY_NAME = "CHARLIE";
    private Cipher cipher;
    private TextView textView;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        //Initialises the fingerprint feature
        KeyguardManager keyguardManager = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager = (FingerprintManager)getSystemService(FINGERPRINT_SERVICE);

        //checks if the fingerprint feature is enabled on the device
        if(!fingerprintManager.isHardwareDetected()) {
            displayMessage("Fingerprint scanner is not enabled");
        }
        else {
            //if not the user has no fingerprint scans set up, they need to do so in their device settings
            if(!fingerprintManager.hasEnrolledFingerprints()) {
                displayMessage("Register at least one fingerprint in your phone settings");
            }
            else {
                genKey();

                //If the cipher is initialised successfully, then a crypto object instance is created
                if(cipherInit()) {
                    FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                    FingerprintHandler helper = new FingerprintHandler(this);
                    helper.startAuthentication(fingerprintManager, cryptoObject);
                }
            }
        }

        userEmail = findViewById(R.id.user_email);
        userPassword = findViewById(R.id.user_password);
        userLogin = findViewById(R.id.user_button);
        loginProgress = findViewById(R.id.login_progress);

        loginProgress.setVisibility(View.INVISIBLE);

        user = new User();

        myAuth = FirebaseAuth.getInstance();
        users = FirebaseDatabase.getInstance().getReference().child("Student");

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userLogin.setVisibility(View.INVISIBLE);
                loginProgress.setVisibility(View.VISIBLE);

                final String email = userEmail.getText().toString();
                final String pass = userPassword.getText().toString();

                //Error checking to ensure the user does not continue without filling all fields
                if(email.isEmpty() || pass.isEmpty()) {

                    displayMessage("Please enter all fields!");
                    userLogin.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.INVISIBLE);
                } else {

                    //The user is able to login using the data that is already stored in the database by the teacher
                    myAuth.signInWithEmailAndPassword(userEmail.getText().toString(),
                            userPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    loginProgress.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(UserLoginActivity.this, UserLanding.class));
                                        myAuth.getCurrentUser();
                                        Common.currentUser = user;
                                    } else {
                                        userLogin.setVisibility(View.VISIBLE);
                                        Toast.makeText(UserLoginActivity.this, task.getException().getMessage(),
                                                Toast.LENGTH_LONG).show();
                                        userPassword.setText("");
                                    }
                                }
                            });
                }

            }
        });
    }

    //Method to initialise the cipher
    private boolean cipherInit() {
        try {
            //obtain and configure with the properties required for fingerprint authentication
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

            try{
                keyStore.load(null);
                SecretKey key = (SecretKey)keyStore.getKey(KEY_NAME, null);
                cipher.init(Cipher.ENCRYPT_MODE,key);
                return true;
            } catch (IOException e1) {

                e1.printStackTrace();
                return false;
            } catch (NoSuchAlgorithmException e1) {

                e1.printStackTrace();
                return false;
            } catch (CertificateException e1) {

                e1.printStackTrace();
                return false;
            } catch (UnrecoverableKeyException e1) {

                e1.printStackTrace();
                return false;
            } catch (KeyStoreException e1) {

                e1.printStackTrace();
                return false;
            } catch (InvalidKeyException e1) {

                e1.printStackTrace();
                return false;
            }
    }

    //Generate the key
    private void genKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,"AndroidKeyStore");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }

        //configure the key so that the user has to confirm their identity each time they login
        try {
            keyStore.load(null);
            keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME,KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT).setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7).build());
            keyGenerator.generateKey();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }


    }

    //Method to display simple toast message
    private void displayMessage(String text) {

        Toast.makeText(getApplicationContext(), text , Toast.LENGTH_LONG).show();
    }
}
