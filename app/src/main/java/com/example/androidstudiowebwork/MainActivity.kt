package com.example.androidstudiowebwork

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

//import com.google.android

class MainActivity : AppCompatActivity() {


    private fun displayToast(s: String) {
        Toast.makeText(applicationContext, s, Toast.LENGTH_SHORT).show()  //show the toast message for show duration
    } //define the display toast function that accepts one string parameter

    //Initialize variables
    lateinit var btSignIn: SignInButton
    lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)  //this is always neccessary when using override
        setContentView(R.layout.activity_main)  //note, I added this code twice, interfering with the toast message for the two seperate buttons

        //Assign variables

        btSignIn = findViewById(R.id.bt_sign_in)

        //Initialize sign in options the client-id is copied from google-services.json file
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("362396821435-9j7cqh04cermrqvohqqbepmisse74t0a.apps.googleusercontent.com") //note: string may need to be adjusted
            .requestEmail()
            .build()

        //initialize the sign in client
        googleSignInClient = GoogleSignIn.getClient(this@MainActivity, googleSignInOptions)
        btSignIn.setOnClickListener {//intialize sign in intent
            val intent: Intent = googleSignInClient.signInIntent
            //start activity for result
            startActivityForResult(intent, 100)
        }

        //initialize firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        //initialize firebase user
        val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
        //check condition
        if (firebaseUser != null) {
            //when user already signs in redirect to profile activity
            startActivity(
                Intent(
                    this@MainActivity,
                    ProfileActivity::class.java //what does this even mean????
                ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }

    }
    /*  --> old code
    private lateinit var oneTapClient: SignInClient  //private functions shoudl be defined outside of local functions
    private lateinit var signInRequest: BeginSignInRequest

    private val REQ_ONE_TAP = 2 //can be any integer unique to the activity
    private var showOneTapUI = true */

    override fun onActivityResult(requestCode: Int, resultCode: Int, data:Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //start of code from tutorial
        val button = findViewById<Button>(R.id.SurpriseButton)  //on the tutorial, the id name was "helloButton", but in my case, I named it "SurpriseButton", and we named it as a variable, in kotlin, it's named "val"

        //implements the functionality for when button gets clicked
        button.setOnClickListener{
            Log.v("Debugging Tool", "Hello There!")  //similar to console.log in javascript, the Log info gets displayed on the "Logcat" section, accepts two parameters, tag and corresponding message, think hashmap application, v stands for verbose
            Toast.makeText(this, "Oh hey, didn't see you there!", Toast.LENGTH_SHORT).show()  //Toast is also built into android studio that works similar to console.alert() message in javascript, need to be set to show(), otherwise, it won't display on the emulator
        }
        //end of code from tutorial

        //check condition --> start of google authentication

        if (requestCode == 100) {
            //When request code is equal to 100 initialize task
            val SignInAccountTask: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)

            //check condition
            if (SignInAccountTask.isSuccessful) {
                //when google sign in successful initialize string
                val s = "Google Sign in Successful"  //this message will be displayed as a toast
                //Display toast
                displayToast(s)  //function for this also needs to be created

                //initialize sign in account
                try {
                    //initialize sign in account
                    val googleSignInAccount = SignInAccountTask.getResult(ApiException::class.java)
                    //check condition
                    if (googleSignInAccount != null) { //meaning no google account exists that can allow user to sign in
                        val authCredential: AuthCredential = GoogleAuthProvider.getCredential(
                            googleSignInAccount.idToken, null
                        )

                        //check credential
                        firebaseAuth.signInWithCredential(authCredential)
                            .addOnCompleteListener(this) {
                                task ->
                                //check condition
                                if  (task.isSuccessful) {
                                    //when task is successful redirect to profile activity
                                    startActivity(
                                        Intent(
                                            this@MainActivity,
                                            ProfileActivity::class.java
                                        ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    )
                                    //display toast
                                    displayToast("Firebase Authentication Successful")
                                } else {
                                    //when task is unsuccessful, display the following toast message
                                    displayToast(
                                        "Authentication Failed"
                                    )
                                }
                            }
                }
            } catch (e: ApiException) {
                e.printStackTrace()  //print out the message related to api errors
            }}}


        /* --> old code
        //the following is to authenticate with firebase
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    //your server's client ID, not the android client id
                    .setServerClientId(getString(R.string.your_web_client_id))
                //only show accounts previously used to sign in
            .setFilterByAuthorizedAccounts(true)
            .build())
        .build()

        when (requestCode) {
            REQ_ONE_TAP -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    when {
                        idToken != null -> {
                            //Got an ID token from google. Use it to authenticate with firebase
                            Log.d(TAG, "Got ID Token")
                        }
                        else -> {
                            //shouldn't happen
                            Log.d(TAG, "No ID token!")
                        }
                    }
                } catch (e: ApiException) {
                    //... leave it blank for now
                }


            }
        }
*/
        //super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_main)

    }
}

//continue implementing this by following the tutorial below: https://firebase.google.com/docs/auth/android/google-signin